package main.backend;
import java.awt.event.WindowEvent;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import main.Client;
/**
 * 
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/27/2023
 */
public class ConnectToChatServer extends SwingWorker<Void,Void> {
	private Socket socket;
	private String username; //username of this instance of the client
	private JTextArea messageBox;
	private JScrollPane messageScrollBar;
	private JScrollBar adjustScroll;
	private JFrame chatFrame;
	private JPanel fillerPanel;
	private JFrame loadingFrame;
	private String serverIP;

	public ConnectToChatServer(Socket socket, Client client, String username,JTextArea messageBox,JScrollPane messageScrollBar,
			JScrollBar adjustScroll, JFrame chatFrame,JPanel fillerPanel, JFrame loadingFrame, String serverIP) {
		this.socket = socket;
		this.username = username;
		this.messageBox = messageBox;
		this.messageScrollBar = messageScrollBar; 
		this.adjustScroll = adjustScroll;
		this.chatFrame = chatFrame;
		this.fillerPanel = fillerPanel;
		this.loadingFrame = loadingFrame;
		this.serverIP = serverIP;
	}

	@Override
	protected Void doInBackground() throws Exception {
		//functionality can be added to search for server automatically on your local network, not optimal for time efficiency 
		/*String subnet = resolveSubnet();
		 * if(subnet == null){
		 * return null;
			}		
		socket = checkHosts(subnet);*/
		try {
			socket = new Socket(serverIP,1234);
			} catch (Exception e) {
			if(socket != null) {
				socket.close();
			}
			failedToConnect("Failed To Connect To Server, Restart Application");
			return null;
		}

		if(socket == null || socket.isClosed()) {
			failedToConnect("Failed To Connect To Server, Restart Application");
			return null;
		}
		try {
			ClientEventHandler.client = new Client(socket, username);
		if(ClientEventHandler.client == null) {
			throw new IllegalArgumentException();
		}
		ClientEventHandler.client.initializeUsername();
		}catch(Exception e) {
			failedToConnect("Display Name is Currently in Use");
			return null;
		}
		ClientEventHandler.client.listenForMessage(messageBox,messageScrollBar,adjustScroll,chatFrame,fillerPanel);
		loadingFrame.dispatchEvent(new WindowEvent(loadingFrame, WindowEvent.WINDOW_CLOSING));
		chatFrame.setVisible(true);
		return null;
	}
	
	public void failedToConnect(String message) {
		JOptionPane.showMessageDialog(null, message);
		loadingFrame.dispatchEvent(new WindowEvent(loadingFrame, WindowEvent.WINDOW_CLOSING));
	}
	//functionality can be added to search for server automatically on your local network not optimal for time efficiency 
	
	/*public static Socket checkHosts(String subnet) throws UnknownHostException, IOException{
		String host = null;
		Socket socket = null;
	   for (int i = 1; i < 255; i++ ){
	        host = subnet + i;
	       if (InetAddress.getByName(host).isReachable(1000)){
	    	   try {
	    		   socket = new Socket(host, 1234);
	    		   break;
	   		} catch (IOException e) {
	   			continue;
	   		} 
	
	       }
	   }
	   return socket;
}
	public static String resolveSubnet() {
		String ip = "";
		try(final DatagramSocket socket = new DatagramSocket()){
			  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			  ip = socket.getLocalAddress().getHostAddress();
			} catch (SocketException e) {
				JOptionPane.showMessageDialog(null, "Failed To Connect To Server, Restart Application");
				return null;
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(null, "Failed To Connect To Server, Restart Application");
				return null;
			}
		ip = ip.replace(".", ".@");
		String ipArr[] = ip.split("@");
		String subnet = ipArr[0] + ipArr[1] + ipArr[2];
				
		return subnet;
	}*/
/*public static String resolveMyIPAddress() {
	String ip = "";
	try(final DatagramSocket socket = new DatagramSocket()){
		  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
		  ip = socket.getLocalAddress().getHostAddress();
		} catch (SocketException e) {
			JOptionPane.showMessageDialog(null, "Failed to Render Server IP");
			return null;
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Failed to Render Server IP");
			return null;
		}
			
	return ip;
}*/


}
