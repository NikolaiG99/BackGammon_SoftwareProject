package graphical_display;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import logic.LogicDice;

import game_controller.Game;

@SuppressWarnings("serial")
public class DicePanel extends JPanel {
	private JLabel dice1;
	private JLabel dice2;
	private JLabel text;
	
	private int numberOfTimesDiceUpdated;

	public DicePanel() {
		numberOfTimesDiceUpdated = 0;
		
		// Set up Dice
		dice1 = new JLabel(getIcon("dice1.png"));
		dice2 = new JLabel(getIcon("dice1.png"));
		
		
		dice1.setBounds(4, 4, 1, 1);
		dice2.setBounds(7, 7, 1, 1);
		text = new JLabel("Total: 2");

		this.add(dice1);
		this.add(dice2);
		this.add(text);

		// add dice to panel and the panel to the game board
		this.setSize(20, 20);
		
	}
	
	/**
	 * Method to be called to update graphical image when logical dice are rolled
	 */
	public void update(LogicDice logicDice) {
		
		//Wait until the logic roll is finished before taking values and updating image
		for(int i = 0; numberOfTimesDiceUpdated != logicDice.getNumberOfTimesDiceRolled()-1; i++) {
	    	try{
	    		Thread.sleep(20);
	    	} catch (Exception e){
	    		e.printStackTrace();
	    	}
	    	//If some program error causes the two variables to become out of sync, reset them after a long period
	    	if(i > 200) {
	    		numberOfTimesDiceUpdated = logicDice.getNumberOfTimesDiceRolled()-1;
	    		throw new IllegalStateException("Error: DicePanel.update() has # of updates out of sync with logical representation.");
	    	}
		}
		
		Icon image1 = getIcon("dice" + (logicDice.getFirstDieRoll()) + ".png");
		Icon image2 = getIcon("dice" + (logicDice.getSecondDieRoll()) + ".png");
		dice1.setIcon(image1);
		dice2.setIcon(image2);
		
		text.setText("Total: " + (logicDice.getFirstDieRoll() + logicDice.getSecondDieRoll()));
		
		numberOfTimesDiceUpdated++;
	}

	private Icon getIcon(String name) {
		return new ImageIcon(Game.class.getResource("/resources/" + name));
	}
}		
	
			