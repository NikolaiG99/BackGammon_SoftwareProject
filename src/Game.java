/**
 * Provisionally, this is the "main" class of our program,
 * it contains the main method which runs our Backgammon yoke. 
 */
public class Game{
	public static void main(String [] args) {
		//Initialise data and logic state for the game
		GameLogicBoard board = new GameLogicBoard();
		
		//Just testing the logic//
		board.printBoardToConsole();
		board.movePipByNPoints(1, 1);
		System.out.println("");
		board.printBoardToConsole();
		board.movePipByNPoints(1, 4);
		System.out.println("");
		board.printBoardToConsole();
		//////////////////////////
		
		//TODO: Call a method to initially display board
		//TODO: Have a loop which waits for commands from the user input
		
	}
}