package com.zengate.minesweeperbattle.Engine.UI;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zengate.minesweeperbattle.Engine.CollisionHandler;
import com.zengate.minesweeperbattle.Engine.Input;

public class Button extends BaseUI {
	
	private boolean isClicked = false;
	protected float radius;
	
	private boolean useCircleCollisions = false;
	
	private Array<ButtonListener> listButtonListeners = new Array<ButtonListener>();
	
	private boolean canClick;
	
	public Button(String _imageName, String _atlasName){
		super("Button",_imageName, _atlasName);
		canClick = true;
	}
	
	//setting circleCollisions to false might cause a bug? use other constructor for non circle buttons
	public Button(String _imageName, String _atlasName, boolean _circleCollisions){
		super("Button",_imageName, _atlasName);
		useCircleCollisions = _circleCollisions;
		radius = size.x/2;
		canClick = false;
	}
	
	@Override
	public void Update(float dt){
		super.Update(dt);
		
		if (canClick){
			if (checkTouchInside()){
				if (!isClicked){
					isClicked = true;
					TouchedEnter();	
				}
			}
			
			if (isClicked){
				TouchedHeld();
			}
			
			if (!Input.getTouched()){
				if (isClicked){
					isClicked = false;
					TouchedReleased();
				}
			}
			
			if (isClicked){
				if (!checkTouchInside()){
					if (Input.getTouched()){
						TouchedLeave();
					}
					
					isClicked = false;
				}
			}
			
			radius *= scale.x;
		}else{
			if (!Input.getTouched()){
				canClick = true;
			}
		}
	}
	
	/**
	 * Called when button is being held and the touch slides out the buttons region
	 */
	public void TouchedLeave() {
		ButtonEvent theEvent = new ButtonEvent(this);
		
		for (ButtonListener aListener : listButtonListeners){
			aListener.onTouchedLeave(theEvent);
		}
	}

	/**
	 * called when touch is inside button region and touching screen
	 */
	public void TouchedHeld(){
	
	}
	
	/**
	 * called when the touch is inside region then stop touching screen
	 */
	public void TouchedReleased(){
		ButtonEvent theEvent = new ButtonEvent(this);
		
		for (ButtonListener aListener : listButtonListeners){
			aListener.onTouchedReleased(theEvent);
		}
	}
	
	/**
	 * called when the touch first enters the region while pressed
	 */
	public void TouchedEnter(){

	}
	
	public void addEventListener(ButtonListener _aListener){
		listButtonListeners.add(_aListener);
	}
	
	private boolean checkTouchInside(){
		if (Input.getTouched()){
			if (!useCircleCollisions){
				if (CollisionHandler.checkPointInRectangle(boundingBox, Input.getTouchedPosition())){
					return true;
				}
			}else{
				if (position.dst(Input.getTouchedPosition()) < radius ){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void Draw(){
		super.Draw();
		//Renderer.drawRectangle(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
	}
}
