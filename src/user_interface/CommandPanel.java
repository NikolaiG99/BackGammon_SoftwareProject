package user_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import graphical_display.BoardPanel;
import logic.GameLogicBoard;

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
		ECHO, QUIT;
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
		if(input.toLowerCase().equals("quit"))
			return new UserCommand(CommandType.QUIT, "");
		else
			return new UserCommand(CommandType.ECHO, input + '\n');
	}

	private void handleEvent(UserCommand u) {
		switch(u.type) {
		case ECHO:	infoPanel.addText(u.input);
					break;
		case QUIT: System.exit(0);
					break;
		}
	}
}