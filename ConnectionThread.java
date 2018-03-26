import java.io.*;
import java.net.*;
import java.util.*;

public class ConnectionThread extends Thread{
	public Pdata pdata;
	public int node_connected_num;
	public ServerSocket serverSocket = null;
	public Socket socket = null;
	public DataOutputStream dout;
	public DataInputStream dis;
    		    
	public ConnectionThread(int i, Pdata pdata){
		// keeping pdata initially with node 1
		this.pdata=pdata;
		this.node_connected_num=i;		
	}
	
	public void run() {
		try
		{
			// if server
			int port;
			if(node_connected_num>pdata.pid)
			{
                System.out.println("Server side "+ node_connected_num +">" + pdata.pid );
				// assigning port number for every connection
				port=pdata.pid*2000+node_connected_num;

        		try {
                    System.out.println("********************************************");
        			System.out.println("port number for connecting Server = "+port);
					serverSocket = new ServerSocket(port);
					socket = serverSocket.accept();
                    System.out.println("connection accepted = "+port);
					
					dout=new DataOutputStream(socket.getOutputStream());
					dis=new DataInputStream(socket.getInputStream());
					
					}

				catch (Exception e) {
					System.out.println("exception 1 here in if loop");
					e.printStackTrace();
					}
			}
			
			else if (node_connected_num < pdata.pid && node_connected_num!=0)
			{
                System.out.println("********************************************");                
                System.out.println("Enters client "+ node_connected_num +" < " + pdata.pid +" && "+ node_connected_num + "!=0");
				port=pdata.pid+node_connected_num*2000;
				try{
					System.out.println("port number for connecting = "+port);
					socket=new Socket(InetAddress.getByName("127.0.0.1"),port);
                    System.out.println("socket initialized to local host");
					dout=new DataOutputStream(socket.getOutputStream());
					dis=new DataInputStream(socket.getInputStream());					
						 }
                catch(Exception e){
                	System.out.println("exception2 here in else if");
					System.out.println(e);				
				}
            }
        }
        catch(Exception e){
                    System.out.println("Exception in total ");   
					System.out.println(e);				
				}
 }
    
   
	
	public void sendpdata(int num)
    {
        System.out.println("*********************************************");
        System.out.println("process"+ num);
        
    		if(num==node_connected_num)
			{
                System.out.println("if " + num + " = node_connected_num");
				try{
					
				System.out.println("Sending pdata to "+ num);
				dout.writeUTF(pdata.lclock[pdata.pid] + "+" + pdata.pid);
				
                    
                } catch (IOException e) {
                    System.out.println("exception4 here in sendpdata");
                    e.printStackTrace();
                }
            }
        System.out.println("**********************************************");
    }
	
	public void receive()
	{
		try{
		
		
		if(dis.available() > 0) {
			String s = dis.readUTF();
			String[] a = s.split("\\+");
			int id = Integer.parseInt(a[1]); 
			int count = Integer.parseInt(a[0]);
			System.out.println("count received " + count);		
						if(count>pdata.lclock[id])
						{
							pdata.lclock[id] = count;
						}					
        
        }
		else
		{
			System.out.println("no data to receive");
		}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}