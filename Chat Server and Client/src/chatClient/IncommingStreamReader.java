package chatClient;


import java.io.ObjectInputStream;

public class IncommingStreamReader implements Runnable {

	Client chatClient;
	ObjectInputStream is;
	
	IncommingStreamReader (Client chatClient, ObjectInputStream is) {
		
		this.chatClient = chatClient;
		this.is = is;
		
	}
	
	public void run() {
		
		Object o; 
		
		try {	
			
			while ((o = is.readObject()) != null) {
				String message = (String) o;
				System.out.println("-> client recieved: " + message); 
				chatClient.displayMessage(message);
			} // close while
						
		} catch(Exception e) {
			chatClient.setConnected(false);
			chatClient.setOutgoingFieldEnabled(false);
			chatClient.displayMessage("Connection to Server has been Lost!");
			//e.printStackTrace();
		}

	} // close run
	
	
}
