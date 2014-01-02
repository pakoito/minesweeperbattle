package com.zengate.minesweeperbattle.EventSystem;

import com.badlogic.gdx.utils.Array;
import com.zengate.minesweeperbattle.DataSender;
import com.zengate.minesweeperbattle.GameActions;

public class EventController {
	
	private Array<Event> outgoingEvents = new Array<Event>();
	
	private EventHandler theEventHandler;
	
	private DataSender theSender;
	
	private boolean isReplaying = false;
	
	private boolean waitingForData = false;
	
	public EventController(){
		theSender = new DataSender();
		theSender.getPlayerMove();
		waitingForData = true;
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
	
	public void sendPlayerMove(){
		theSender.sendPlayerMove(eventQueToString(outgoingEvents));
	}
	
	public void update(float delta){
		theEventHandler.update(delta);
		
		if (waitingForData){
			if (theSender.getHasData()){
				theEventHandler.recreateEventQue(stringToEventQue(theSender.getFetchedData()));
				waitingForData = false;
			}
		}
	}
	
	private String eventQueToString(Array<Event> _eventQue){
		String stringQue= "";
		
		for(Event aEvent : _eventQue){
			
			switch(aEvent.getEventType()){
			case CellClicked:
					CellClickedEvent theEvent = (CellClickedEvent) aEvent;
					String tempEvent = "|";
					
					tempEvent += aEvent.getEventType().toString()+",";
					tempEvent += theEvent.getXIndex()+",";
					tempEvent += theEvent.getYIndex();
					
					stringQue+=tempEvent;
				break;
			}
		}
		return stringQue;
	}
	
	private Array<Event> stringToEventQue(String _eventQue){
		Array<Event> theEventQue = new Array<Event>();
		
		String[] events = _eventQue.split("\\|");
		
		for (int i = 0; i < events.length; i++){
			String[] eventData = events[i].split(",");
			
			if (eventData[0].equals("CellClicked")){
				CellClickedEvent newEvent = new CellClickedEvent(Integer.parseInt(eventData[1]),Integer.parseInt(eventData[2]));
				theEventQue.add(newEvent);
			}
		}
		
		
		return theEventQue;
	}

}
