package chatClient;

public interface ChatClient {

	public void displayMessage(String message);
	
	public void setConnected(boolean connected);
	
	public void setOutgoingFieldEnabled(boolean enabled);
	
}
