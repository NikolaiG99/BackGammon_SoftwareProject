package user_interface;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import game_controller.Game;
import game_controller.GameMethods;
import logic.AvailablePlayAnalyser;
import logic.GameState;

public class EndFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Game game;
	JFrame end;

	public EndFrame(String player){
		JFrame end = new JFrame();
		end.setTitle("BackGammon");

		JLabel image = new JLabel(new ImageIcon("src/resources/Screen Shot 2019-03-19 at 19.25.41.png"));
		image.setBounds(0, 0, 1600, 1400);
		end.setSize(910, 450);
		end.add(image);

		end.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		end.setResizable(false);
		end.setVisible(true);

		JOptionPane frame = null;

		// Create label to say who won

		JLabel labelplayer1;

		labelplayer1 = new JLabel("Congrats " + player + ", you are the winner, do you want to play again?");
		labelplayer1.setBounds(100, 300, 800, 80);
		labelplayer1.setFont(new Font("Serif", Font.PLAIN, 25));
		labelplayer1.setForeground(Color.white);
		image.add(labelplayer1);

		JTextField field;

		field = new JTextField();
		field.setBounds(250, 375, 100, 20);
		image.add(field);

		field.addActionListener(new ActionListener() {
			@SuppressWarnings({ "null", "deprecation" })
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = field.getText().toLowerCase();
				
				field.setText(text);
				if (text.equals("yes")) {
					end.dispose();
					Game.cl.previous(getParent());

				
		/*
					try {
					
							game = new Game();
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
										if(game.rollInitialThrows().isBlack()) {
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
							frame.disable();
							
							
						} catch (IOException e2) {
						}
					
					*/
					frame.disable();
					} 
					

				

				if (text.equals("no")) {
					System.exit(0);
				}

				else {
					JOptionPane.showMessageDialog(frame, "Answer yes or no");

				}

			}

		});

		

	}
}
