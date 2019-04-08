package game_controller;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import graphical_display.BoardPanel;
import graphical_display.DicePanel;
import logic.AvailablePlayAnalyser;
import logic.GameLogicBoard;
import logic.GameLogicPip;
import logic.GameState;
import logic.LogicDice;
import user_interface.CommandPanel;
import user_interface.InformationPanel;

/**
 * This class connects the different aspects of the game, and the main class
 * runs the actual game.
 */

public class Game {
	// Store player 1 and 2 name
	public static String p1;
	public static String p2;
	
	// store match ending score
	public static int endScore;
	
	// Store current score
	public static int BScore = 0;
	public static int RScore = 0;

	public GameLogicBoard gameBoard;
	public BoardPanel boardPanel;
	public InformationPanel infoPanel;
	public final CommandPanel commandPanel;
	public  LogicDice logicDice;
	public DicePanel dicePanel;
	public GameState gameState;
	public JFrame gameFrame;
	
	public static JPanel screenContainer;
	public IntroPanel titlePanel;
	private EndPanel endPanel;
	public static CardLayout cl;
	public static JLabel matchLength;
	
	public Game() throws IOException {
		// Initialize data and logic
		gameBoard = new GameLogicBoard();
		logicDice = new LogicDice();

		// Set up JFrame
		gameFrame = new JFrame();
		gameFrame.setSize(1000, 880);
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
		dicePanel = new DicePanel();
		commandPanel = new CommandPanel(gameBoard, logicDice, boardPanel, infoPanel, dicePanel, gameFrame);
		
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

	    // Initialize end screen panel
	    endPanel = new EndPanel();
	    
	    //Attach the title panel and game panel to the card layout container
	    screenContainer.add(gamePanel, "game");
	    screenContainer.add(titlePanel, "title screen");
	    screenContainer.add(endPanel, "end screen");

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
						game.endScore = Integer.parseInt(game.titlePanel.matchScore.getText());
					} catch (Exception e1) {
						game.p1 = "Player 1";
						game.p2 = "Player 2";
					}
					game.infoPanel.addText(game.p1 + ", you are the black checker.\n");
					game.infoPanel.addText(game.p2 + ", you are the red checker.\n");
					game.infoPanel.addText("You are playing to " + game.endScore + "\n");
					
					// display match length
					matchLength = new JLabel("                                   Match Length: " + endScore + "                    Current Score: " + BScore +" - " + RScore);
					matchLength.setBounds(100, 610, 300, 40);
					matchLength.setForeground(Color.black);
					game.dicePanel.add(matchLength);
					//Display initial positions of all checkers
					GameMethods.drawAllPips(game.boardPanel, game.gameBoard);
					
					//Roll dice to see who starts and initialize game state
					if(GameMethods.rollInitialThrows(game.infoPanel, game.logicDice, game.dicePanel).isBlack()) {
						game.gameState = new GameState(true, game.logicDice); //Set black has first move
						game.infoPanel.addText(game.p1 + " starts.\n");
					}
					else {
						game.gameState = new GameState(false, game.logicDice); //Set red has first move
						game.infoPanel.addText(game.p2 + " starts.\n");
					}
					game.commandPanel.initializeGameState(game.gameState);
					
					//Display initial pip numbering according to whose turn in it
					game.boardPanel.displayPipEnumeration(game.gameState.isBlackTurn());
					
					//List initial possible moves for the starting player
			        game.gameState.currentTurnPlays = new AvailablePlayAnalyser(game.gameBoard, game.gameState);
			        List<String> moves = game.gameState.currentTurnPlays.getAvailablePlays();
			        game.infoPanel.addText("The legal moves are:\n");
			        for(String s : moves) {
			        		game.infoPanel.addText(s + "\n");
			        	}
				 	}
		});
	}
}