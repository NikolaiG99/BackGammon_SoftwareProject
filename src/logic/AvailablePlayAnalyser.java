package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AvailablePlayAnalyser{
	
	/*
	 * A GameLogicBoard which is used for simulation: It has a method to be able to move 
	 * pips in any direction regardless of colour, and it copies all data from a real
	 * GameLogicBoard handed to it in the contructor
	 */
	class GameLogicBoardSimulation extends GameLogicBoard{
		
		//Move a checker from one stack to another
		public void moveCheckerFromStackToStack(int from, int to){
			Stack<GameLogicPip> s1 = gameBoard.getPoint(from);
			Stack <GameLogicPip> s2 = gameBoard.getPoint(to);
			s2.push(s1.pop());
		}
		
		GameLogicBoardSimulation(GameLogicBoard toCopyFrom){
			gameBoard = toCopyFrom.getDataStructure();
		}
	}
	
	private GameLogicBoard gameBoard;
	private GameState gameState;
	private GameLogicBoardSimulation gameBoardSimulation;
	
	public AvailablePlayAnalyser(GameLogicBoard gameBoard, GameState gameState) {
		gameBoardSimulation = new GameLogicBoardSimulation(gameBoard);
		
		this.gameBoard = gameBoard;
		this.gameState = gameState;
	}
	
	
	/*
	 * A class representing a play in the game.
	 */
	private class gameMove{
		Hop firstHop;
		Hop secondHop;
		
		gameMove(int firstHopStart, int firstHopEnd, int secondHopStart, int secondHopEnd, boolean firstHopIsHit, boolean secondHopisHit){
			firstHop = new Hop(firstHopStart, firstHopEnd, firstHopIsHit);
			secondHop = new Hop(secondHopStart, secondHopEnd, secondHopisHit);
		}
		
		public String toString(){
			//TODO
			return "";
		}
	}
	/*
	 * A class representing an individual hop within a move
	 */
	private class Hop{
		int start;
		int end;
		boolean isHit;
		
		Hop(int start, int end, boolean isHit){
			this.start = start;
			this.end = end;
			this.isHit = isHit;
		}
		
		public String toString() {
			String ret = "" + start + "-" + end;
			ret += isHit ? "*" : "";
			return ret;
		}
	}
	
	/**
	 * Method which returns a list of Strings which denote possible plays as described in the "Assignments"
	 * document(Without A,B,C,... enumeration). The list does not contain duplicate plays
	 */
	public List<String> ListAvailablePlays() {
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
			availablePositions = black ? getStacksWithBlackCheckers() : getStacksWithRedCheckers();
			
			//Get a list of possible hops using the first die roll
			List<Hop> possibleFirstHopsWithRoll1 = new ArrayList<Hop>();
			possibleFirstHopsWithRoll1 = generatePossibleHops(roll1, availablePositions);
			
			//Check if any hop is a hit, and annotate appropriately 
			checkForAndHandleHits(possibleFirstHopsWithRoll1);
			
			//Check if any hop is invalid because of opponents checkers
			checkForAndHandleInvalidHops(possibleFirstHopsWithRoll1);
			
			//Check if with the second roll we have new possible second hops & form plays where there are
			List<Integer> availableNewPositions = new ArrayList<Integer>();
			
			for(Hop h : possibleFirstHops) {
				System.out.println(h.toString() + "\n");
			}
			return null;
			
			//For any plays that might be duplicates, check them for being duplicates and remove them if they are
		}
			
	}
	
	/*
	 * The following two methods return a list containing the point numbers with checkers of a certain colour
	 */
	private List<Integer> getStacksWithBlackCheckers(){
		List<Integer> availablePositions = new ArrayList<Integer>();
		for(int i = 0; i < 28; i++)
			if(gameBoardSimulation.getNumberOfPipsOnPoint(i) > 0)
				if(!gameBoardSimulation.topPipColourOnPointIsRed(i)) 
					availablePositions.add(i);
		return availablePositions;
	}
	private List<Integer> getStacksWithRedCheckers(){
		List<Integer> availablePositions = new ArrayList<Integer>();
		for(int i = 0; i < 28; i++)
			if(gameBoardSimulation.getNumberOfPipsOnPoint(i) > 0)
				if(gameBoardSimulation.topPipColourOnPointIsRed(i)) 
					availablePositions.add(i);
		return availablePositions;
	}
	
	/*
	 * Method checks if any hop lands on a stack with one checker of the opposite colour and adds "*" if so
	 */
	private void checkForAndHandleHits(List<Hop> l) {
		for(Hop h: l) {
			if(gameBoard.getNumberOfPipsOnPoint(h.end) == 1)
				if(gameBoard.topPipColourOnPointIsRed(h.start) != gameBoard.topPipColourOnPointIsRed(h.end))
					h.isHit = true;
		}
	}
	
	/*
	 * Method checks if any hops lands on a stack with more than one checker of the opposite colour and removes it if so
	 */
	private void checkForAndHandleInvalidHops(List<Hop> l) {
		for(Hop h: l) {
			if(gameBoard.getNumberOfPipsOnPoint(h.end) > 1)
				if(gameBoard.topPipColourOnPointIsRed(h.start) != gameBoard.topPipColourOnPointIsRed(h.end))
					l.remove(h);
		}
	}
	
	/*
	 * Method generates a list of possible hops given a die value(1 to 6) and a list of possible starting points
	 */
	private List<Hop> generatePossibleHops(int rollValue, List<Integer> availablePositions){
		List<Hop> possibleHops = new ArrayList<Hop>();
		
		//For each available position starting from the smallest generate every possible first hop
		for(Integer i: availablePositions) {
			if(i == 26){ //Bar for red
				Hop hop = new Hop(i, 25 + rollValue, false);
				possibleHops.add(hop);
			} else if(i == 27) { //Bar for black
				Hop hop = new Hop(i, rollValue, false);
				possibleHops.add(hop);
			}else if(i + rollValue >= 0 && i + rollValue <= 25){ //General case
				Hop hop = new Hop(i, i+rollValue, false);
				possibleHops.add(hop);
			}
		}
		
		return possibleHops;
	}
	
	//TODO
	private List<String> ListAvailablePlaysDoubleRoll(GameLogicBoard gameBoard, GameState gameState){
		return null;
	}
	
}