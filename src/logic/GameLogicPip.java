package logic;

/**
 * A class representing a Pip(BackGammon terminology) for
 * the game logic.
 */
public class GameLogicPip{
	PipColour colour;
	final int pipId;
	
	GameLogicPip(PipColour colour, int id){
		this.colour = colour;
		this.pipId = id;
	}
}

enum PipColour{
	BLACK, RED
}