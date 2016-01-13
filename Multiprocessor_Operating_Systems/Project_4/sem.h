#include"threads.h"
#include<stdio.h>

// Declare a semaphore
struct Semaphore_t
{
	int count;
	struct tcb_t *SemQ;
};

// Initialize the semaphore
struct Semaphore_t* CreateSem(int inputvalue)
{
	struct Semaphore_t *sem = (struct Semaphore_t*) malloc(sizeof(struct Semaphore_t));
	sem->count = inputvalue;
	sem->SemQ = NULL;
	return sem;
}

void P(struct Semaphore_t *sem)
{
	struct tcb_t *temp;
	sem->count--;
	if(sem->count < 0)
	{
		temp = deleteQ(&RunQ);
		addQ(&sem->SemQ,temp);
		swapcontext(&(temp->context),&(RunQ->context));
	}
}

void V(struct Semaphore_t *sem)
{
	struct tcb_t *temp;
	sem->count++;
	if(sem->count <= 0)
	{
		temp = deleteQ(&sem->SemQ);
		addQ(&RunQ,temp);
	}
	yield();
}



