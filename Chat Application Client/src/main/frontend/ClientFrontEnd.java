package main.frontend;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.backend.ClientEventHandler;
/**
 * 
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/27/2023
 */
public class ClientFrontEnd {
	
	private JFrame frame = new JFrame();
	private JPanel backPanel = new JPanel(); //main panel in which all components reside
	private JPanel fillerPanel = new JPanel(); //panel to create a break between top of JFrame and rest of components, which centers all other components
	private JPanel innerPanel = new JPanel(); //panel that holds submit startChat
	private JPanel innerPanel1 = new JPanel(); //panel that holds submit startChat
		
	//all home page images that need to be displayed
	private ImageIcon zdxsu; //image of zdxsu 
	private String zdxsuPath = "resources/images/scarlettlogo.png";
	private Image zdxsuImage;
	private JLabel zdxsuLabel;
	
	
	private ImageIcon titleImageIcon; //image of zdxsu 
	private String titlePath = "resources/images/title.png";
	private Image titleImage;
	private JLabel titleLabel;
	
	private  ImageIcon background; //background image
	private String backgroundPath  ="resources/images/CloudBackground.png";
	private String backgroundPathExtended  ="resources/images/CloudBackgroundExtended.png";
	private String greyScalePath  ="resources/images/greyScale.png";
	private String greyScalePathExtended  ="resources/images/greyScaleExtended.png";
	private Image backgroundImage;
	private JLabel backgroundLabel;
	private  ImageIcon applicationIcon;
	private String applicationIconPath = "resources/images/scarlettlogo.png";
	private Image applicationIconImage;
	
	private JLabel usernameLabel;
	private JLabel connectedUsersLabel;
	private  JTextField chatBox = new JTextField(60); //urlTextBox to take snipeBotStrikePrice from user

	//private  JButton startChat = new JButton("<html><span style='color:white'>Start Chatting</span></html>"); //startChat for user to submit URL input
	private  JButton restartChat = new JButton("<html><span style='color:white'>Restart Chat</span></html>"); //startChat for user to submit URL input
	
	Color color = new Color(0, 0, 0, 0); //blank color that components can use to be transparent
	
	 public ClientFrontEnd(String username, int port, int screenSize, boolean darkMode,String serverIP){ 
		
		//set attributes of all panels
		backPanel.setLayout(new FlowLayout());
		backPanel.setPreferredSize(new Dimension(950, 525));
		//set attributes for panel that holds title and application logo 
		fillerPanel.setLayout(new FlowLayout());
		fillerPanel.setPreferredSize(new Dimension(700, 180));
		//set attributes for panel that holds username title
		innerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		innerPanel.setPreferredSize(new Dimension(600, 85));
		//set attributes for panel that holds message sending box, chat box, and stop button
		innerPanel1.setLayout(new FlowLayout());
		innerPanel1.setPreferredSize(new Dimension(950, 200));

		try {
			zdxsuImage = ImageIO.read(getClass().getResourceAsStream(zdxsuPath));
			zdxsu = new ImageIcon(zdxsuImage);
			zdxsuLabel = new JLabel(zdxsu); 
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Failed to Load Application Logo");
			return;
		}

		try {
			titleImage = ImageIO.read(getClass().getResourceAsStream(titlePath));
			titleImageIcon = new ImageIcon(titleImage);
			titleLabel = new JLabel(titleImageIcon); 
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Failed to Load Application Logo");
			return;
		}
		
		if(darkMode) {
			if(screenSize > 2) {
				try {
					backgroundImage = ImageIO.read(getClass().getResourceAsStream(greyScalePathExtended));

				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Failed to Load Background Image");
					return;
				}
			}else {
				try {
					backgroundImage = ImageIO.read(getClass().getResourceAsStream(greyScalePath));

				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Failed to Load Background Image");
					return;
				}
			}
			
		}else {
			if(screenSize > 2) {
				try {
					backgroundImage = ImageIO.read(getClass().getResourceAsStream(backgroundPathExtended));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Failed to Load Background Image");
					return;
				}
			}else {
				try {
					backgroundImage = ImageIO.read(getClass().getResourceAsStream(backgroundPath));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Failed to Load Background Image");
					return;
				}
			}

		}
		background = new ImageIcon(backgroundImage);
		backgroundLabel = new JLabel(background);

		try {
			applicationIconImage = ImageIO.read(getClass().getResourceAsStream(applicationIconPath));
			applicationIcon = new ImageIcon(applicationIconImage);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Failed to Load Application Icon");
			return;
		}
		
		//set attributes of usernameLabel
		usernameLabel = new JLabel("<html>Username: " + "<span style='color:blue'>"+ username+ "</span></html>"); //urlTextBox title
		usernameLabel.setFont(new Font("Arial", Font.BOLD,20));
		usernameLabel.setPreferredSize(new Dimension(364,30));
		
		connectedUsersLabel = new JLabel("Others in Chat: "); 
		connectedUsersLabel.setFont(new Font("Arial", Font.BOLD,20));
		connectedUsersLabel.setPreferredSize(new Dimension(155,30));
				
		JTextArea messageBox   = new JTextArea(8,60); 
		
		JScrollPane messageScrollBar = new JScrollPane(messageBox);
		JScrollBar adjustScroll = messageScrollBar.getVerticalScrollBar();
		
		chatBox.setText("Type here to Send your Message"); 
		
		chatBox.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				chatBox.setText(""); 
			}


			public void focusLost(FocusEvent e) {
				chatBox.setText("Type here to Send your Message"); 
			}

			});

		//set attributes of restartChat
		restartChat.setBackground(Color.red);
		restartChat.setFont(new Font("Arial", Font.BOLD, 20));
		restartChat.setFocusable(false);

		messageBox.setLineWrap(true);
		messageBox.setEditable(false);
				
		backPanel.setBackground(color);
		innerPanel.setBackground(color);
		innerPanel1.setBackground(color);
		fillerPanel.setBackground(color);
		
		//add components to frame
		fillerPanel.add(zdxsuLabel);
		fillerPanel.add(titleLabel);
		fillerPanel.add(connectedUsersLabel);
		
		innerPanel.add(usernameLabel);
		
		innerPanel1.add(chatBox);
		innerPanel1.add(messageScrollBar);
		
		backPanel.add(fillerPanel);
		backPanel.add(innerPanel);
		backPanel.add(innerPanel1);
		//backPanel.add(restartChat);
		frame.setContentPane(backgroundLabel);
		frame.add(backPanel);
		//set Frame attributes
		if(screenSize == 1) {
			frame.setLocation(480, 180);
			frame.setSize(900, 620);
		}else if(screenSize == 2){			
			frame.setLocation(355,120);
			frame.setSize(900, 620);
		}else {
			frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
		}
		frame.setLayout(new FlowLayout());
		frame.setTitle("Scarlett's Chat");
		frame.setResizable(false);
		frame.setIconImage(applicationIcon.getImage());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.addWindowListener(new WindowAdapter(){
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	System.exit(0);
		    }
		});
		
		messageBox.requestFocusInWindow();
		
		ClientEventHandler event = new ClientEventHandler(frame,username,port,chatBox,messageScrollBar,adjustScroll,messageBox,restartChat,fillerPanel,screenSize, serverIP);
		chatBox.addActionListener(event);
		restartChat.addActionListener(event);
	 }
	 
}
