package com.zengate.minesweeperbattle.EventSystem;

import com.badlogic.gdx.utils.Array;
import com.zengate.minesweeperbattle.GameActions;

public class EventHandler {
	private EventController theEventController;
	private Array<Event> replayQue;
	private GameActions theGameActions;
	
	private float eventDelay = 1.5f;
	private float eventTime = 0;
	
	private boolean start = false;
	
	public EventHandler(EventController _theEventController, GameActions _theGameActions){
		theEventController = _theEventController;
		theGameActions = _theGameActions;
	}
	
	public void recreateEventQue(Array<Event> _theQue){
		replayQue = _theQue;
		start = true;
	}
	
	private void handleEvent(Event _theEvent){
		switch(_theEvent.getEventType()){
			case CellClicked:
				CellClickedEvent aEvent = (CellClickedEvent) _theEvent;
				theGameActions.cellClicked(aEvent.getXIndex(), aEvent.getYIndex(), false);
				break;
		}
	}
	
	public void update(float delta){
		if (start){
			if (replayQue.size > 0){
				if (eventTime < eventDelay){
					eventTime += 1 * delta;
				}else{
					eventTime = 0;
					
					Event nextEvent = replayQue.get(0);
					replayQue.removeIndex(0);
					handleEvent(nextEvent);
				}
			}
		}
	}

}