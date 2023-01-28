package main.backend;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import main.Client;
import main.frontend.ConnectionFrame;
/**
 * 
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/27/2023
 */
public class ClientEventHandler implements ActionListener{
	JTextField usernameField;
	JFrame frame;
	String username;
	Socket socket;
	int port;
	JTextField chatBox;
	JButton useTerms;
	JTextArea messageBox;
	JScrollPane messageScrollBar;
	JScrollBar adjustScroll;
	JPanel fillerPanel;
	int screenSize;
	String serverIP;
	static Client client;

	JFrame loadingFrame;
	
	public ClientEventHandler(JFrame frame,String username, int port,JTextField chatBox,JScrollPane messageScrollBar,
			JScrollBar adjustScroll,JTextArea messageBox , JButton useTerms,JPanel fillerPanel,int screenSize, String serverIP) {
		this.frame = frame;
		this.username = username;
		this.port = port;
		this.chatBox = chatBox;
		this.useTerms = useTerms;
		this.messageBox = messageBox;
		this.messageScrollBar = messageScrollBar; 
		this.adjustScroll = adjustScroll;
		this.fillerPanel = fillerPanel;
		this.screenSize = screenSize;
		this.serverIP = serverIP;
		
		socket = null;
		loadingFrame = new JFrame ();
		ConnectionFrame loadingPage = new ConnectionFrame(loadingFrame,screenSize);
		ConnectToChatServer find = new ConnectToChatServer(socket,client,username,messageBox,messageScrollBar, // in a background thread, load client connection
				adjustScroll,frame,fillerPanel,loadingFrame,serverIP);
		try {
			backgroundtask(find);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Failed To Connect To Server, Restart Application");
			return;
		}
	} 
	 
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == chatBox) {
			String chatBoxText = chatBox.getText();
			if(client == null) {
				return;
			}
			if(chatBoxText.contains(":")) {
				chatBoxText = chatBoxText.replace(":", ";");
			}

			client.messageAllNodes(chatBoxText);
			chatBox.setText("");
			messageScrollBar.revalidate();
			adjustScroll.setValue(adjustScroll.getMaximum());
		}
	}
	
	private void backgroundtask(SwingWorker<Void,Void> findServer){
		findServer.execute();
	}
	
}
