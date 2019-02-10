package user_interface;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This class is responsible for the information panel in the game,
 * and associated methods.
 */
@SuppressWarnings("serial")
public class InformationPanel extends JPanel{
	private JTextArea text;
	
	public InformationPanel() {
		text = new JTextArea(8, 20);
		text.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(text);
		
		this.add(scrollPane);
	}
	
	public void addText(String s) {
		text.setText(text.getText() + s);
		return;
	}
}