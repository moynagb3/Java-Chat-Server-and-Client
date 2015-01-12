package chatServer;

import java.io.ObjectOutputStream;

public interface Registrar {

	public void register(int id, ObjectOutputStream os);
	
	public void deregister(int id);
	
}
