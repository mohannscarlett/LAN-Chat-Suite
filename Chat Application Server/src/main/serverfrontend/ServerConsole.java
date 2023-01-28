package main.serverfrontend;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.Server;
import main.serverbackend.utilities.LoadImage;
import main.serverbackend.utilities.ResolveServerIP;
/**
 * @apiNote
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/25/2023
 */
public class ServerConsole {

	public ServerConsole(Server server,int port,JTextArea terminal,JScrollPane terminalScrollBar, JScrollBar adjustScroll) {

		String serverIP = ResolveServerIP.getServerIPAddress(); 

		JFrame frame = new JFrame();
		JPanel backPanel = new JPanel(); //main panel in which all components reside
		JPanel topPanel = new JPanel(); //encompasses top section (1/3) of the JFrame
		JPanel middlePanel = new JPanel(); //encompasses middle section (2/3) of the JFrame
		JPanel bottomPanel = new JPanel(); //encompasses bottom section (3/3) of the JFrame
		Color color = new Color(0, 0, 0, 0); //blank color that components can use to be transparent
		
		JLabel title = new JLabel("<html><span style='color:white'>Server Console :</span><span style='color:red'> Port <>" + port + "</span></html>", 2); 
		title.setFont(new Font("Arial", Font.BOLD,20));
		
		JLabel serverIPLabel = new JLabel("<html><span style='color:white'>Server IP Address: </span><span style='color:Red'>" + serverIP +"</span></html>", 2); 
		serverIPLabel.setFont(new Font("Arial", Font.BOLD,20));
		
		terminal.setLineWrap(true);
		terminal.setEditable(false);
		
		LoadImage loadImages = new LoadImage();
		
		String scarlettPath = "resources/images/scarlettlogo.png";
		JLabel scarlettLabel = loadImages.loadImageIntoJLabel(scarlettPath,"Failed to Load Application Logo");//JLabel with Image that will be inserted into the frame as a Company logo
		
		String backgroundPath = "resources/images/black.jpg";
		JLabel backgroundLabel = loadImages.loadImageIntoJLabel(backgroundPath,"Failed to Load Background Image");//JLabel with Image that will become the frame background
		
		String frameIconPath = "resources/images/server.png";
		Image applicationImage = loadImages.loadImage(frameIconPath,"Failed to Load Application Icon"); //Image that will become the frame icon
		
		backPanel.setLayout(new FlowLayout());
		backPanel.setPreferredSize(new Dimension(950, 525));
		topPanel.setLayout(new FlowLayout());
		topPanel.setPreferredSize(new Dimension(700, 180));
		middlePanel.setLayout(new FlowLayout());
		middlePanel.setPreferredSize(new Dimension(600, 85));
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.setPreferredSize(new Dimension(950, 200));
		
		backPanel.setBackground(color);
		topPanel.setBackground(color);
		middlePanel.setBackground(color);
		bottomPanel.setBackground(color);
		
		backPanel.add(topPanel);
		backPanel.add(middlePanel);
		backPanel.add(bottomPanel);
		topPanel.add(scarlettLabel);
		topPanel.add(title);
		middlePanel.add(serverIPLabel);
	    bottomPanel.add(terminalScrollBar);
		
		frame.setContentPane(backgroundLabel);
		frame.add(backPanel);
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	    int displayWidth = gd.getDisplayMode().getWidth();
	    int displayHeight = gd.getDisplayMode().getHeight();
	    
	    if(displayWidth == 1920 && displayHeight == 1080 && java.awt.Toolkit.getDefaultToolkit().getScreenResolution() == 96) {
			frame.setLocation(480, 180); // if screen is 1920x1080 at 100% scale, set frame location
	    }else if(displayWidth == 1920 && displayHeight == 1080 && java.awt.Toolkit.getDefaultToolkit().getScreenResolution() == 120){
			frame.setLocation(355,120); // if screen is 1920x1080 at 125% scale, set frame qualities
	    }else {
			frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH ); // if screen is anything else open in full screen	
	    }
		frame.setSize(900, 620);
		frame.setLayout(new FlowLayout());
		frame.setTitle("Scarlett's Chat Server");
		frame.setIconImage(applicationImage);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter(){
		    @Override
		    public void windowClosing(WindowEvent e){
		    	server.stopServer();
		    }
		});
	}
}
