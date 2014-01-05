package com.zengate.minesweeperbattle.UIButtons;

import com.badlogic.gdx.math.Vector2;
import com.zengate.minesweeperbattle.Engine.ContentManager;
import com.zengate.minesweeperbattle.Engine.Renderer;
import com.zengate.minesweeperbattle.Engine.SceneManager;
import com.zengate.minesweeperbattle.Engine.UI.Button;

public class ButtonLogin extends Button{

	public ButtonLogin() {
		super("sprButton","data/pack0/pack0.pack");
	}
	
	@Override
	public void Update(float dt){
		super.Update(dt);
		
		Renderer.drawText("testFont", "Login", new Vector2(
				position.x + size.x/2 - ContentManager.getFont("testFont").getBounds("Login").width/2 ,
				position.y + size.y/2 - ContentManager.getFont("testFont").getBounds("Login").height/2),
				0, 0, 
				0, 1);
	}
	
	@Override
	public void TouchedReleased(){
		super.TouchedReleased();
		
		SceneManager.switchScene("LoginScene");
	}

}
