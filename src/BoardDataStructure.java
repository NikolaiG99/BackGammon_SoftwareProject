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
	public BoardDataStructure() {
		board = new ArrayList<Stack<GameLogicPip>>(24);
		
		for(int i = 0; i < 24; i++) {
			board.add(new Stack<GameLogicPip>());
		}
	}
}