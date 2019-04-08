package graphical_display;

import java.awt.Color;

import java.awt.Dimension;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class which is responsible for the displaying the board and
 * the pips, as well as for operations manipulating the display
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel {
	Ellipse2D.Double [] pips;	
	String numbersAtBottom[];
	public int doublingCubePosition;
	public int doublingCubeValue;
	public final int TOP = 0;
	public final int MIDDLE = 1;
	public final int BOTTOM = 2;
	
	private Image backgroundImage;

	// Some code to initialize the background image.
	// Here, we use the constructor to load the image. This
	// can vary depending on the use case of the panel.
	public BoardPanel(String fileName) throws IOException {
	    backgroundImage = ImageIO.read(new File(fileName));
	    doublingCubePosition = MIDDLE;
	    doublingCubeValue = 64;

	    pips = new Ellipse2D.Double [30];
	    for(int i = 0; i < 30; i++) 
	    	pips[i] = new Ellipse2D.Double(0, 0, 30, 30);
	    
	    numbersAtBottom = new String[24];
	    for(int i = 0; i < 24; i++)
	    	numbersAtBottom[i] = ""+(i+1);
	}
	
	class ImageLabel extends JLabel {

		  public ImageLabel(String img) {
		    this(new ImageIcon(img));
		  }

		  public ImageLabel(ImageIcon icon) {
		    setIcon(icon);
		    // setMargin(new Insets(0,0,0,0));
		    setIconTextGap(0);
		    // setBorderPainted(false);
		    setBorder(null);
		    setText(null);
		    setSize(icon.getImage().getWidth(null), icon.getImage().getHeight(null));
		  }

		}
	
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 410);
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
                Thread.sleep(0);
            } catch (Exception e){
            	e.printStackTrace();
            }
            this.repaint();
		}
	}
	
	/**
	 * A method which displays the numbers at the base of each pip,
	 * The numbers may be displayed in two orders, with the #1 pip(red turn)
	 * being the top right, or the bottom left(black turn) depending on whether
	 * the "isBlackTurn" flag is to false or true respectively
	 */
	public void displayPipEnumeration(boolean isBlackTurn) {
		if((isBlackTurn && (numbersAtBottom[0].equals("24")))
				|| (!isBlackTurn && (numbersAtBottom[0].equals("1")))) {
			List<String> listOfProducts = Arrays.asList(numbersAtBottom);      
			Collections.reverse(listOfProducts);
		}
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);

		// Draw the background image.
		g.drawImage(backgroundImage, 5, 10, this);
		
		//Draw the doubling cube
	    try {
	    	if(doublingCubeValue > 64)
	    		doublingCubeValue /= 64;
	    	
	    	String filePath;
	    	
	    	if(doublingCubeValue == 1) 
	    		filePath = "src/resources/doublingDie" + 64 + ".png";
	    	else
	    		filePath = "src/resources/doublingDie" + doublingCubeValue + ".png";
	    	
			Image doublingCubeImage = ImageIO.read(new File(filePath));
			
		    g.drawImage(doublingCubeImage, 
		    		BoardCoordinateConstants.COORDS_POINT[26],
		    		BoardCoordinateConstants.DOUBLING_CUBE[doublingCubePosition],
		    		this);
		} catch (IOException e) {
			System.out.println("ERROR: Doubling Cube Image not Found\n");
			e.printStackTrace();
		}
		
		//Draw the numbers at the bottom of the pips
	    Font font = g.getFont().deriveFont(15.0f);
	    g.setFont(font);
		g.setColor(Color.YELLOW);
		
		for(int i = 0; i < 12; i++) {
			g.drawString(numbersAtBottom[i], BoardCoordinateConstants.COORDS_POINT[i+1] + 7, 
					BoardCoordinateConstants.COORDS_BOTTOM_ROW[1]+43);
		}
		for(int i = 12; i < 24; i++) {
			g.drawString(numbersAtBottom[i], BoardCoordinateConstants.COORDS_POINT[i+1] + 5,
					BoardCoordinateConstants.COORDS_TOP_ROW[1]-4);
		}
		
		//Draw Black checkers(Colour of each element(checker) is
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
		
		
		//Draw Red checker(Colour of each element(checker) is
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



