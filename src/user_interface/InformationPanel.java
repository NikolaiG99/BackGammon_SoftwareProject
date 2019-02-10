package user_interface;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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