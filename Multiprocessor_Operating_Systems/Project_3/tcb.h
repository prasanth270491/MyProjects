#ifndef TCB_H_
#define TCB_H_

#include <stdio.h>
#include <stdlib.h>
#include <ucontext.h>
#include <string.h>

//Structure of TCB
struct tcb_t
{
	int threadid;
	ucontext_t context;
	struct tcb_t* next;
	struct tcb_t* prev;
};

//Initialize the TCB
void init_TCB (struct tcb_t *tcb, void (*function), void* stackP, int stack_size)
{
    memset(tcb, '\0', sizeof(struct tcb_t));       
    getcontext(&tcb->context);              
    tcb->context.uc_stack.ss_sp = stackP;
    tcb->context.uc_stack.ss_size = (size_t) stack_size;
    makecontext(&tcb->context, function, 0);
}

#endif
