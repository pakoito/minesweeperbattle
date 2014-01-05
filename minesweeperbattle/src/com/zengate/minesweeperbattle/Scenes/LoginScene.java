package com.zengate.minesweeperbattle.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.zengate.minesweeperbattle.LocalValues;
import com.zengate.minesweeperbattle.DataSending.DataSender;
import com.zengate.minesweeperbattle.DataSending.WebCallback;
import com.zengate.minesweeperbattle.Engine.Scene;
import com.zengate.minesweeperbattle.Engine.SceneManager;

public class LoginScene extends Scene implements TextInputListener {

	private boolean state = false;
	
	private String username;
	private String password;
	
	WebCallback accountCreationCB;
	
	private boolean waitingForResponse = false;
	
	public LoginScene() {
		super("LoginScene");
	}

	@Override
	public void SceneBegin(){
		super.SceneBegin();
		
		state = false;
		waitingForResponse = false;
		username = "";
		password = "";
		accountCreationCB = new WebCallback();
		
		Gdx.input.getTextInput(this, "Enter Username", "");
	}
	
	@Override
	public void SceneEnd(){
		super.SceneEnd();
		this.reset();
	}
	
	@Override
	public void input(String text) {
		if (!state){
			username = text;
			Gdx.input.getTextInput(this, "Enter Password", "");
			state = !state;
		}else{
			password = text;
			
			DataSender aSender = new DataSender();
			aSender.login(username, password, accountCreationCB);
			waitingForResponse = true;
		}
		
	}

	@Override
	public void canceled() {
		SceneManager.switchScene("MenuScene");
		
	}
	
	@Override
	public void Update(float delta){
		super.Update(delta);
		
		if (waitingForResponse){
			if (accountCreationCB.getRecieved()){
				if(accountCreationCB.getResult()){
					LocalValues.setUsername(username);
					LocalValues.setPassword(password);
					SceneManager.switchScene("LobbyScene");
					waitingForResponse = false;
				}else{
					SceneManager.switchScene("MenuScene");
					waitingForResponse = false;
				}
			}
		}
	}
}
