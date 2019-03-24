package logic;

import java.util.Stack;

/**
 * This class is responsible for handling the logical representation
 * of the game in terms of data.
 */
public class GameLogicBoard{
	private static BoardDataStructure gameBoard;
	public GameState gameState;
	
	public GameLogicBoard(){
		gameBoard = new BoardDataStructure();
		
		//Fill board with the starting pip positions
		setStartingPositions();
	}

	//Method for testing which prints the board's contents to console
	public void printBoardToConsole(){
		for(int i = 13; i < 28; i ++) {
			for(int numPips = gameBoard.getPoint(i).size(); numPips > 0; numPips--) {
				System.out.print("*");
			}		
			if(i == 25)
				System.out.print("}");
			
			System.out.print("|");
			
			if(i == 24)
				System.out.print("{");
		}
		
		System.out.println("");
		
		for(int i = 12; i >= 0; i--) {
			for(int numPips = gameBoard.getPoint(i).size(); numPips > 0; numPips--) {
				System.out.print("*");
			}
			if(i==0)
				System.out.print("}");
			
			System.out.print("|");
			
			if(i == 1)
				System.out.print("{");
		}
		
		System.out.println("");	
	}
	
	//A method which returns an two-dimensional int array representation
	//of the position of the pips. [1] is point 1, [24] is point
	//24, [0], [25] is bear off for black and red respectively. [26], [27] is
	//bar for black and red respectively.
	//The numbers in each array corresponds to the pip ids 
	public int[][] getPipPositions(){
		int[][] representation = new int[28][];
		
		//Set all arrays to 15 -1s (15 is max number of pips on a point)
		// -1 because there is no pip with this id
		for(int i = 0; i <= 27; i ++) {
			representation[i] = new int[] {
					-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
			};
		}
		
		//Go through each stack, and fill the appropriate element with the pip id 
		for(int i = 0; i <= 27; i++) {
			Stack<GameLogicPip> stack = gameBoard.getPoint(i);
			Stack<GameLogicPip> tmp = new Stack<GameLogicPip>();
			
			int size = stack.size();
			for(int j = size; j > 0; j--) {
				representation[i][stack.size()-1] = stack.peek().pipId;
				//save pip objects as they are pushed off
					tmp.push(stack.pop());
			}
			
			//Push the elements back on to the stack
			for(int j = size; j > 0; j--) {
				stack.push(tmp.pop());
			}
		}
		
		return representation;
	}
	
	//Method which returns the pip id of the top most pip on a given point
	public int getTopPipIdOnPoint(int point){
		return gameBoard.getPoint(point).peek().pipId;
	}
	
	//Method which returns true if the colour of the top pip on a point is red and false otherwise
	public boolean topPipColourOnPointIsRed(int point){
		if(gameBoard.getPoint(point).peek().colour == PipColour.RED)
			return true;
		else return false;
	}
	
	//Method which returns the number of pips on a given point
	public int getNumberOfPipsOnPoint(int point) {
		return gameBoard.getPoint(point).size();
	}
	
	//A method which moves the pip on the specified point 1 point
	//Returns the point number the pip was moved to
	private int movePipByOne(int pointNum) {
		Stack<GameLogicPip> s1, s2;
		
		s1 = gameBoard.getPoint(pointNum);
		
		//If there are no pips at this point throw an exception
		if(s1.empty() == true)
			throw new RuntimeException("No pips on given point to move!");
		
		//If the specified point is a valid point
		if(pointNum >= 1 && pointNum <= 24) {
			//If top pip is red, move it forwards, if it is black move it backwards
			int newPos;
			if(s1.peek().colour == PipColour.RED) {
				newPos = pointNum+1;
			}else {
				newPos = pointNum-1;
			}
			s2 = gameBoard.getPoint(newPos);
			s2.push(s1.pop());
			
			return newPos;
		}
		

		//If the point is on the bar, move it appropriately
		if(pointNum == 26) {
			s2 = gameBoard.getPoint(24);
			s2.push(s1.pop());
			return 24;
		}
		else if(pointNum == 27) {
			s2 = gameBoard.getPoint(1);
			s2.push(s1.pop());
			return 1;
		}
			
		//Otherwise, it is an invalid point. Throw exception.
		else
			throw new RuntimeException("Invalid point(does not exist) so cannot move any pips!");
			

	}
	
