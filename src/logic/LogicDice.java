package logic;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;


/**
 * The logical representation of the dice and the dice rolling, such that the game can
 * be logically run independently of the graphical side(e.g. panels, etc..)
 * 
 * @author Nikolai
 */
public class LogicDice extends TimerTask {
	private JLabel text;
	private Random number = new Random();
	public int lastRollResult;
	public int currentRoll;
	public int roll1;
	public int roll2;

	public LogicDice() {
	}

	public JLabel getText() {
		JLabel ret = new JLabel();
		ret = text;
		return text;
	}
	
	public void run() {
			roll1 = number.nextInt(6);
			roll2 = number.nextInt(6);
			lastRollResult = roll1 + roll2 + 2;
			text.setText("Total: " + lastRollResult);
	}

	// Method to roll dice when user types next
	public int roll() {
		currentRoll = 0;
		
		// Roll the dice
		LogicDice roll = new LogicDice();
		Timer timer = new Timer();
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
	
		// Return the amount rolled
		return currentRoll;
	}
}	