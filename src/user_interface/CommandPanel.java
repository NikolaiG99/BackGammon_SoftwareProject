package user_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import graphical_display.BoardPanel;
import graphical_display.DicePanel;
import logic.AvailablePlayAnalyser;
import logic.GameLogicBoard;
import logic.GameState;
import logic.LogicDice;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import game_controller.Game;
import game_controller.GameMethods;

/**
 * This class is responsible for the command panel in the game.
 * And handles commands inputed by the user. Each CommandPanel
 * then must be associated with a GameLogicBoard, a BordPanel, and
 * an InformationPanel. 
 */
@SuppressWarnings("serial")
public class CommandPanel extends JPanel{
	private JTextField field;
	private JLabel label;
	
	private GameLogicBoard gameBoard;
	private BoardPanel boardPanel;
	InformationPanel infoPanel;
	private DicePanel dicePanel;
	private LogicDice logicDice;
	private GameState gameState = null;
	
	public CommandPanel(GameLogicBoard gameBoard, LogicDice logicDice, BoardPanel boardPanel, InformationPanel infoPanel, DicePanel dicePanel){
		this.gameBoard = gameBoard;
		this.boardPanel = boardPanel;
		this.infoPanel = infoPanel;
		this.dicePanel = dicePanel;
		this.logicDice = logicDice;
		
		label = new JLabel("Enter Command: ");
		this.add(label);
		
		field = new JTextField(16);
		field.addActionListener(new ActionListener() {
			 @Override
	            public void actionPerformed(ActionEvent e) {
				 		try {
							handleEvent(parseInputText(field.getText()));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 		field.setText("");
				 	}
		});
		
		this.add(field);
	}
	
	public void initializeGameState(GameState gameState) {
		if(this.gameState == null)
			this.gameState = gameState;
	}
	
	enum CommandType{
		ECHO, QUIT, MOVE, UNKNOWN, NEXT, CHEAT;
	}
	
	class UserCommand{
		CommandType type;
		String input;
		
		public UserCommand(CommandType type, String input) {
			this.type = type;
			this.input = input;
		}
	}
	
	private UserCommand parseInputText(String input){
		
		if(input.toLowerCase().equals("next")) {
			return new UserCommand(CommandType.NEXT, "");
		}
		else if(input.toLowerCase().equals("quit")) {
			return new UserCommand(CommandType.QUIT, "");
		}
		else if(input.toLowerCase().equals("cheat")) {
			return new UserCommand(CommandType.CHEAT, "");
		}
		else if(input.trim().matches("[a-z|A-Z]+")){
			return new UserCommand(CommandType.MOVE, input);
		}
		else
			return new UserCommand(CommandType.UNKNOWN, "");
	}

	private void handleEvent(UserCommand u) throws IOException {
		switch(u.type) {	
		case MOVE: 		Pattern p = Pattern.compile("([a-z|A-Z]+)");
						Matcher m = p.matcher(u.input);
						m.find();
						try {
							AvailablePlayAnalyser.executePlay(m.group(1), boardPanel, gameBoard, gameState);
						}	catch(EmptyStackException e) { infoPanel.addText("Error: No checkers there to move.\nTry again.\n"); break;
						}	catch (Exception e) {infoPanel.addText("Error: " + e.getMessage() + "\n Try again.\n"); break;}
						
						//Do next turn
						Timer timer = new Timer();
			        	TimerTask nextTurn = new TimerTask() {
			        		public void run() {
			        			GameMethods.next(boardPanel, gameBoard, gameState, infoPanel, logicDice, dicePanel);
			        		}
			        	};
			        	timer.schedule(nextTurn, 1000);
						break;
						
		case NEXT: 		GameMethods.next(boardPanel, gameBoard, gameState, infoPanel, logicDice, dicePanel);
		                break;
		                
		case QUIT:	 	System.exit(0);
						break;
						
		case UNKNOWN:	infoPanel.addText("Invalid command, try again.\n");
						break;
						
		case ECHO:		infoPanel.addText(u.input);
						break;
						
		case CHEAT:     gameBoard.setStartingPositions();
		                GameMethods.cheat(boardPanel, gameBoard);
		                GameMethods.drawAllPips(boardPanel, gameBoard);
		                infoPanel.addText((gameState.isBlackTurn() ? Game.p1 : Game.p2) + ", you used cheat" + ".\n");
		                break;
			}
	}
}
	
	