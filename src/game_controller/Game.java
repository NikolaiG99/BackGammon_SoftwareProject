package game_controller;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import graphical_display.BoardCoordinateConstants;
import graphical_display.BoardPanel;
import logic.GameLogicBoard;
import user_interface.CommandPanel;
import user_interface.InformationPanel;

/**
 * Provisionally, this is the "main" class of our program,
 * it contains the main method which runs our Backgammon yoke. 
 */
public class Game{
	private GameLogicBoard gameBoard;
	private BoardPanel boardPanel;
	private InformationPanel infoPanel;
	private CommandPanel commandPanel;
	private JFrame gameFrame;
	
	public Game() throws IOException{
		//Initialize data and logic
		gameBoard = new GameLogicBoard();
		
		//Set up JFrame
		gameFrame = new JFrame();
	    gameFrame.setSize(910, 450);
	    gameFrame.setTitle("Backgammon");
	    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    gameFrame.setResizable(false);
	    
	    //Initialize and attach the board display panel
	    boardPanel = new BoardPanel("src/graphical_display/Screen Shot 2019-02-05 at 21.04.05.png");
	    gameFrame.add(boardPanel, BorderLayout.CENTER);
	    
	    //Initialize information and command panels
	    infoPanel = new InformationPanel();
	    commandPanel = new CommandPanel(gameBoard, boardPanel, infoPanel);
	    
	    //Attach the information and command panels
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.add(infoPanel, BorderLayout.NORTH);
	    panel.add(commandPanel, BorderLayout.SOUTH);
	    gameFrame.add(panel, BorderLayout.EAST);
	    
	    //Display JFrame
	    gameFrame.setVisible(true);
	}
	
	public static void main(String [] args) throws IOException {
		//Start game
		Game game = new Game();
		
		//Display initial positions of all pips
		game.drawAllPips();

		game.Sprint1_MovePipFromPointToBearOff(6);
		game.Sprint1_MovePipFromPointToBearOff(12);
		game.Sprint1_MovePipFromPointToBar(13);
		game.Sprint1_MovePipFromPointToBearOff(26);

	}
	
	void drawAllPips() {
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
	
	void movePipFromPointByNPoints(int pointNum, int numSteps){
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
	
	void Sprint1_MovePipFromPointToBar(int pointNum){
		int barPos = gameBoard.movePipToBar(pointNum);
		int yPos;
		//Move the pip on the given point, to the bar
		if(barPos == 26)
			yPos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[0];
		else
			yPos = BoardCoordinateConstants.COORDS_TOP_ROW[0];
		
		boardPanel.movePip(gameBoard.getTopPipIdOnPoint(barPos), 
				BoardCoordinateConstants.COORDS_POINT[barPos], 
				yPos);
	}
	
	int Sprint1_MovePipOffBar(int barNum){
		int pointNum = gameBoard.movePipOffBar(barNum);
		
		int yPos;

		if(pointNum == 1)
			yPos = BoardCoordinateConstants.COORDS_BOTTOM_ROW[1];
		else
			yPos = BoardCoordinateConstants.COORDS_TOP_ROW[1];
		
		//Move the pip on the given bar, to the start point
		boardPanel.movePip(gameBoard.getTopPipIdOnPoint(pointNum), 
				BoardCoordinateConstants.COORDS_POINT[pointNum], 
				yPos);
		
		return pointNum;
	}
	
	void Sprint1_MovePipFromPointToBearOff(int pointNum) {
		//If the pip is on a bar take it off the bar
		if(pointNum == 26 || pointNum == 27) {
			int i = Sprint1_MovePipOffBar(pointNum);
			pointNum = i;
		}
		//If it's red step through each point going up
		if(gameBoard.topPipColourOnPointIsRed(pointNum)) {
			for(int i = 0; i < 25-pointNum; i++) {
				movePipFromPointByNPoints(pointNum+i, 1);
			}
		}
		//If it's black step through each point going down
		else {
			for(int i = 0; i < pointNum; i++) {
				movePipFromPointByNPoints(pointNum-i, 1);
			}
		}
			
	}
}