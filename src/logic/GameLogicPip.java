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
	
	public GameLogicPip(boolean blackPip, int id){
		if(blackPip)
			colour = PipColour.BLACK;
		else
			colour = PipColour.RED;
		
		this.pipId = id;
	}
	
	public boolean isBlack() {
		if(colour == PipColour.BLACK)
			return true;
		else
			return false;
	}
}

enum PipColour{
	BLACK, RED
}