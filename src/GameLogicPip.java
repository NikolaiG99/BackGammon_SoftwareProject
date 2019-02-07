/**
 * A class representing a Pip(BackGammon terminology) for
 * the game logic.
 */
public class GameLogicPip{
	PipColour colour;
	
	GameLogicPip(PipColour colour){
		this.colour = colour;
	}
}

enum PipColour{
	BLACK, RED
}