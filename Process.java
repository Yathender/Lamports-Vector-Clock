import java.io.*;
import java.net.*;
import java.util.*;  

public class Process
{
	public static Pdata pdata;
	
	public static void main(String args[]){

		pdata = new Pdata();

		System.out.println("*************************************************************************************");
		System.out.println("Process  " + args[0] + "  of  " +args[1] + " processess ");
		System.out.println("*************************************************************************************");

		String node_str = new String(args[0]);

		//System.out.println("enteresd process");

		pdata.pid = Integer.parseInt(args[0]);
		String total_str = new String(args[1]);
		pdata.total_num = Integer.parseInt(args[1]);
		Arrays.fill(pdata.lclock, 0);
		ArrayList<ConnectionThread> threads = new ArrayList<ConnectionThread>(pdata.total_num+1);					

		for(int i=0;i<=pdata.total_num;i++)
		{
			threads.add(new ConnectionThread(i,pdata));
			threads.get(i).start();	
		}	

		Scanner sc = new Scanner(System.in);
		
		while(true)
		{		
			//System.out.println("polling and enter 1 if you want to enter critical section");
			
			
            try
			{
				Thread.sleep(5000);
				System.out.println("Testing at ..." + new Date());
				
                
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
			
			float probability = new Random().nextFloat();      //generate random event;
				
				if(probability <= 0.40)
				{
					System.out.println("*************************************************************************************");
					System.out.println("Send event");
		            System.out.println("*************************************************************************************");
					
					int p = 0;

                    while((p==0)||(p == pdata.pid))            //generate random process number to send
					{
						p = new Random().nextInt(pdata.total_num+1);					    
						
					}
					System.out.println("Sending to " + p);
					threads.get(p).sendpdata(p);
					pdata.lclock[pdata.pid]++;
				System.out.print("clock value updated to : [ " );
					for(int k=1; k<=pdata.total_num; k++)
					{
						System.out.print(","+pdata.lclock[k]);
					}
					System.out.print("]" );
				}
				
				else if(probability <= 0.80)
				{
					System.out.println("*************************************************************************************");
		            System.out.println("Receive event");
				    System.out.println("*************************************************************************************");
		            
					int p = 0;

                    while((p==0)||(p == pdata.pid))           // generate random process number to receive
					{
						p = new Random().nextInt(pdata.total_num+1);					    
						
					}
					System.out.println("Receiving from " + p);
					threads.get(p).receive();	
					pdata.lclock[pdata.pid]++;
				System.out.println("clock value updated to : [ " );
					for(int k=0; k<pdata.total_num; k++)
					{
						System.out.print(","+pdata.lclock[k]);
					}
					System.out.print("]" );
				}
				
				
				else if(probability<= 1)
				{
					System.out.println("*************************************************************************************");
		            System.out.println("*************************************************************************************");
		            System.out.println("Internal Event");
					
					pdata.lclock[pdata.pid]++;
				System.out.println("clock value updated to : [ " );
					for(int k=0; k<pdata.total_num; k++)
					{
						System.out.print(","+pdata.lclock[k]);
					}
					System.out.print("]" );
				}
				
			
			
		}				
	}
}