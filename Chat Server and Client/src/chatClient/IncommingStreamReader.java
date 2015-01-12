package chatClient;

import java.io.BufferedReader;

public class IncommingStreamReader implements Runnable {

	Client chatClient;
	BufferedReader reader;
	
	IncommingStreamReader (Client chatClient, BufferedReader reader) {
		
		this.chatClient = chatClient;
		this.reader = reader;
		
	}
	
	public void run() {
		
		String message; 
		
		try {	
			
			while ((message = reader.readLine()) != null) { 
				System.out.println("-> client recieved: " + message); 
				chatClient.displayMessage(message);
			} // close while
			
			chatClient.setConnected(false);
			chatClient.setOutgoingFieldEnabled(false);
			chatClient.displayMessage("Connection to Server has been Lost!");
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}

	} // close run
	
	
}
