package game_controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;

import graphical_display.BoardCoordinateConstants;
import graphical_display.BoardPanel;
import graphical_display.DicePanel;
import logic.AvailablePlayAnalyser;
import logic.GameLogicBoard;
import logic.GameLogicPip;
import logic.GameState;
import logic.LogicDice;

import user_interface.EndFrame;
import user_interface.InformationPanel;

/**
 * Class to contain public static methods which carry out game functions.
 * It has no data members of it's own, and each method takes some game components
 * which it needs as arguments, e.g. a board panel and a game logic board, etc...
 */
public class GameMethods{
	
	// Position of the pip in bar position
	public static int cheatTop= 0;
	public static int cheatBottom = 0;
	
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
	public static void Sprint3_MoveCheckerFromPipToPip(int startPip, int endPip, BoardPanel boardPanel, GameLogicBoard gameBoard, GameState gameState) {
		int step;
		step = Math.abs(startPip - endPip);
		
		//Prevent the moving of checkers of wrong colour(not their turn)
		if((gameState.isBlackTurn() && gameBoard.topPipColourOnPointIsRed(startPip))
				|| (!gameState.isBlackTurn() && !gameBoard.topPipColourOnPointIsRed(startPip)))
				throw new RuntimeException("Cannot move this checker colour, it is not their turn.");
		
		//If a checker is on the bar, take it off the bar
		if(startPip == 26 || startPip == 27) {
			if(gameState.isBlackTurn()) {
				Sprint1_MovePipOffBar(26, boardPanel, gameBoard);
				startPip = 24; 
				if(endPip == 24)
					return;
				step = Math.abs(startPip-endPip);
			}
			else {
				Sprint1_MovePipOffBar(27, boardPanel, gameBoard);
				startPip = 1;
				if(endPip == 1)
					return;
				step = Math.abs(startPip-endPip);
			}
		}
		
		//Provide error information if user tries to move checker wrong way
		if((!gameState.isBlackTurn() && startPip > endPip) || (gameState.isBlackTurn() && startPip < endPip))
			throw new RuntimeException("You cannot move a checker backwards.");
		
		//If checker moves to a stack with a single opponent piece, send that piece to bar
		if(gameBoard.getNumberOfPipsOnPoint(endPip) > 0)
			if((!gameBoard.topPipColourOnPointIsRed(startPip) & gameBoard.topPipColourOnPointIsRed(endPip))
					|| (gameBoard.topPipColourOnPointIsRed(startPip) & !gameBoard.topPipColourOnPointIsRed(endPip))) {
				Sprint1_MovePipFromPointToBar(endPip, boardPanel, gameBoard);
			}
			
        //Check for the end of the game
		/*if(GameMethods.gameIsEnded(gameBoard)) {
			Game.cl.show(Game.screenContainer, "end screen");
			new EndFrame(Game.p1);
		}*/
		
		//Move pips
		movePipFromPointByNPoints(startPip, step, boardPanel, gameBoard);		
	}
	
	/**
	 * Method which doubles the cube, and carries out the associated necessary tasks
	 */
	public static void acceptDouble(BoardPanel boardPanel, GameState gameState) {
		gameState.acceptDouble();
		boardPanel.doublingCubeValue = gameState.getDoublingCubeValue();
		if(gameState.BlackHasDoublingCube()) {
			boardPanel.doublingCubePosition = boardPanel.BOTTOM;
		} else {
			boardPanel.doublingCubePosition = boardPanel.TOP;
		}
		boardPanel.repaint();
		return;
	}
	
	/**
	 * Method which ends the game and updates match score when a double is rejected
	 */
	public static void rejectDouble() {
		return;
	}
	
