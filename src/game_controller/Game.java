package game_controller;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import graphical_display.BoardPanel;
import graphical_display.DicePanel;
import logic.GameLogicBoard;
import user_interface.CommandPanel;
import user_interface.InformationPanel;

/**
 * This class connects the different aspects of the game, and the main class
 * runs the actual game.
 */

public class Game {
	// Store player 1 and 2 name
	public String p1;
	public String p2;

	// Position of the pip in bar position
	public static int CHEAT_TOP = 0;
	public static int CHEAT_BOTTOM = 0;

	private GameLogicBoard gameBoard;
	private BoardPanel boardPanel;
	private InformationPanel infoPanel;
	private static CommandPanel commandPanel;
	private DicePanel dicePanel;
	
	private JPanel screenContainer;
	private IntroPanel titlePanel;
	static CardLayout cl;
	
	public Game() throws IOException {
		// Initialize data and logic
		gameBoard = new GameLogicBoard();

		// Set up JFrame
		JFrame gameFrame = new JFrame();
		gameFrame.setSize(940, 680);
		gameFrame.setTitle("Backgammon");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setResizable(false);

		// Set up Card Layout to control different screens, e.g. title screen, game screen etc..
		CardLayout cl = new CardLayout(5, 5);
		screenContainer = new JPanel(cl);
		
		// Initialize and attach the board display panel
		boardPanel = new BoardPanel("src/resources/Screen Shot 2019-02-05 at 21.04.05.png");
		JPanel gamePanel = new JPanel();
		gamePanel.add(boardPanel, BorderLayout.CENTER);

		// Initialize information, dice, and command panels
		infoPanel = new InformationPanel();
		dicePanel = new DicePanel(infoPanel);
		commandPanel = new CommandPanel(gameBoard, boardPanel, infoPanel, dicePanel);
		
		// Attach the information and command panels to the game panel
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(infoPanel, BorderLayout.NORTH);
		panel.add(commandPanel, BorderLayout.SOUTH);
		gamePanel.add(panel, BorderLayout.EAST);
		
		//Initialize and attach the panel with the dice
	    gamePanel.add(dicePanel, BorderLayout.SOUTH);

	    // Initialize title screen panel
	    titlePanel = new IntroPanel();
	    
	    //Add a listener to the title panel button to switch to the game when pressed
	    titlePanel.start.addActionListener(e -> cl.show(screenContainer, "game"));

	    //Attach the title panel and game panel to the card layout container
	    screenContainer.add(gamePanel, "game");
	    screenContainer.add(titlePanel, "title screen");

		// Display JFrame
	    gameFrame.add(screenContainer);
	    cl.show(screenContainer, "title screen");
		gameFrame.setVisible(true);

	}

	/*
	 * The game is started here and immediately passed off to the introductory
	 * frame, which when it's closed, continues the game at the "runGame()"
	 * method.
	 */
	public static void main(String[] args) throws IOException {
		//Start game
		Game game = new Game();
		
		game.titlePanel.start.addActionListener(new ActionListener() {
			 @Override
	            public void actionPerformed(ActionEvent e) {
				 
					//Get player names and inform them of their colour
					try {
						game.p1 = game.titlePanel.p1.getText();
						game.p2 = game.titlePanel.p2.getText();
					} catch (Exception e1) {
						game.p1 = "Player 1";
						game.p2 = "Player 2";
					}
					game.infoPanel.addText(game.p1 + ", you are the black checker.\n");
					game.infoPanel.addText(game.p2 + ", you are the red checker.\n");
					
					//Display initial positions of all checkers
					GameMethods.drawAllPips(game.boardPanel, game.gameBoard);

					//Roll dice to see who starts and initialize game state
					game.dicePanel.rollInitialThrows();
					
					if(game.dicePanel.getFirstRoll() > game.dicePanel.getSecondRoll()) {
						game.gameBoard.newGameState(true); //Set black has first move
						game.infoPanel.addText(game.p1 + " starts.\n");
					}
					else {
						game.gameBoard.newGameState(false); //Set red has first move
						game.infoPanel.addText(game.p2 + " starts.\n");
					}
					
					//Display initial pip numbering according to whose turn in it
					game.boardPanel.displayPipEnumeration(game.gameBoard.isBlackTurn());
					
				 	}
		});
	}
}