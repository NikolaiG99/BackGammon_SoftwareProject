package game_controller;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import graphical_display.BoardCoordinateConstants;
import graphical_display.BoardPanel;
import graphical_display.DicePanel;
import logic.GameLogicBoard;
import user_interface.CommandPanel;
import user_interface.InformationPanel;
import user_interface.IntroFrame;

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
	private DicePanel dicePanel;
	private JFrame gameFrame;
	private Gamestate gameState;
	

	public Game() throws IOException {
		// Initialize data and logic
		gameBoard = new GameLogicBoard();
		gameState = new Gamestate(true);
		
		// Set up JFrame
		gameFrame = new JFrame();
		gameFrame.setSize(910, 680);
		gameFrame.setTitle("Backgammon");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setResizable(false);
	    
	       
	    // Initialize and attach the board display panel
		boardPanel = new BoardPanel("src/resources/Screen Shot 2019-02-05 at 21.04.05.png");
		gameFrame.add(boardPanel, BorderLayout.CENTER);

		// Initialize information and command panels
		infoPanel = new InformationPanel();
		commandPanel = new CommandPanel(gameBoard, boardPanel, infoPanel);

		// Attach the information and command panels
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(infoPanel, BorderLayout.NORTH);
		panel.add(commandPanel, BorderLayout.SOUTH);
		gameFrame.add(panel, BorderLayout.EAST);
		
		// Initialize and attach panel with the dice
		dicePanel = new DicePanel(infoPanel);
	    gameFrame.add(dicePanel, BorderLayout.SOUTH);

		// Display JFrame
		gameFrame.setVisible(false);
	}
	
	/*
	 * Class which keeps track of some necessary information as the game goes on
	 */
	class Gamestate{
		boolean isBlackTurn;
		
		public Gamestate(boolean isBlackTurn) {
			this.isBlackTurn = isBlackTurn;
		}
		
		//Resets the game state
		public void reset(boolean isBlackTurn) {
			this.isBlackTurn = isBlackTurn;
		}
	}
	
	/*
	 * The game is started here and immediately passed off to the introductory frame,
	 * which when it's closed, continues the game at the "runGame()" method.
	 */
	public static void main(String [] args) throws IOException {
		//Start game
		Game game = new Game();
		
		// Create intro Frame with background image and start button
		new IntroFrame(game.gameFrame, game.infoPanel, game);
	}
	
	public void runGame(Game game){
		//Display initial positions of all checkers
		game.drawAllPips();

		//Roll dice to see who starts and initialize game state
		game.dicePanel.rollInitialThrows();
		
		if(DicePanel.getFirstRoll() > DicePanel.getSecondRoll()) {
			game.gameState.reset(true);
			game.infoPanel.addText(p1 + " starts.\n");
		}
		else {
			game.gameState.reset(false);
			game.infoPanel.addText(p2 + " starts.\n");
		}
		
		//Display initial pip numbering
		game.boardPanel.displayPipEnumeration(gameState.isBlackTurn);
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
	 * A method which moves a pip from any to the bar.
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
			yPos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[gameBoard.getNumberOfPipsOnPoint(1)];
		else
			yPos = BoardCoordinateConstants.COORDS_TOP_ROW[gameBoard.getNumberOfPipsOnPoint(24)];
		
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