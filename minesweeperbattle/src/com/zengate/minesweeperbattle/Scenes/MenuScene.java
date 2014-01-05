package com.zengate.minesweeperbattle.Scenes;

import com.zengate.minesweeperbattle.LocalValues;
import com.zengate.minesweeperbattle.DataSending.DataSender;
import com.zengate.minesweeperbattle.DataSending.WebCallback;
import com.zengate.minesweeperbattle.Engine.Renderer;
import com.zengate.minesweeperbattle.Engine.Scene;
import com.zengate.minesweeperbattle.Engine.SceneManager;
import com.zengate.minesweeperbattle.UIButtons.ButtonCreateAccount;
import com.zengate.minesweeperbattle.UIButtons.ButtonLogin;

public class MenuScene extends Scene {

	private ButtonCreateAccount buttonCreateAccount;
	private ButtonLogin buttonLogin;
	
	private WebCallback autoLoginCallBack;
	private boolean isAutoLogging = false;
	
	public MenuScene() {
		super("MenuScene");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void SceneBegin(){
		//LocalValues.setUsername("");
		//LocalValues.setPassword("");
		if (!LocalValues.getUsername().equals("")){
			autoLoginCallBack = new WebCallback();
			DataSender aSender = new DataSender();
			aSender.login(LocalValues.getUsername(), LocalValues.getPassword(), autoLoginCallBack);
			isAutoLogging = true;
		}else{
			setupScene();
		}
	}
	
	@Override
	public void SceneEnd(){
		super.SceneEnd();
		this.reset();
	}
	
	@Override
	public void Update(float delta){
		super.Update(delta);
		
		if (isAutoLogging){
			if (autoLoginCallBack.getRecieved()){
				if (autoLoginCallBack.getResult()){
					SceneManager.switchScene("LobbyScene");
				}else{
					setupScene();
				}
				isAutoLogging = false;
			}
		}
	}
	
	private void setupScene(){
		buttonLogin = new ButtonLogin();
		buttonLogin.setPosition(Renderer.getCameraSize().x/2 - buttonLogin.getSize().x/2, 10);
		SceneManager.Scene().addEntity(buttonLogin);
		
		buttonCreateAccount = new ButtonCreateAccount();
		buttonCreateAccount.setPosition(Renderer.getCameraSize().x/2 - buttonCreateAccount.getSize().x/2, 80);
		SceneManager.Scene().addEntity(buttonCreateAccount);
	}

}
