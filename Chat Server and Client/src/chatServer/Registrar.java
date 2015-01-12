package chatServer;

import java.io.PrintWriter;

public interface Registrar {

	public void register(int id, PrintWriter writer);
	
	public void deregister(int id);
	
}
