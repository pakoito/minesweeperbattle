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
	public void SceneEnd(){
		super.SceneEnd();
		this.reset();
	}
	
	@Override
	public void Update(float delta){
		theGame.Update(delta);
		super.Update(delta);
	}

}
