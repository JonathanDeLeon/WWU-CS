#include "kernel.hh"
#include "k-apic.hh"
#include "k-chkfs.hh"
#include "k-devices.hh"
#include "k-vmiter.hh"

// kernel.cc
//
//    This is the kernel.

volatile unsigned long ticks;   // # timer interrupts so far on CPU 0
int kdisplay;                   // type of display

static void kdisplay_ontick();
static void process_setup(pid_t pid, const char* program_name);


// kernel_start(command)
//    Initialize the hardware and processes and start running. The `command`
//    string is an optional string passed from the boot loader.

void kernel_start(const char* command) {
    init_hardware();
    console_clear();
    kdisplay = KDISPLAY_MEMVIEWER;

    // Set up process descriptors
    for (pid_t i = 0; i < NPROC; i++) {
        ptable[i] = nullptr;
    }

    auto irqs = ptable_lock.lock();
    process_setup(1, "allocator");
    ptable_lock.unlock(irqs);

    // Switch to the first process
    cpus[0].schedule(nullptr);
}


// process_setup(pid, name)
//    Load application program `name` as process number `pid`.
//    This loads the application's code and data into memory, sets its
//    %rip and %rsp, gives it a stack page, and marks it as runnable.

void process_setup(pid_t pid, const char* name) {
#ifdef CHICKADEE_FIRST_PROCESS
    name = CHICKADEE_FIRST_PROCESS;
#endif

    assert(!ptable[pid]);
    proc* p = ptable[pid] = kalloc_proc();
    x86_64_pagetable* npt = kalloc_pagetable();
    assert(p && npt);
    p->init_user(pid, npt);

    int r = p->load(name);
    assert(r >= 0);
    p->regs_->reg_rsp = MEMSIZE_VIRTUAL;
    x86_64_page* stkpg = kallocpage();
    assert(stkpg);
    r = vmiter(p, MEMSIZE_VIRTUAL - PAGESIZE).map(ka2pa(stkpg));
    assert(r >= 0);

    int cpu = pid % ncpu;
    cpus[cpu].runq_lock_.lock_noirq();
    cpus[cpu].enqueue(p);
    cpus[cpu].runq_lock_.unlock_noirq();
}


// proc::exception(reg)
//    Exception handler (for interrupts, traps, and faults).
//
//    The register values from exception time are stored in `reg`.
//    The processor responds to an exception by saving application state on
//    the current CPU stack, then jumping to kernel assembly code (in
//    k-exception.S). That code transfers the state to the current kernel
//    task's stack, then calls proc::exception().

void proc::exception(regstate* regs) {
    // It can be useful to log events using `log_printf`.
    // Events logged this way are stored in the host's `log.txt` file.
    /*log_printf("proc %d: exception %d\n", this->pid_, regs->reg_intno);*/

    // Show the current cursor location.
    console_show_cursor(cursorpos);


    // Actually handle the exception.
    switch (regs->reg_intno) {

    case INT_IRQ + IRQ_TIMER: {
        cpustate* cpu = this_cpu();
        if (cpu->index_ == 0) {
            ++ticks;
            kdisplay_ontick();
        }
        lapicstate::get().ack();
        this->regs_ = regs;
        this->yield_noreturn();
        break;                  /* will not be reached */
    }

    case INT_PAGEFAULT: {
        // Analyze faulting address and access type.
        uintptr_t addr = rcr2();
        const char* operation = regs->reg_err & PFERR_WRITE
                ? "write" : "read";
        const char* problem = regs->reg_err & PFERR_PRESENT
                ? "protection problem" : "missing page";

        if (!(regs->reg_err & PFERR_USER)) {
            panic("Kernel page fault for %p (%s %s, rip=%p)!\n",
                  addr, operation, problem, regs->reg_rip);
        }
        error_printf(CPOS(24, 0), 0x0C00,
                     "Process %d page fault for %p (%s %s, rip=%p)!\n",
                     pid_, addr, operation, problem, regs->reg_rip);
        this->state_ = proc::broken;
        this->yield();
        break;
    }

    case INT_IRQ + IRQ_KEYBOARD:
        keyboardstate::get().handle_interrupt();
        break;

    default:
        if (sata_disk && regs->reg_intno == INT_IRQ + sata_disk->irq_) {
            sata_disk->handle_interrupt();
        } else {
            panic("Unexpected exception %d!\n", regs->reg_intno);
        }
        break;                  /* will not be reached */

    }


    // Return to the current process.
    // If exception arrived in user mode, the process must be runnable.
    assert((regs->reg_cs & 3) == 0 || this->state_ == proc::runnable);
}

