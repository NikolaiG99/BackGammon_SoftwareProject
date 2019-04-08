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

import javax.swing.JFrame;
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
	private JFrame gameFrame;
	InformationPanel infoPanel;
	private DicePanel dicePanel;
	private LogicDice logicDice;
	private GameState gameState = null;
	
	private boolean commandIsResponse;
	
	public CommandPanel(GameLogicBoard gameBoard, LogicDice logicDice, BoardPanel boardPanel, InformationPanel infoPanel, DicePanel dicePanel, JFrame gameFrame){
		this.gameBoard = gameBoard;
		this.boardPanel = boardPanel;
		this.infoPanel = infoPanel;
		this.dicePanel = dicePanel;
		this.logicDice = logicDice;
		this.gameFrame = gameFrame;
		
		commandIsResponse = false;
		
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
		ECHO, QUIT, MOVE, UNKNOWN, NEXT, CHEAT, OFFERDOUBLE, ACCEPTDOUBLE;
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
		else if(input.toLowerCase().equals("double")){
			return new UserCommand(CommandType.OFFERDOUBLE, "");
		}
		else if(commandIsResponse) {
			commandIsResponse = false;
			if(input.toLowerCase().equals("yes") || input.toLowerCase().equals("y")){
				return new UserCommand(CommandType.ACCEPTDOUBLE, "y");
			} else if(input.toLowerCase().equals("no") || input.toLowerCase().equals("n")) {
				return new UserCommand(CommandType.ACCEPTDOUBLE, "n");
			} else {
				return new UserCommand(CommandType.ACCEPTDOUBLE, "e");
			}
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
			        			GameMethods.next(boardPanel, gameBoard, gameState, infoPanel, logicDice, dicePanel, gameFrame);
			        		}
			        	};
			        	timer.schedule(nextTurn, 1000);
						break;
						
		case NEXT: 		GameMethods.next(boardPanel, gameBoard, gameState, infoPanel, logicDice, dicePanel, gameFrame);
		                break;
		                
		case QUIT:	 	System.exit(0);
						break;

		case OFFERDOUBLE: 	if(!gameState.doublingIsAllowed()) {
						infoPanel.addText("Error: Doubling is not allowed at the moment.\n");
						break;
						}
						else {
						infoPanel.addText("Double has been offered. Does the opponent accept the double? (yes)/(no)\n");
						commandIsResponse = true;
						break;
						}
		
		case ACCEPTDOUBLE: if(u.input.equals("e")) {
						infoPanel.addText("Error: Invalid response. Try again.\n Does the opponent accept the double? (yes)/(no)\n");
						} else if(u.input.equals("y")) {
							GameMethods.acceptDouble(boardPanel, gameState);
						} else if(u.input.equals("n")){
							GameMethods.rejectDouble();
						}
						break;
						
		case UNKNOWN:	infoPanel.addText("Invalid command, try again.\n");
						break;
						
		case ECHO:		infoPanel.addText(u.input);
						break;
						
		case CHEAT:     gameBoard.setStartingPositions();
			            GameMethods.drawAllPips(boardPanel, gameBoard);
		                GameMethods.cheat(boardPanel, gameBoard);
		                infoPanel.addText((gameState.isBlackTurn() ? Game.p1 : Game.p2) + ", you used cheat" + ".\n");
		                
		    			gameState.currentTurnPlays = new AvailablePlayAnalyser(gameBoard, gameState);
		    			List<String> moves = gameState.currentTurnPlays.getAvailablePlays();
		    			infoPanel.addText("The new(after using 'cheat') legal moves are:\n");
		    			for (String s : moves) {
		    				infoPanel.addText(s + "\n");
		    			}
		                
		                break;
			}
	}
}
	
	