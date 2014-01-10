package com.zengate.minesweeperbattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class LocalValues {
	private static Preferences thePreferences;
	
	public static void Init(){
		thePreferences = Gdx.app.getPreferences("GameData");
		//clearLocalMatches();
	}
	
	public static String getUsername(){
		return thePreferences.getString("username","");
	}
	
	public static void setUsername(String _username){
		thePreferences.putString("username", _username);
		thePreferences.flush();
	}
	
	public static void addMovesToMatch(int matchID, String _events){
		
		if (!thePreferences.contains(""+matchID)){
			thePreferences.putString(""+matchID, _events);
		}else{
			String oldMoves = thePreferences.getString(""+matchID);
			thePreferences.remove(""+matchID);
			String allMoves = oldMoves + _events;
			thePreferences.putString(""+matchID, allMoves);
		}
		
		thePreferences.flush();
	}
	
	public static String getPastMoves(int matchID){
		if (thePreferences.contains(""+matchID)){
			return thePreferences.getString(""+matchID);
		}
		
		return "";
	}
	
	public static String getPassword(){
		return thePreferences.getString("password", "");
	}
	
	public static void setPassword(String _password){
		thePreferences.putString("password", _password);
		thePreferences.flush();
	}
	
	private static void clearLocalMatches(){
		for (int i = 0; i < 200; i++){
			if (thePreferences.contains(""+i)){
				thePreferences.remove(""+i);
			}
		}
		
		thePreferences.flush();
	}
}
