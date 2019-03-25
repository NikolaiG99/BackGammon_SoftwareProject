package logic;

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
		
		public AvailablePlayAnalyser currentTurnPlays; 
		
		public GameState(boolean isBlackTurn, LogicDice logicDice) {
			this.isBlackTurn = isBlackTurn;
			turnNumber = 1;
			initialNumberOfTimesDiceRolled = logicDice.numberOfTimesDiceRolled;
			updateRoll(logicDice);
		}

		private void updateRoll(LogicDice logicDice) {
			currentRollDie1 = logicDice.firstDieRoll;
			currentRollDie2 = logicDice.secondDieRoll;
			currentRollTotal = currentRollDie1 + currentRollDie2;
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
}