package bots;
import java.util.Random;
import java.util.Vector;

public class Bot1 implements BotAPI {

    // The public API of Bot must not change
    // This is ONLY class that you can edit in the program
    // Rename Bot to the name of your team. Use camel case.
    // Bot may not alter the state of the game objects
    // It may only inspect the state of the board and the player objects

    private PlayerAPI me, opponent;
    private BoardAPI board;
    private CubeAPI cube;
    private MatchAPI match;
    private InfoPanelAPI info;

    Bot1 (PlayerAPI me, PlayerAPI opponent, BoardAPI board, CubeAPI cube, MatchAPI match, InfoPanelAPI info) {
        this.me = me;
        this.opponent = opponent;
        this.board = board;
        this.cube = cube;
        this.match = match;
        this.info = info;
    }

    public String getName() {
        return "Bot1"; // must match the class name
    }

    public String getCommand(Plays possiblePlays) {
        // Add your code here
    	Random rand = new Random();
    	int size = possiblePlays.plays.size();
    	return "" + (rand.nextInt(size)+1);
    }

    public String getDoubleDecision() {
        // Add your code here
        return "n";
    }
    
    private int pipDifference() {
    	Vector<Integer> C0 = new Vector<Integer>();
    	Vector<Integer> C1 = new Vector<Integer>();
    	int numCheckersOnPip;
    	int P0 = 0;
    	int P1 = 0;
    	int Pd;
    	int blockCount = 0;
    	
		// Compute number of checkers on each pip and add to C0 and C1
		for (int i = 0; i < 25; i++) {
			numCheckersOnPip = board.getNumCheckers(0, i);
			C0.add(i, numCheckersOnPip);
		}

		for (int i = 0; i < 25; i++) {
			numCheckersOnPip = board.getNumCheckers(1, i);
			C1.add(i, numCheckersOnPip);
		}

		// Compute Pip difference
		for (int i = 0; i < 25; i++) {
			int temp = C0.elementAt(i);
			temp *= i;
			P0 += temp;
		}
		for (int i = 0; i < 25; i++) {
			int temp = C1.elementAt(i);
			temp *= i;
			P1 += temp;
		}
		Pd = P1 - P0;

		return Pd;
    }
    
    private int blotDifference() {
    	int blockCount = 0;
    	int blotCount = 0;
    	int Sd;
    	
		// Compute Block-Blot difference
		for (int i = 0; i < 25; i++) {
			if (board.getNumCheckers(1, i) > 1)
				blockCount++;
		}

		for (int i = 0; i < 25; i++) {
			if (board.getNumCheckers(0, i) == 1)
				blotCount++;
		}

		Sd = blockCount - blotCount;
		
		return Sd;
    }
    
    private int numHomeboardBlocks() {
    	int H1 = 0;
    	
		// Compute Number of home board Blocks
		for (int i = 1; i < 7; i++){
			if (board.getNumCheckers(1, i) > 1)
				H1++;
		}
		
		return H1;
    }
    
    private int lengthOfPrime() {
    	int lengthPrime = 0;
    	int primeLength = 0;
    	
		// Compute length of prime
		for (int i = 0; i < 25; i++) {
			if ((board.getNumCheckers(1, i) > 1) && (board.getNumCheckers(1, i + 1) > 1)) {
				int tempPrimeLength = 2;
				for (int j = i + 2; j < j + 4; j++) {
					if (board.getNumCheckers(1, j) > 1) {
						tempPrimeLength++;
					}
					else
						break;

				}
				int primeBlot = 0;
				for (int k = i; k > k - 6; k--) {
					if (k == 0)
						break;
					if (board.getNumCheckers(0, k) > 0)
						primeBlot++;
				}
				if (primeBlot > 1 && primeLength < tempPrimeLength) {
					primeLength = tempPrimeLength;

				}
			}
		}
		return primeLength;
    }

    
    private int checkersInHomeBoard() {
    	int checkersHomeBoard = 0;
    	
		// Calculate checkers in Home board
    	for (int i = 1; i < 7; i++){
			int temp = board.getNumCheckers(1, i);
			checkersHomeBoard += temp;
		}
		
		return checkersHomeBoard;
    }
    
    private int anchorLocation() {
    	int anchorLocation = 0;
    	int numCheckersAnchor = 0;
    	
		// Calculate best Anchor location
    	for (int i = 19; i < 25; i++) {
			if (board.getNumCheckers(1, i) > 1) {
				if (numCheckersAnchor < board.getNumCheckers(1, i)) {
					numCheckersAnchor = board.getNumCheckers(1, i);
					anchorLocation = i;
				}
			}
		}
		
		return anchorLocation;
    }
    
	private int escapedCheckers() {
    	int lastP1 = 0;
    	int numEscapedCheckers = 0;

		
		// Calculate no. of escaped checkers
    	int lastP1Index = 0;
    	for (int j = 24; j > 0; j--)
    		if (board.getNumCheckers(0, j) > 0){
    			lastP1 = j;
    			lastP1Index = 25 - j;
    			break;
    		}
    	
    	for (int i = lastP1Index; i > 0; i--){
    		if (board.getNumCheckers(1, i) > 0){
    			numEscapedCheckers += board.getNumCheckers(1, i);
    		}
    	}
			
    	return numEscapedCheckers;
	}
}
