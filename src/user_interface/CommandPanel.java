package user_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EmptyStackException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import graphical_display.BoardPanel;
import logic.GameLogicBoard;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import game_controller.GameMethods;
import graphical_display.DicePanel.ThrowDice;

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
	
	public CommandPanel(GameLogicBoard gameBoard, BoardPanel boardPanel, InformationPanel infoPanel){
		this.gameBoard = gameBoard;
		this.boardPanel = boardPanel;
		this.infoPanel = infoPanel;
		
		label = new JLabel("Enter Command: ");
		this.add(label);
		
		field = new JTextField(16);
		field.addActionListener(new ActionListener() {
			 @Override
	            public void actionPerformed(ActionEvent e) {
				 		handleEvent(parseInputText(field.getText()));
				 		field.setText("");
				 	}
		});
		
		this.add(field);
	}
	
	enum CommandType{
		ECHO, QUIT, MOVE, UNKNOWN, NEXT;
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
		
		if(input.matches("(\\d+)(\\s)(\\d+)")){
			return new UserCommand(CommandType.MOVE, input);
		}
		else if(input.toLowerCase().equals("next")) {
			return new UserCommand(CommandType.NEXT, "");
		}
		else if(input.toLowerCase().equals("quit")) {
			return new UserCommand(CommandType.QUIT, "");
		}
		else
			return new UserCommand(CommandType.UNKNOWN, "");
	}

	private void handleEvent(UserCommand u) {
		switch(u.type) {	
		case MOVE: 		Pattern p = Pattern.compile("(\\d+)(\\s)(\\d+)");
						Matcher m = p.matcher(u.input);
						m.find();
						try {
							GameMethods.Sprint2_MoveCheckerFromPipToPip(
									Integer.parseInt(m.group(1)),
									Integer.parseInt(m.group(3)), boardPanel, gameBoard);
						}	catch(EmptyStackException e) { infoPanel.addText("Error: No checkers there to move.\nTry again.\n");
						}	catch (Exception e) {infoPanel.addText("Error: " + e.getMessage() + "\n Try again.\n");}
						break;
		case NEXT: 		GameMethods.next(boardPanel, gameBoard, infoPanel);
		                ThrowDice.roll(gameBoard);
						break;
		case QUIT:	 	System.exit(0);
						break;
		case UNKNOWN:	infoPanel.addText("Invalid command, try again.\n");
						break;
		case ECHO:		infoPanel.addText(u.input);
						break;
		}
	}
}
	
	