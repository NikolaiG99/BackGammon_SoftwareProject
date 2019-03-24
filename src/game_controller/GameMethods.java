package game_controller;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import graphical_display.BoardCoordinateConstants;
import graphical_display.BoardPanel;
import graphical_display.DicePanel;
import logic.GameLogicBoard;
import logic.GameLogicPip;
import logic.GameState;
import logic.LogicDice;
import user_interface.InformationPanel;

/**
 * Class to contain public static methods which carry out game functions.
 * It has no data members of it's own, and each method takes some game components
 * which it needs as arguments, e.g. a board panel and a game logic board, etc...
 */
public class GameMethods{
	
	/**
	 * Method scans through the logical representation of the board,
	 * and draw's each pip it finds in the appropriate position
	 */
	public static void drawAllPips(BoardPanel boardPanel, GameLogicBoard gameBoard) {
		int[][] positions = gameBoard.getPipPositions();
		for(int i = 0; i < 26; i++) {
			for(int j = 0; positions[i][j] != -1 && j < 15; j++) {
				if(i <= 12)
				boardPanel.drawPip(positions[i][j], 
						BoardCoordinateConstants.COORDS_POINT[i], 
						BoardCoordinateConstants.COORDS_BOTTOM_ROW[j+1]);
				else
				boardPanel.drawPip(positions[i][j], 
						BoardCoordinateConstants.COORDS_POINT[i], 
						BoardCoordinateConstants.COORDS_TOP_ROW[j+1]);
			}
		}
	}
	
	/**
	 * A method which moves a checker from a given pip to another pip as described in the documentation
	 * It does not include support for moving checkers to and from the bar/bear off positions as it
	 * is not specified in the documentation for sprint 2, so we cannot know the format of a command
	 * to do one of these tasks. Our Sprint 1 code also only supports moving the checkers forward along
	 * the board; and since it's a step in the right direction we have not reversed this progress, and 
	 * therefore this method does not suppoty moving backwards.
	 * 
	 * @param startPoint The pip to move the checker from
	 * @param endPoint The pip to move the checker to
	 */
	public static void Sprint2_MoveCheckerFromPipToPip(int startPip, int endPip, BoardPanel boardPanel, GameLogicBoard gameBoard, GameState gameState) {
		int step;
		
		//Account for ordering of pip enumeration in the input
		if(!gameState.isBlackTurn()) {
			startPip = 25 - startPip;
			endPip = 25 - endPip;
			step = endPip - startPip;
		}
		else
			step = startPip - endPip;
		
		//Prevent the moving of checkers of wrong colour(not their turn)
		if((gameState.isBlackTurn() && gameBoard.topPipColourOnPointIsRed(startPip))
				|| (!gameState.isBlackTurn() && !gameBoard.topPipColourOnPointIsRed(startPip)))
				throw new RuntimeException("Cannot move this checker colour, it is not their turn.");
		
		//Provide error information if user tries to move checker wrong way
		if((!gameState.isBlackTurn() && startPip > endPip) || (gameState.isBlackTurn() && startPip < endPip))
			throw new RuntimeException("You cannot move a checker backwards.");
		
		//Move pips
		movePipFromPointByNPoints(startPip, step, boardPanel, gameBoard);		
	}
	
	/**
	 * Method moves the game forward one turn, updating all necessary parts
	 */
	public static void next(BoardPanel boardPanel, GameLogicBoard gameBoard, GameState gameState, InformationPanel infoPanel, LogicDice logicDice, DicePanel dicePanel) {
		logicDice.roll();
		dicePanel.update(logicDice);
		
		gameState.nextTurn(logicDice);
		boardPanel.displayPipEnumeration(gameState.isBlackTurn());
		
		dicePanel.repaint();
		boardPanel.repaint();
		
		infoPanel.addText("Next turn.\nRolling.\n");
		String msg = gameState.isBlackTurn() ? "Black rolled " : "Red rolled ";
		msg += gameState.getCurrentRoll();
		infoPanel.addText(msg + "\n");
	}
	
