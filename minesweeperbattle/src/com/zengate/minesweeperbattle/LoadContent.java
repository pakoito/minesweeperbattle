package com.zengate.minesweeperbattle;

import com.zengate.minesweeperbattle.Engine.ContentManager;
import com.zengate.minesweeperbattle.Engine.Scene;
import com.zengate.minesweeperbattle.Engine.SceneManager;

public class LoadContent {

	public LoadContent(){
		ContentManager.addFont("testFont", "data/fonts/AldoTheApache_20.fnt", "data/fonts/AldoTheApache_20.png", true);
		ContentManager.loadAtlas("data/pack0/pack0.pack");
		
		SceneManager.addScene(new Scene("TestScene"));
		
		SceneManager.switchScene("TestScene");
		
		PostTest aTest = new PostTest();
	}
	
}
