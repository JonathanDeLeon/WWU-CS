#include "kernel.hh"
#include "k-lock.hh"

typedef int16_t page_t;
typedef int8_t order_t;

static const order_t min_order = 12;
static const order_t max_order = 21;
static const page_t pages_in_system = MEMSIZE_PHYSICAL / PAGESIZE;

struct pageData_t {
    bool isFree;
    order_t order;
};

static pageData_t pages[pages_in_system];

static spinlock page_lock;
static uintptr_t next_free_pa;

x86_64_page *kallocpage() {
    return reinterpret_cast<x86_64_page *>(kalloc(PAGESIZE));
}


// init_kalloc
//    Initialize stuff needed by `kalloc`. Called from `init_hardware`,
//    after `physical_ranges` is initialized.
void init_kalloc() {
    // do nothing for now
    auto irqs = page_lock.lock();

    for (page_t page = 0; page < pages_in_system; page++) {
        auto range = physical_ranges.find(page * PAGESIZE);
        pages[page].isFree = range->type() == mem_available;
        pages[page].order = min_order;
    }
    page_lock.unlock(irqs);

    test_kalloc();
}

// kalloc(sz)
//    Allocate and return a pointer to at least `sz` contiguous bytes
//    of memory. Returns `nullptr` if `sz == 0` or on failure.
void *kalloc(size_t sz) {
    // Static size
    assert(sz == PAGESIZE);

    auto irqs = page_lock.lock();

    // Find a free page
    page_t foundPage = -1;
    for (page_t page = 0; page < pages_in_system; page++) {
        if (pages[page].isFree) {
            foundPage = page;
            break;
        }
    }

    // Allocate free page to pointer
    void *p = nullptr;
    if (foundPage != -1) {
        // Assign a kernel (high canonical) address
        p = (void *) pa2ka<x86_64_page *>(foundPage * PAGESIZE);
        pages[foundPage].isFree = false;
    }

    page_lock.unlock(irqs);
    return (void *) p;
}

// kfree(ptr)
//    Free a pointer previously returned by `kalloc`, `kallocpage`, or
//    `kalloc_pagetable`. Does nothing if `ptr == nullptr`.
void kfree(void *ptr) {
    // Convert kernel address to physical address and then divide by PAGESIZE to get page #
    page_t page = ka2pa((x86_64_page *) ptr) / PAGESIZE;
    pages[page].isFree = true;
}

// test_kalloc
//    Run unit tests on the kalloc system.
void test_kalloc() {
    // Allocate one page and then free it
    void *p1 = kalloc(PAGESIZE);
    assert(p1);
    kfree(p1);
    // Allocate one page and then free it
    // Goal: Allocate the same page that was freed
    void *p2 = kalloc(PAGESIZE);
    kfree(p2);
    // Both pages should be the same
    assert(p1 == p2);
}