	/**
	 * Method moves a pip on a given point by a given number of spots,
	 * where the bottom right points is point #1, and the top right is #24
	 * 
	 * @param pointNum: Which point to move the top pip from
	 * @param numSteps: How many steps to move the top pip by
	 */
	public static void movePipFromPointByNPoints(int pointNum, int numSteps, BoardPanel boardPanel, GameLogicBoard gameBoard){
		int pipId = gameBoard.getTopPipIdOnPoint(pointNum);
		
		//Move the pip on the given points, vertically to the middle of the board
		boardPanel.movePip(pipId,
				BoardCoordinateConstants.COORDS_POINT[pointNum],
				BoardCoordinateConstants.MIDDLE_ROW);
		
		//Move the logic representation of the pip
		int newPointNum = gameBoard.movePipByNPoints(pointNum, numSteps);
		
		//Move the pip horizontally to the new X position
		boardPanel.movePip(pipId,
				BoardCoordinateConstants.COORDS_POINT[newPointNum],
				BoardCoordinateConstants.MIDDLE_ROW);
		
		//Find the Y-coordinate of the new position
		int newYpos;
		if(newPointNum >= 13 && newPointNum <= 24)
			newYpos = BoardCoordinateConstants.COORDS_TOP_ROW[gameBoard.getNumberOfPipsOnPoint(newPointNum)];
		else if(newPointNum <= 12 && newPointNum >= 1)
			newYpos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[gameBoard.getNumberOfPipsOnPoint(newPointNum)];
		else if(newPointNum == 0 || newPointNum == 26)
			newYpos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[0];
		else if(newPointNum == 25 || newPointNum == 27)
			newYpos = BoardCoordinateConstants.COORDS_TOP_ROW[0];
		else
			throw new RuntimeException("Could not find y-coordinates of new position");
			
		
		//Move the pip vertically to it's new position
		boardPanel.movePip(pipId,
				BoardCoordinateConstants.COORDS_POINT[newPointNum],
				newYpos);
	}
	
	/**
	 * A method to demonstrate part of the functionality of the program as mentioned
	 * in the docs for Sprint 1. It moves a pip off the bar to the starting position.
	 * Returns the point number moved to.
	 * 
	 * @param pointNum: Which point to move the top pip from
	 */
	public static int Sprint1_MovePipOffBar(int barNum, BoardPanel boardPanel, GameLogicBoard gameBoard){
		//Move logical pip off bar
		int pointNum = gameBoard.movePipOffBar(barNum);
		
		//Calculate new coordinates for visual pip
		int yPos;
		if(pointNum == 1)
			yPos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[gameBoard.getNumberOfPipsOnPoint(1)];
		else
			yPos = BoardCoordinateConstants.COORDS_TOP_ROW[gameBoard.getNumberOfPipsOnPoint(24)];
		
		//Move the pip on the given bar, to the start point
		boardPanel.movePip(gameBoard.getTopPipIdOnPoint(pointNum), 
				BoardCoordinateConstants.COORDS_POINT[pointNum], 
				yPos);
		
		return pointNum;
	}
	
	/**
	 * A method which moves a pip from any to the bar.
	 * 
	 * @param pointNum: Which point to move the top pip from
	 */
	public static void Sprint1_MovePipFromPointToBar(int pointNum, BoardPanel boardPanel, GameLogicBoard gameBoard){
		//Move logical representation of pip
		int barPos = gameBoard.movePipToBar(pointNum);

		//Calculate new coordinates for visual pip
		int yPos;	
		if(barPos == 26)
			yPos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[0];
		else
			yPos = BoardCoordinateConstants.COORDS_TOP_ROW[0];
		
		//Move the pip on the given point, to the bar
		boardPanel.movePip(gameBoard.getTopPipIdOnPoint(barPos), 
				BoardCoordinateConstants.COORDS_POINT[barPos], 
				yPos);
	}
	
	/**
	 * A method to demonstrate part of the functionality of the program as mentioned
	 * in the docs for Sprint 1. It moves a pip on a given point to bear off, moving
	 * one point at a time.
	 * 
	 * @param pointNum: Which point to move the top pip from
	 */
	public static void Sprint1_MovePipFromPointToBearOff(int pointNum, BoardPanel boardPanel, GameLogicBoard gameBoard) {
		//If the pip is on a bar take it off the bar
		if(pointNum == 26 || pointNum == 27) {
			int i = Sprint1_MovePipOffBar(pointNum, boardPanel, gameBoard);
			pointNum = i;
		}
		//If it's red step through each point going up
		if(gameBoard.topPipColourOnPointIsRed(pointNum)) {
			for(int i = 0; i < 25-pointNum; i++) {
				movePipFromPointByNPoints(pointNum+i, 1, boardPanel, gameBoard);
			}
		}
		//If it's black step through each point going down
		else {
			for(int i = 0; i < pointNum; i++) {
				movePipFromPointByNPoints(pointNum-i, 1, boardPanel, gameBoard);
			}
		}
	}
	
