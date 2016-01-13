/* Prasanth Kumar Dhanamjayulu ASU ID: 1209328974
   Sadhana Ramachandran ASU ID: 1209355364
   Vivek Muniyandi ASU ID: 1209224688 */


#include <stdio.h>
#include <time.h>
#include "threads.h"

int global=0;

void func1()
{
	int x=0;
	while(1)
	{
		printf("Global : %d\t",global++);
		printf("X : %d\n",x++);
		sleep(1);
		yield();
	}
}

void func2()
{
	int y=0;
	while(1)
	{
		printf("Global : %d\t",global++);
		printf("Y : %d\n",y++);
		sleep(1);
		yield();
	}
}

void func3()
{
	int z=0;
	while(1)
	{
		printf("Global : %d\t",global++);
		printf("Z : %d\n",z++);
		sleep(1);
		yield();
	}
}

int main()
{
	printf("In main\n");
	startThread(func1);
	startThread(func2);
	startThread(func3);
	run();
}
