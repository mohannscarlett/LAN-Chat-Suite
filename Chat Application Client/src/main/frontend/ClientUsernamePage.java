package main.frontend;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.backend.ClientUsernamePageEventHandler;
/**
 * 
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/27/2023
 */
public class ClientUsernamePage {
	JFrame frame = new JFrame();
	String backgroundPath = "resources/images/CloudBackground.png";
	Image backgroundImage;
	ImageIcon background;
	JLabel backgroundLabel = null;
	JLabel title = new JLabel("Enter Chat Display Name");
	JCheckBox darkModeBox = new JCheckBox("Enable Grey Scale",false);
	JTextField usernameField = new JTextField("", 20);
	JTextField ipField = new JTextField("", 20);

	private  ImageIcon applicationIcon;
	private String applicationIconPath = "resources/images/scarlettlogo.png";
	private Image applicationIconImage;
	
	public ClientUsernamePage (){
	JOptionPane.showMessageDialog(null, "Application Error 1105, Please Close Application");	
	}
	public ClientUsernamePage(String username, int port,int screenSize){

		try {
			backgroundImage = ImageIO.read(getClass().getResourceAsStream(backgroundPath));
			background = new ImageIcon(backgroundImage);
			backgroundLabel = new JLabel(background);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			applicationIconImage = ImageIO.read(getClass().getResourceAsStream(applicationIconPath));
			applicationIcon = new ImageIcon(applicationIconImage);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Failed to Load Application Icon");
			return;
		}
		
		frame.setContentPane(backgroundLabel);

		darkModeBox.setFocusable(false);
		darkModeBox.setOpaque(false);
		
		title.setFont(new Font("Arial", Font.BOLD, 15));
		
		frame.add(title);
		frame.add(usernameField);
		//frame.add(darkModeBox);
		
		frame.setTitle("Scarlett's Chat");
		if(screenSize == 1) {
			frame.setLocation(1645/2,940/2);
			frame.setSize(new Dimension(276,120));
		}else if(screenSize == 2){			
			frame.setLocation(1300/2,725/2);
			frame.setSize(new Dimension(276,120));
		}else {
			frame.setLocationRelativeTo(null);
			frame.setSize(new Dimension(276,120));
		}
		frame.setLayout(new FlowLayout());
		frame.setResizable(false);
		frame.setIconImage(applicationIcon.getImage());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		ClientUsernamePageEventHandler event = new ClientUsernamePageEventHandler(username, port ,usernameField,frame,screenSize, darkModeBox,ipField,title);
		usernameField.addActionListener(event);	
		ipField.addActionListener(event);
	}
}
