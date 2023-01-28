package main;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.serverbackend.ClientHandler;
import main.serverfrontend.ServerConsole;
/**
 * 
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/25/2023
 */
public class Server {
	
	private static JTextArea terminal   = new JTextArea(8,60); 
	private static JScrollPane messageScrollBar = new JScrollPane(terminal);
	private static JScrollBar adjustScroll = messageScrollBar.getVerticalScrollBar();
	private ServerSocket serverSocket;
	
	public Server (ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = null;
		int port = 1234; //Arbitrary port this server listen on, clients expected to connect on this port
		
		while(true){//Until the user enters valid port number (not in use and also within port range) keeping asking for a new one

			try {
				serverSocket = new ServerSocket(port);
				break;
			}catch(IOException io) {
				JOptionPane.showMessageDialog(null, "Error Encountered Creating Server Socket");
				return;
			}catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error Encountered Creating Server Socket");
				return;
			}
		}
		Server server =  new Server(serverSocket);
		ServerConsole console = new ServerConsole(server,port,terminal, messageScrollBar,  adjustScroll);//create server frontend view
		server.startServer(); 
	}
	//as long as the serversocket is not closed, accept valid connections from clients
	private void startServer() {
		try {
			while(!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				ClientHandler clientHandler = new ClientHandler(socket, terminal, messageScrollBar,  adjustScroll);// individual client handler to communicate with every client
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		} catch(IOException e) {
			stopServer();
		}
	}
	
	//used to shut down the server
	public void stopServer() {
		try {
			if(serverSocket != null) {
				serverSocket.close();
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
