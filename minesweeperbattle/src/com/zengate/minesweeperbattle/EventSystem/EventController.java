package com.zengate.minesweeperbattle.EventSystem;

import com.badlogic.gdx.utils.Array;
import com.zengate.minesweeperbattle.GameActions;

public class EventController {
	
	private Array<Event> outgoingEvents = new Array<Event>();
	
	private EventHandler theEventHandler;
	
	private boolean isReplaying = false;
	
	public EventController(){

	}
	
	public void setup(GameActions _theGameActions){
		theEventHandler = new EventHandler(this, _theGameActions);
	}
	
	public void addOutGoingEvent(Event _aEvent){
		outgoingEvents.add(_aEvent);
	}
	
	
	public void replay(){
		isReplaying = true;
		theEventHandler.recreateEventQue(outgoingEvents);
	}
	
	public boolean getIsReplaying(){
		return isReplaying;
	}
	
	public void update(float delta){
		theEventHandler.update(delta);
	}

}
