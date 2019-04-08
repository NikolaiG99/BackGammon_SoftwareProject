package logic;

import game_controller.Game;

/**
 * Class which keeps track of some necessary information as the game goes on
 */
public class GameState{
		private boolean isBlackTurn;
		private int currentRollTotal;
		private int currentRollDie1;
		private int currentRollDie2;
		private int turnNumber;
		private int initialNumberOfTimesDiceRolled;
		
		private boolean blackHasDoublingCube;
		private boolean redHasDoublingCube;
		private int doublingCubeValue;
		public boolean crawfordRuleInEffect;
		
		public AvailablePlayAnalyser currentTurnPlays; 
		
		public GameState(boolean isBlackTurn, LogicDice logicDice) {
			this.isBlackTurn = isBlackTurn;
			turnNumber = 1;
			initialNumberOfTimesDiceRolled = logicDice.numberOfTimesDiceRolled;
			updateRoll(logicDice);
			
			doublingCubeValue = 1;
			blackHasDoublingCube = false;
			redHasDoublingCube = false;
		}

		private void updateRoll(LogicDice logicDice) {
			currentRollDie1 = logicDice.firstDieRoll;
			currentRollDie2 = logicDice.secondDieRoll;
			currentRollTotal = currentRollDie1 + currentRollDie2;
		}
		
		/**
		 * Method which resets the GameState to the starting position
		 */
		public void resetGameState(boolean isBlackTurn, LogicDice logicDice) {
			this.isBlackTurn = isBlackTurn;
			turnNumber = 1;
			initialNumberOfTimesDiceRolled = logicDice.numberOfTimesDiceRolled;
			updateRoll(logicDice);
			
			doublingCubeValue = 1;
			blackHasDoublingCube = false;
			redHasDoublingCube = false;
		}
		
		/**
		 * Method which updates GameState appropriately when a double is accepted
		 */
		public void acceptDouble(){
			if(isBlackTurn) {
				redHasDoublingCube = true;
				blackHasDoublingCube = false;
			}
			else {
				redHasDoublingCube = false;
				blackHasDoublingCube = true;
			}
			
			doublingCubeValue *= 2;
		}
		
		//Getter methods for doubling cube information
		public boolean BlackHasDoublingCube() {
			return blackHasDoublingCube;
		}
		public boolean RedHasDoublingCube() {
			return redHasDoublingCube;
		}
		public int getDoublingCubeValue() {
			return doublingCubeValue;
		}
		
		/**
		 * Method returns true if it's black's turn, false otherwise.
		 */
		public boolean isBlackTurn(){
			boolean r = isBlackTurn;
			return r;
		}
		
		/**
		 * Method which returns the current dice roll total
		 */
		public int getCurrentRoll() {
			return currentRollTotal;
		}
		
		/**
		 * Method which returns the roll on the first die
		 */
		public int getCurrentRollDie1() {
			return currentRollDie1;
		}
		
		/**
		 * Method which returns the roll on the second die
		 */
		public int getCurrentRollDie2() {
			return currentRollDie2;
		}
		
		/**
		 * Method which steps the game state forward one turn
		 */
		public void nextTurn(LogicDice logicDice) {
			//Wait until the logic roll is finished before taking values and updating game state
			for(int i = 0; (turnNumber + initialNumberOfTimesDiceRolled) != logicDice.getNumberOfTimesDiceRolled(); i++) {
		    	try{
		    		Thread.sleep(20);
		    	} catch (Exception e){
		    		e.printStackTrace();
		    	}
		    	//If some program error causes the two variables to become out of sync, throw an exception
		    	if(i > 200) {
		    		throw new IllegalStateException("Error: GameState has # of rolls and # of turns out of sync");
		    	}
			}
			updateRoll(logicDice);
			turnNumber++;
			isBlackTurn = !isBlackTurn;
			
		}
		
		/**
		 * Method which determines whether the player's whose move it is, is allowed to offer a double
		 */
		public boolean doublingIsAllowed() {
			//Check that the current player owns the cube, or that the cube is in the middle of the board
			if(isBlackTurn & redHasDoublingCube)
				return false;
			else if(!isBlackTurn & blackHasDoublingCube)
				return false;
			
			//Check the Crawford Rule
			if(crawfordRuleInEffect)
				return false;
			
			//Check for a dead cube
			if(isBlackTurn && (Game.BScore + doublingCubeValue*2 > Game.endScore))
				return false;
			else if(!isBlackTurn && (Game.RScore + doublingCubeValue*2 > Game.endScore))
				return false;
			
			return true;
		}
}