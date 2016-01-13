/********************************************************************************/
/*      CSE 531 Distributed Multiprocessor Operating Systems                    */
/*      Project 3                                                               */
/*      Group members:   Prasanth Kumar Dhanamjayulu(1209328974)                */
/*                       Vivek Muniyandi(1209224688)                            */
/*                       Sadhana Ramachandran(1209355364)                       */
/********************************************************************************/

#include "msgs.h"

void client0()
{
	while(1)
	{
		int a[10] = {10,20,30,40,50,60,70,80,90,100};
		printf("Sending from Client 0 on port 99:  ");
		for(i=0;i<10;i++) 
			printf("%d ",a[i]);
		printf("\n\n");
		Send(&p[99],a);
		printf("########## Client 0: Send successful\n\n");
		sleep(1);

		int b[10];
		Receive(&p[0],b);
		printf("Received by Client 0 on port 0:  ");
		for(i=0;i<10;i++) 
			printf("%d ",b[i]);
		printf("\n\n");
		sleep(1);
	}
}

void client1()
{
	while(1)
	{
		int a[10] = {110,120,130,140,150,160,170,180,190,200};
		printf("Sending from Client 1 on port 99:  ");
		for(i=0;i<10;i++)
			printf("%d ",a[i]);
		printf("\n\n");
	        Send(&p[99],a);
		printf("########## Client 1: Send successful\n\n");
		sleep(1);
		
                int b[10];
		Receive(&p[0],b);
		printf("Received by Client 1 on port 0:  ");
		for(i=0;i<10;i++) 
			printf("%d ",b[i]);
		printf("\n\n");
		sleep(1);
	}
}

void server()
{
	while(1)
        {
		int a[10];
		Receive(&p[99],a);
		printf("\t\t\t\t\t\tReceived by Server on port 99:  ");
		for(i=0;i<10;i++)
			printf("%d ",a[i]);
		printf("\n\n");
                sleep(1);

                printf("\t\t\t\t\t\tSending Response from Server on port 0:  ");
                for(i=0;i<10;i++) {
                	printf("%d ",a[i]);
                }
                printf("\n\n");
                Send(&p[0],a);
                printf("##########  Server: Send successful\n\n");
                sleep(1);
	}
}

int main()
{   
	initialize_ports();
	printf("\n***********Starting Client 0**********\n");
	startThread(client0);
	printf("\n***********Starting Client 1**********\n");
	startThread(client1);
	printf("\n***********Starting Server************\n\n");
	startThread(server);
	run();
	while (1) 
        {
        	sleep(1);
        }
}
