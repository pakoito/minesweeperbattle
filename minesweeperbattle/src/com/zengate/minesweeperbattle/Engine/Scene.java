package com.zengate.minesweeperbattle.Engine;

import com.badlogic.gdx.math.Vector2;

public class Scene extends EntityManager {

	private String sceneName;
	
	private boolean pause;
	
	public Scene(String _sceneName){
		super();
		sceneName = _sceneName;
		pause = false;
	}
	
	public void SceneBegin(){
		Renderer.setCameraPos(new Vector2(0,0));
	}
	
	public void SceneEnd(){
		
	}

	public String getSceneName(){
		return sceneName;
	}
	
	@Override
	public void Update(float dt){
		if (pause){
			dt = 0;
		}
			
		super.Update(dt);
	}
	
	public void setScenePause(boolean _aBool){
		pause = _aBool;
	}
	
		
		
}
