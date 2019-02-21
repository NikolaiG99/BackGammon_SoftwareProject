package game_controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import graphical_display.BoardCoordinateConstants;
import graphical_display.BoardPanel;
import logic.GameLogicBoard;
import user_interface.CommandPanel;
import user_interface.InformationPanel;

/**
 * This class connects the different aspects of the game, and the main class
 * runs the actual game. 
 */

public class Game{
	// Store player 1 and 2 name
	public static String p1;
	public static String p2;

	private GameLogicBoard gameBoard;
	private BoardPanel boardPanel;
	private InformationPanel infoPanel;
	private CommandPanel commandPanel;
	private JFrame gameFrame;
	

	public Game() throws IOException {
		// Initialize data and logic
		gameBoard = new GameLogicBoard();

		// Set up JFrame
		gameFrame = new JFrame();
		gameFrame.setSize(910, 500);
		gameFrame.setTitle("Backgammon");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setResizable(false);
		
		
		// Set up Dice
		getDiceImage images = new getDiceImage();
		JLabel dice1 = new JLabel(images.getIcon("dice1.png"));
		JLabel dice2 = new JLabel(images.getIcon("dice1.png"));
		JButton button = new JButton("Throw");
		button.setBounds(2, 2, 1, 1);
		dice1.setBounds(4, 4, 1, 1);
		dice2.setBounds(7, 7, 1, 1);
		JLabel text = new JLabel("Total: 2");
		
		// add dice to panel and the panel to the game board
		JPanel dicePanel = new JPanel();
		dicePanel.setSize(20, 20);
		button.addActionListener(new ButtonListener(dice1, dice2, text));
	        
	    dicePanel.add(button);
	    dicePanel.add(dice1);
	    dicePanel.add(dice2);
	    dicePanel.add(text);
	    
	    gameFrame.add(dicePanel, BorderLayout.SOUTH);
	    
	       
	    // Initialize and attach the board display panel
		boardPanel = new BoardPanel("resources/Screen Shot 2019-02-05 at 21.04.05.png");
		gameFrame.add(boardPanel, BorderLayout.CENTER);

		// Initialize information and command panels
		infoPanel = new InformationPanel();
		commandPanel = new CommandPanel(gameBoard, boardPanel, infoPanel);

		// Attach the information and command panels
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(infoPanel, BorderLayout.NORTH);
		panel.add(commandPanel, BorderLayout.SOUTH);
		gameFrame.add(panel, BorderLayout.EAST);

		// Display JFrame
		gameFrame.setVisible(false);

		// Create intro Frame with background image and start button
		JFrame intro = new JFrame("BackGammon");
		JButton start = new JButton("Click to start game");
		JLabel image = new JLabel(new ImageIcon("resources/Screen Shot 2019-02-20 at 11.31.11.png"));
		image.setBounds(0, 0, 1600, 1400);
		intro.setSize(910, 450);
		intro.add(image);
		start.setBounds(400, 315, 150, 40);
		image.add(start);
		intro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		intro.setResizable(false);

		// Create TextField to get player 1 name
		JTextField textplayer1;
		JLabel labelplayer1;

		labelplayer1 = new JLabel("Player 1 Name: ");
		labelplayer1.setBounds(100, 300, 150, 40);
		labelplayer1.setForeground(Color.white);
		image.add(labelplayer1);
		textplayer1 = new JTextField();
		textplayer1.setBounds(195, 310, 100, 20);
		image.add(textplayer1);

		// Create TextField to get player 2 name
		JTextField textplayer2;
		JLabel labelplayer2;

		labelplayer2 = new JLabel("Player 2 Name: ");
		labelplayer2.setBounds(100, 350, 150, 40);
		labelplayer2.setForeground(Color.white);
		image.add(labelplayer2);
		textplayer2 = new JTextField();
		textplayer2.setBounds(195, 360, 100, 20);
		image.add(textplayer2);
		intro.setVisible(true);

		// Once you click button,game will start
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String p1 = textplayer1.getText();
				String p2 = textplayer2.getText();
				intro.dispose();
				infoPanel.addText(p1 + ", You are the Red Checker");
				infoPanel.addText("\n");
				infoPanel.addText(p2 + ", You are the Black Checker");
				infoPanel.addText("\n");
				gameFrame.setVisible(true);

			}
		});
	}
	
	
	public static void main(String [] args) throws IOException {

		//Start game
		Game game = new Game();
		
		//Display initial positions of all pips
		game.drawAllPips();

		//Demonstrate functionality for Sprint 1
		game.Sprint1_MovePipFromPointToBearOff(6);
		game.Sprint1_MovePipFromPointToBearOff(12);
		game.Sprint1_MovePipFromPointToBar(13);
		game.Sprint1_MovePipFromPointToBearOff(26);

	}
	
	/**
	 * Method scans through the logical representation of the board,
	 * and draw's each pip it finds in the appropriate position
	 */
	void drawAllPips() {
		int[][] positions = gameBoard.getPipPositions();
		for(int i = 0; i < 26; i++) {
			for(int j = 0; positions[i][j] != -1 && j < 15; j++) {
				if(i <= 12)
				boardPanel.drawPip(positions[i][j], 
						BoardCoordinateConstants.COORDS_POINT[i], 
						BoardCoordinateConstants.COORDS_BOTTOM_ROW[j+1]);
				else
				boardPanel.drawPip(positions[i][j], 
						BoardCoordinateConstants.COORDS_POINT[i], 
						BoardCoordinateConstants.COORDS_TOP_ROW[j+1]);
			}
		}
	}
	
	/**
	 * Method moves a pip on a given point by a given number of spots,
	 * where the bottom right points is point #1, and the top right is #24
	 * 
	 * @param pointNum: Which point to move the top pip from
	 * @param numSteps: How many steps to move the top pip by
	 */
	void movePipFromPointByNPoints(int pointNum, int numSteps){
		int pipId = gameBoard.getTopPipIdOnPoint(pointNum);
		
		//Move the pip on the given points, vertically to the middle of the board
		boardPanel.movePip(pipId,
				BoardCoordinateConstants.COORDS_POINT[pointNum],
				BoardCoordinateConstants.MIDDLE_ROW);
		
		//Move the logic representation of the pip
		int newPointNum = gameBoard.movePipByNPoints(pointNum, numSteps);
		
		//Move the pip horizontally to the new X position
		boardPanel.movePip(pipId,
				BoardCoordinateConstants.COORDS_POINT[newPointNum],
				BoardCoordinateConstants.MIDDLE_ROW);
		
		//Find the Y-coordinate of the new position
		int newYpos;
		if(newPointNum >= 13 && newPointNum <= 24)
			newYpos = BoardCoordinateConstants.COORDS_TOP_ROW[gameBoard.getNumberOfPipsOnPoint(newPointNum)];
		else if(newPointNum <= 12 && newPointNum >= 1)
			newYpos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[gameBoard.getNumberOfPipsOnPoint(newPointNum)];
		else if(newPointNum == 0 || newPointNum == 26)
			newYpos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[0];
		else if(newPointNum == 25 || newPointNum == 27)
			newYpos = BoardCoordinateConstants.COORDS_TOP_ROW[0];
		else
			throw new RuntimeException("Could not find y-coordinates of new position");
			
		
		//Move the pip vertically to it's new position
		boardPanel.movePip(pipId,
				BoardCoordinateConstants.COORDS_POINT[newPointNum],
				newYpos);
	}
	
	/**
	 * A method to demonstrate part of the functionality of the program as mentioned
	 * in the docs for Sprint 1. It moves a pip to the bar.
	 * 
	 * @param pointNum: Which point to move the top pip from
	 */
	void Sprint1_MovePipFromPointToBar(int pointNum){
		//Move logical representation of pip
		int barPos = gameBoard.movePipToBar(pointNum);

		//Calculate new coordinates for visual pip
		int yPos;	
		if(barPos == 26)
			yPos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[0];
		else
			yPos = BoardCoordinateConstants.COORDS_TOP_ROW[0];
		
		//Move the pip on the given point, to the bar
		boardPanel.movePip(gameBoard.getTopPipIdOnPoint(barPos), 
				BoardCoordinateConstants.COORDS_POINT[barPos], 
				yPos);
	}
	
	/**
	 * A method to demonstrate part of the functionality of the program as mentioned
	 * in the docs for Sprint 1. It moves a pip off the bar to the starting position.
	 * Returns the point number moved to.
	 * 
	 * @param pointNum: Which point to move the top pip from
	 */
	int Sprint1_MovePipOffBar(int barNum){
		//Move logical pip off bar
		int pointNum = gameBoard.movePipOffBar(barNum);
		
		//Calculate new coordinates for visual pip
		int yPos;
		if(pointNum == 1)
			yPos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[1];
		else
			yPos = BoardCoordinateConstants.COORDS_TOP_ROW[1];
		
		//Move the pip on the given bar, to the start point
		boardPanel.movePip(gameBoard.getTopPipIdOnPoint(pointNum), 
				BoardCoordinateConstants.COORDS_POINT[pointNum], 
				yPos);
		
		return pointNum;
	}
	
	/**
	 * A method to demonstrate part of the functionality of the program as mentioned
	 * in the docs for Sprint 1. It moves a pip on a given point to bear off, moving
	 * one point at a time.
	 * 
	 * @param pointNum: Which point to move the top pip from
	 */
	void Sprint1_MovePipFromPointToBearOff(int pointNum) {
		//If the pip is on a bar take it off the bar
		if(pointNum == 26 || pointNum == 27) {
			int i = Sprint1_MovePipOffBar(pointNum);
			pointNum = i;
		}
		//If it's red step through each point going up
		if(gameBoard.topPipColourOnPointIsRed(pointNum)) {
			for(int i = 0; i < 25-pointNum; i++) {
				movePipFromPointByNPoints(pointNum+i, 1);
			}
		}
		//If it's black step through each point going down
		else {
			for(int i = 0; i < pointNum; i++) {
				movePipFromPointByNPoints(pointNum-i, 1);
			}
		}
			
	}
}