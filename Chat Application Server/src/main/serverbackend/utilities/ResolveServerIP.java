package main.serverbackend.utilities;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
/**
 * 
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/25/2023
 */

//return the IP Address of the system this code is run on
public class ResolveServerIP {
	
	public static String getServerIPAddress() {
	String serverIP = "";
	try(final DatagramSocket socket = new DatagramSocket()){
		  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
		  serverIP = socket.getLocalAddress().getHostAddress();
		} catch (SocketException e) {
			JOptionPane.showMessageDialog(null, "Failed to Fetch Server IP");
			return null;
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Failed to Fetch Server IP");
			return null;
		}
			
	return serverIP;
	}
}
