package main.backend;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import main.Client;
import main.frontend.ClientFrontEnd;
/**
 * 
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/27/2023
 */
public class ClientUsernamePageEventHandler implements ActionListener{
	JTextField usernameField;
	Client client;
	String username;
	int port;
	JFrame frame;
	int screenSize;
	JCheckBox darkModeBox;
	JTextField ipField;
	JLabel title;
	
	public ClientUsernamePageEventHandler(String username, int port,JTextField usernameField, JFrame frame, int screenSize,
			JCheckBox darkModeBox, JTextField ipField, JLabel title){
		this.username = username;
		this.port = port;
		this.usernameField = usernameField;
		this.frame = frame;
		this.screenSize = screenSize;
		this.darkModeBox = darkModeBox;
		this.ipField = ipField;
		this.title = title;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == usernameField) {
			Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(usernameField.getText());
			boolean b = m.find();

			//if the user's username contains any special characters return;
			if(b) {
				JOptionPane.showMessageDialog(null, "Username May not Contain Special Characters");	
				return;
			}else if(usernameField.getText().length() > 14 || usernameField.getText().length() < 4) {
				JOptionPane.showMessageDialog(null, "Username must be between 4 and 14 characters");
				return;
			}else {
				username = usernameField.getText();
				username = username.toLowerCase();
				username = username.replace(" ", "");
				
				frame.remove(usernameField);
				title.setText("Enter Server IP Address");
				frame.add(ipField);
				frame.add(darkModeBox);
				
				ipField.requestFocus();
			}
		
		}
		if(event.getSource() == ipField) {
			String serverIP =  ipField.getText().replace(" ", "");
			if(serverIP.length() > 15 || serverIP.length() < 8 || !(serverIP.contains("."))) {
				JOptionPane.showMessageDialog(null, "Invalid IP Address");
				return;
			}
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			ClientFrontEnd secondPage = new ClientFrontEnd(username,port,screenSize, darkModeBox.isSelected(),serverIP);
		}
	}
}
