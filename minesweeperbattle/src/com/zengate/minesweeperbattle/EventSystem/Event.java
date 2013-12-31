package com.zengate.minesweeperbattle.EventSystem;

public class Event {
	public enum EventType{CellClicked , BombUsed} 
	
	private EventType eventType;
	
	public Event(EventType _eventType){
		eventType = _eventType;
	}
	
	public EventType getEventType(){
		return eventType;
	}
	
}
