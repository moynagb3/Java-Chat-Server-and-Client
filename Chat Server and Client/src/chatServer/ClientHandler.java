package chatServer;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
	
	static int nextClientId;
	
	BufferedReader reader;
	PrintWriter writer;
	Server server;
	Socket clientSocket;
	int clientId;
	
	public ClientHandler(Server server, Socket clientSocket) {
		
		try {
			this.clientSocket = clientSocket;
			this.server = server;
			this.clientId = nextClientId;
			nextClientId++;

			//System.out.println("Client " + clientId + " connected to Server");
			
			writer = new PrintWriter(clientSocket.getOutputStream());
			server.register(clientId, writer);
			
			InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
			reader = new BufferedReader(isReader);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}//End constructor
	
	@Override
	public void run() {
	
		String message;
		
		try {
			
			while((message = reader.readLine()) != null) {
				server.broadcast(this.clientId +": " +  message);
			}//close while
			
			closeSocket();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}//end run
	
	//gracefully close the socket connection 
  public void closeSocket() { 
    
  	try {
  		//this.os.close();
      reader.close();
      clientSocket.close();
      
      //System.out.println("Client " + clientId + " disconnected from Server");
      
      server.deregister(clientId);
    } catch (Exception e) {
      System.out.println("XX. " + e.getStackTrace());
    }
  	
  }//End closeSocket method
	
}//end class
