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
	
	public Game() throws IOException {
		// Initialize data and logic
		gameBoard = new GameLogicBoard();

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
		
		//Initialize the dice panel
		dicePanel = new DicePanel(infoPanel);

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
	 * The game is started here and immediately passed off to the introductory
	 * frame, which when it's closed, continues the game at the "runGame()"
	 * method.
	 */
	public static void main(String[] args) throws IOException {
		// Start game
		Game game = new Game();

		// Create intro Frame with background image and start button
		
		//TODO Make this work
		new IntroFrame(game.gameFrame, game.infoPanel, game);
		
	}
	
}