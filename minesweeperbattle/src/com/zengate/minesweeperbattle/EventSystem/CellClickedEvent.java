package com.zengate.minesweeperbattle.EventSystem;

public class CellClickedEvent extends Event {

	private int xIndex;
	private int yIndex;
	
	public CellClickedEvent(int _xIndex, int _yIndex){
		super(EventType.CellClicked);
		xIndex = _xIndex;
		yIndex = _yIndex;
	}
	
	public int getXIndex(){
		return xIndex;
	}
	
	public int getYIndex(){
		return yIndex;
	}
}
