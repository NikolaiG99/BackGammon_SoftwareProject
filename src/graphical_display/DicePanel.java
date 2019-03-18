package graphical_display;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import logic.GameLogicBoard;
import user_interface.InformationPanel;

import game_controller.Game;

@SuppressWarnings("serial")
public class DicePanel extends JPanel {
	// Variables storing the first two rolls to see who goes first
	private static int firstRoll;
	private static int secondRoll;
	
	private static int currentRoll;
	
	

	// The information panel which the panel can send text to
	static InformationPanel infoPanel;

	// Timer to carry out rolls
	private static Timer timer;

	private JLabel dice1;
	private JLabel dice2;
	private JLabel text;

	public DicePanel(InformationPanel infoPanel) {
		DicePanel.infoPanel = infoPanel;

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

	/*
	 * Method which does intial game rolls to see who starts
	 */
	public void rollInitialThrows() {
		firstRoll = 0;
		secondRoll = 0;

		// Roll first die
		ThrowDice initialThrow1 = new ThrowDice(dice1, dice2, text);
		timer.schedule(initialThrow1, 0);

		// Wait until roll finished and take result
		while (firstRoll == 0) {
			try {
				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}
			firstRoll = initialThrow1.lastRollResult;
		}

		infoPanel.addText("Black rolled " + firstRoll + ".\n");

		// Roll second die
		ThrowDice initialThrow2 = new ThrowDice(dice1, dice2, text);
		timer.schedule(initialThrow2, 0);

		// Wait until roll finished and take result
		while (secondRoll == 0) {
			try {
				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}
			secondRoll = initialThrow2.lastRollResult;
		}
		infoPanel.addText("Red rolled " + secondRoll + ".\n");

		// If the rolls were the same, print a message and redo the procedure
		if (firstRoll == secondRoll) {
			infoPanel.addText("Same result for intial rolls. Rerolling.\n");
			this.rollInitialThrows();
		}
	}

	public static int getFirstRoll() {
		return firstRoll;
	}

	public static int getSecondRoll() {
		return secondRoll;
	}

	private static Icon getIcon(String name) {
		return new ImageIcon(Game.class.getResource("/resources/" + name));
	}

	/**
	 * Method which rolls the two dice & displays them, and returns the result
	 */

	public static class ThrowDice extends TimerTask {
		private static JLabel dice1;
		private static JLabel dice2;
		private static JLabel text;
		private Random number = new Random();
		private int count;
		int lastRollResult;

		public ThrowDice(JLabel dice1, JLabel dice2, JLabel text) {
			ThrowDice.dice1 = dice1;
			ThrowDice.dice2 = dice2;
			ThrowDice.text = text;
			count = 25;
		}

		public void run() {
			if (count > 0) {
				count--;
				int roll1 = number.nextInt(6);
				int roll2 = number.nextInt(6);
				Icon image1 = getIcon("dice" + (roll1 + 1) + ".png");
				Icon image2 = getIcon("dice" + (roll2 + 1) + ".png");
				dice1.setIcon(image1);
				dice2.setIcon(image2);
				lastRollResult = roll1 + roll2 + 2;
				text.setText("Total: " + lastRollResult);
			} else {
				this.cancel();
			}
		}

		// Method to roll dice when user types next
		public static void roll(GameLogicBoard gameBoard) {
			currentRoll = 0;

			// Roll the dice
			ThrowDice roll = new ThrowDice(dice1, dice2, text);
			timer.schedule(roll, 0);

			// Wait until roll finished and take result
			while (currentRoll == 0) {
				try {
					Thread.sleep(20);
				} catch (Exception e) {
					e.printStackTrace();
				}
				currentRoll = roll.lastRollResult;
			}

			// Check and print which player rolled the dice and the dice total

			infoPanel.addText((gameBoard.isBlackTurn() ? Game.p1 : Game.p2) + ", you rolled  " + currentRoll + ".\n");

		}
	}
}

