#include <stdio.h>
#include "queue.h"

#define stack_size 8192

struct tcb_t *RunQ;

//Start the thread with the function
void startThread(void (*func) (void))
{
    void *stack = malloc(stack_size);
    struct tcb_t *tcb = (struct tcb_t *) malloc(sizeof (struct tcb_t));
    init_TCB(tcb, *func, stack, stack_size);	
    addQ(&RunQ,tcb);
}

//Run function
void run()
{
    ucontext_t parent;     
    getcontext(&parent);   
    swapcontext(&parent, &(RunQ->context)); 
}

//Yield function
void yield()
{
    rotateQ(&RunQ);
    swapcontext(&((RunQ->prev)->context), &(RunQ->context));
}
