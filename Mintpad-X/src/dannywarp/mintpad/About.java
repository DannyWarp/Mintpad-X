package dannywarp.mintpad;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class About extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image about;
	
	public About() {
		//about = getToolkit().getImage(ClassLoader.getSystemResource("resources/about_mintpad_x.png")); // uncomment for JAR build
		about = new ImageIcon("resources/about_mintpad_x.png").getImage();
		setPreferredSize(new Dimension(983, 589));      
	}
	
	public void paint(Graphics g) {
		Graphics g2d = (Graphics2D) g;
		g2d.drawImage(about, 0, 0, this);
	}

}
