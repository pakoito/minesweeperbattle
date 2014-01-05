package com.zengate.minesweeperbattle;

import com.zengate.minesweeperbattle.Engine.ContentManager;
import com.zengate.minesweeperbattle.Engine.Scene;
import com.zengate.minesweeperbattle.Engine.SceneManager;
import com.zengate.minesweeperbattle.Scenes.CreateAccountScene;
import com.zengate.minesweeperbattle.Scenes.LobbyScene;
import com.zengate.minesweeperbattle.Scenes.LoginScene;
import com.zengate.minesweeperbattle.Scenes.MenuScene;
import com.zengate.minesweeperbattle.Scenes.TestScene;

public class LoadContent {

	public LoadContent(){
		ContentManager.addFont("testFont", "data/fonts/AldoTheApache_20.fnt", "data/fonts/AldoTheApache_20.png", true);
		ContentManager.loadAtlas("data/pack0/pack0.pack");
		
		SceneManager.addScene(new TestScene());
		SceneManager.addScene(new MenuScene());
		SceneManager.addScene(new CreateAccountScene());
		SceneManager.addScene(new LobbyScene());
		SceneManager.addScene(new LoginScene());
		SceneManager.switchScene("MenuScene");
	}
	
}
