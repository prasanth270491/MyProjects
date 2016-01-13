/********************************************************************************/
/*	CSE 531 Distributed Multiprocessor Operating Systems	                */
/*	Project 2 			                                        */
/*	Group members:   Prasanth Kumar Dhanamjayulu(1209328974)                */
/*                       Vivek Muniyandi(1209224688)                            */
/*                       Sadhana Ramachandran(1209355364)                       */
/********************************************************************************/

#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include"sem.h"

int readwritec = 0 , writewritec = 0 , readc = 0 , writec = 0, global_ID = 0;

struct Semaphore_t *read_sem,*write_sem,*mutex;

void reader_entry(int ID)
{

	printf("[reader: %d] Trying to read\n",ID);
	P(mutex);
	if(writewritec > 0 || writec > 0)
	{
		printf("[reader: %d] blocking for writer\n", ID);
		//Increment the read counter
		readwritec++;
                //Let other process use this mutex.
		V(mutex);
		//Sleep on read semaphore
		P(read_sem);
		readwritec--;

	}
	readc++;
	if( readwritec > 0 )
		V(read_sem);
	else{
		V(mutex);
		}
}

void reader_exit(int ID)
{
	P(mutex);
	readc--;
	if(readc == 0 && writewritec > 0 )
	{
		V(write_sem);
	}
	else
		V(mutex);
	
}

void writer_entry(int ID)
{
	printf("\t\t\t\t\t\t\t\t\t[writer: %d] trying to write\n",ID);
	P(mutex);
	if(readc > 0 || writec > 0 )
	{
		printf("\t\t\t\t\t\t\t\t\t[writer: %d] blocking for others\n", ID);
		writewritec++;
		V(mutex);
		P(write_sem);
		writewritec--;
	}
	writewritec++;
	V(mutex);
}

void writer_exit(int ID)
{
	P(mutex);
	writec--;
	if(readwritec > 0)
		V(read_sem);
	else if( writewritec > 0 )
		V(write_sem);
	else
		V(mutex);
}

void reader()
{
	int ID;
	P(mutex);
	ID = global_ID++;
	V(mutex);
	while(1)
	{
		reader_entry(ID);
		printf("[reader: %d] READING\n",ID);
		sleep(1);
		reader_exit(ID);
	};

}

void writer()
{
	int ID;
	P(mutex);
	ID = global_ID++;
	V(mutex);
	while(1)
	{
		writer_entry(ID);
		printf("\t\t\t\t\t\t\t\t\t[writer: %d] WRITING\n ",ID);
		sleep(1);
		writer_exit(ID);
	};
}
int main()
{
	mutex = CreateSem(1);
        read_sem = CreateSem(0);
        write_sem = CreateSem(0);
	
	startThread(reader);
	startThread(reader);
	startThread(reader);
	startThread(writer);
	startThread(writer);
	
	
	run();
}

