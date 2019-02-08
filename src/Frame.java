import java.awt.BorderLayout;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class Frame extends JPanel {

	public final static int POINT1_X = 530;
	public final static int POINT24_X = 530;
	public final static int POINT2_X = 490;
	public final static int POINT23_X = 490;
	public final static int POINT3_X = 445;
	public final static int POINT22_X = 445;
	public final static int POINT4_X = 400;
	public final static int POINT21_X = 400;
	public final static int POINT5_X = 360;
	public final static int POINT20_X = 360;
	public final static int POINT6_X = 316;
	public final static int POINT19_X = 316;
	public final static int POINT7_X = 240;
	public final static int POINT18_X = 240;
	public final static int POINT8_X = 198;
	public final static int POINT17_X = 198;
	public final static int POINT9_X = 155;
	public final static int POINT16_X = 155;
	public final static int POINT10_X = 115;
	public final static int POINT15_X = 115;
	public final static int POINT11_X = 70;
	public final static int POINT14_X = 70;
	public final static int POINT12_X = 25;
	public final static int POINT13_X = 25;
	
	Ellipse2D.Double [] pips;	
	
	private Image backgroundImage;

	// Some code to initialize the background image.
	// Here, we use the constructor to load the image. This
	// can vary depending on the use case of the panel.
	public Frame(String fileName) throws IOException {
		    backgroundImage = ImageIO.read(new File(fileName));
		    pips = new Ellipse2D.Double [30];
		    
			pips[0] = new Ellipse2D.Double(25,25,30,30);
			pips[1] = new Ellipse2D.Double(25,55,30,30);
			pips[2] = new Ellipse2D.Double(25,85,30,30);
			pips[3] = new Ellipse2D.Double(25,115,30,30);
			pips[4] = new Ellipse2D.Double(25,145,30,30);	
			pips[5] = new Ellipse2D.Double(198,362,30,30);
			pips[6] = new Ellipse2D.Double(198,332,30,30);
			pips[7] = new Ellipse2D.Double(198,302,30,30);
			pips[8] = new Ellipse2D.Double(198,85,30,30);
			pips[9] = new Ellipse2D.Double(198,55,30,30);
			pips[10] = new Ellipse2D.Double(198,25,30,30);
			pips[11] = new Ellipse2D.Double(25,362,30,30);
			pips[12] = new Ellipse2D.Double(25,332,30,30);
			pips[13] = new Ellipse2D.Double(25,302,30,30);
			pips[14] = new Ellipse2D.Double(25,272,30,30);
			pips[15] = new Ellipse2D.Double(25,242,30,30);
			pips[16] = new Ellipse2D.Double(316,362,30,30);
			pips[17] = new Ellipse2D.Double(316,332,30,30);
			pips[18] = new Ellipse2D.Double(316,302,30,30);
			pips[19] = new Ellipse2D.Double(316,272,30,30);
			pips[20] = new Ellipse2D.Double(316,242,30,30);
			pips[21] = new Ellipse2D.Double(316,25,30,30);
			pips[22] = new Ellipse2D.Double(316,55,30,30);
			pips[23] = new Ellipse2D.Double(316,85,30,30);
			pips[24] = new Ellipse2D.Double(316,115,30,30);
			pips[25] = new Ellipse2D.Double(316,145,30,30);
			pips[26] = new Ellipse2D.Double(530,25,30,30);
			pips[27] = new Ellipse2D.Double(530,55,30,30);
			pips[28] = new Ellipse2D.Double(530,362,30,30);
			pips[29] = new Ellipse2D.Double(530,332,30,30);
		  }

	
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);

		// Draw the background image.
		g.drawImage(backgroundImage, 5, 10, this);
		
		// Upper left black
		g.setColor(Color.BLACK);
		((Graphics2D) g).draw(pips[0]);
		((Graphics2D) g).fill(pips[0]);
		g.setColor(Color.BLACK);
		((Graphics2D) g).draw(pips[1]);
		((Graphics2D) g).fill(pips[1]);
		((Graphics2D) g).draw(pips[2]);
		((Graphics2D) g).fill(pips[2]);
		((Graphics2D) g).draw(pips[3]);
		((Graphics2D) g).fill(pips[3]);
		((Graphics2D) g).draw(pips[4]);
		((Graphics2D) g).fill(pips[4]);
		
		// Left side, lower right Black
		((Graphics2D) g).draw(pips[5]);
		((Graphics2D) g).fill(pips[5]);
		((Graphics2D) g).draw(pips[6]);
		((Graphics2D) g).fill(pips[6]);
		((Graphics2D) g).draw(pips[7]);
		((Graphics2D) g).fill(pips[7]);
		
		
		// Left side, upper right red
		g.setColor(Color.RED);
		((Graphics2D) g).draw(pips[8]);
		((Graphics2D) g).fill(pips[8]);
		((Graphics2D) g).draw(pips[9]);
		((Graphics2D) g).fill(pips[9]);
		((Graphics2D) g).draw(pips[10]);
		((Graphics2D) g).fill(pips[10]);
		
		// Lower left red
		((Graphics2D) g).draw(pips[11]);
		((Graphics2D) g).fill(pips[11]);
		((Graphics2D) g).draw(pips[12]);
		((Graphics2D) g).fill(pips[12]);
		((Graphics2D) g).draw(pips[13]);
		((Graphics2D) g).fill(pips[13]);
		((Graphics2D) g).draw(pips[14]);
		((Graphics2D) g).fill(pips[14]);
		((Graphics2D) g).draw(pips[15]);
		((Graphics2D) g).fill(pips[15]);
		
		
		// Right side, lower left black
		g.setColor(Color.BLACK);
		((Graphics2D) g).draw(pips[16]);
		((Graphics2D) g).fill(pips[16]);
		((Graphics2D) g).draw(pips[17]);
		((Graphics2D) g).fill(pips[17]);
		((Graphics2D) g).draw(pips[18]);
		((Graphics2D) g).fill(pips[18]);
		((Graphics2D) g).draw(pips[19]);
		((Graphics2D) g).fill(pips[19]);
		((Graphics2D) g).draw(pips[20]);
		((Graphics2D) g).fill(pips[20]);
		
		
		// Right side Board, upper left red
		g.setColor(Color.RED);
		((Graphics2D) g).draw(pips[21]);
		((Graphics2D) g).fill(pips[21]);
		((Graphics2D) g).draw(pips[22]);
		((Graphics2D) g).fill(pips[22]);
		((Graphics2D) g).draw(pips[23]);
		((Graphics2D) g).fill(pips[23]);
		((Graphics2D) g).draw(pips[24]);
		((Graphics2D) g).fill(pips[24]);
		((Graphics2D) g).draw(pips[25]);
		((Graphics2D) g).fill(pips[25]);
		
		
		// Top Right Black 
		g.setColor(Color.BLACK);
		((Graphics2D) g).draw(pips[26]);
		((Graphics2D) g).fill(pips[26]);
		((Graphics2D) g).draw(pips[27]);
		((Graphics2D) g).fill(pips[27]);
		
		// Lower right red
		g.setColor(Color.RED);
		((Graphics2D) g).draw(pips[28]);
		((Graphics2D) g).fill(pips[28]);
		((Graphics2D) g).draw(pips[29]);
		((Graphics2D) g).fill(pips[29]);
	}

	//Method to draw/move a pip on the boards
	public void drawPip(int pipNum, int x, int y) {		
		pips[pipNum].x = x;
		pips[pipNum].y = y;
		return;
	}
	
public static void main(String[] args) throws IOException{
	JFrame frame = new JFrame();
    frame.setSize(950, 500);
    frame.setTitle("Backgammon");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Frame component = new Frame("src/Screen Shot 2019-02-05 at 21.04.05.png");
    frame.add(component);
    
    
    // Text field to enter in move
    JLabel move = new JLabel("Enter your move:");
    JTextField field = new JTextField(20);
    move.setLabelFor(field);

    // Create panel for text field
    JPanel panel = new JPanel();
    
    // set panel size
    panel.setSize(5,5);
    
    // add text field to panel
    panel.add(move);
    panel.add(field);
    
    // add panel to frame
    frame.add(panel, BorderLayout.EAST);
    
    frame.setVisible(true);
}
}



