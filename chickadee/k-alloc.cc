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

page_t buddy(page_t page) {
    page_t ptr = page * PAGESIZE;
    order_t order = pages[page].order;
    page_t size = 1 << order;
    page_t buddy = ptr ^size;
    return buddy;
}

void removeFromFreeList(page_t page) {
    order_t order = pages[page].order;
    page_t current = freeLists[order];
    page_t prev = -1;
    while (current != -1) {
        if (current == page) {
            if (prev == -1) {
                // if page we were looking for was at the top of the stack
                freeLists[order] = pages[current].next;
            } else {
                pages[prev].next = pages[current].next;
            }
            pages[current].isFree = false;
            pages[current].next = -1;
            return;
        }
        prev = current;
        current = pages[current].next;
    }
}

void coalesce(order_t order) {
    if (order >= max_order) {
        return;
    }
    bool coalesced = false;
    page_t page = freeLists[order];
    // iterate over freeList[order] giving each page in free list
    while (page != -1) {
        page_t buddyPage = buddy(page);
        if (pages[buddyPage].isFree && pages[buddyPage].order == order) {
            // Remove page and buddy from the stack
            removeFromFreeList(page);
            removeFromFreeList(buddyPage);

            // Increment order
            pages[page].order++;
            pages[buddyPage].order++;

            // Select new lead page for block (lesser value)
            if (page < buddyPage) {
                // Add page to stack in order + 1
                pages[page].next = freeLists[order + 1];
                freeLists[order + 1] = page;

                pages[buddyPage].isFree = false;
            } else {
                // Add buddy to stack in order + 1
                pages[buddyPage].next = freeLists[order + 1];
                freeLists[order + 1] = buddyPage;
            }

            coalesced = true;
        }
        page = pages[page].next;
    }
    if (coalesced) {
        coalesce(order + 1);
    }
}

void split(order_t order) {
    if (order > max_order) {
        return;
    }
    if (freeLists[order] == -1) {
        split(order + 1);
    }
    if (freeLists[order] == -1) {
        return;
    }
    // remove page from freeList[order]
    page_t page = freeLists[order];
    freeLists[order] = pages[page].next;
    pages[page].order = order - 1;

    // add page and buddy to freeList[order - 1]
    page_t buddyPage = buddy(page);

    pages[page].next = freeLists[order - 1];
    pages[page].isFree = true;
    freeLists[order - 1] = page;

    pages[buddyPage].next = freeLists[order - 1];
    pages[buddyPage].isFree = true;
    freeLists[order - 1] = buddyPage;
}

// init_kalloc
//    Initialize stuff needed by `kalloc`. Called from `init_hardware`,
//    after `physical_ranges` is initialized.
void init_kalloc() {
    // do nothing for now
    auto irqs = page_lock.lock();

    // Initialize the free list for each order 12-21
    for (order_t order = 0; order < max_order + 1; order++) {
        freeLists[order] = -1;
    }

    // Initialize each page and determine if they are free or not
    for (page_t page = 0; page < pages_in_system; page++) {
        auto range = physical_ranges.find(page * PAGESIZE);
        pages[page].isFree = range->type() == mem_available;
        pages[page].order = min_order;
        pages[page].next = -1;
        if (pages[page].isFree) {
            // Insert free page to the head of the linked list
            pages[page].next = freeLists[min_order];
            freeLists[min_order] = page;
        }
    }
    coalesce(min_order);
    page_lock.unlock(irqs);

    test_kalloc();
}

// kalloc(sz)
//    Allocate and return a pointer to at least `sz` contiguous bytes
//    of memory. Returns `nullptr` if `sz == 0` or on failure.
void *kalloc(size_t sz) {

    auto irqs = page_lock.lock();
    order_t order = msb(sz - 1); // order for size


    page_t foundPage = freeLists[order];
    // If no free page, see if we can split a higher order
    if (foundPage == -1) {
        split(order + 1);
    }
    // If no free pages after split, we cannot allocate a page
    if (freeLists[order] == -1) {
        page_lock.unlock(irqs);
        return nullptr;
    }

    // Pop free page from the stack
    freeLists[order] = pages[foundPage].next;
    pages[foundPage].next = -1;
    pages[foundPage].isFree = false;

    // Allocate free page to pointer
    // Assign a kernel (high canonical) address
    x86_64_page *p = pa2ka<x86_64_page *>(foundPage * PAGESIZE);

    page_lock.unlock(irqs);
    return p;
}

// kfree(ptr)
//    Free a pointer previously returned by `kalloc`, `kallocpage`, or
//    `kalloc_pagetable`. Does nothing if `ptr == nullptr`.
void kfree(void *ptr) {
    auto irqs = page_lock.lock();

    // Convert kernel address to physical address and then divide by PAGESIZE to get page #
    page_t page = ka2pa((x86_64_page *) ptr) / PAGESIZE;

    assert(!pages[page].isFree);

    pages[page].isFree = true;
    // Add free page to the head of the freeList based on order
    order_t order = pages[page].order;
    pages[page].next = freeLists[order];
    freeLists[order] = page;
    coalesce(order);

    page_lock.unlock(irqs);
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

    // Allocate two different pages
    p1 = kalloc(PAGESIZE);
    p2 = kalloc(PAGESIZE);
    assert(p1 != p2);
    kfree(p1);
    kfree(p2);

    // Allocate page with higher order
//    p1 = kalloc(PAGESIZE + 4);
//    assert(p1);
//    kfree(p1);
}

#pragma GCC pop_options
