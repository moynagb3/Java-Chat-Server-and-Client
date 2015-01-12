package chatClient;

import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;

public class Client implements ChatClient {
	
	private static final int PORT_NUM = 5055;
	private static final String SERVER_IP = "127.0.0.1";
	
	JTextPane incoming;
	JTextField outgoing; 
	JButton connectButton, sendButton;
	
	BufferedReader reader; 
	PrintWriter writer; 
	Socket socket;
	
	boolean connected;
	
	public void init() {
		
		setConnected(false);
		
		JFrame frame = new JFrame("Chat Client"); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(320,400));
		
		Box northPanel = new Box(BoxLayout.LINE_AXIS);
		Border border = BorderFactory.createEmptyBorder(15,15,15,15);
		northPanel.setBorder(border);
		
		connectButton = new JButton("<html>Connect</html>");
		connectButton.addActionListener(new ConnectListener());
		northPanel.add(connectButton);
		
		//JPanel to hold the JTextArea
		JPanel centrePanel = new JPanel(new BorderLayout());
		border = BorderFactory.createEmptyBorder(0,15,15,15);
		centrePanel.setBorder(border);
		
		incoming = new JTextPane();
		incoming.setContentType("text/html");
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming); 
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); 
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		centrePanel.add(qScroller);

		Box southPanel = new Box(BoxLayout.PAGE_AXIS);
		border = BorderFactory.createEmptyBorder(0,15,15,15);
		southPanel.setBorder(border);
		
		outgoing = new JTextField(20);
		outgoing.addActionListener(new SendListener());
		
		southPanel.add(outgoing);
		southPanel.add(Box.createVerticalStrut(5));
		
		sendButton = new JButton("<html>Send</html>");
		sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		sendButton.addActionListener(new SendListener()); 
		southPanel.add(sendButton);
		
		frame.getContentPane().add(BorderLayout.NORTH, northPanel);
		frame.getContentPane().add(BorderLayout.CENTER, centrePanel);
		frame.getContentPane().add(BorderLayout.SOUTH, southPanel);
		
		frame.setSize(320,400);
		frame.pack();
		setOutgoingFieldEnabled(false);
		frame.setVisible(true);
		
	} // close go

	private void setUpNetworking() {
		
		try {
			socket = new Socket(SERVER_IP, PORT_NUM);
			InputStreamReader streamReader = new InputStreamReader(socket.getInputStream()); 
			reader = new BufferedReader(streamReader);
			
			writer = new PrintWriter(socket.getOutputStream()); 
			System.out.println("networking established");
			
			Thread readerThread = new Thread(new IncommingStreamReader(this, reader));
			readerThread.start();
			
			setConnected(true);
			setOutgoingFieldEnabled(true);
			connectButton.setText("<html>Disconnect</html>");
			displayMessage("Connected to Server!");
			
		} catch(ConnectException e) {
			displayMessage("Could not connect to Server!");
		} catch(IOException e) {
			e.printStackTrace(); 
		}
	} // close setUpNetworking

//-------------------------------------------------------------------------------------//
//-------------------------------------------------------------------------------------//
	
	public void setOutgoingFieldEnabled(boolean enabled) {
		outgoing.setEnabled(enabled);
		sendButton.setEnabled(enabled);
	}
	
	public void displayMessage(String message){
		
    try {
    	StyledDocument document = (StyledDocument) incoming.getDocument();
			document.insertString(document.getLength(), ("    " + message + "\n"), null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
    
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
//---------------------------- ActionListeners ----------------------------------------//
//-------------------------------------------------------------------------------------//
	
	public class SendListener implements ActionListener { 
		
		public void actionPerformed(ActionEvent ev) {
			
			String message = outgoing.getText();
			
			if(message.length()>0) {
				
				try {
					System.out.println("Client is writing -> " + outgoing.getText());
					writer.println(outgoing.getText()); 
					writer.flush();
					System.out.println("Client is FINISHED writing");
				} catch(Exception ex) {
					ex.printStackTrace(); 
				}
				
				outgoing.setText("");
				outgoing.requestFocus();
				
			}//End if
			 
		}//End Method
		
	}//End inner class

	public class ConnectListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(!connected)	{
				setUpNetworking();
			}
			
		}
		
	}

//-------------------------------------------------------------------------------------//
//-------------------------------------------------------------------------------------//
	
	public static void main(String[] args) { 
		
		SwingUtilities.invokeLater(new Runnable () {
    	
			public void run () {
				Client client = new Client();
				client.init(); 
			}
			
		});
		
	}
	
} // close outer class
