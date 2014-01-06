package com.zengate.minesweeperbattle;

public class MatchProperties {

	private static long seed;
	private static int matchID;
	private static boolean newMatch = false;
	private static int roundNumber;
	private static int playerNum;
	private static String opponent;
	
	public static void setData(long _seed, int _matchId, int _roundNumber, boolean _isNewMatch, int _playerNum,
			String _opponent){
		seed = _seed;
		matchID = _matchId;
		newMatch = _isNewMatch;
		roundNumber = _roundNumber;
		playerNum = _playerNum;
		opponent = _opponent;
	}
	
	public static long getSeed(){
		return seed;
	}
	
	public static int getMatchID(){
		return matchID;
	}
	
	public static boolean getNewMatch(){
		return newMatch;
	}
	
	public static int getRoundNumber(){
		return roundNumber;
	}
	
	public static int getPlayerNum(){
		return playerNum;
	}
	
	public static String getOpponent(){
		return opponent;
	}
	
}
