package main.serverbackend.utilities;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
/**
 * 
 * @author Mohann Scarlett mohannscarlett3@gmail.com
 * @version 1/25/2023
 */
public class LoadImage {
	//loads image from specified path into a JLabel that can be inserted into a JFrame
	public JLabel loadImageIntoJLabel(String path,String errorMessage) {
		JLabel imageLabel = null;
		ImageIcon imageAsIcon = null;
		Image image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream(path));
			imageAsIcon = new ImageIcon(image);
			imageLabel = new JLabel(imageAsIcon); 
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, errorMessage);
			return null;
		}
		return imageLabel;
	}
	//loads image from specified path into an ImageIcon that can be used
	public Image loadImage(String path, String errorMessage) {
		Image image;
		try {
			image = ImageIO.read(getClass().getResourceAsStream(path));
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, errorMessage);
			return null;
		}
		return image;
	}
}
