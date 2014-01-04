package com.zengate.minesweeperbattle.Scenes;

import com.zengate.minesweeperbattle.GameController;
import com.zengate.minesweeperbattle.Engine.Scene;

public class TestScene extends Scene {

	private GameController theGame; 
	
	public TestScene() {
		super("TestScene");

	}
	
	@Override
	public void SceneBegin(){
		theGame = new GameController();
	}
	
	@Override
	public void Update(float delta){
		super.Update(delta);
		theGame.Update(delta);
	}

}
