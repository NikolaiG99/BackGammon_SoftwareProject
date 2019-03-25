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
	List<GameMove> availablePlays;
	
	public AvailablePlayAnalyser(GameLogicBoard gameBoard, GameState gameState) {
		gameBoardSimulation = new GameLogicBoardSimulation(gameBoard);
		
		this.gameBoard = gameBoard;
		this.gameState = gameState;
		
		availablePlays = new ArrayList<GameMove>();
	}
	
	
	/*
	 * A class representing a play in the game.
	 */
	class GameMove{
		Hop firstHop;
		Hop secondHop;
		
		GameMove(Hop firstHop, Hop secondHop){
			this.firstHop = firstHop;
			this.secondHop = secondHop;
		}
		
		public String toString(){
			String ret = firstHop.toString() + " " + secondHop.toString(); 
			return ret;
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
			for(Hop h : possibleFirstHopsWithRoll1) {
				availableNewPositions.add(h.end);
			}
			List<Hop> possibleNewHops = new ArrayList<Hop>();
			possibleNewHops = generatePossibleHops(roll2, availableNewPositions);
			for(Hop h1 : possibleFirstHopsWithRoll1) {
				for(Hop h2 : possibleNewHops) {
					if(h1.end != h2.start)
						continue;
					GameMove play = new GameMove(h1, h2);
					//Check for hits
					checkForAndHandleHits(play);
					//Check for invalid play
					if(moveIsValid(play))
						availablePlays.add(new GameMove(h1, h2));
				}
			}
			
			//REPEAT ABOVE FOR SECOND DIE VALUE:
			//Scan through board and record all stacks with a player's checkers on them
			availablePositions = new ArrayList<Integer>();
			availablePositions = black ? getStacksWithBlackCheckers() : getStacksWithRedCheckers();
			//Get a list of possible hops using the second die roll
			List<Hop> possibleFirstHopsWithRoll2 = new ArrayList<Hop>();
			possibleFirstHopsWithRoll2 = generatePossibleHops(roll2, availablePositions);
			//Check if any hop is a hit, and annotate appropriately 
			checkForAndHandleHits(possibleFirstHopsWithRoll2);
			//Check if any hop is invalid because of opponents checkers
			checkForAndHandleInvalidHops(possibleFirstHopsWithRoll2);
			//Check if with the second roll we have new possible second hops & form plays where there are
			availableNewPositions = new ArrayList<Integer>();
			for(Hop h : possibleFirstHopsWithRoll2) {
				availableNewPositions.add(h.end);
			}
			possibleNewHops = new ArrayList<Hop>();
			possibleNewHops = generatePossibleHops(roll1, availableNewPositions);
			for(Hop h1 : possibleFirstHopsWithRoll2) {
				for(Hop h2 : possibleNewHops) {
					if(h1.end != h2.start)
						continue;
					GameMove play = new GameMove(h1, h2);
					//Check for hits
					checkForAndHandleHits(play);
					//Check for invalid play
					if(moveIsValid(play))
						availablePlays.add(new GameMove(h1, h2));
				}
			}
			
			//NOW ADD REMAINING POSSIBLE MOVES
			for(Hop h1 : possibleFirstHopsWithRoll1) {
				for(Hop h2 : possibleFirstHopsWithRoll2) {
					availablePlays.add(new GameMove(h1, h2));
				}
			}
			
			for(GameMove g : availablePlays) {
				System.out.println(g.toString());
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
			if(gameBoardSimulation.getNumberOfPipsOnPoint(h.end) == 1)
				if(gameBoardSimulation.topPipColourOnPointIsRed(h.start) != gameBoardSimulation.topPipColourOnPointIsRed(h.end))
					h.isHit = true;
		}
	}
	
	/*
	 * Method does same as above except for a GameMove rather than Lists of Hops
	 */
	private void checkForAndHandleHits(GameMove play) {
		//Check for a hit on first hop
		if(gameBoardSimulation.getNumberOfPipsOnPoint(play.firstHop.end) == 1)
			if(gameBoardSimulation.topPipColourOnPointIsRed(play.firstHop.start) != gameBoardSimulation.topPipColourOnPointIsRed(play.firstHop.end))
				play.firstHop.isHit = true;
		
		//Simulate first hop
		gameBoardSimulation.moveCheckerFromStackToStack(play.firstHop.start, play.firstHop.end);
		
		//Check for a hit on second hop
		if(gameBoardSimulation.getNumberOfPipsOnPoint(play.secondHop.end) == 1)
			if(gameBoardSimulation.topPipColourOnPointIsRed(play.secondHop.start) != gameBoardSimulation.topPipColourOnPointIsRed(play.secondHop.end))
				play.secondHop.isHit = true;
		
		//Revert simulation
		gameBoardSimulation.moveCheckerFromStackToStack(play.firstHop.end, play.firstHop.start);
	}
	
	/*
	 * Method checks if any hops lands on a stack with more than one checker of the opposite colour and removes it if so
	 */
	private void checkForAndHandleInvalidHops(List<Hop> l) {
		List<Hop> toRemove = new ArrayList<Hop>();
		for(Hop h: l) {
			if(gameBoardSimulation.getNumberOfPipsOnPoint(h.end) > 1)
				//if checkers on the two stacks are different colours(statement has form (!A^B)v(A^!B) i.e. A XOR B)
				if((!gameBoardSimulation.topPipColourOnPointIsRed(h.start) && gameBoardSimulation.topPipColourOnPointIsRed(h.end))
						|| (gameBoardSimulation.topPipColourOnPointIsRed(h.start) && !gameBoardSimulation.topPipColourOnPointIsRed(h.end)))
					toRemove.add(h);
		}
		for(Hop h : toRemove) {
			l.remove(h);
		}
	}
	
	/*
	 * Method takes a GameMove and returns true if it is a valid move and false otherwise
	 */
	private boolean moveIsValid(GameMove g) {
		//Check first hop isn't invalid due to opponent's checkers
		if(gameBoardSimulation.getNumberOfPipsOnPoint(g.firstHop.end) > 1)
			if((!gameBoardSimulation.topPipColourOnPointIsRed(g.firstHop.start) && gameBoardSimulation.topPipColourOnPointIsRed(g.firstHop.end))
					|| (gameBoardSimulation.topPipColourOnPointIsRed(g.firstHop.start) && !gameBoardSimulation.topPipColourOnPointIsRed(g.firstHop.end)))
				return false;
		
		//Simulate first hop
		gameBoardSimulation.moveCheckerFromStackToStack(g.firstHop.start, g.firstHop.end);
		
		//Check second hop isn't invalid due to opponent's checkers
		if(gameBoardSimulation.getNumberOfPipsOnPoint(g.secondHop.end) > 1)
			if((!gameBoardSimulation.topPipColourOnPointIsRed(g.secondHop.start) && gameBoardSimulation.topPipColourOnPointIsRed(g.secondHop.end))
					|| (gameBoardSimulation.topPipColourOnPointIsRed(g.secondHop.start) && !gameBoardSimulation.topPipColourOnPointIsRed(g.secondHop.end))) {
				//Revert simulation
				gameBoardSimulation.moveCheckerFromStackToStack(g.firstHop.end, g.firstHop.start);
				return false;
			}
		
		gameBoardSimulation.moveCheckerFromStackToStack(g.firstHop.end, g.firstHop.start);
		return true;
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