	/**
	 * Method to check, whether the game is over(if there is a winner)
	 */
	public static boolean gameIsEnded(GameLogicBoard gameBoard){
		if(gameBoard.getNumberOfPipsOnPoint(0) == 30 || gameBoard.getNumberOfPipsOnPoint(25) == 30)
			return true;
		
		else return false;
	}
	
	/**
	 * Method which returns a list of Strings which denote possible plays as described in the "Assignments"
	 * document(Without A,B,C,... enumeration). The list does not contain duplicate plays
	 */
	public static List<String> ListAvailablePlays(GameLogicBoard gameBoard, GameState gameState) {
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
		
		//If a player rolls a double, they move twice and then the algorithm/mechanisms must be different so we
		//pass to a special method
		if(gameState.getCurrentRollDie1() == gameState.getCurrentRollDie2())
			return ListAvailablePlaysDoubleRoll(gameBoard, gameState);
		else {
		
			
			
		}
	}
	
	//TODO
	private static List<String> ListAvailablePlaysDoubleRoll(GameLogicBoard gameBoard, GameState gameState){
		
	}
	
	static void CheatMovePipFromPointToBar(int pointNum) {
		// Move logical representation of pip
		int barPos = gameBoard.movePipToBar(pointNum);

		// Calculate new coordinates for visual pip
		int yPos;
		if (barPos == 26) {
			yPos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[cheatBottom + 7];
			cheatBottom++;
			if (cheatBottom == 3)
				cheatBottom =0;
		}

		else {
			yPos = BoardCoordinateConstants.COORDS_TOP_ROW[cheatTop + 7];
			cheatTop++;
			if(cheatTop == 3)
				cheatTop = 0;
		}

		// Move the pip on the given point, to the bar
		boardPanel.movePip(gameBoard.getTopPipIdOnPoint(barPos), BoardCoordinateConstants.COORDS_POINT[barPos], yPos);
	}

	// Cheat Method to display pips in cheat position and be able to play on after cheat called
	public static void cheat() {

		// move black 24 to 5
		movePipFromPointByNPoints(24, 19);
		movePipFromPointByNPoints(24, 19);
		// move red 19 to 24
		movePipFromPointByNPoints(19, 5);
		movePipFromPointByNPoints(19, 5);
		movePipFromPointByNPoints(19, 5);
		// move red 19 and 17 to 22
		movePipFromPointByNPoints(19, 3);
		movePipFromPointByNPoints(19, 3);
		movePipFromPointByNPoints(17, 5);

		// move red 17 and 12 to 21
		movePipFromPointByNPoints(17, 4);
		movePipFromPointByNPoints(17, 4);
		movePipFromPointByNPoints(12, 9);

		// move red 12 to 21,22 and 24 to then add to bar
		movePipFromPointByNPoints(12, 9);
		movePipFromPointByNPoints(12, 10);
		movePipFromPointByNPoints(12, 12);
		CheatMovePipFromPointToBar(21);
		CheatMovePipFromPointToBar(22);
		CheatMovePipFromPointToBar(24);

		// move rest of red to bear off
		Sprint1_MovePipFromPointToBearOff(12);
		Sprint1_MovePipFromPointToBearOff(1);
		Sprint1_MovePipFromPointToBearOff(1);

		// move black 13 to 4,3 and 2
		movePipFromPointByNPoints(13, 9);
		movePipFromPointByNPoints(13, 9);
		movePipFromPointByNPoints(13, 10);
		movePipFromPointByNPoints(13, 10);
		movePipFromPointByNPoints(13, 11);

		// move black 8 to 2 and 1
		movePipFromPointByNPoints(8, 6);
		movePipFromPointByNPoints(8, 7);
		movePipFromPointByNPoints(8, 7);

		// move 3 from black 6 to bar
        CheatMovePipFromPointToBar(6);
		CheatMovePipFromPointToBar(6);
		CheatMovePipFromPointToBar(6);

		// move rest to bear off
		Sprint1_MovePipFromPointToBearOff(6);
		Sprint1_MovePipFromPointToBearOff(6);

	}
}