#pragma GCC push_options
#pragma GCC optimize ("O0")
uintptr_t proc::fork(regstate *regs) {
    // Lock ptable
    auto irqs = ptable_lock.lock();

    // Allocate new PID
    // Note: pid = 0, should be reserved for the scheduler responsible for paging
    // pid = 1 is the init process
    pid_t newPID = -1;
    for (pid_t i = 1; newPID == -1 && i < NPROC; ++i) {
        if (ptable[i] == nullptr) {
            newPID = i;
        }
    }

    // If system is not out of PIDs
    if (newPID <= 0) {
        ptable_lock.unlock(irqs);
        return -1;
    }

    // Allocate proc and ptable
    proc * p = ptable[newPID] = kalloc_proc();
    x86_64_pagetable *npt = kalloc_pagetable();
    assert(p && npt);
    p->init_user(newPID, npt);
    // Copy the parent process’s user-accessible memory and map the copies into the new process’s page table
    for (vmiter iter(this); iter.low(); iter.next()) {
        if (iter.user()) {
            uintptr_t virtualAddress = iter.va();
            assert(virtualAddress);
            uintptr_t parentPhyiscalAddressU = iter.pa();
            assert(parentPhyiscalAddressU);
            uintptr_t parentPhysicalAddressK = pa2ka(parentPhyiscalAddressU);

            // Allocate memory to child process
            x86_64_page *childPhysicalAddressK = kallocpage();
            assert(childPhysicalAddressK);
            int r = vmiter(p, virtualAddress).map(ka2pa(childPhysicalAddressK));
            assert(r >= 0);

            memcpy(
                    (void *) childPhysicalAddressK,
                    (const void *) parentPhysicalAddressK,
                    (size_t) PAGESIZE
            );
        }
    }
    // Initialize the new process’s registers to a copy of the old process’s registers.
    memcpy(
            (void *) p->regs_,
            (const void *) regs,
            sizeof(regstate)
    );
    // Store the new process in the process table
    // This process should already be in the process table

    // Arrange for the new PID to be returned to the parent process and 0 to be returned to the child process.
    if (this->pid_ == newPID) {
        return 0;
    }

    // Enqueue new process on the run queue
    int cpu = newPID % ncpu;
    cpus[cpu].runq_lock_.lock_noirq();
    cpus[cpu].enqueue(p);
    cpus[cpu].runq_lock_.unlock_noirq();

    ptable_lock.unlock(irqs);
    return newPID;
}
#pragma GCC pop_options


// proc::syscall(regs)
//    System call handler.
//
//    The register values from system call time are stored in `regs`.
//    The return value from `proc::syscall()` is returned to the user
//    process in `%rax`.

