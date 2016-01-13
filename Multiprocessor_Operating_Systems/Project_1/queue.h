#ifndef QUEUE_H_
#define QUEUE_H_


#include <stdio.h>
#include <stdlib.h>
# include "tcb.h"

//Rotate the queue
void rotateQ(struct tcb_t **Q)
{
	if(*Q == NULL)
		return;
	else
	{
		*Q = (*Q)->next;
	}
}

//Add a node to the queue
void addQ(struct tcb_t **Q, struct tcb_t *datav)
{
	struct tcb_t *temp;
	if(*Q == NULL)
	{
		*Q = datav;
		(*Q)->next = *Q;
		(*Q)->prev = *Q;
	}
	else
	{
		temp = (*Q)->prev;
		(*Q)->prev = datav;
		temp->next = datav;
		datav->next = *Q;
		datav->prev = temp;
	}
}

//Delete a node from the queue
struct tcb_t * deleteQ(struct tcb_t **Q)
{
	struct tcb_t *temp;
	temp = *Q;
	
	if(*Q == NULL)
		return NULL;

	if((*Q)->next == *Q && ((*Q)->prev) == *Q)
	{
		*Q = NULL;
	}
	else
	{
		(*Q)->next->prev = (*Q)->prev;
		(*Q)->prev->next = (*Q)->next;
		*Q = (*Q)->next;
	}
	temp->next = temp->prev = NULL;
	return temp;
}

void initQ(struct tcb_t **Q) {
	*Q = NULL;
}

#endif
