package graphical_display;

import java.util.Timer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import logic.LogicDice;
import user_interface.InformationPanel;

import game_controller.Game;

@SuppressWarnings("serial")
public class DicePanel extends JPanel {
	// Variables storing the first two rolls to see who goes first
	private int firstRoll;
	private int secondRoll;

	// The information panel which the panel can send text to
	InformationPanel infoPanel;

	// Timer to carry out rolls
	private static Timer timer;

	private JLabel dice1;
	private JLabel dice2;
	private JLabel text;

	public DicePanel(InformationPanel infoPanel) {
		this.infoPanel = infoPanel;

		timer = new Timer();

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
		Icon image1 = getIcon("dice" + (logicDice.roll1 + 1) + ".png");
		Icon image2 = getIcon("dice" + (logicDice.roll2 + 1) + ".png");
		dice1.setIcon(image1);
		dice2.setIcon(image2);
		
		text.setText("Total: " + logicDice.currentRoll);
	}

	/*
	 * Method which does initial game rolls to see who starts
	 */
	public void rollInitialThrows() {
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

	public int getFirstRoll() {
		return firstRoll;
	}

	public int getSecondRoll() {
		return secondRoll;
	}

	private Icon getIcon(String name) {
		return new ImageIcon(Game.class.getResource("/resources/" + name));
	}
}		
	
			