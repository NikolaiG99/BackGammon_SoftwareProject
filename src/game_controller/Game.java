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

public class Game {
	// Store player 1 and 2 name
	public static String p1;
	public static String p2;

	// Position of the pip in bar position
	public static int cheatTop = 0;
	public static int cheatBottom = 0;

	private static GameLogicBoard gameBoard;
	private static BoardPanel boardPanel;
	private InformationPanel infoPanel;
	private CommandPanel commandPanel;
	private DicePanel dicePanel;
	public static JFrame gameFrame;
	private Gamestate gameState;

	public Game() throws IOException {
		// Initialize data and logic
		gameBoard = new GameLogicBoard();
		gameState = new Gamestate(true);

		// Set up JFrame
		gameFrame = new JFrame();
		gameFrame.setSize(940, 680);
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
	class Gamestate {
		boolean isBlackTurn;

		public Gamestate(boolean isBlackTurn) {
			this.isBlackTurn = isBlackTurn;
		}

		// Resets the game state
		public void reset(boolean isBlackTurn) {
			this.isBlackTurn = isBlackTurn;
		}
	}

	/*
	 * The game is started here and immediately passed off to the introductory
	 * frame, which when it's closed, continues the game at the "runGame()"
	 * method.
	 */
	public static void main(String[] args) throws IOException {
		// Start game
		Game game = new Game();

		// Create intro Frame with background image and start button
		new IntroFrame(game.gameFrame, game.infoPanel, game);
		
	}

	public void runGame(Game game) {
		// Display initial positions of all checkers
		Game.drawAllPips();

		// Roll dice to see who starts and initialize game state
		game.dicePanel.rollInitialThrows();

		if (DicePanel.getFirstRoll() > DicePanel.getSecondRoll()) {
			game.gameState.reset(true);
			game.infoPanel.addText(p1 + " starts.\n");
		} else {
			game.gameState.reset(false);
			game.infoPanel.addText(p2 + " starts.\n");
		}

		// Display initial pip numbering
		Game.boardPanel.displayPipEnumeration(gameState.isBlackTurn);
	}

	/**
	 * Method scans through the logical representation of the board, and draw's
	 * each pip it finds in the appropriate position
	 */
	public static void drawAllPips() {
		int[][] positions = gameBoard.getPipPositions();
		for (int i = 0; i < 26; i++) {
			for (int j = 0; positions[i][j] != -1 && j < 15; j++) {
				if (i <= 12)
					boardPanel.drawPip(positions[i][j], BoardCoordinateConstants.COORDS_POINT[i],
							BoardCoordinateConstants.COORDS_BOTTOM_ROW[j + 1]);
				else
					boardPanel.drawPip(positions[i][j], BoardCoordinateConstants.COORDS_POINT[i],
							BoardCoordinateConstants.COORDS_TOP_ROW[j + 1]);
			}
		}
	}

	/**
	 * Method moves a pip on a given point by a given number of spots, where the
	 * bottom right points is point #1, and the top right is #24
	 * 
	 * @param pointNum:
	 *            Which point to move the top pip from
	 * @param numSteps:
	 *            How many steps to move the top pip by
	 */
	static void movePipFromPointByNPoints(int pointNum, int numSteps) {
		int pipId = gameBoard.getTopPipIdOnPoint(pointNum);

		// Move the pip on the given points, vertically to the middle of the
		// board
		boardPanel.movePip(pipId, BoardCoordinateConstants.COORDS_POINT[pointNum], BoardCoordinateConstants.MIDDLE_ROW);

		// Move the logic representation of the pip
		int newPointNum = gameBoard.movePipByNPoints(pointNum, numSteps);

		// Move the pip horizontally to the new X position
		boardPanel.movePip(pipId, BoardCoordinateConstants.COORDS_POINT[newPointNum],
				BoardCoordinateConstants.MIDDLE_ROW);

		// Find the Y-coordinate of the new position
		int newYpos;
		if (newPointNum >= 13 && newPointNum <= 24)
			newYpos = BoardCoordinateConstants.COORDS_TOP_ROW[gameBoard.getNumberOfPipsOnPoint(newPointNum)];
		else if (newPointNum <= 12 && newPointNum >= 1)
			newYpos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[gameBoard.getNumberOfPipsOnPoint(newPointNum)];
		else if (newPointNum == 0 || newPointNum == 26)
			newYpos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[0];
		else if (newPointNum == 25 || newPointNum == 27)
			newYpos = BoardCoordinateConstants.COORDS_TOP_ROW[0];
		else
			throw new RuntimeException("Could not find y-coordinates of new position");

		// Move the pip vertically to it's new position
		boardPanel.movePip(pipId, BoardCoordinateConstants.COORDS_POINT[newPointNum], newYpos);
	}

	/**
	 * A method which moves a pip from any to the bar.
	 * 
	 * @param pointNum:
	 *            Which point to move the top pip from
	 */
	static void Sprint1_MovePipFromPointToBar(int pointNum) {
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
	 * A method to demonstrate part of the functionality of the program as
	 * mentioned in the docs for Sprint 1. It moves a pip off the bar to the
	 * starting position. Returns the point number moved to.
	 * 
	 * @param pointNum:
	 *            Which point to move the top pip from
	 */
	static int Sprint1_MovePipOffBar(int barNum) {
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
	 * A method to demonstrate part of the functionality of the program as
	 * mentioned in the docs for Sprint 1. It moves a pip on a given point to
	 * bear off, moving one point at a time.
	 * 
	 * @param pointNum:
	 *            Which point to move the top pip from
	 */
	static void Sprint1_MovePipFromPointToBearOff(int pointNum) {
		// If the pip is on a bar take it off the bar
		if (pointNum == 26 || pointNum == 27) {
			int i = Sprint1_MovePipOffBar(pointNum);
			pointNum = i;
		}
		// If it's red step through each point going up
		if (gameBoard.topPipColourOnPointIsRed(pointNum)) {
			for (int i = 0; i < 25 - pointNum; i++) {
				movePipFromPointByNPoints(pointNum + i, 1);
			}
		}
		// If it's black step through each point going down
		else {
			for (int i = 0; i < pointNum; i++) {
				movePipFromPointByNPoints(pointNum - i, 1);
			}
		}

	}
	
	static void CheatMovePipFromPointToBar(int pointNum) {
		// Move logical representation of pip
		int barPos = gameBoard.movePipToBar(pointNum);

		// Calculate new coordinates for visual pip
		int yPos;
		if (barPos == 26) {
			yPos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[cheatBottom + 7];
			cheatBottom++;
			if (cheatBottom == 3)
				cheatBottom =0;
		}

		else {
			yPos = BoardCoordinateConstants.COORDS_TOP_ROW[cheatTop + 7];
			cheatTop++;
			if(cheatTop == 3)
				cheatTop = 0;
		}

		// Move the pip on the given point, to the bar
		boardPanel.movePip(gameBoard.getTopPipIdOnPoint(barPos), BoardCoordinateConstants.COORDS_POINT[barPos], yPos);
	}

	// Cheat Method to display pips in cheat position and be able to play on after cheat called
	public static void cheat() {

		// move black 24 to 5
		movePipFromPointByNPoints(24, 19);
		movePipFromPointByNPoints(24, 19);
		// move red 19 to 24
		movePipFromPointByNPoints(19, 5);
		movePipFromPointByNPoints(19, 5);
		movePipFromPointByNPoints(19, 5);
		// move red 19 and 17 to 22
		movePipFromPointByNPoints(19, 3);
		movePipFromPointByNPoints(19, 3);
		movePipFromPointByNPoints(17, 5);

		// move red 17 and 12 to 21
		movePipFromPointByNPoints(17, 4);
		movePipFromPointByNPoints(17, 4);
		movePipFromPointByNPoints(12, 9);

		// move red 12 to 21,22 and 24 to then add to bar
		movePipFromPointByNPoints(12, 9);
		movePipFromPointByNPoints(12, 10);
		movePipFromPointByNPoints(12, 12);
		CheatMovePipFromPointToBar(21);
		CheatMovePipFromPointToBar(22);
		CheatMovePipFromPointToBar(24);

		// move rest of red to bear off
		Sprint1_MovePipFromPointToBearOff(12);
		Sprint1_MovePipFromPointToBearOff(1);
		Sprint1_MovePipFromPointToBearOff(1);

		// move black 13 to 4,3 and 2
		movePipFromPointByNPoints(13, 9);
		movePipFromPointByNPoints(13, 9);
		movePipFromPointByNPoints(13, 10);
		movePipFromPointByNPoints(13, 10);
		movePipFromPointByNPoints(13, 11);

		// move black 8 to 2 and 1
		movePipFromPointByNPoints(8, 6);
		movePipFromPointByNPoints(8, 7);
		movePipFromPointByNPoints(8, 7);

		// move 3 from black 6 to bar
        CheatMovePipFromPointToBar(6);
		CheatMovePipFromPointToBar(6);
		CheatMovePipFromPointToBar(6);

		// move rest to bear off
		Sprint1_MovePipFromPointToBearOff(6);
		Sprint1_MovePipFromPointToBearOff(6);

	}


}