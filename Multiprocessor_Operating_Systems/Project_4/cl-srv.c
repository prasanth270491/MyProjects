/********************************************************************************/
/*      CSE 531 Distributed Multiprocessor Operating Systems                    */
/*      Project 4                                                               */
/*      Group members:   Prasanth Kumar Dhanamjayulu(1209328974)                */
/*                       Vivek Muniyandi(1209224688)                            */
/*                       Sadhana Ramachandran(1209355364)                       */
/********************************************************************************/

#include "msgs.h"


void client1()
{
	char **client_strings;
	client_strings = (char **) malloc(5*sizeof(char *));
	int j;
	for(j=0;j<5;j++)
	        client_strings[j]=(char *)malloc(20*sizeof(char));
	
	client_strings[0]="abcdef12345";
	client_strings[1]="ghijkl12";
	client_strings[2]="mnopqr12345";
	client_strings[3]="stuvwx";
	client_strings[4]="yzabcd";
        client_strings[5]="abcdef";
        client_strings[6]="klmnop";
        client_strings[7]="ghijkl";
        client_strings[8]="pqrstuv";
        client_strings[9]="xyz123";
	while(1)
	{
		int num[10];
		int b[10];
                int randnum = rand()%10;
                int s=0,z=0;
                int j=0,m=0;
		int numOfSends;
		int add_or_del = rand()%2;
		for(z=0;z<10;z++)
		{
			num[z]=0;
		}
		num[0] = add_or_del;
		Send(&p[97],num);
                b[s++] = randnum;
                b[s++] = strlen(client_strings[randnum]);
		int size = b[s-1];
		if(size < 8)
		{
			while (j < size)
			{
				b[s++] = (int) client_strings[randnum][j];
				j++;
			}
			b[s++]='\0';
			Send(&p[99],b); 
		}
		else
		{
			while(s < 10)
			{
				b[s++] = (int) client_strings[randnum][j];
				j++;
			}
			Send(&p[99],b);
			numOfSends = ((size-8)/10);
			if(((size-8)%10)!=0)
			{
				numOfSends+=1;
			}
			printf("\n");
			for(m=0; m<numOfSends; m++)
			{
				
				s=0;
				while(s<10)
				{
					b[s++] = (int) client_strings[randnum][j];
					j++;
					if((char)b[s]=='\0')
					{
						break;
					}
				}
				Send(&p[99],b);	
			}
		}	
                
		if(add_or_del == 1)
		{
			printf("Request to add/modify %s from Client 1 on position %d", client_strings[randnum], randnum+1);
			//printf("%s",client_strings[randnum]);
			printf("\n\n");
		}
		else
		{
			printf("Request to delete from Client 1 on position %d", randnum+1);
			//printf("%s",client_strings[randnum]);
			printf("\n\n");
		}

		printf("########## Client 1: Send successful\n\n");
		sleep(1);

		int d[10];
		Receive(&p[0],d);
		printf("Received Success by Client 1 on port 0");
		printf("\n\n");
		sleep(1);
	}
}

