#include "kernel.hh"
#include "k-lock.hh"

#pragma GCC push_options
#pragma GCC optimize ("O0")

typedef int16_t page_t;
typedef int8_t order_t;

static spinlock page_lock;
static const order_t min_order = 12;
static const order_t max_order = 21;
// MEMSIZE_PHYSICAL 2MB; PAGESIZE 4KB; pages_in_system = 512
static const page_t pages_in_system = MEMSIZE_PHYSICAL / PAGESIZE;

struct pageData_t {
    bool isFree;
    order_t order;
    page_t next;
};
static pageData_t pages[pages_in_system];
// Free lists is used as a stack to store the free pages for a particular order
static page_t freeLists[max_order + 1];

x86_64_page *kallocpage() {
    return reinterpret_cast<x86_64_page *>(kalloc(PAGESIZE));
}


// init_kalloc
//    Initialize stuff needed by `kalloc`. Called from `init_hardware`,
//    after `physical_ranges` is initialized.
void init_kalloc() {
    // do nothing for now
    auto irqs = page_lock.lock();

    // Initialize the free list for each order 12-21
    for (order_t order = 0; order < max_order +1; order++) {
        freeLists[order] = -1;
    }

    // Initialize each page and determine if they are free or not
    for (page_t page = 0; page < pages_in_system; page++) {
        auto range = physical_ranges.find(page * PAGESIZE);
        pages[page].isFree = range->type() == mem_available;
        pages[page].order = min_order;
        pages[page].next = -1;
        if (pages[page].isFree) {
            // Insert page to the head of the linked list
            pages[page].next = freeLists[min_order];
            freeLists[min_order] = page;
        }
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

    /*
    // Find a free page in page table
    page_t foundPage = -1;
    for (page_t page = 0; page < pages_in_system; page++) {
        if (pages[page].isFree) {
            foundPage = page;
            break;
        }
    }
     */
    // More efficient then above
    page_t foundPage = freeLists[min_order];
    // Allocate free page to pointer
    void *p = nullptr;
    if (foundPage != -1) {
        // Assign a kernel (high canonical) address
        p = (void *) pa2ka<x86_64_page *>(foundPage * PAGESIZE);
        // Pop free page from the stack
        freeLists[min_order] = pages[foundPage].next;
        pages[foundPage].next = -1;
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
    // Add free page to the head of the freeList
    pages[page].next = freeLists[min_order];
    freeLists[min_order] = page;
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
#pragma GCC pop_options
