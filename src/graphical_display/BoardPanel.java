package graphical_display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Class which is responsible for the displaying the board and
 * the pips, as well as for operations manipulating the display
 */
public class BoardPanel extends JPanel {
	Ellipse2D.Double [] pips;	
	
	private Image backgroundImage;

	// Some code to initialize the background image.
	// Here, we use the constructor to load the image. This
	// can vary depending on the use case of the panel.
	public BoardPanel(String fileName) throws IOException {
	    backgroundImage = ImageIO.read(new File(fileName));

	    pips = new Ellipse2D.Double [30];
		  
	    for(int i = 0; i < 30; i++) {
	    	pips[i] = new Ellipse2D.Double(0, 0, 30, 30);
	    }
	}

	//Method to draw a pip at a location on the board
	public void drawPip(int pipNum, int x, int y) {		
		pips[pipNum].x = x;
		pips[pipNum].y = y;
		return;
	}
	
	//Method to move a pip to a location on the board
	public void movePip(int pipNum, int newX, int newY) {
		while(pips[pipNum].x != newX || pips[pipNum].y != newY) {
			
			if(pips[pipNum].x < newX)
				pips[pipNum].x++;
			else if (pips[pipNum].x > newX)
				pips[pipNum].x--;
			
			if(pips[pipNum].y < newY)
				pips[pipNum].y++;
			else if (pips[pipNum].y > newY)
				pips[pipNum].y--;
			
            try{
                Thread.sleep(2);
            } catch (Exception e){
            	e.printStackTrace();
            }
            this.repaint();
		}
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);

		// Draw the background image.
		g.drawImage(backgroundImage, 5, 10, this);
		
		//Draw Black pips(Colour of each element(pip) is
		//hardcoded according to colours set in GameLogicBoard.java
		g.setColor(Color.BLACK);
		((Graphics2D) g).draw(pips[2]);
		((Graphics2D) g).fill(pips[2]);
		((Graphics2D) g).draw(pips[3]);
		((Graphics2D) g).fill(pips[3]);
		((Graphics2D) g).draw(pips[4]);
		((Graphics2D) g).fill(pips[4]);
		((Graphics2D) g).draw(pips[5]);
		((Graphics2D) g).fill(pips[5]);
		((Graphics2D) g).draw(pips[6]);
		((Graphics2D) g).fill(pips[6]);
		((Graphics2D) g).draw(pips[7]);
		((Graphics2D) g).fill(pips[7]);
		((Graphics2D) g).draw(pips[8]);
		((Graphics2D) g).fill(pips[8]);
		((Graphics2D) g).draw(pips[9]);
		((Graphics2D) g).fill(pips[9]);
		((Graphics2D) g).draw(pips[15]);
		((Graphics2D) g).fill(pips[15]);
		((Graphics2D) g).draw(pips[16]);
		((Graphics2D) g).fill(pips[16]);
		((Graphics2D) g).draw(pips[17]);
		((Graphics2D) g).fill(pips[17]);
		((Graphics2D) g).draw(pips[18]);
		((Graphics2D) g).fill(pips[18]);
		((Graphics2D) g).draw(pips[19]);
		((Graphics2D) g).fill(pips[19]);
		((Graphics2D) g).draw(pips[28]);
		((Graphics2D) g).fill(pips[28]);
		((Graphics2D) g).draw(pips[29]);
		((Graphics2D) g).fill(pips[29]);
		
		
		//Draw Red pips(Colour of each element(pip) is
		//hardcoded according to colours set in GameLogicBoard.java
		g.setColor(Color.RED);
		((Graphics2D) g).draw(pips[0]);
		((Graphics2D) g).fill(pips[0]);
		((Graphics2D) g).draw(pips[1]);
		((Graphics2D) g).fill(pips[1]);
		((Graphics2D) g).draw(pips[10]);
		((Graphics2D) g).fill(pips[10]);
		((Graphics2D) g).draw(pips[11]);
		((Graphics2D) g).fill(pips[11]);
		((Graphics2D) g).draw(pips[12]);
		((Graphics2D) g).fill(pips[12]);
		((Graphics2D) g).draw(pips[13]);
		((Graphics2D) g).fill(pips[13]);
		((Graphics2D) g).draw(pips[14]);
		((Graphics2D) g).fill(pips[14]);
		((Graphics2D) g).draw(pips[20]);
		((Graphics2D) g).fill(pips[20]);
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
		((Graphics2D) g).draw(pips[26]);
		((Graphics2D) g).fill(pips[26]);
		((Graphics2D) g).draw(pips[27]);
		((Graphics2D) g).fill(pips[27]);
	}
}