void client2()
{
        char **client_strings;
	client_strings = (char **) malloc(5*sizeof(char *));
	int j;
	for(j=0;j<5;j++)
	        client_strings[j]=(char *)malloc(20*sizeof(char));
	
	client_strings[0]="aaaaaa12323";
        client_strings[1]="bbbbbb";
        client_strings[2]="ccccccwe3131";
        client_strings[3]="dddddd";
        client_strings[4]="eeeeee";
        client_strings[5]="ffffff";
        client_strings[6]="gggggg";
        client_strings[7]="hhhhhh";
        client_strings[8]="iiiiii";
        client_strings[9]="jjjjjj";
	while(1)
	{
		int num[10];
		int b[10];
                int randnum = rand()%10;
                int s=0,z=0;
                int j=0,m=0;
		int numOfSends;
		int add_or_del = rand()%2;
		for(z=0;z<10;z++)
		{
			num[z]=0;
		}
		num[0] = add_or_del;
		Send(&p[97],num);
                b[s++] = randnum;
                b[s++] = strlen(client_strings[randnum]);
		int size = b[s-1];
		if(size < 8)
		{
			while (j < size)
			{
				b[s++] = (int) client_strings[randnum][j];
				j++;
			}
			b[s++]='\0';
			Send(&p[99],b); 
		}
		else
		{
			while(s < 10)
			{
				b[s++] = (int) client_strings[randnum][j];
				j++;
			}
			Send(&p[99],b);
			numOfSends = ((size-8)/10);
			if(((size-8)%10)!=0)
			{
				numOfSends+=1;
			}
			printf("\n");
			for(m=0; m<numOfSends; m++)
			{
				
				s=0;
				while(s<10)
				{
					b[s++] = (int) client_strings[randnum][j];
					j++;
					if((char)b[s]=='\0')
					{
						break;
					}
				}
				Send(&p[99],b);	
			}
		}	
                
		if(add_or_del == 1)
		{
			printf("Request to add/modify %s from Client 2 on position %d ",client_strings[randnum], randnum+1);
		//	printf("%s",client_strings[randnum]);
			printf("\n\n");
		}
		else
		{
			printf("Request to delete from Client 2 on position %d", randnum+1);
			//printf("%s",client_strings[randnum]);
			printf("\n\n");
		}

		printf("########## Client 2: Send successful\n\n");
		sleep(1);

		int d[10];
		Receive(&p[0],d);
		printf("Received Success by Client 2 on port 0");
		printf("\n\n");
		sleep(1);
	}
}

void client3()
{
	char **print_table;
        print_table = (char **) malloc(5*sizeof(char *));
        int j=0,k=0;
	int numToPrint=0;
       	for(j=0;j<10;j++)
        		print_table[j]=(char *)malloc(20*sizeof(char));
	int count = 1;
	int a[10];
        while(1)
	{
		Receive(&p[98],a);
		count++;
		numToPrint = a[0];
		for(k=0;k<numToPrint;k++)
		{
			Receive(&p[98],a);
             		char *msg;
			msg=(char*)malloc(20*sizeof(char));
          	        int size = a[0];
			int numOfRecvs=0;
			int m=0;
                	j=0; i=1;
			if(size <= 9)
			{
				while(j<size)
				{
                	 		msg[j++]= (char) a[i++];
				}
			}
			else
			{
				while(j < 9)
				{
					msg[j++]= (char) a[i++];
				}
				numOfRecvs = ((size-9)/10);
				if(((size-9)%10)!=0)
				{
					numOfRecvs+=1;
				}
				for(m=0; m<numOfRecvs; m++)
				{
					int end = 10;
					if(m == numOfRecvs-1)
					{
						end = (size-9)%10;
					}
					Receive(&p[98],a);
					i=0;
					while(i<end)
					{
						msg[j++] = (char) a[i++];
						if((char)a[i]=='\0')
						{
							break;
						}
					}
					
				}
			}	
			print_table[k]=msg;
		}
		if(count%3 == 0)
		{
			printf("\n###########Yielding and client 3 not printing##########\n\n");
			yield();
	
		}
		else
		{
			printf("\n\nPrinting from Client 3###########################\n\n");
			for(k=0;k<10;k++)
			{       
                                if(strcmp(print_table[k],"empty") != 0)
	                 	     printf("%s\n", print_table[k]);
			}
			printf("\n\n");
		}
	}
}


