package main.serverbackend;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.serverbackend.utilities.EncryptString;
/**
 * 
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/25/2023
 */
public class ClientHandler implements Runnable{

	public static TreeMap<String,ClientHandler> clientHandlers = new TreeMap<>();
	private Socket socket;
	private BufferedReader bufferedReader; //used to interpret messages
	private BufferedWriter bufferedWriter; //used to broadcast messages to the server
	private String clientUsername; //username of client whom this clienthandler is associated with
	
    private final String UNICODE_FORMAT = "UTF-8";
    private final byte[] encoded = {-114, -117, 47, 86, 23, -19, -78, 98, -32, 86, 44, -9, -112, 124, -20, 27};//Encryption key to communicate locally in a semi-secure manner
    private final SecretKey key = new SecretKeySpec(encoded, "AES");
    
	private JTextArea terminal; 
	private JScrollPane terminalScrollBar;
	private JScrollBar adjustScroll;
    
	public ClientHandler(Socket socket,JTextArea terminal,JScrollPane terminalScrollBar, JScrollBar adjustScroll) {
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter (new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader (new InputStreamReader(socket.getInputStream()));
			this.clientUsername = bufferedReader.readLine();
			this.terminal = terminal;
			this.terminalScrollBar = terminalScrollBar;
			this.adjustScroll = adjustScroll;
			
			if(isUsernameUsed(clientUsername)) {
				clientUsername = "null";
				closeDuplicate(socket, bufferedReader, bufferedWriter);
				return;
			}

			clientHandlers.put(clientUsername,this); 
			broadcastMessageAllEncrypted(clientUsername + ": has entered the chat!");
			updateServerConsole( "A New Client " + "'" + clientUsername + "'" + " Has Connected From IP Address: " + socket.getRemoteSocketAddress().toString());
		} catch(IOException e) {
			String message = clientUsername + ": has Left the Chat";
			if(clientUsername.equals("null")) {
				message ="Client Who Tried Connecting With Duplicate Display Name Was Rejected";
			}
			updateServerConsole(message);
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
	}
	
	
	/**This method is used to continually scan for messages from all clients.
	 * The messages are then interpreted for meaning, any message with a colon ':'
	 * character are messages for server use only and are filtered and handled appropriately.
	 */
	@Override
	public void run() {
		String messageFromClient;
		while (socket.isConnected()) {
			try {
				messageFromClient = bufferedReader.readLine();
				messageFromClient = EncryptString.decryptString(messageFromClient,key);
				if(messageFromClient == null) {
					throw new IOException();
				} 
				if(messageFromClient.contains("messageOthers:")) {
					String temp[] = messageFromClient.split(":", 3);
					String username = temp[1];
					messageFromClient = temp[2];
					broadcastMessageOthersEncrypted(username + ":" + messageFromClient,username);	
					continue;
				}
				broadcastMessageAllEncrypted(messageFromClient);
				
			} catch(IOException e) {
				String message = clientUsername + ": has Left the Chat";
				if(clientUsername.equals("null")) {
					message ="Client Who Tried Connecting With Duplicate Display Name Was Rejected";
				}
				updateServerConsole(message);
				closeEverything(socket, bufferedReader, bufferedWriter);
				break;
			}
		}
	}
	
	//forward message from a client to ALL client nodes
	private void broadcastMessageAllEncrypted (String messageToSend) {
		for (Map.Entry<String,ClientHandler> entry : clientHandlers.entrySet()) {// for all clients connected to the server
				try {
					ClientHandler clientHandler = entry.getValue();
			        String encryptedMessage = EncryptString.encryptString(messageToSend,UNICODE_FORMAT, key);

				     //if the client sending the message is found in the list of connected clients, echo message to all nodes
			        clientHandler.bufferedWriter.write(encryptedMessage);
					clientHandler.bufferedWriter.newLine();
					clientHandler.bufferedWriter.flush();
					
				}catch (IOException e) {
					String message = clientUsername + ": has Left the Chat";
					if(clientUsername.equals("null")) {
						message ="Client Who Tried Connecting With Duplicate Display Name Was Rejected";
					}
					updateServerConsole(message);
					closeEverything(socket, bufferedReader, bufferedWriter);
				}
		}
	}
	
	//forward message from a client to all OTHER client nodes
	private void broadcastMessageOthersEncrypted (String messageToSend, String username) {
		for (Map.Entry<String,ClientHandler> entry : clientHandlers.entrySet()) {
			if(!entry.getKey().equals(username)) { //if client username is not the same as the one who sent the message
				try {
					ClientHandler clientHandler = entry.getValue();
					
			        String encryptedMessage = EncryptString.encryptString(messageToSend,UNICODE_FORMAT, key);

				     //if the client sending the message is found in the list of connected clients, echo message to all OTHER nodes
					clientHandler.bufferedWriter.write(encryptedMessage);
					clientHandler.bufferedWriter.newLine();
					clientHandler.bufferedWriter.flush();
					
				}catch (IOException e) {
					String message = clientUsername + ": has Left the Chat";
					if(clientUsername.equals("null")) {
						message ="Client Who Tried Connecting With Duplicate Display Name Was Rejected";
					}
					updateServerConsole(message);
					closeEverything(socket, bufferedReader, bufferedWriter);
				}
			}
		}
	}
	
	//discard client from list of connected users, and tell other clients about their disconnection
	private void removeClientHandler() {
		clientHandlers.remove(clientUsername);
		broadcastMessageAllEncrypted(clientUsername + ": has Left the Chat!");
	}
	
	//close all resources maintaining a chat connection
	private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
			removeClientHandler();
		try {
			if(bufferedReader != null) {
				bufferedReader.close();
			}
			if(bufferedWriter != null) {
				bufferedWriter.close();
			}
			if(socket != null) {
				socket.close();
			}				
		}catch(IOException e){
			String message = clientUsername + ": Encountered an Issue While Disonnecting";
			if(clientUsername.equals("null")) {
				message ="Client Who Tried Connecting With Duplicate Display Name Encountered Error While Disconnecting";
			}
			updateServerConsole(message);
		}
		
	}
	//close all resources maintaining a chat connection for any client with a duplicate username
	private void closeDuplicate(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

		try {
			if(bufferedReader != null) {
				bufferedReader.close();
			}
			if(bufferedWriter != null) {
				bufferedWriter.close();
			}
			if(socket != null) {
				socket.close();
			}				
		}catch(IOException e){
			String message = clientUsername + ": Encountered an Issue While Disonnecting";
			if(clientUsername.equals("null")) {
				message ="Client Who Tried Connecting With Duplicate Display Name Encountered Error While Disconnecting";
			}
			updateServerConsole(message);
		}
		
	}
	//check if list of connected users has the username of the client trying to connect
	private boolean isUsernameUsed(String username){
		for(Map.Entry<String,ClientHandler> entry: clientHandlers.entrySet()) {
			if(entry.getKey().equals(username)) {
				return true;
			} 
		}
		return false;
	}
	//main server console will be updated with message
	private void updateServerConsole(String message) {
		terminal.append(message + "\n");
		terminalScrollBar.revalidate();
		adjustScroll.setValue(adjustScroll.getMaximum());
	}
}// end of class

