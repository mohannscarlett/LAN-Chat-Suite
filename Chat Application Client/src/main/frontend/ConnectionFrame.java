package main.frontend;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
/**
 * 
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/27/2023
 */
public class ConnectionFrame extends Thread {
		public ConnectionFrame(JFrame frame,int screenSize){
			
			 ImageIcon applicationIcon;
			 String applicationIconPath = "resources/images/zdxsu.png";
			 Image applicationIconImage;
			 
			try {
				applicationIconImage = ImageIO.read(getClass().getResourceAsStream(applicationIconPath));
				applicationIcon = new ImageIcon(applicationIconImage);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Failed to Load Application Icon");
				return;
			}
						
			ImageIcon icon = fetchImageFromGIF("resources/images/connecting-loading.gif",null);
			JLabel lLogo = new JLabel(icon);

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
			
			frame.getContentPane().add(lLogo);
			frame.setLayout(new FlowLayout());
			frame.setTitle("Scarlett's Chat");
			frame.setVisible(true);
			frame.setResizable(false);
			frame.setIconImage(applicationIcon.getImage());
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
		}
		//render GIF to imageIcon
		private ImageIcon fetchImageFromGIF(String path,String description) {
			java.net.URL imgURL = getClass().getResource(path);
			if (imgURL != null) {
				return new ImageIcon(imgURL, description);
			} else {
				JOptionPane.showMessageDialog(null, "Failed to Display Loading Bar");
				return null;
			}
		}
}