	//Method moves the top pip on a given point to it's bar, returns point number
	public int movePipToBar(int pointNum) {
		Stack<GameLogicPip> s1, s2;
		s1 = gameBoard.getPoint(pointNum);
		
		int barPos;
		
		if(s1.peek().colour == PipColour.BLACK)
			barPos = 26;
		else
			barPos = 27;
		
		s2 = gameBoard.getPoint(barPos);
		s2.push(s1.pop());
		return barPos;
	}
	
	//Method moves the top pip on either bar to it's starting point
	public int movePipOffBar(int barPos) {
		Stack<GameLogicPip> s1, s2;
		s1 = gameBoard.getPoint(barPos);
		
		int pointNum;
		
		if(s1.peek().colour == PipColour.BLACK)
			pointNum = 24;
		else
			pointNum = 1;
		
		s2 = gameBoard.getPoint(pointNum);
		s2.push(s1.pop());
		return pointNum;
	}
	
	//Method moves the top pip on a given point by a given number of steps
	//Returns the point number the pip was moved to
	public int movePipByNPoints(int pointNum, int numSteps) {
		int newPointNum = pointNum;
		
		for(int i = numSteps; i > 0; i--) {
			newPointNum = movePipByOne(newPointNum) ;
		}
		
		return newPointNum;
	}
	
	//Method which fills the board with the pips in their starting positions
	public static void setStartingPositions() {
		if(gameBoard.boardIsEmpty() == false)
			gameBoard.emptyBoard();
		
	//The structure has stack element 0, corresponding to bottom right point(point #1), and stack element 23
	//corresponding to top right point(point #24).
	Stack<GameLogicPip> currentStack;
		
	//Fill point 1 (2 pips)
	currentStack = gameBoard.getPoint(1);
	for(int i = 0; i < 2; i++)
		currentStack.push(new GameLogicPip(PipColour.RED, i));
	
	//Fill point 6 (5 pips)
	currentStack = gameBoard.getPoint(6);
	for(int i = 0; i < 5; i++)
		currentStack.push(new GameLogicPip(PipColour.BLACK, 2+i));
	
	//Fill point 8 (3 pips)
	currentStack = gameBoard.getPoint(8);
	for(int i = 0; i < 3; i++)
		currentStack.push(new GameLogicPip(PipColour.BLACK, 7+i));
	
	//Fill point 12 (5 pips)
	currentStack = gameBoard.getPoint(12);
	for(int i = 0; i < 5; i++)
		currentStack.push(new GameLogicPip(PipColour.RED, 10+i));
	
	//Fill point 13 (5 pips)
	currentStack = gameBoard.getPoint(13);
	for(int i = 0; i < 5; i++)
		currentStack.push(new GameLogicPip(PipColour.BLACK, 15+i));
	
	//Fill point 17 (3 pips)
	currentStack = gameBoard.getPoint(17);
	for(int i = 0; i < 3; i++)
		currentStack.push(new GameLogicPip(PipColour.RED, 20+i));
	
	//Fill point 19 (5 pips)
	currentStack = gameBoard.getPoint(19);
	for(int i = 0; i < 5; i++)
		currentStack.push(new GameLogicPip(PipColour.RED, 23+i));
	
	//Fill point 24 (2 pips)
	currentStack = gameBoard.getPoint(24);
	for(int i = 0; i < 2; i++)
		currentStack.push(new GameLogicPip(PipColour.BLACK, 28+i));
	}
}