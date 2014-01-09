package com.zengate.minesweeperbattle.Notifications;

import com.badlogic.gdx.math.Vector2;
import com.zengate.minesweeperbattle.Engine.ContentManager;
import com.zengate.minesweeperbattle.Engine.Renderer;

public class Notification {
	
	private String notificationText;
	
	private boolean doEnd = false;
	private boolean isDone = false;
	private float alpha = 1;
	public Notification(String _text){
		notificationText = _text;
		alpha = 1;
		doEnd = false;
	}
	
	public void displayNotification(float dt){
		
		if (doEnd){
			if (alpha - (1 * dt) >= 0){
				alpha -= 1 * dt;
			}else{
				alpha = 0;
				isDone = true;
			}
		}
		
		Renderer.drawText("testFont", notificationText, new Vector2(
				Renderer.getCameraSize().x/2 - ContentManager.getFont("testFont").getBounds(notificationText).width/2 ,
				Renderer.getCameraSize().y -30 - ContentManager.getFont("testFont").getBounds(notificationText).height/2),
				1, 0, 
				0, alpha);
	}
	
	public void fadeOut(){
		doEnd = true;
	}
	
	public boolean isDone(){
		return isDone;
	}

}
