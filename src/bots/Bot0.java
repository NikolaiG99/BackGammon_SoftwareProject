package bots;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Bot0 implements BotAPI {

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
    
    private scoreFunctionWeightsData [][] scoreWeightsData;
    private final int EARLY_GAME = 0;
    private final int MIDDLE_GAME = 1;
    private final int END_GAME = 2;
    private final int RACE_GAME = 3;
    private final int ADVANTAGEOUS_POSITION = 0;
    private final int EVEN_POSITION = 1;
    private final int DISADVANTAGEOUS_POSITION = 2;
    
    private int currentStage;
    private int currentPositionType;
    
    private int numberOfTurns = 0;

    private List<ScoreProfile> [][] learnedDataForStages;
    
    Bot0(PlayerAPI me, PlayerAPI opponent, BoardAPI board, CubeAPI cube, MatchAPI match, InfoPanelAPI info) {
        this.me = me;
        this.opponent = opponent;
        this.board = board;
        this.cube = cube;
        this.match = match;
        this.info = info;
        
        currentStage = EARLY_GAME;
        currentPositionType = EVEN_POSITION;
        
        learnedDataForStages = new List[4][3];
        for(int i = 0; i < 4; i++) {
        	for(int j = 0; j < 3; j++) {
        		learnedDataForStages[i][j] = new ArrayList<ScoreProfile>();
        	}
        }
        
        try {
			loadScoreFunctionWeightsData();
		} catch (IOException e) {
			System.out.println("ScoresFunctionNotLoaded");
			e.printStackTrace();
		}
        System.out.println("Constructor finished");
    }

    public String getName() {
    	System.out.println("Entered getName()");
        return "Bot0"; // must match the class name
    }

    public String getCommand(Plays possiblePlays) {
    	System.out.println("getCommand Entered");
    	
    	numberOfTurns++;
    	determineGameStageAndPositionType();
    	
    	String latestInfo = info.getLatestInfo(); 
    	if(latestInfo.contains("(" + me.getColorName() + ") WINS THE")) {
    		//Compile unique move profiles per stage		
    		compileLearnedData();
    		//Update score files weight to reinforce bot's current conditions
    		try {
				updateScoreWeights(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		//Reset learning infrastructure for next game
    		//Set number of turns to 0
    		for(int i = 0; i < 4; i++) {
    			for(int j = 0; j < 3; j++) {
    				learnedDataForStages[i][j].clear();
    			}
    		}
    		numberOfTurns = 0;
    	}
    	else if(latestInfo.contains("WINS THE")) {
    		//Compile unique move profiles per stage
    		compileLearnedData();
    		//Update score files weight to opposite to the bot's current conditions
    		try {
				updateScoreWeights(false);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		//Reset learning infrastructure for next game
    		//Set number of turns to 0
    		for(int i = 0; i < 4; i++) {
    			for(int j = 0; j < 3; j++) {
    				learnedDataForStages[i][j].clear();
    			}
    		}
    		numberOfTurns = 0;
    	}
    		
    	
    	int bestPlay = 1;
    	int bestPlayScore = 0;
    	ScoreProfile bestPlayScoreProfile = new ScoreProfile();
    	
    	List<ScoreProfile> scoreProportionProfiles = new ArrayList<ScoreProfile>();
    	
    	Iterator<Play> iter = possiblePlays.iterator();
    	for(int i = 1; iter.hasNext(); i++) {
    		Play p = iter.next();
    		
    		ScoreProfile sProf = new ScoreProfile();
    		
    		int s = scorePlay(p, sProf);
    		if(s > bestPlayScore) {
    			scoreProportionProfiles.add(bestPlayScoreProfile);
    			bestPlay = i;
    			bestPlayScore = s;
    			bestPlayScoreProfile = sProf;
    		}
    		else
    			scoreProportionProfiles.add(sProf);
    			
    	}
    	
    	learn(scoreProportionProfiles, bestPlayScoreProfile);
    	
    	return "" + bestPlay;
	}

	public String getDoubleDecision() {
		// Add your code here
		return "n";
    }
	
	private void updateScoreWeights(boolean victory) throws IOException {
		File file = new File("bots/Bot0_scoreFunctionValues.txt");
			
			String feature1 = "_Pip Difference ";
			String feature2 = "_Blot Difference ";
			String feature3 = "_Num Home Board Blocks ";
			String feature4 = "_Prime Length ";
			String feature5 = "_#Check HomeBoardVsOpp ";
			String feature6 = "_Anchor location ";
			
			String posTypes[] = new String[3];
			posTypes[0] = "Advantage !~!\n";
			posTypes[1] = "Even !~!\n";
			posTypes[2] = "Disadvantage !~!\n";
			
			String stages[] = new String[4];
			stages[0] = "Early Game !*!\n";
			stages[1] = "Middle Game !*!\n";
			stages[2] = "End Game !*!\n";
			stages[3] = "Race Game !*!\n";
		
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			for(int i = 0; i < 4; i++) {
				writer.write(stages[i]);
				for(int j = 0; j < 3; j++) {
					
					writer.write(posTypes[j]);
					writer.write(feature1);
					if(learnedDataForStages[i][j].size() > 0) {
						if(victory) {
							scoreWeightsData[i][j].pipDifferenceWeight *= (1 + learnedDataForStages[i][j].get(0).portionDueToPipDifference);
							scoreWeightsData[i][j].numHomeBoardBlocksWeight *= (1 + learnedDataForStages[i][j].get(0).portionDueToNumOnHomeBoard);
							scoreWeightsData[i][j].blockBlotDifferenceWeight *= (1 + learnedDataForStages[i][j].get(0).portionDueToBlotBlockDiff);
							scoreWeightsData[i][j].primeLength *= (1 + learnedDataForStages[i][j].get(0).portionDueToPrimeLength);
							scoreWeightsData[i][j].checkDifferenceHomeBoards *= (1 + learnedDataForStages[i][j].get(0).portionDueToCheckDifference);
							scoreWeightsData[i][j].anchorLocation *= (1 + learnedDataForStages[i][j].get(0).portionDueToAnchorLocation);
						}
						else {
							scoreWeightsData[i][j].pipDifferenceWeight *= (1 - learnedDataForStages[i][j].get(0).portionDueToPipDifference);
							scoreWeightsData[i][j].numHomeBoardBlocksWeight *= (1 - learnedDataForStages[i][j].get(0).portionDueToNumOnHomeBoard);
							scoreWeightsData[i][j].blockBlotDifferenceWeight *= (1 - learnedDataForStages[i][j].get(0).portionDueToBlotBlockDiff);
							scoreWeightsData[i][j].primeLength *= (1 - learnedDataForStages[i][j].get(0).portionDueToPrimeLength);
							scoreWeightsData[i][j].checkDifferenceHomeBoards *= (1 - learnedDataForStages[i][j].get(0).portionDueToCheckDifference);
							scoreWeightsData[i][j].anchorLocation *= (1 - learnedDataForStages[i][j].get(0).portionDueToAnchorLocation);
						}
					}
					writer.write("" + scoreWeightsData[i][j].pipDifferenceWeight + "    \n");
					writer.write(feature2);
					writer.write("" + scoreWeightsData[i][j].blockBlotDifferenceWeight + "    \n");
					writer.write(feature3);
					writer.write("" + scoreWeightsData[i][j].numHomeBoardBlocksWeight + "    \n");
					writer.write(feature4);
					writer.write("" + scoreWeightsData[i][j].primeLength + "    \n");
					writer.write(feature5);
					writer.write("" + scoreWeightsData[i][j].checkDifferenceHomeBoards + "    \n");
					writer.write(feature6);
					writer.write("" + scoreWeightsData[i][j].anchorLocation + "    \n\n");
				}
			}
			writer.close();
	}
	
	private void compileLearnedData() {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 3; j++) {
				int size = learnedDataForStages[i][j].size();
				ScoreProfile avg = new ScoreProfile();
				if(size > 0) {
					for(ScoreProfile s: learnedDataForStages[i][j]){
						avg.portionDueToBlotBlockDiff += s.portionDueToBlotBlockDiff;
						avg.portionDueToNumOnHomeBoard += s.portionDueToNumOnHomeBoard;
						avg.portionDueToPipDifference += s.portionDueToPipDifference;
						avg.portionDueToPrimeLength += s.portionDueToPrimeLength;
						avg.portionDueToCheckDifference += s.portionDueToCheckDifference;
						avg.portionDueToAnchorLocation += s.portionDueToAnchorLocation;
					}
					avg.portionDueToBlotBlockDiff /= size;
					avg.portionDueToNumOnHomeBoard /= size;
					avg.portionDueToPipDifference /= size;
					avg.portionDueToPrimeLength /= size;
					avg.portionDueToCheckDifference /= size;
					avg.portionDueToAnchorLocation /= size;

					learnedDataForStages[i][j].clear();
					learnedDataForStages[i][j].add(avg);
				}
			}
		}
	}
	
	private void determineGameStageAndPositionType() {
		currentPositionType = EVEN_POSITION;
		
		if(numberOfTurns < 5)
			currentStage = EARLY_GAME;
		else if(numberOfTurns < 18)
			currentStage = MIDDLE_GAME;
		else
			currentStage = END_GAME;
		
		int lastCheckerPosition;
		for(lastCheckerPosition = 24; board.getNumCheckers(0, lastCheckerPosition) > 0; lastCheckerPosition--)
			;
		
		while(lastCheckerPosition > 1) {
			if(board.getNumCheckers(1, lastCheckerPosition) > 0)
				return;
			lastCheckerPosition--;
		}
		
		currentStage = RACE_GAME;
	}
	
	private void learn(List<ScoreProfile> scoreProportionProfiles, ScoreProfile bestPlayScoreProfile) {
		int n = scoreProportionProfiles.size();
		//System.out.println("size is" + n);
		ScoreProfile avgOfNonSelected = new ScoreProfile();
		
		for(ScoreProfile s : scoreProportionProfiles) {
			avgOfNonSelected.portionDueToBlotBlockDiff += s.portionDueToBlotBlockDiff;
			avgOfNonSelected.portionDueToNumOnHomeBoard += s.portionDueToNumOnHomeBoard;
			avgOfNonSelected.portionDueToPipDifference += s.portionDueToPipDifference;
			avgOfNonSelected.portionDueToPrimeLength += s.portionDueToPrimeLength;
			avgOfNonSelected.portionDueToCheckDifference += s.portionDueToCheckDifference;
			avgOfNonSelected.portionDueToAnchorLocation += s.portionDueToAnchorLocation;
		}
		
		avgOfNonSelected.portionDueToBlotBlockDiff /= n;
		avgOfNonSelected.portionDueToNumOnHomeBoard /= n;
		avgOfNonSelected.portionDueToPipDifference /= n;
		avgOfNonSelected.portionDueToPrimeLength /= n;
		avgOfNonSelected.portionDueToCheckDifference /= n;
		avgOfNonSelected.portionDueToAnchorLocation /= n;
		
		bestPlayScoreProfile.portionDueToBlotBlockDiff -= avgOfNonSelected.portionDueToBlotBlockDiff;
		bestPlayScoreProfile.portionDueToNumOnHomeBoard -= avgOfNonSelected.portionDueToNumOnHomeBoard;
		bestPlayScoreProfile.portionDueToPipDifference -= avgOfNonSelected.portionDueToPipDifference;
		bestPlayScoreProfile.portionDueToPrimeLength -= avgOfNonSelected.portionDueToPrimeLength;
		bestPlayScoreProfile.portionDueToCheckDifference -= avgOfNonSelected.portionDueToCheckDifference;
		bestPlayScoreProfile.portionDueToAnchorLocation -= avgOfNonSelected.portionDueToAnchorLocation;
		
		if(bestPlayScoreProfile.portionDueToBlotBlockDiff < -0.18 ||
				bestPlayScoreProfile.portionDueToNumOnHomeBoard < -0.18 ||
				bestPlayScoreProfile.portionDueToPipDifference < -0.18 ||
				bestPlayScoreProfile.portionDueToPrimeLength < -0.18 ||
				bestPlayScoreProfile.portionDueToCheckDifference < -0.18 ||
				bestPlayScoreProfile.portionDueToAnchorLocation < -0.18) {
			return;
		}
		else {
			learnedDataForStages[currentStage][currentPositionType].add(bestPlayScoreProfile);
		}
	}
	
	private class scoreFunctionWeightsData{
		scoreFunctionWeightsData(){
			pipDifferenceWeight = 0;
			blockBlotDifferenceWeight = 0;
			numHomeBoardBlocksWeight = 0;
			primeLength = 0;
			checkDifferenceHomeBoards = 0;
			anchorLocation = 0;
		}
		
		int pipDifferenceWeight;
		int blockBlotDifferenceWeight;
		int numHomeBoardBlocksWeight;
		int primeLength;
		int checkDifferenceHomeBoards;
		int anchorLocation;
	}
	
	private void loadScoreFunctionWeightsData() throws IOException{
		File file = new File("bots/Bot0_scoreFunctionValues.txt");
		BufferedReader BReader = new BufferedReader(new FileReader(file));
		
		scoreWeightsData = new scoreFunctionWeightsData[4][3];
		
		for(int i = 0; i < 4 ; i++) {
			String s = BReader.readLine();
			while(!s.contains("!*!")) {
				s = BReader.readLine();
				System.out.println(s);
			}
			
			for(int j = 0; j < 3; j++) {
				scoreWeightsData[i][j] = new scoreFunctionWeightsData();
				
				String es = BReader.readLine();
				while(!es.contains("!~!")) {
					es = BReader.readLine();
					System.out.println(es);
				}
		
				System.out.println("Past while1");
				
				scoreWeightsData[i][j].pipDifferenceWeight =
						Integer.parseInt(BReader.readLine().substring(16, 21).trim());
				scoreWeightsData[i][j].blockBlotDifferenceWeight =
						Integer.parseInt(BReader.readLine().substring(17, 22).trim());
				scoreWeightsData[i][j].numHomeBoardBlocksWeight =
						Integer.parseInt(BReader.readLine().substring(23, 28).trim());
				scoreWeightsData[i][j].primeLength =
						Integer.parseInt(BReader.readLine().substring(14, 19).trim());
				scoreWeightsData[i][j].checkDifferenceHomeBoards =
						Integer.parseInt(BReader.readLine().substring(23, 27).trim());
				scoreWeightsData[i][j].anchorLocation = 
						Integer.parseInt(BReader.readLine().substring(17, 21).trim());
			}
			System.out.println("here2");
		}
		
		System.out.println("here3");
		BReader.close();
	}
	
	
	private int scorePlay(Play play, ScoreProfile sProfile) {		
		int pipDiff = pipDifference() + pipDifferenceChangeDueToMove(play);
		int blotDiff = blotDifference() + blotBlockDifferenceChangeDueToMove(play);
		int numHomeBoard = numHomeboardBlocks() + numHomeBoardBlocksChangeDueToMove(play);
		int primeLength = lengthOfPrime() + primeLengthChangeDueToMove(play); 
		int checkDiffHomeBoards = diffOfCheckNumInOwnHomeBoardAndOpponents() + diffOfCheckNumChange(play);
		int anchorLocation = anchorLocation() + anchorLocationChangeDueToMove(play);
		
		pipDiff *= scoreWeightsData[currentStage][currentPositionType].pipDifferenceWeight;
		blotDiff *= scoreWeightsData[currentStage][currentPositionType].blockBlotDifferenceWeight;
		numHomeBoard *= scoreWeightsData[currentStage][currentPositionType].numHomeBoardBlocksWeight;
		primeLength *= scoreWeightsData[currentStage][currentPositionType].primeLength;
		checkDiffHomeBoards *= scoreWeightsData[currentStage][currentPositionType].checkDifferenceHomeBoards;
		anchorLocation *= scoreWeightsData[currentStage][currentPositionType].anchorLocation;
		
		int score = pipDiff + blotDiff + numHomeBoard + primeLength + checkDiffHomeBoards + anchorLocation;
		int absScore = Math.abs(pipDiff) + Math.abs(blotDiff) + Math.abs(numHomeBoard) + Math.abs(primeLength)
		+ Math.abs(checkDiffHomeBoards) + Math.abs(anchorLocation);
		
		sProfile.portionDueToPipDifference = Math.abs((double)pipDiff)/absScore;
		sProfile.portionDueToBlotBlockDiff = Math.abs((double)blotDiff)/absScore;
		sProfile.portionDueToNumOnHomeBoard = Math.abs((double)numHomeBoard)/absScore;
		sProfile.portionDueToPrimeLength = Math.abs((double)primeLength)/absScore;
		sProfile.portionDueToCheckDifference = Math.abs((double)checkDiffHomeBoards)/absScore;
		sProfile.portionDueToAnchorLocation = Math.abs((double)anchorLocation)/absScore;
		
		return score;
	}
	
	private class ScoreProfile{
		double portionDueToPipDifference;
		double portionDueToBlotBlockDiff;
		double portionDueToNumOnHomeBoard;
		double portionDueToPrimeLength;
		double portionDueToCheckDifference;
		double portionDueToAnchorLocation;
		
		void scoreProfile(){
			portionDueToPipDifference = 0;
			portionDueToBlotBlockDiff = 0;
			portionDueToNumOnHomeBoard = 0;
			portionDueToPrimeLength = 0;
			portionDueToCheckDifference = 0;
			portionDueToAnchorLocation = 0;
		}
	}

	private int pipDifference() {
    	Vector<Integer> C0 = new Vector<Integer>();
    	Vector<Integer> C1 = new Vector<Integer>();
    	int numCheckersOnPip;
    	int P0 = 0;
    	int P1 = 0;
    	int Pd;
    	
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
    	if(me.getColorName().equals("RED")) {
    		Pd = P0 - P1;
    	}
    	else {
    		Pd = P1 - P0;
    	}
    	
    		return Pd;
	}	
	
	private int blotDifference() {
    	int blockCount = 0;
    	int blotCount = 0;
    	int Sd;
    	
    	
    	if(me.getColorName().equals("RED")) {
    		// Compute Block-Blot difference
    		for (int i = 0; i < 25; i++) {
    			if (board.getNumCheckers(0, i) > 1)
    				blockCount++;
    		}

    		for (int i = 0; i < 25; i++) {
    			if (board.getNumCheckers(1, i) == 1)
    				blotCount++;
    		}
    	}
    	else {
    		// Compute Block-Blot difference
    		for (int i = 0; i < 25; i++) {
    			if (board.getNumCheckers(1, i) > 1)
    				blockCount++;
    		}

    		for (int i = 0; i < 25; i++) {
    			if (board.getNumCheckers(0, i) == 1)
    				blotCount++;
    		}
    	}
    	
    	Sd = blockCount - blotCount;
		
		return Sd;
	}
	
	private int numHomeboardBlocks() {
		int H0 = 0;
		
		// Compute Number of home board Blocks
		for (int i = 1; i < 7; i++){
			if (board.getNumCheckers(0, i) > 1)
				H0++;
		}
		
		return H0;
	}
	
	private int lengthOfPrime() {
		int primeLength = 0;
    	
		// Compute length of prime
		for (int i = 0; i < 25; i++) {
			if ((board.getNumCheckers(0, i) > 1) && (board.getNumCheckers(0, i + 1) > 1)) {
				int tempPrimeLength = 2;
				for (int j = i + 2; j < j + 4; j++) {
					if (board.getNumCheckers(0, j) > 1) {
						tempPrimeLength++;
					}
					else
						break;
    				
				}
				int primeBlot = 0;
				for (int k = i; k > k - 6; k--) {
					if (k == 0)
						break;
					if (board.getNumCheckers(1, k) > 0)
						primeBlot++;
				}
				if (	primeBlot > 1 && primeLength < tempPrimeLength) {
					primeLength = tempPrimeLength;
					
				}	
			}	
		}
    	
		return primeLength;
	}	
	
	//Changed so that this method returns number of checkers
	private int diffOfCheckNumInOwnHomeBoardAndOpponents() {
		int myPlayerId;
		
		if(me.getColorName().equals("RED")) {
			myPlayerId = 0;
		}
		else {
			myPlayerId = 1;
		}
		int checkersHomeBoard = 0;
		
		// Calculate checkers in Home board
		for (int i = 1; i < 7; i++){
			int temp = board.getNumCheckers(myPlayerId, i);
			checkersHomeBoard += temp;
		}
		
		for (int i = 19; i < 25; i++) {
			int temp = board.getNumCheckers(myPlayerId, i);
			checkersHomeBoard -= temp;
		}
		
		return checkersHomeBoard;
	}
	
	private int anchorLocation() {
		int myPlayerId;
		
		if(me.getColorName().equals("RED")) {
			myPlayerId = 0;
		}
		else {
			myPlayerId = 1;
		}
		
		int anchorLocation = 0;
		int numCheckersAnchor = 0;
		
		// Calculate best Anchor location
		for (int i = 19; i < 25; i++) {
			if (board.getNumCheckers(myPlayerId, i) > 1) {
				//if (numCheckersAnchor < board.getNumCheckers(myPlayerId, i)) {
				//	numCheckersAnchor = board.getNumCheckers(myPlayerId, i);
				anchorLocation = i;
				//}
			}
		}
		
		return (25 - anchorLocation);
	}
	
	private int escapedCheckers() {
    	int lastP1 = 0;
    	int numEscapedCheckers = 0;

		
		// Calculate no. of escaped checkers
    	int lastP1Index = 0;
    	for (int j = 24; j > 0; j--)
    		if (board.getNumCheckers(1, j) > 0){
    			lastP1 = j;
    			lastP1Index = 25 - j;
    			break;
    		}
    	
    	for (int i = lastP1Index; i > 0; i--){
    		if (board.getNumCheckers(0, i) > 0){
    			numEscapedCheckers += board.getNumCheckers(0, i);
    		}
    	}
			
    	return numEscapedCheckers;
	}
	
	private int pipDifferenceChangeDueToMove(Play play) {
		int myPlayerId;
		int myOpponentId;
		
		if(me.getColorName().equals("RED")) {
			myPlayerId = 0;
			myOpponentId = 1;
		}
		else {
			myPlayerId = 1;
			myOpponentId = 0;
		}
		
		int change = 0;
		if(play.getMove(myPlayerId).getFromPip() == 25)
			change += play.getMove(myPlayerId).getToPip();
		else 
			change += play.getMove(myPlayerId).getToPip() - play.getMove(myPlayerId).getFromPip();
		
		if(play.getMove(myOpponentId).getFromPip() == 25) 
			change += play.getMove(myOpponentId).getToPip();
		else 
			change += play.getMove(myOpponentId).getToPip() - play.getMove(myOpponentId).getFromPip();
		
		return change;
	}
	
	private int blotBlockDifferenceChangeDueToMove(Play play) {
		int myPlayerId, myOpponentId;
		
		if(me.getColorName().equals("RED")) {
			myPlayerId = 0;
			myOpponentId = 1;
		}
		else {
			myPlayerId = 1;
			myOpponentId = 0;
		}
		
		int blockChange = 0;
		if(board.getNumCheckers(myPlayerId, play.getMove(myPlayerId).getFromPip()) < 3)
			blockChange -= 1;
		if(board.getNumCheckers(myPlayerId, play.getMove(myPlayerId).getToPip()) == 1)
			blockChange += 1;
		if(board.getNumCheckers(myPlayerId, play.getMove(myOpponentId).getFromPip()) < 3)
			blockChange -= 1;
		if(board.getNumCheckers(myPlayerId, play.getMove(myOpponentId).getToPip()) == 1)
			blockChange += 1;
		
		int blotChange = 0;
		if(play.getMove(myPlayerId).isHit())
			blotChange -= 1;
		if(play.getMove(myOpponentId).isHit())
			blotChange -= 1;
			
		return blockChange - blotChange;
	}
	
	private int numHomeBoardBlocksChangeDueToMove(Play play) {
		int myPlayerId, myOpponentId;
		
		if(me.getColorName().equals("RED")) {
			myPlayerId = 0;
			myOpponentId = 1;
		}
		else {
			myPlayerId = 1;
			myOpponentId = 0;
		}
		
		
		int change = 0;
		if (play.getMove(myPlayerId).getFromPip() < 7 && play.getMove(myPlayerId).getFromPip() > 0)
			if(play.getMove(myPlayerId).getToPip() >= 7)
				change -= 1;
		
		if (play.getMove(myOpponentId).getFromPip() < 7 && play.getMove(myOpponentId).getFromPip() > 0)
			if(play.getMove(myOpponentId).getToPip() >= 7)
				change -= 1;
		
		return change;
	}
	
	//Note this returns the change in *any* consecutive series of blocks, not just the longest prime change
	private int primeLengthChangeDueToMove(Play play) {
		int myPlayerId;
		if(me.getColorName().equals("RED")) {
			myPlayerId = 0;
		}
		else {
			myPlayerId = 1;
		}
		
		int primeLengthChange = 0;
		
		for(int j = 0; j < 2; j++) {
			if(board.getNumCheckers(myPlayerId, play.getMove(j).getFromPip()) > 1) {
				int primeBackwards = 0;
				for(int i = play.getMove(j).getFromPip()-1; i > 0; i--) {
					if(board.getNumCheckers(myPlayerId, i) < 2)
						break;
					primeBackwards++;
				}
				int primeForwards = 0;
				for(int i = play.getMove(j).getFromPip()+1; i < 25; i++) {
					if(board.getNumCheckers(myPlayerId, i) < 2)
						break;
					primeForwards++;
				}
			
				if(board.getNumCheckers(myPlayerId, play.getMove(j).getFromPip()) == 2) {
					primeLengthChange -= primeForwards >= primeBackwards ? (primeBackwards) : (primeForwards);
				}
			
		}
		}
		
		if(board.getNumCheckers(myPlayerId, play.getMove(0).getToPip()) == 1
				|| (board.getNumCheckers(myPlayerId, play.getMove(0).getFromPip()) ==
				board.getNumCheckers(myPlayerId, play.getMove(0).getToPip()) 
				&& board.getNumCheckers(myPlayerId, play.getMove(0).getToPip()) == 0)) {
			
			int primeBackwards = 0;
			for(int i = play.getMove(0).getToPip()-1; i > 0; i--) {
				if(board.getNumCheckers(myPlayerId, i) < 2)
					break;
				primeBackwards++;
			}
			int primeForwards = 0;
			for(int i = play.getMove(0).getToPip()+1; i < 25; i++) {
				if(board.getNumCheckers(myPlayerId, i) < 2)
					break;
				if(i == play.getMove(0).getFromPip() && board.getNumCheckers(myPlayerId, i) < 3)
					break;
				primeForwards++;
			}
			
			if(primeBackwards > 0 && primeForwards > 0) {
				primeLengthChange += primeForwards >= primeBackwards ? (primeBackwards) : (primeForwards);
			}
			else if(primeBackwards > 0 || primeForwards > 0) {
				primeLengthChange += 1;
			}
		}
		
		if(board.getNumCheckers(myPlayerId, play.getMove(1).getToPip()) == 1) {
			int primeBackwards = 0;
			for(int i = play.getMove(1).getToPip()-1; i > 0; i--) {
				if(board.getNumCheckers(myPlayerId, i) < 2)
					break;
				primeBackwards++;
			}
			int primeForwards = 0;
			for(int i = play.getMove(1).getToPip()+1; i < 25; i++) {
				if(board.getNumCheckers(myPlayerId, i) < 2)
					break;
				if(i == play.getMove(1).getFromPip() && board.getNumCheckers(myPlayerId, i) < 3)
					break;
				primeForwards++;
			}
			
			if(primeBackwards > 0 && primeForwards > 0) {
				primeLengthChange += primeForwards >= primeBackwards ? (primeBackwards) : (primeForwards);
			}
			else if(primeBackwards > 0 || primeForwards > 0) {
				primeLengthChange += 1;
			}
		}
		
		return primeLengthChange;	
	}
	
	private int diffOfCheckNumChange(Play play) {
		int change = 0;
		
		if(play.getMove(0).getFromPip() > 6 && play.getMove(0).getToPip() < 7)
			change++;
		
		if(play.getMove(1).getFromPip() > 6 && play.getMove(1).getToPip() < 7)
			change++;
		
		if(play.getMove(0).getFromPip() > 19 && play.getMove(0).getToPip() <= 19)
			change++;
		
		if(play.getMove(1).getFromPip() > 19 && play.getMove(1).getToPip() <= 19)
			change++;
		
		return change;
	}
	
	private int anchorLocationChangeDueToMove(Play play) {
		int myPlayerId;
		if(me.getColorName().equals("RED")) {
			myPlayerId = 0;
		}
		else {
			myPlayerId = 1;
		}
		
		int newLocation = 0;
		int checkNum = 0;
		
		for(int i = 19; i < 25; i++) {
			checkNum = board.getNumCheckers(myPlayerId, i);
			if(play.getMove(0).getToPip() == i)
				checkNum++;
			if(play.getMove(1).getToPip() == i)
				checkNum++;
			if(checkNum >= 2) {
				newLocation = i;
				break;
			}
		}
		
		newLocation = 25 - newLocation;
		return newLocation-anchorLocation();
		
	}
}				