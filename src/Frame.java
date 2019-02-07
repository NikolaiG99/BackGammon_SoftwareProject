package zbackgammon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JPanel {

	private Image backgroundImage;

	// Some code to initialize the background image.
	// Here, we use the constructor to load the image. This
	// can vary depending on the use case of the panel.
	public Frame(String fileName) throws IOException {
		    backgroundImage = ImageIO.read(new File(fileName));
		  }

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);

		// Draw the background image.
		g.drawImage(backgroundImage, 5, 10, this);
		
		// Upper left black
		g.setColor(Color.BLACK);
		Ellipse2D.Double ellipse = new Ellipse2D.Double(25,25,30,30);
		((Graphics2D) g).draw(ellipse);
		((Graphics2D) g).fill(ellipse);
		g.setColor(Color.BLACK);
		Ellipse2D.Double ellipse2 = new Ellipse2D.Double(25,55,30,30);
		((Graphics2D) g).draw(ellipse2);
		((Graphics2D) g).fill(ellipse2);
		Ellipse2D.Double ellipse3 = new Ellipse2D.Double(25,85,30,30);
		((Graphics2D) g).draw(ellipse3);
		((Graphics2D) g).fill(ellipse3);
		Ellipse2D.Double ellipse4 = new Ellipse2D.Double(25,115,30,30);
		((Graphics2D) g).draw(ellipse4);
		((Graphics2D) g).fill(ellipse4);
		Ellipse2D.Double ellipse5 = new Ellipse2D.Double(25,145,30,30);
		((Graphics2D) g).draw(ellipse5);
		((Graphics2D) g).fill(ellipse5);
		
		// Left side, lower right Black
		Ellipse2D.Double ellipse6 = new Ellipse2D.Double(198,362,30,30);
		((Graphics2D) g).draw(ellipse6);
		((Graphics2D) g).fill(ellipse6);
		Ellipse2D.Double ellipse7 = new Ellipse2D.Double(198,332,30,30);
		((Graphics2D) g).draw(ellipse7);
		((Graphics2D) g).fill(ellipse7);
		Ellipse2D.Double ellipse8 = new Ellipse2D.Double(198,302,30,30);
		((Graphics2D) g).draw(ellipse8);
		((Graphics2D) g).fill(ellipse8);
		
		
		// Left side, upper right red
		g.setColor(Color.RED);
		Ellipse2D.Double ellipse9 = new Ellipse2D.Double(198,85,30,30);
		((Graphics2D) g).draw(ellipse9);
		((Graphics2D) g).fill(ellipse9);
		Ellipse2D.Double ellipse10 = new Ellipse2D.Double(198,55,30,30);
		((Graphics2D) g).draw(ellipse10);
		((Graphics2D) g).fill(ellipse10);
		Ellipse2D.Double ellipse11 = new Ellipse2D.Double(198,25,30,30);
		((Graphics2D) g).draw(ellipse11);
		((Graphics2D) g).fill(ellipse11);
		
		// Lower left red
		Ellipse2D.Double ellipse12 = new Ellipse2D.Double(25,362,30,30);
		((Graphics2D) g).draw(ellipse12);
		((Graphics2D) g).fill(ellipse12);
		Ellipse2D.Double ellipse13 = new Ellipse2D.Double(25,332,30,30);
		((Graphics2D) g).draw(ellipse13);
		((Graphics2D) g).fill(ellipse13);
		Ellipse2D.Double ellipse14 = new Ellipse2D.Double(25,302,30,30);
		((Graphics2D) g).draw(ellipse14);
		((Graphics2D) g).fill(ellipse14);
		Ellipse2D.Double ellipse15 = new Ellipse2D.Double(25,272,30,30);
		((Graphics2D) g).draw(ellipse15);
		((Graphics2D) g).fill(ellipse15);
		Ellipse2D.Double ellipse16 = new Ellipse2D.Double(25,242,30,30);
		((Graphics2D) g).draw(ellipse16);
		((Graphics2D) g).fill(ellipse16);
		
		
		// Right side, lower left black
		g.setColor(Color.BLACK);
		Ellipse2D.Double ellipse17 = new Ellipse2D.Double(316,362,30,30);
		((Graphics2D) g).draw(ellipse17);
		((Graphics2D) g).fill(ellipse17);
		Ellipse2D.Double ellipse18 = new Ellipse2D.Double(316,332,30,30);
		((Graphics2D) g).draw(ellipse18);
		((Graphics2D) g).fill(ellipse18);
		Ellipse2D.Double ellipse19 = new Ellipse2D.Double(316,302,30,30);
		((Graphics2D) g).draw(ellipse19);
		((Graphics2D) g).fill(ellipse19);
		Ellipse2D.Double ellipse20 = new Ellipse2D.Double(316,272,30,30);
		((Graphics2D) g).draw(ellipse20);
		((Graphics2D) g).fill(ellipse20);
		Ellipse2D.Double ellipse21 = new Ellipse2D.Double(316,242,30,30);
		((Graphics2D) g).draw(ellipse21);
		((Graphics2D) g).fill(ellipse21);
		
		
		// Right side Board, upper left red
		g.setColor(Color.RED);
		Ellipse2D.Double ellipse22 = new Ellipse2D.Double(316,25,30,30);
		((Graphics2D) g).draw(ellipse22);
		((Graphics2D) g).fill(ellipse22);
		Ellipse2D.Double ellipse23 = new Ellipse2D.Double(316,55,30,30);
		((Graphics2D) g).draw(ellipse23);
		((Graphics2D) g).fill(ellipse23);
		Ellipse2D.Double ellipse24 = new Ellipse2D.Double(316,85,30,30);
		((Graphics2D) g).draw(ellipse24);
		((Graphics2D) g).fill(ellipse24);
		Ellipse2D.Double ellipse25 = new Ellipse2D.Double(316,115,30,30);
		((Graphics2D) g).draw(ellipse25);
		((Graphics2D) g).fill(ellipse25);
		Ellipse2D.Double ellipse26 = new Ellipse2D.Double(316,145,30,30);
		((Graphics2D) g).draw(ellipse26);
		((Graphics2D) g).fill(ellipse26);
		
		
		// Top Right Black 
		g.setColor(Color.BLACK);
		Ellipse2D.Double ellipse27 = new Ellipse2D.Double(530,25,30,30);
		((Graphics2D) g).draw(ellipse27);
		((Graphics2D) g).fill(ellipse27);
		Ellipse2D.Double ellipse28 = new Ellipse2D.Double(530,55,30,30);
		((Graphics2D) g).draw(ellipse28);
		((Graphics2D) g).fill(ellipse28);
		
		// Lower right red
		g.setColor(Color.RED);
		Ellipse2D.Double ellipse29 = new Ellipse2D.Double(530,362,30,30);
		((Graphics2D) g).draw(ellipse29);
		((Graphics2D) g).fill(ellipse29);
		Ellipse2D.Double ellipse30 = new Ellipse2D.Double(530,332,30,30);
		((Graphics2D) g).draw(ellipse30);
		((Graphics2D) g).fill(ellipse30);
		
	}

	
		
	
public static void main(String[] args) throws IOException{
	JFrame frame = new JFrame();
    frame.setSize(600, 450);
    frame.setTitle("Backgammon");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Frame component = new Frame("src/image/Screen Shot 2019-02-05 at 21.04.05.png");
    frame.add(component);
    
    frame.setVisible(true);
}
}



