package com.tomwang;
/*
 * Copyright (c) 2016, TomWang. All rights reserved.
 *
 * 
 */ 

import java.net.*;
import java.io.*;


public class EasyVPNServer {
	
	static ProxyServerThread proxyServerThread;
	static LocalProxyServerThread localProxyServerThread;  
	
public class ProxyServerThread extends Thread{
		   Socket proxyClientSocket;
		   Socket localProxyClientSocket;
		   ProxyServerThread(Socket proxyClientSocket, Socket localProxyClientSocket) {
		      this.proxyClientSocket = proxyClientSocket;
		      this.localProxyClientSocket = localProxyClientSocket;
		   }

		   public void run() {
		      try {
		    	
		        final BufferedOutputStream proxyServerOut = new BufferedOutputStream(proxyClientSocket.getOutputStream());
		        final BufferedInputStream localProxyServerIn = new BufferedInputStream(localProxyClientSocket.getInputStream());
		        final byte[] buffer = new byte[4096];
		        for (int read = localProxyServerIn.read(buffer); read >= 0; read = localProxyServerIn.read(buffer))
		        {
		        	proxyServerOut.write(buffer, 0, read);
		        	proxyServerOut.flush();
		        	
		        //	System.out.println("From Client:" + new String(buffer));
		        	
		        }


				

		        
		      }
		      catch (IOException e) {
		         System.out.println(e);
					try {
						proxyClientSocket.close();
						localProxyClientSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

		      }
		      
		    	System.out.println("ProxyServerThread finished!");
		   }
		}	

public class LocalProxyServerThread extends Thread{
	   Socket proxyClientSocket;
	   Socket localProxyClientSocket;
	   LocalProxyServerThread(Socket proxyClientSocket, Socket localProxyClientSocket) {
	      this.proxyClientSocket = proxyClientSocket;
	      this.localProxyClientSocket = localProxyClientSocket;
	   }

	   public void run() {
	      try {

	    	final BufferedOutputStream localProxyServerOut = new BufferedOutputStream(localProxyClientSocket.getOutputStream());
	        final BufferedInputStream proxyServerIn = new BufferedInputStream(proxyClientSocket.getInputStream());
	        final byte[] buffer = new byte[4096];
	        for (int read = proxyServerIn.read(buffer); read >= 0; read = proxyServerIn.read(buffer))
	        {
	        	localProxyServerOut.write(buffer, 0, read);
	        	localProxyServerOut.flush();
	        //	System.out.println("From Server:" + new String(buffer));
	        }
	        
	      }
	      catch (IOException e) {
	         System.out.println(e);
				try {
					proxyClientSocket.close();
					localProxyClientSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
	      }
	      
	      System.out.println("LocalProxyServerThread finished");
	   }
	}	
	
    public static void main(String[] args) throws IOException {
        
        if (args.length != 2) {
            System.err.println("Usage: java EasyVPNServer <remote port number> <local port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        int portNumber2 = Integer.parseInt(args[1]);

        try ( 
            ServerSocket proxyServerSocket = new ServerSocket(portNumber);
            ServerSocket localProxyserverSocket = new ServerSocket(portNumber2);
        	
        ) {
        	EasyVPNServer easyVPNServer = new EasyVPNServer();
        	
        	while(true)
        	{
	            Socket proxyClientSocket = proxyServerSocket.accept();	
	            Socket localProxyClientSocket = localProxyserverSocket.accept();   		
	
	        	proxyServerThread = easyVPNServer.new ProxyServerThread(proxyClientSocket, localProxyClientSocket);
	
	       	 	proxyServerThread.start();
	       	 	localProxyServerThread = easyVPNServer.new LocalProxyServerThread(proxyClientSocket, localProxyClientSocket);
	       	 	localProxyServerThread.start();       	 	
       	 	
        	}

  
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or " + portNumber2 + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
