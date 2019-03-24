package game_controller;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import graphical_display.BoardPanel;
import graphical_display.DicePanel;
import logic.GameLogicBoard;
import logic.LogicDice;
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
					game.rollInitialThrows();
					
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
	
	/*
	 * Method which rolls the dice initially to see who goes first and carries out associated tasks
	 */
	private void rollInitialThrows() {
			firstRoll = 0;
			secondRoll = 0;

			// Roll first die
			LogicDice initialThrow1 = new LogicDice();
			timer.schedule(initialThrow1, 0);

			// Wait until roll finished and take result
			while (firstRoll == 0) {
				try {
					Thread.sleep(20);
				} catch (Exception e) {
					e.printStackTrace();
				}
				firstRoll = initialThrow1.lastRollResult;
				
				Icon image1 = getIcon("dice" + (initialThrow1.roll1 + 1) + ".png");
				Icon image2 = getIcon("dice" + (initialThrow1.roll2 + 1) + ".png");
				dice1.setIcon(image1);
				dice2.setIcon(image2);
				
				text.setText("Total: " + firstRoll);
			}

			infoPanel.addText("Black rolled " + firstRoll + ".\n");

			// Roll second die
			LogicDice initialThrow2 = new LogicDice();
			timer.schedule(initialThrow2, 0);

			// Wait until roll finished and take result
			while (secondRoll == 0) {
				try {
					Thread.sleep(20);
				} catch (Exception e) {
					e.printStackTrace();
				}
				secondRoll = initialThrow2.lastRollResult;
				
				Icon image1 = getIcon("dice" + (initialThrow2.roll1 + 1) + ".png");
				Icon image2 = getIcon("dice" + (initialThrow2.roll2 + 1) + ".png");
				dice1.setIcon(image1);
				dice2.setIcon(image2);
				
				text.setText("Total: " + secondRoll);
			}
			infoPanel.addText("Red rolled " + secondRoll + ".\n");

			// If the rolls were the same, print a message and redo the procedure
			if (firstRoll == secondRoll) {
				infoPanel.addText("Same result for intial rolls. Rerolling.\n");
				rollInitialThrows();
			}
	}
}