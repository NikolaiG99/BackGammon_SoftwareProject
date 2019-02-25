package game_controller;

import graphical_display.BoardCoordinateConstants;
import graphical_display.BoardPanel;
import logic.GameLogicBoard;
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
	public static void Sprint2_MoveCheckerFromPipToPip(int startPip, int endPip, BoardPanel boardPanel, GameLogicBoard gameBoard) {
		int step;
		
		//Account for ordering of pip enumeration in the input
		if(!gameBoard.isBlackTurn()) {
			startPip = 25 - startPip;
			endPip = 25 - endPip;
			step = endPip - startPip;
		}
		else
			step = startPip - endPip;
		
		//Prevent the moving of checkers of wrong colour(not their turn)
		if((gameBoard.isBlackTurn() && gameBoard.topPipColourOnPointIsRed(startPip))
				|| (!gameBoard.isBlackTurn() && !gameBoard.topPipColourOnPointIsRed(startPip)))
				throw new RuntimeException("Cannot move this checker colour, it is not their turn.");
		
		//Provide error information if user tries to move checker wrong way
		if((!gameBoard.isBlackTurn() && startPip > endPip) || (gameBoard.isBlackTurn() && startPip < endPip))
			throw new RuntimeException("You cannot move a checker backwards.");
		
		//Move pips
		movePipFromPointByNPoints(startPip, step, boardPanel, gameBoard);		
	}
	
	/**
	 * Method moves the game forward one turn, updating all necessary parts
	 */
	public static void next(BoardPanel boardPanel, GameLogicBoard gameBoard, InformationPanel infoPanel) {
		gameBoard.nextTurn();
		boardPanel.displayPipEnumeration(gameBoard.isBlackTurn());
		boardPanel.repaint();
		infoPanel.addText("Next turn.\n");
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
}