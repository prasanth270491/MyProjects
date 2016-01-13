#include "sem.h"

typedef struct port 
{
	int msgs[10][10];
	int in, out;
	struct Semaphore_t *mutex, *full, *empty;
}port_1;

port_1 p[100];
int i;

void initialize_ports()
{
	for(i =0;i<100;i++)
        {
        	p[i].full = CreateSem(0);
                p[i].empty = CreateSem(10);
                p[i].mutex = CreateSem(1);
                p[i].in = 0;
                p[i].out = 0;
        }
}

void Send(port_1 *pIndex, int *msg)
{
        P(pIndex->empty);
        P(pIndex->mutex);
  	for(i=0;i<10;i++)
	{
		pIndex->msgs[pIndex->in][i] = msg[i];
        }
	pIndex->in = ((pIndex->in)+1)%10;
        V(pIndex->mutex);
        V(pIndex->full);
}

void Receive(port_1 *pIndex, int *msg)
{
        P(pIndex->full);
        P(pIndex->mutex);
	for(i=0;i<10;i++)
  	{
  		msg[i]=pIndex->msgs[pIndex->out][i];
  	}
  	pIndex->out = ((pIndex->out)+1)%10;
        V(pIndex->mutex);
        V(pIndex->empty);
}
