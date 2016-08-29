package com.tomwang;
/*
 * Copyright (c) 2016, TomWang. All rights reserved.
 *
 * 
 */ 

import java.io.*;
import java.net.*;

public class EasyVPNClient {
	
public class TargetClientThread extends Thread{
	   Socket ProxyClientSocket;
	   Socket TargetServerClientSocket;
	   TargetClientThread(Socket ProxyClientSocket, Socket TargetServerClientSocket) {
	      this.ProxyClientSocket = ProxyClientSocket;
	      this.TargetServerClientSocket = TargetServerClientSocket;
	   }

	   public void run() {
	      try {
	   // 	String outputLine;  

	        final BufferedOutputStream ProxyServerOut = new BufferedOutputStream(ProxyClientSocket.getOutputStream());
	        final BufferedInputStream TargetServerIn = new BufferedInputStream(TargetServerClientSocket.getInputStream());
	        final byte[] buffer = new byte[4096];
	        for (int read = TargetServerIn.read(buffer); read >= 0; read = TargetServerIn.read(buffer))
	        {
	        //	outputLine = new String(buffer);
	        	ProxyServerOut.write(buffer, 0, read);
	        	ProxyServerOut.flush();
	        //	System.out.println("From Server:" + outputLine);
	        	
	        }

	      }
	      catch (IOException e) {
	         System.out.println("TargetClientThread:" + e);
	         
		        try {
					TargetServerClientSocket.close();
			        ProxyClientSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

	      }
	      
	      System.out.println("TargetClientThread finished!");
	   }
	}	
	
    public static void main(String[] args) throws IOException {
        
        if (args.length != 4) {
            System.err.println(
                "Usage: java EasyVPNClient <EasyVPNServer host name> <port number> <Service Host Name> <Service Port Number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        String hostName2 = args[2];
        int portNumber2 = Integer.parseInt(args[3]);

        Socket ProxyClientSocket = null;
        Socket TargetServerClientSocket = null;
        
        try {
       // 	String inputLine;	
            ProxyClientSocket = new Socket(hostName, portNumber);
            TargetServerClientSocket = new Socket(hostName2, portNumber2);	
            
        	EasyVPNClient easyVPNClient = new EasyVPNClient();
        	
           	TargetClientThread targetClientThread = easyVPNClient.new TargetClientThread(ProxyClientSocket, TargetServerClientSocket);
        	targetClientThread.start();       	 	
            
	        final BufferedOutputStream TargetServerOut = new BufferedOutputStream(TargetServerClientSocket.getOutputStream());
	        final BufferedInputStream ProxyServerIn = new BufferedInputStream(ProxyClientSocket.getInputStream());
	        final byte[] buffer = new byte[4096];
	        for (int read = ProxyServerIn.read(buffer); read >= 0; read = ProxyServerIn.read(buffer))
	        {
	    //    	inputLine = new String(buffer);
	    //    	System.out.println("From Client:" + inputLine);
	        	TargetServerOut.write(buffer, 0, read);
	        	TargetServerOut.flush();
	        }
	        
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName + " or host " + hostName2);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName + " or " + hostName2);
            System.exit(1);
        }
        finally
        {
        	if(TargetServerClientSocket!=null)
        		TargetServerClientSocket.close();
        	if(ProxyClientSocket!=null)
        		ProxyClientSocket.close();        	
        }
        
    }
}
