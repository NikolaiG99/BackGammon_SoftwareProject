package logic;

import java.util.Random;


/**
 * The logical representation of the dice and the dice rolling, such that the game can
 * be logically run independently of the graphical side(e.g. panels, etc..)
 */
public class LogicDice{
	int numberOfTimesDiceRolled;
	
	int firstDieRoll;
	int secondDieRoll;

	public LogicDice(){
		numberOfTimesDiceRolled = 0;
	}
	
	// Method which rolls the logical representation of the dice. It updates the game state appropriately
	public void roll() {
			Random randomObject = new Random();
		
			firstDieRoll = randomObject.nextInt(6) + 1;
			secondDieRoll = randomObject.nextInt(6) + 1;
			
			numberOfTimesDiceRolled++;
			
			//TODO Update GameState
			
	}
	
	public int getFirstDieRoll() {
		return firstDieRoll;
	}
	
	public int getSecondDieRoll() {
		return secondDieRoll;
	}
	
	public int getNumberOfTimesDiceRolled() {
		return numberOfTimesDiceRolled;
	}
}	