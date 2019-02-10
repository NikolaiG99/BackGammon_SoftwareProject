package logic;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class of the data structure representing the board to be used
 * for the game logic. It consists of a List of 24 Stacks, where
 * each Stack represents a "Point"(BackGammon terminology).
 */
public class BoardDataStructure{
	private List<Stack<GameLogicPip>> board;
	
	//Initialize list of stacks, then initialize each stack.
	
	//Stacks in positions 1-24 are the points, while  positions 0 & 25 are 
	//the bear off position for black and red respectively.
	//26 and 27 are the bar positions for black and red respectively
	public BoardDataStructure() {
		board = new ArrayList<Stack<GameLogicPip>>(28);
		
		for(int i = 0; i < 28; i++) {
			board.add(new Stack<GameLogicPip>());
		}
	}
	
	//Get backgammon point number i, where the points are numbered from 1-24 inclusive
	public Stack<GameLogicPip> getPoint(int i){
		return board.get(i);
	}
	
	//Method which checks if the board is empty
	public boolean boardIsEmpty() {
		
		for(int i = 0; i < 28; i++) {
			if(board.get(i).empty() == false)
				return false;
		}
		
		return true;
	}
	
	//Helper method which empties all the stacks of pips
	public void emptyBoard(){
		Stack<GameLogicPip> s;
		
		for(int i = 0; i < 28; i++) {
			s = board.get(i);
			while(s.empty() == false){
				s.pop();
			}
		}
		
		return;
	}
}