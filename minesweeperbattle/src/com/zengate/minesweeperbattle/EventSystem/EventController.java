package com.zengate.minesweeperbattle.EventSystem;

import com.badlogic.gdx.utils.Array;
import com.zengate.minesweeperbattle.GameActions;
import com.zengate.minesweeperbattle.LocalValues;
import com.zengate.minesweeperbattle.MatchProperties;
import com.zengate.minesweeperbattle.DataSending.DataSender;
import com.zengate.minesweeperbattle.DataSending.WebCallback;
import com.zengate.minesweeperbattle.Engine.SceneManager;
import com.zengate.minesweeperbattle.Notifications.Notification;
import com.zengate.minesweeperbattle.Notifications.NotificationManager;

public class EventController {
	
	private Array<Event> outgoingEvents = new Array<Event>();
	
	private EventHandler theEventHandler;
	
	private DataSender theSender;
	
	private boolean isReplaying = true;
	
	private boolean waitingForData = false;
	
	private WebCallback playerMoveCB = new WebCallback();
	private WebCallback playerMoveSentCB = new WebCallback();
	private boolean waitingForSentData = false;
	
	public EventController(){
		theSender = new DataSender();
		
		if (!MatchProperties.getNewMatch()){
			if (MatchProperties.getPlayerNum() == 2){
				theSender.getPlayerMove(MatchProperties.getMatchID(),MatchProperties.getOpponent(),
						MatchProperties.getRoundNumber(),playerMoveCB);
			}else{
				theSender.getPlayerMove(MatchProperties.getMatchID(),MatchProperties.getOpponent(),
						MatchProperties.getRoundNumber()-1,playerMoveCB);
			}
		}else{
			isReplaying = false;
		}
		waitingForData = true;
	}
	
	public void setup(GameActions _theGameActions){
		theEventHandler = new EventHandler(_theGameActions);
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
		theSender.sendPlayerMove(eventQueToString(outgoingEvents),LocalValues.getUsername(),MatchProperties.getRoundNumber(),
				MatchProperties.getMatchID(),MatchProperties.getOpponent(),playerMoveSentCB);
		waitingForSentData = true;
		LocalValues.addMovesToMatch(MatchProperties.getMatchID(), eventQueToString(outgoingEvents));
	}
	
	public void update(float delta){
		theEventHandler.update(delta);
		
		if (waitingForData){
			if (playerMoveCB.getRecieved()){
				LocalValues.addMovesToMatch(MatchProperties.getMatchID(), playerMoveCB.getMessage());
				theEventHandler.recreateEventQue(stringToEventQue(playerMoveCB.getMessage()));
				isReplaying = true;
				waitingForData = false;
			}
		}else{
			if (isReplaying){
				if (!theEventHandler.getIsReplaying()){
					isReplaying = false;
				}
			}
		}
		
		if (waitingForSentData){
			if (playerMoveSentCB.getRecieved()){
				if (playerMoveSentCB.getResult()){
					waitingForSentData = false;
					SceneManager.switchScene("LobbyScene");
				}else{
					NotificationManager.addNotification(new Notification("Failed to send move to server"));
				}
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
					
					tempEvent += aEvent.getEventType().toString()+"-";
					tempEvent += theEvent.getXIndex()+"-";
					tempEvent += theEvent.getYIndex()+"-";
					tempEvent += theEvent.getPlayerNum();
					
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
		
			String[] eventData = events[i].split("-");
			
			if (eventData[0].equals("CellClicked")){
				CellClickedEvent newEvent = new CellClickedEvent(Integer.parseInt(eventData[1]),
												Integer.parseInt(eventData[2]),
												Integer.parseInt(eventData[3]));
				theEventQue.add(newEvent);
			}
		}
		
		
		return theEventQue;
	}
	
	public void reCreateInstant(String _eventQue){
		theEventHandler.reCreateInstant(stringToEventQue(_eventQue));
	}

}
