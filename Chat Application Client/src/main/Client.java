package main;

import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.backend.utilities.EncryptString;
import main.frontend.ClientUsernamePage;
/**
 * 
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/27/2023
 */
public class Client {
	private Socket socket;
	private BufferedReader bufferedReader; //used to interpret messages
	private BufferedWriter bufferedWriter; //used to broadcast messages to the server
	private String username; //username of this instance of the client
	TreeMap<String,JLabel> usernamePanel = new TreeMap<>();
    private final String UNICODE_FORMAT = "UTF-8";
    private final byte[] encoded = {-114, -117, 47, 86, 23, -19, -78, 98, -32, 86, 44, -9, -112, 124, -20, 27};//Key data
    private final SecretKey key = new SecretKeySpec(encoded, "AES");
    private int clientAcknowledge = 0;

	public Client(Socket socket, String username) {
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter (new OutputStreamWriter(socket.getOutputStream())); //use initialized socket as a resource for which data will be written to
			this.bufferedReader = new BufferedReader (new InputStreamReader(socket.getInputStream())); //use initialized socket as a resource for which data will be read from
			this.username = username;
		}catch (IOException e) {
			closeEverything("Error Occured Connecting to Chat Server, Restart Application");
		}
	}
	
	public static void main (String[] args) throws IOException {
		String username = ""; //this will be the username of THIS instance of the client who connected
		int port = 1234; //port to communicate on
		int screenSize = 0;
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	    int width = gd.getDisplayMode().getWidth();
	    int height = gd.getDisplayMode().getHeight();
	    
	    if(width == 1920 && height == 1080 && java.awt.Toolkit.getDefaultToolkit().getScreenResolution() == 96) {
	    	screenSize = 1;
			ClientUsernamePage firstPage = new ClientUsernamePage(username,port,screenSize);
	    }else if(width == 1920 && height == 1080 && java.awt.Toolkit.getDefaultToolkit().getScreenResolution() == 120){
	    	screenSize = 2;
			ClientUsernamePage firstPage = new ClientUsernamePage(username,port,screenSize);
	    }else {
	    	screenSize = 3;
			ClientUsernamePage firstPage = new ClientUsernamePage(username,port, screenSize);
	    }
		
	}
	//method to send a message to server automatically, this will be the username of the client
	public void initializeUsername() {
		try {
			bufferedWriter.write(username);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			
		} catch (IOException e) {
			closeEverything("Error Occured Connecting to Chat Server, Restart Application");
		}
		
	}
	
	//used to send messages by use of bufferedwriter+socket. These messages are taken from the user from commandline input and distributed to all nodes.
	public void messageAllNodes(String messageToSend) {
		if(socket.isConnected()) {
			try {
				messageToSend = username + ": " + messageToSend;
				String encryptedString = EncryptString.encryptString(messageToSend,UNICODE_FORMAT, key);
				bufferedWriter.write(encryptedString);
				bufferedWriter.newLine();
				bufferedWriter.flush();
				
			} catch (IOException e) {
				closeEverything("Error Occured Connecting to Chat Server, Restart Application");
			}
		}
		
	}
	//used to send messages by use of bufferedwriter+socket. These messages are taken from the user from commandline input and distributed to all OTHER nodes.
	private void messageOtherNodes(String messageToSend) {
		if(socket.isConnected()) {
			try {
				messageToSend = "messageOthers:" + username + ":" + messageToSend;
				String encryptedString = EncryptString.encryptString(messageToSend,UNICODE_FORMAT, key);
				bufferedWriter.write(encryptedString);
				bufferedWriter.newLine();
				bufferedWriter.flush();
				
			} catch (IOException e) {
				closeEverything("Error Occured Connecting to Chat Server, Restart Application");
			}
		}
		
	}
		//listen for messages in a separate thread to avoid application halting 
		public void listenForMessage(JTextArea messageBox,JScrollPane messageScrollBar, JScrollBar adjustScroll, JFrame chatFrame,JPanel fillerPanel) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					
					String systemMessage = "" ;
					String messageFromGroupChat = "";
					while(socket.isConnected()) {
						try {
							systemMessage = bufferedReader.readLine();
							if(systemMessage == null || systemMessage.length() == 0) {
								continue;
							}
					        messageFromGroupChat = EncryptString.decryptString(systemMessage,key);
					        if(messageFromGroupChat == null || messageFromGroupChat.length() == 0) {
								continue;
							}
					        if(messageFromGroupChat.equals("null: has Left the Chat!")) {
								continue;
							}
					        
							if(messageFromGroupChat.contains(": has entered the chat!")) {
								clientAcknowledge++;
								if(clientAcknowledge > 1) {
									messageOtherNodes("Client:Acknowledged");
								}
							}
							if(messageFromGroupChat.contains("Client:Acknowledged")) {
								if(isUsernameInUse(messageFromGroupChat)) {
									break;
								}
								isNewUser(messageFromGroupChat,fillerPanel);
								chatFrame.revalidate();
								chatFrame.repaint();
								continue;
							}
							if(isNewUser(messageFromGroupChat,fillerPanel)) {
								chatFrame.revalidate();
								chatFrame.repaint();
								updateChatBox(messageFromGroupChat,messageBox,messageScrollBar,adjustScroll);
								
							}else if(leftChat(messageFromGroupChat,fillerPanel)) {
								updateChatBox(messageFromGroupChat,messageBox,messageScrollBar,adjustScroll);
								chatFrame.revalidate();
								chatFrame.repaint();
								continue;
								
							} else {
								updateChatBox(messageFromGroupChat,messageBox,messageScrollBar,adjustScroll);
							}

						} catch (Exception e) {
							closeEverything("Error Occured Connecting to Chat Server, Restart Application");
							chatFrame.dispatchEvent(new WindowEvent(chatFrame, WindowEvent.WINDOW_CLOSING));
							return;
						}
					}
				}
			}).start();
		}
	
		private void closeEverything(String message) {
			JOptionPane.showMessageDialog(null, message);
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
				e.printStackTrace();
			}
		}
		private boolean isUsernameInUse(String s) {
			String tempArr[] = s.split(":", 2);
			String peerUsername = tempArr[0];
			if(peerUsername.equals(username)) {
				closeEverything("Error Occured Connecting to Chat Server, Restart Application");
				return true;
			}
			return false;
		}

		private boolean isNewUser(String s, JPanel panel) {
			String tempArr[] = s.split(":", 2);
			
		if(usernamePanel.containsKey(tempArr[0])) {
				return false;
			}

		JLabel label = new JLabel("<HTML><span style='color:darkblue'>" + tempArr[0] + "</span> <span style='color:red'>|</span></HTML>",2);
		label.setFont(new Font("Arial", Font.BOLD,20));
		panel.add(label);
		usernamePanel.put(tempArr[0], label);
			return true;
		}
		private boolean leftChat(String s, JPanel panel) {
			if(s.contains(": has Left the Chat!")) {
				String username[] = s.split(":");
				if(username[1].equals(" has Left the Chat!")) {
					panel.remove(usernamePanel.get(username[0]));
					usernamePanel.remove(username[0]);
				}else {
					JOptionPane.showMessageDialog(null,  "Failed to Recognize User Who Disconnected");
				}
				return true;
			}
			return false;
		}
		private void updateChatBox(String messageFromGroupChat,JTextArea messageBox,JScrollPane messageScrollBar, JScrollBar adjustScroll) {
			//Otherwise, mirror the message to all sockets connected to the server
			messageBox.append(messageFromGroupChat + "\n");
			messageScrollBar.revalidate();
			adjustScroll.setValue(adjustScroll.getMaximum());
		}
	}
