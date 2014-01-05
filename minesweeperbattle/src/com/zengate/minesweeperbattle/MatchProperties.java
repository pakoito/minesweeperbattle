package com.zengate.minesweeperbattle;

public class MatchProperties {

	private static long seed;
	private static int matchID;
	private static boolean newMatch = false;
	private static int roundNumber;
	
	public static void setData(long _seed, int _matchId, int _roundNumber, boolean _isNewMatch){
		seed = _seed;
		matchID = _matchId;
		newMatch = _isNewMatch;
		roundNumber = _roundNumber;
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
	
}