	/**
	 * Method moves the game forward one turn, updating all necessary parts
	 */
	@SuppressWarnings("deprecation")
	public static void next(BoardPanel boardPanel, GameLogicBoard gameBoard, GameState gameState,
			InformationPanel infoPanel, LogicDice logicDice, DicePanel dicePanel, JFrame gameFrame) {
		// Check for the end of the game
		if (GameMethods.gameIsEndedB(gameBoard)) {
			if (gameBoard.getNumberOfPipsOnPoint(25) > 0){
				Game.BScore++;
				gameFrame.dispose();
				if (Game.BScore >= Game.endScore){
					
					new EndFrame(Game.p1);
				}
				else{
					Game game;
					try {
						game = new Game();
					
					game.titlePanel.start.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							// Get player names and inform them of their
							// colour
							
							game.infoPanel.addText(game.p1 + ", you are the black checker.\n");
							game.infoPanel.addText(game.p2 + ", you are the red checker.\n");
							game.infoPanel.addText("You are playing to " + game.endScore + "\n");
							game.infoPanel.addText("Current Score is: " + Game.p1 + " " + Game.BScore + " - " + Game.RScore + " " + Game.p2 + "\n");
							
							// display match length
							game.matchLength = new JLabel("                                   Match Length: " + Game.endScore + "                    Current Score: " + Game.BScore +" - " + Game.RScore);
							game.matchLength.setBounds(100, 610, 300, 40);
							game.matchLength.setForeground(Color.black);
							game.dicePanel.add(game.matchLength);

							// Display initial positions of all checkers
							GameMethods.drawAllPips(game.boardPanel, game.gameBoard);

							// Roll dice to see who starts and initialize
							// game state
							if (game.rollInitialThrows().isBlack()) {
								game.gameState = new GameState(true, game.logicDice); // Set
																						// black
																						// has
																						// first
																						// move
								game.infoPanel.addText(game.p1 + " starts.\n");
							} else {
								game.gameState = new GameState(false, game.logicDice); // Set
																						// red
																						// has
																						// first
																						// move
								game.infoPanel.addText(game.p2 + " starts.\n");
							}
							game.commandPanel.initializeGameState(game.gameState);

							// Display initial pip numbering according to
							// whose turn in it
							game.boardPanel.displayPipEnumeration(game.gameState.isBlackTurn());

							// List initial possible moves for the starting
							// player
							game.gameState.currentTurnPlays = new AvailablePlayAnalyser(game.gameBoard,
									game.gameState);
							List<String> moves = game.gameState.currentTurnPlays.getAvailablePlays();
							game.infoPanel.addText("The legal moves are:\n");
							for (String s : moves) {
								game.infoPanel.addText(s + "\n");
							}
						}

					});
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			else{
				Game.BScore +=2;
				gameFrame.dispose();
				if (Game.BScore >= Game.endScore){
					
					new EndFrame(Game.p1);
				}
				else{
					Game game;
					System.out.println("new Game() once.\\");
					try {
						game = new Game();
					
					game.titlePanel.start.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							// Get player names and inform them of their
							// colour
							
							game.infoPanel.addText(game.p1 + ", you are the black checker.\n");
							game.infoPanel.addText(game.p2 + ", you are the red checker.\n");
							game.infoPanel.addText("You are playing to " + game.endScore + "\n");
							game.infoPanel.addText("Current Score is: " + Game.p1 + " " + Game.BScore + " - " + Game.RScore + " " + Game.p2 + "\n");
							
							
							// display match length
							game.matchLength = new JLabel("                                   Match Length: " + Game.endScore + "                    Current Score: " + Game.BScore +" - " + Game.RScore);
							game.matchLength.setBounds(100, 610, 300, 40);
							game.matchLength.setForeground(Color.black);
							game.dicePanel.add(game.matchLength);

							// Display initial positions of all checkers
							GameMethods.drawAllPips(game.boardPanel, game.gameBoard);

							// Roll dice to see who starts and initialize
							// game state
							if (game.rollInitialThrows().isBlack()) {
								game.gameState = new GameState(true, game.logicDice); // Set
																						// black
																						// has
																						// first
																						// move
								game.infoPanel.addText(game.p1 + " starts.\n");
							} else {
								game.gameState = new GameState(false, game.logicDice); // Set
																						// red
																						// has
																						// first
																						// move
								game.infoPanel.addText(game.p2 + " starts.\n");
							}
							game.commandPanel.initializeGameState(game.gameState);

							// Display initial pip numbering according to
							// whose turn in it
							game.boardPanel.displayPipEnumeration(game.gameState.isBlackTurn());

							// List initial possible moves for the starting
							// player
							game.gameState.currentTurnPlays = new AvailablePlayAnalyser(game.gameBoard,
									game.gameState);
							List<String> moves = game.gameState.currentTurnPlays.getAvailablePlays();
							game.infoPanel.addText("The legal moves are:\n");
							for (String s : moves) {
								game.infoPanel.addText(s + "\n");
							}
						}

					});
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
			
		
				
		    	
			
			//gameFrame.dispose();
			
			//new EndFrame(Game.p1);

		

		else if (GameMethods.gameIsEndedR(gameBoard)) {
			if (gameBoard.getNumberOfPipsOnPoint(0) > 0){
				Game.RScore++;
				gameFrame.dispose();
				if (Game.RScore >= Game.endScore){
					
					new EndFrame(Game.p2);
				}
				else{
					Game game;
					try {
						game = new Game();
					
					game.titlePanel.start.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							// Get player names and inform them of their
							// colour
							
							game.infoPanel.addText(game.p1 + ", you are the black checker.\n");
							game.infoPanel.addText(game.p2 + ", you are the red checker.\n");
							game.infoPanel.addText("You are playing to " + game.endScore + "\n");
							game.infoPanel.addText("Current Score is: " + Game.p1 + " " + Game.BScore + " - " + Game.RScore + " " + Game.p2 + "\n");
							
							
							// display match length
							game.matchLength = new JLabel("                                   Match Length: " + Game.endScore + "                    Current Score: " + Game.BScore +" - " + Game.RScore);
							game.matchLength.setBounds(100, 610, 300, 40);
							game.matchLength.setForeground(Color.black);
							game.dicePanel.add(game.matchLength);

							// Display initial positions of all checkers
							GameMethods.drawAllPips(game.boardPanel, game.gameBoard);

							// Roll dice to see who starts and initialize
							// game state
							if (game.rollInitialThrows().isBlack()) {
								game.gameState = new GameState(true, game.logicDice); // Set
																						// black
																						// has
																						// first
																						// move
								game.infoPanel.addText(game.p1 + " starts.\n");
							} else {
								game.gameState = new GameState(false, game.logicDice); // Set
																						// red
																						// has
																						// first
																						// move
								game.infoPanel.addText(game.p2 + " starts.\n");
							}
							game.commandPanel.initializeGameState(game.gameState);

							// Display initial pip numbering according to
							// whose turn in it
							game.boardPanel.displayPipEnumeration(game.gameState.isBlackTurn());

							// List initial possible moves for the starting
							// player
							game.gameState.currentTurnPlays = new AvailablePlayAnalyser(game.gameBoard,
									game.gameState);
							List<String> moves = game.gameState.currentTurnPlays.getAvailablePlays();
							game.infoPanel.addText("The legal moves are:\n");
							for (String s : moves) {
								game.infoPanel.addText(s + "\n");
							}
						}

					});
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			else{
				Game.RScore +=2;
				gameFrame.dispose();
				if (Game.RScore >= Game.endScore){
					
					new EndFrame(Game.p2);
				}
				else{
					Game game;
					try {
						game = new Game();
					
					game.titlePanel.start.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							// Get player names and inform them of their
							// colour
							
							game.infoPanel.addText(game.p1 + ", you are the black checker.\n");
							game.infoPanel.addText(game.p2 + ", you are the red checker.\n");
							game.infoPanel.addText("You are playing to " + game.endScore + "\n");
							game.infoPanel.addText("Current Score is: " + Game.p1 + " " + Game.BScore + " - " + Game.RScore + " " + Game.p2 + "\n");
							
							
							// display match length
							game.matchLength = new JLabel("                                   Match Length: " + Game.endScore + "                    Current Score: " + Game.BScore +" - " + Game.RScore);
							game.matchLength.setBounds(100, 610, 300, 40);
							game.matchLength.setForeground(Color.black);
							game.dicePanel.add(game.matchLength);

							// Display initial positions of all checkers
							GameMethods.drawAllPips(game.boardPanel, game.gameBoard);

							// Roll dice to see who starts and initialize
							// game state
							if (game.rollInitialThrows().isBlack()) {
								game.gameState = new GameState(true, game.logicDice); // Set
																						// black
																						// has
																						// first
																						// move
								game.infoPanel.addText(game.p1 + " starts.\n");
							} else {
								game.gameState = new GameState(false, game.logicDice); // Set
																						// red
																						// has
																						// first
																						// move
								game.infoPanel.addText(game.p2 + " starts.\n");
							}
							game.commandPanel.initializeGameState(game.gameState);

							// Display initial pip numbering according to
							// whose turn in it
							game.boardPanel.displayPipEnumeration(game.gameState.isBlackTurn());

							// List initial possible moves for the starting
							// player
							game.gameState.currentTurnPlays = new AvailablePlayAnalyser(game.gameBoard,
									game.gameState);
							List<String> moves = game.gameState.currentTurnPlays.getAvailablePlays();
							game.infoPanel.addText("The legal moves are:\n");
							for (String s : moves) {
								game.infoPanel.addText(s + "\n");
							}
						}

					});
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

		}

		else {
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

			gameState.currentTurnPlays = new AvailablePlayAnalyser(gameBoard, gameState);
			List<String> moves = gameState.currentTurnPlays.getAvailablePlays();
			infoPanel.addText("The legal moves are:\n");
			for (String s : moves) {
				infoPanel.addText(s + "\n");
			}

			// If there's only one legal move, execute it, inform user, do next
			// turn
			if (gameState.currentTurnPlays.availablePlays.size() == 1) {
				Timer timer = new Timer();

				TimerTask informUser = new TimerTask() {
					public void run() {
						infoPanel.addText("There is only one possible move, it will be executed automatically.\n");
					}
				};

				TimerTask executeMove = new TimerTask() {
					public void run() {
						try {
							AvailablePlayAnalyser.executePlay("A", boardPanel, gameBoard, gameState);
						} catch (Exception e) { // Game.cl.show(Game.screenContainer,
												// "end screen");
						}
					}

				};

				TimerTask nextTurn = new TimerTask() {
					public void run() {
						try {
							next(boardPanel, gameBoard, gameState, infoPanel, logicDice, dicePanel, gameFrame);
						} catch (Exception e) { // Game.cl.show(Game.screenContainer,
												// "end screen");
						}
					}

				};

				timer.schedule(informUser, 400);
				timer.schedule(executeMove, 1000);
				timer.schedule(nextTurn, 1600);
			}

			// If there are no legal moves, inform user, do next turn
			if (gameState.currentTurnPlays.availablePlays.size() == 0) {
				Timer timer = new Timer();

				TimerTask informUser = new TimerTask() {
					public void run() {
						infoPanel.addText("There are no possible moves. Next turn.\n");
					}
				};

				TimerTask nextTurn = new TimerTask() {
					public void run() {
						next(boardPanel, gameBoard, gameState, infoPanel, logicDice, dicePanel, gameFrame);
					}
				};

				timer.schedule(informUser, 400);
				timer.schedule(nextTurn, 1200);
			}
		}
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
	public static boolean gameIsEndedB(GameLogicBoard gameBoard){
		if(gameBoard.getNumberOfPipsOnPoint(0) == 15)
			return true;
		else 
			return false;
	}
	
	/**
	 * Method to check, whether the game is over(if there is a winner)
	 */
	public static boolean gameIsEndedR(GameLogicBoard gameBoard){
		if(gameBoard.getNumberOfPipsOnPoint(25) == 15)
			return true;
		else 
			return false;
	}
	
	
	static void CheatMovePipFromPointToBar(int pointNum, BoardPanel boardPanel, GameLogicBoard gameBoard) {
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
	public static void cheat(BoardPanel boardPanel, GameLogicBoard gameBoard) {

		
		// move red 19 to 24
		movePipFromPointByNPoints(19, 5, boardPanel, gameBoard);
		movePipFromPointByNPoints(19, 5, boardPanel, gameBoard);
		movePipFromPointByNPoints(19, 5, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(24, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(24, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(24, boardPanel, gameBoard);
		// move red 19 and 17 to 22
		movePipFromPointByNPoints(19, 3, boardPanel, gameBoard);
		movePipFromPointByNPoints(19, 3, boardPanel, gameBoard);
		movePipFromPointByNPoints(17, 5, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(22, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(22, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(22, boardPanel, gameBoard);

		// move red 17 and 12 to 21
		movePipFromPointByNPoints(17, 4, boardPanel, gameBoard);
		movePipFromPointByNPoints(17, 4, boardPanel, gameBoard);
		movePipFromPointByNPoints(12, 9, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(21, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(21, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(21, boardPanel, gameBoard);

		// move red 12 to 21,22 and 24 to then add to bar
		movePipFromPointByNPoints(12, 9, boardPanel, gameBoard);
		movePipFromPointByNPoints(12, 10, boardPanel, gameBoard);
		movePipFromPointByNPoints(12, 12, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(21, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(22, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(24, boardPanel, gameBoard);
		

		// move rest of red to bear off
		Sprint1_MovePipFromPointToBearOff(12, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(1, boardPanel, gameBoard);
		
		

		// move black 13 to 4,3 and 2 then to bear off
		movePipFromPointByNPoints(13, 9, boardPanel, gameBoard);
		movePipFromPointByNPoints(13, 9, boardPanel, gameBoard);
		movePipFromPointByNPoints(13, 10, boardPanel, gameBoard);
		movePipFromPointByNPoints(13, 10, boardPanel, gameBoard);
		movePipFromPointByNPoints(13, 11, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(4, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(4, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(3, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(3, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(2, boardPanel, gameBoard);
		

		// move black 8 to 2 and 1 then to bear off
		movePipFromPointByNPoints(8, 6, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(8, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(8, boardPanel, gameBoard);
		
		Sprint1_MovePipFromPointToBearOff(2, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(24, boardPanel, gameBoard);
		

		// move 3 from black 6 to bar then to bear off
		Sprint1_MovePipFromPointToBearOff(6, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(6, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(6, boardPanel, gameBoard);
		

		// move rest to bear off
		Sprint1_MovePipFromPointToBearOff(6, boardPanel, gameBoard);
		Sprint1_MovePipFromPointToBearOff(6, boardPanel, gameBoard);
		
		// move pips both remaining pips from being on oponents ace points to own ace points
		movePipFromPointByNPoints(24, 22, boardPanel, gameBoard);
		movePipFromPointByNPoints(1, 23, boardPanel, gameBoard);
		movePipFromPointByNPoints(2, 1, boardPanel, gameBoard);

	}
}