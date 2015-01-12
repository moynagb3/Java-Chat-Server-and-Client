package chatServer;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements Broadcaster, Registrar{
	
	private static final int PORT_NUM = 5055;
	
	private boolean listening;
	
	Map <Integer, PrintWriter> clientOutputStreams;

	public void initialise (){
		
		clientOutputStreams = new ConcurrentHashMap<>();
		listening = true;
				
			try {
				
				ServerSocket serverSocket = new ServerSocket(PORT_NUM);
				
				
				try {
					
					
					while(listening) {
						
						System.out.println("Server is listening on port: " + PORT_NUM);
						
							Socket clientSocket = serverSocket.accept();	
							Thread t = new Thread(new ClientHandler(this, clientSocket));
							t.start();
							
							System.out.println("Client connected from: " + clientSocket.getInetAddress() 
																														+ "::" + clientSocket.getPort());		
							
							System.out.println(clientOutputStreams.size() + " clients connected to server.");
							
					}//End while
					
				} finally {
					 serverSocket.close();
				}
								
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}//End initialise method
	
//--------------------- Implement the Registrar Interface -----------------------------//
//-------------------------------------------------------------------------------------//
	
	public void register(int id, PrintWriter writer) {
		clientOutputStreams.put(id, writer);
	}

	public void deregister(int id){
		clientOutputStreams.remove(id);
	}
	
//--------------------- Implement the Broadcaster Interface -----------------------------//
//-------------------------------------------------------------------------------------//
	
	public void broadcast(String message) {
		
		synchronized(clientOutputStreams) {
			
			for (PrintWriter writer : clientOutputStreams.values()) {
				try {
					writer.println(message);
					writer.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}//End for

		}//End synchronized
		
	}//End broadcast method

//-------------------------------------------------------------------------------------//
//-------------------------------------------------------------------------------------//
		
	public static void main(String args []) {
		new Server().initialise();
	}

}//End Server class
