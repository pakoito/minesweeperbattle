package com.zengate.minesweeperbattle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class LocalValues {
	private static Preferences thePreferences;
	
	public static void Init(){
		thePreferences = Gdx.app.getPreferences("GameData");
	}
	
	public static String getUsername(){
		return thePreferences.getString("username","");
	}
	
	public static void setUsername(String _username){
		thePreferences.putString("username", _username);
		thePreferences.flush();
	}
	
	public static String getPassword(){
		return thePreferences.getString("password", "");
	}
	
	public static void setPassword(String _password){
		thePreferences.putString("password", _password);
		thePreferences.flush();
	}
}
