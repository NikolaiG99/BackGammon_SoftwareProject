package logic;

import java.util.ArrayList;
import java.util.List;

public class AvailablePlayAnalyser{
	private int[][] board;
	private int[][] testBoard;
	
	public AvailablePlayAnalyser() {
		board = new int[28][];
	}
	
	
	/*
	 * A class representing a play in the game.
	 */
	private class gameMove{
		int[] firstHop;
		int[] secondHop;
		
		boolean firstHopIsHit;
		boolean secondHopIsHit;
		
		gameMove(int firstHopStart, int firstHopEnd, int secondHopStart, int secondHopEnd, boolean firstHopIsHit, boolean secondHopisHit){
			firstHop = new int[2];
			secondHop = new int[2];
			this.firstHopIsHit = firstHopIsHit;
			this.secondHopIsHit = secondHopIsHit;
		}
		
		public String toString(){
			//TODO
		}
	}
	
	/**
	 * Method which returns a list of Strings which denote possible plays as described in the "Assignments"
	 * document(Without A,B,C,... enumeration). The list does not contain duplicate plays
	 */
	public List<String> ListAvailablePlays(GameLogicBoard gameBoard, GameState gameState) {
		/* 
		 * The mechanism which removes duplicates works using the fact that: if a play consists of moving two
		 * checkers off two different stacks which are further away from each other than the largest roll(out of
		 * the two dice), then it cannot be a duplicate of any other play. This is true since checkers can 
		 * only move in one direction, and a checker from one stack cannot be moved past the other stack
		 * position(as it is further away than the largest die value will take it).
		 * 
		 * The method also checks for hits, and annotates the play appropriately
		 * 
		 * Also it makes the use of a "readyForBearOff" flag which checks that all the checkers are on a
		 * player's home board, in which case it starts checking for bear-off plays.
		 */
		
		//If a player rolls a double, they move twice so the algorithm/mechanisms must be different so we
		//pass to a special method
		if(gameState.getCurrentRollDie1() == gameState.getCurrentRollDie2())
			return ListAvailablePlaysDoubleRoll(gameBoard, gameState);
		else {
			board = gameBoard.getPipPositions();
			testBoard = gameBoard.getPipPositions();
		
			int roll1 = gameState.getCurrentRollDie1();
			int roll2 = gameState.getCurrentRollDie2();
			boolean black = gameState.isBlackTurn();
			
			//If it's black's turn multiply rolls by -1, since black moves backwards
			if(black) {
				roll1 *= -1;
				roll2 *= -1;
			}
			
			//Scan through board and record all stacks with a player's checkers on them
			List<Integer> availablePositions = new ArrayList<Integer>();
			for(int i = 0; i < 28; i++)
				if(gameBoard.getNumberOfPipsOnPoint(i) > 0)
					if(gameBoard.topPipColourOnPointIsRed(i) != black) 
						availablePositions.add(i);
			
			List<gameMove> availablePlays = new ArrayList<gameMove>();
			
			//For each available position starting from the smallest generate every possible play	
			for(Integer i: availablePositions) {
				if(i + roll1 >= 0 && i + roll1 <= 27){
					gameMove play = new gameMove(i, i+roll1, 0, 0, false, false);
					availablePlays.add(play);
				}
			}
				//Check if a play is a hit, and annotate appropriately 
				
			}
			//For any plays that might be duplicates, check them for being duplicates and remove them if they are
			
			//
			
			int[][] board = new int[28][];
			board = gameBoard.getPipPositions();
			
			
	}
	
	
	//TODO
	private List<String> ListAvailablePlaysDoubleRoll(GameLogicBoard gameBoard, GameState gameState){
		
	}
	
}