void server()
{
        char **server_table;
        server_table = (char **) malloc(5*sizeof(char *));
        int j=0;
	int numOfStrings =0;
        for(j=0;j<10;j++)
        server_table[j]=(char *)malloc(20*sizeof(char));

        server_table[0]="abc123";
        server_table[1]="def123";
        server_table[2]="ghi123";
        server_table[3]="jkl123";
        server_table[4]="mno123";
        server_table[5]="pqr567";
        server_table[6]="stu567";
        server_table[7]="vwx567";
        server_table[8]="yza567";
        server_table[9]="abc567";
	while(1)
        {
		int num[10];
                int a[10];
		for(j=0;j<10;j++)
		{
			num[j]=0;
		}
		Receive(&p[97],num);
		int add_or_mod = num[0];
		Receive(&p[99],a);
                char *msg;
		msg=(char*)malloc(20*sizeof(char));
                int index = a[0];
                int size = a[1];
		int numOfRecvs=0;
		int m=0;
                j=0; i=2;
		if(size <= 8)
		{
			while(j<size)
			{
                 		msg[j++]= (char) a[i++];
			}
		}
		else
		{
			while(j < 8)
			{
				msg[j++]= (char) a[i++];
			}
			numOfRecvs = ((size-8)/10);
			if(((size-8)%10)!=0)
			{
				numOfRecvs+=1;
			}
			for(m=0; m<numOfRecvs; m++)
			{
				int end = 10;
				if(m == numOfRecvs-1)
				{
					end = (size-8)%10;
				}
				Receive(&p[99],a);
				i=0;
				while(i<end)
				{
					msg[j++] = (char) a[i++];
					if((char)a[i]=='\0')
					{
						break;
					}
				}
					
			}
		}
                if(add_or_mod == 1)
                {
                        if(strcmp(server_table[index],"empty") == 0)
                	{
                       		printf("------------------------> Adding string '%s' to server table", msg);
                       		server_table[index]=msg;
                	} else { 
                       		printf("------------------------> Modifying string '%s' to '%s' in server table", server_table[index],msg);
		       		server_table[index]=msg;
                	}
                } 
		else 
		{
			if(strcmp(server_table[index],"empty") == 0)
			{
				printf("Cannot delete!! Already empty");
			}
			else
			{

	                        printf("------------------------> Deleting string '%s' from server table", server_table[index]);        
	                        server_table[index] = "empty";
			}
                }
		printf("\n\n");
                sleep(1);
		int b[10];
		int k=0;
		numOfStrings=10;
		for(k=0;k<10;k++)
		{
			b[k]=0;
		}
		b[0]=numOfStrings;
		Send(&p[98],b);
		for(k=0;k<numOfStrings;k++)
		{
			int s=0;
           		int j=0,m=0;
			int numOfSends;
                	b[s++] = strlen(server_table[k]);
			int size = b[s-1];
			if(size < 9)
			{
				while (j < size)
				{
					b[s++] = (int) server_table[k][j];
					j++;
				}
				b[s++]='\0';
				Send(&p[98],b); 
			}
			else
			{
				while(s < 10)
				{
					b[s++] = (int) server_table[k][j];
					j++;
				}
				Send(&p[98],b);
				numOfSends = ((size-9)/10);
				if(((size-9)%10)!=0)
				{
					numOfSends+=1;
				}
				printf("\n");
				for(m=0; m<numOfSends; m++)
				{			
					s=0;
					while(s<10)
					{
						b[s++] = (int) server_table[k][j];
						j++;
						if((char)b[s]=='\0')
						{
							break;
						}
					}
					Send(&p[98],b);	
				}
			}	
		}
		printf("Sending Success from Server to Client");
		printf("\n\n");
                Send(&p[0],a);
                printf("##########  Server: Send successful\n\n");
	}
}

int main()
{   
	printf("The server has 10 strings and a client has some pre coded strings. The Client 1 and 2 picks up an index to modify/add/delete in a random fashion and the operation is also decided randomly. Client 1 sends the operation id, index of the string it needs to modify and the string to add/modify. The server receives everything and based on the operation id, it does the operation and calls Client 3 to print the server table. Client 3 prints them all and send a success and the server sends a success back to Client 1 and Client 2. Once in every 3 times, client 3 yields and skips the printing. \nThe server sees the index position. If it is not empty, it does a modify. If it is empty, it just adds the string in that specified index. Delete just makes the index empty for add to fill it up.");
	sleep(5); 	
	initialize_ports();
	printf("\n***********Starting Client 1**********\n");
	startThread(client1);
	printf("\n***********Starting Client 2**********\n");
	startThread(client2);
	printf("\n***********Starting Client 3**********\n");
	startThread(client3);
	printf("\n***********Starting Server************\n\n");
	startThread(server);
	run();
	while (1) 
        {
        	sleep(1);
        }
}
