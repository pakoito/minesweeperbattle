package com.zengate.minesweeperbattle.UIButtons;

import com.badlogic.gdx.math.Vector2;
import com.zengate.minesweeperbattle.LocalValues;
import com.zengate.minesweeperbattle.Engine.ContentManager;
import com.zengate.minesweeperbattle.Engine.Renderer;
import com.zengate.minesweeperbattle.Engine.SceneManager;
import com.zengate.minesweeperbattle.Engine.UI.Button;

public class ButtonLogout extends Button {

	public ButtonLogout() {
		super("sprButton","data/pack0/pack0.pack");
	}
	
	@Override
	public void Update(float dt){
		super.Update(dt);
		
		Renderer.drawText("testFont", "Logout", new Vector2(
				position.x + size.x/2 - ContentManager.getFont("testFont").getBounds("Logout").width/2 ,
				position.y + size.y/2 - ContentManager.getFont("testFont").getBounds("Logout").height/2),
				0, 0, 
				0, 1);
	}
	
	@Override
	public void TouchedReleased(){
		super.TouchedReleased();
		
		LocalValues.setUsername("");
		LocalValues.setPassword("");
		SceneManager.switchScene("MenuScene");
	}

}