uintptr_t proc::syscall(regstate* regs) {
    switch (regs->reg_rax) {

    case SYSCALL_KDISPLAY:
        if (kdisplay != (int) regs->reg_rdi) {
            console_clear();
        }
        kdisplay = regs->reg_rdi;
        return 0;

    case SYSCALL_PANIC:
        panic(NULL);
        break;                  // will not be reached

    case SYSCALL_GETPID:
        return pid_;

    case SYSCALL_YIELD:
        this->yield();
        return 0;

    case SYSCALL_PAGE_ALLOC: {
        uintptr_t addr = regs->reg_rdi;
        if (addr >= VA_LOWEND || addr & 0xFFF) {
            return -1;
        }
        x86_64_page* pg = kallocpage();
        if (!pg || vmiter(this, addr).map(ka2pa(pg)) < 0) {
            return -1;
        }
        return 0;
    }

    case SYSCALL_PAUSE: {
        sti();
        for (uintptr_t delay = 0; delay < 1000000; ++delay) {
            pause();
        }
        cli();
        return 0;
    }

    case SYSCALL_FORK:
        return this->fork(regs);

    case SYSCALL_READ: {
        int fd = regs->reg_rdi;
        uintptr_t addr = regs->reg_rsi;
        size_t sz = regs->reg_rdx;

        auto& kbd = keyboardstate::get();
        auto irqs = kbd.lock_.lock();

        // mark that we are now reading from the keyboard
        // (so `q` should not power off)
        if (kbd.state_ == kbd.boot) {
            kbd.state_ = kbd.input;
        }

        // block until a line is available
        waiter(this).block_until(kbd.wq_, [&] () {
                return sz == 0 || kbd.eol_ != 0;
            }, kbd.lock_, irqs);

        // read that line or lines
        size_t n = 0;
        while (kbd.eol_ != 0 && n < sz) {
            if (kbd.buf_[kbd.pos_] == 0x04) {
                // Ctrl-D means EOF
                if (n == 0) {
                    kbd.consume(1);
                }
                break;
            } else {
                *reinterpret_cast<char*>(addr) = kbd.buf_[kbd.pos_];
                ++addr;
                ++n;
                kbd.consume(1);
            }
        }

        kbd.lock_.unlock(irqs);
        return n;
    }

    case SYSCALL_WRITE: {
        int fd = regs->reg_rdi;
        uintptr_t addr = regs->reg_rsi;
        size_t sz = regs->reg_rdx;

        auto& csl = consolestate::get();
        auto irqs = csl.lock_.lock();

        size_t n = 0;
        while (n < sz) {
            int ch = *reinterpret_cast<const char*>(addr);
            ++addr;
            ++n;
            console_printf(0x0F00, "%c", ch);
        }

        csl.lock_.unlock(irqs);
        return n;
    }

    case SYSCALL_READDISKFILE: {
        const char* filename = reinterpret_cast<const char*>(regs->reg_rdi);
        unsigned char* buf = reinterpret_cast<unsigned char*>(regs->reg_rsi);
        uintptr_t sz = regs->reg_rdx;
        uintptr_t off = regs->reg_r10;

        if (!sata_disk) {
            return E_IO;
        }

        return chickadeefs_read_file_data(filename, buf, sz, off);
    }

    default:
        // no such system call
        log_printf("%d: no such system call %u\n", pid_, regs->reg_rax);
        return E_NOSYS;

    }
}


// memshow()
//    Draw a picture of memory (physical and virtual) on the CGA console.
//    Switches to a new process's virtual memory map every 0.25 sec.
//    Uses `console_memviewer()`, a function defined in `k-memviewer.cc`.

static void memshow() {
    static unsigned last_ticks = 0;
    static int showing = 1;

    // switch to a new process every 0.25 sec
    if (last_ticks == 0 || ticks - last_ticks >= HZ / 2) {
        last_ticks = ticks;
        showing = (showing + 1) % NPROC;
    }

    auto irqs = ptable_lock.lock();

    int search = 0;
    while ((!ptable[showing]
            || !ptable[showing]->pagetable_
            || ptable[showing]->pagetable_ == early_pagetable)
           && search < NPROC) {
        showing = (showing + 1) % NPROC;
        ++search;
    }

    extern void console_memviewer(proc* vmp);
    console_memviewer(ptable[showing]);

    ptable_lock.unlock(irqs);
}


// kdisplay_ontick()
//    Shows the currently-configured kdisplay. Called once every tick
//    (every 0.01 sec) by CPU 0.

void kdisplay_ontick() {
    if (kdisplay == KDISPLAY_MEMVIEWER) {
        memshow();
    }
}
