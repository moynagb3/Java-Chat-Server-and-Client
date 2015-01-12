package chatServer;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
	
	static int nextClientId;
	
	ObjectInputStream is;
	ObjectOutputStream os;
	
	Server server;
	Socket clientSocket;
	int clientId;
	
	//Constructor
	public ClientHandler(Server server, Socket clientSocket) {
		
		try {
			
			this.clientSocket = clientSocket;
			this.server = server;
			this.clientId = nextClientId;
			nextClientId++;
			
			os = new ObjectOutputStream(clientSocket.getOutputStream());
			server.register(clientId, os);
			
			is = new ObjectInputStream(clientSocket.getInputStream());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}//End constructor
	
	@Override
	public void run() {
	
		Object o;
		
		try {
			
			while((o = is.readObject()) != null) {
				//instead of broadcasting pass the message to a filter to decide who it should be sent
				String message = (String) o;
				server.broadcast(this.clientId +": " +  message);
			}//close while
			
		} catch (Exception e) {
			closeSocket();
			//e.printStackTrace();
		}
		
	}//end run
	
	//gracefully close the socket connection 
  public void closeSocket() { 
   
  	try {
  		os.close();
      is.close();
      clientSocket.close();
      
      System.out.println("Client " + clientId + " disconnected from Server");
      server.deregister(clientId);
      
    } catch (Exception e) {
    	
      System.out.println(e.getStackTrace());
    }
  	
  }//End closeSocket method
	
}//end class
