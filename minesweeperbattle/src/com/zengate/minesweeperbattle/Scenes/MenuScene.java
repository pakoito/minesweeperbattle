package com.zengate.minesweeperbattle.Scenes;

import com.zengate.minesweeperbattle.Engine.Renderer;
import com.zengate.minesweeperbattle.Engine.Scene;
import com.zengate.minesweeperbattle.Engine.SceneManager;
import com.zengate.minesweeperbattle.UIButtons.ButtonCreateAccount;

public class MenuScene extends Scene {

	private ButtonCreateAccount buttonCreateAccount;
	
	public MenuScene() {
		super("MenuScene");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void SceneBegin(){
		buttonCreateAccount = new ButtonCreateAccount();
		buttonCreateAccount.setPosition(Renderer.getCameraSize().x/2 - buttonCreateAccount.getSize().x/2, 10);
		SceneManager.Scene().addEntity(buttonCreateAccount);
	}

}
