import java.util.Stack;

public class GameLogicBoard{
	private BoardDataStructure gameBoard;
	
	GameLogicBoard(){
		gameBoard = new BoardDataStructure();
		
		//Fill board with the starting pip positions
		setStartingPositions();
	}

	//Method for testing which prints the board's contents to console
	public void printBoardToConsole(){
		for(int i = 13; i < 26; i ++) {
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
	
	//A method which moves the pip on the specified point 1 point
	//Returns the point number the pip was moved to
	private int movePipByOne(int pointNum) {
		Stack<GameLogicPip> s1, s2;
		
		s1 = gameBoard.getPoint(pointNum);
		
		//If there are no pips at this point throw an exception
		if(s1.empty() == true)
			throw new RuntimeException("No pips on given point to move!");
		
		//If the specified point does not exist(i.e. not a number between 1-24) throw an exception
		if(pointNum > 24 || pointNum < 1)
			throw new RuntimeException("Invalid point(does not exist) so cannot move any pips!");
		
		//If top pip is red, move it forwards, if it is black move it backwards
		int newPos;
		if(s1.peek().colour == PipColour.RED) {
			newPos = pointNum+1;
		}else {
			newPos = pointNum-1;
		}
		s2 = gameBoard.getPoint(newPos);
		s2.push(s1.pop());
		
		//TODO: Call Graphical Board Update Method
		
		return newPos;
	}
	
	//Method moves the top pip on a given point by a given number of steps
	public void movePipByNPoints(int pointNum, int numSteps) {
		int newPointNum = pointNum;
		
		for(int i = numSteps; i > 0; i--) {
			newPointNum = movePipByOne(newPointNum) ;
		}
	}
	
	//Method which fills the board with the pips in their starting positions
	public void setStartingPositions() {
		if(gameBoard.boardIsEmpty() == false)
			gameBoard.emptyBoard();
		
	//The structure has stack element 0, corresponding to bottom right point(point #1), and stack element 23
	//corresponding to top right point(point #24).
	Stack<GameLogicPip> currentStack;
		
	//Fill point 1 (2 pips)
	currentStack = gameBoard.getPoint(1);
	for(int i = 0; i < 2; i++)
		currentStack.push(new GameLogicPip(PipColour.RED));
	
	//Fill point 6 (5 pips)
	currentStack = gameBoard.getPoint(6);
	for(int i = 0; i < 5; i++)
		currentStack.push(new GameLogicPip(PipColour.BLACK));
	
	//Fill point 8 (3 pips)
	currentStack = gameBoard.getPoint(8);
	for(int i = 0; i < 3; i++)
		currentStack.push(new GameLogicPip(PipColour.BLACK));
	
	//Fill point 12 (5 pips)
	currentStack = gameBoard.getPoint(12);
	for(int i = 0; i < 5; i++)
		currentStack.push(new GameLogicPip(PipColour.RED));
	
	//Fill point 13 (5 pips)
	currentStack = gameBoard.getPoint(13);
	for(int i = 0; i < 5; i++)
		currentStack.push(new GameLogicPip(PipColour.BLACK));
	
	//Fill point 17 (3 pips)
	currentStack = gameBoard.getPoint(17);
	for(int i = 0; i < 3; i++)
		currentStack.push(new GameLogicPip(PipColour.RED));
	
	//Fill point 19 (5 pips)
	currentStack = gameBoard.getPoint(19);
	for(int i = 0; i < 5; i++)
		currentStack.push(new GameLogicPip(PipColour.RED));
	
	//Fill point 24 (2 pips)
	currentStack = gameBoard.getPoint(24);
	for(int i = 0; i < 2; i++)
		currentStack.push(new GameLogicPip(PipColour.BLACK));
	}
}