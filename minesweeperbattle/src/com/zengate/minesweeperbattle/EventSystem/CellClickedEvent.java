package com.zengate.minesweeperbattle.EventSystem;

public class CellClickedEvent extends Event {

	private int xIndex;
	private int yIndex;
	private int playerNum;
	
	public CellClickedEvent(int _xIndex, int _yIndex, int _playerNum){
		super(EventType.CellClicked);
		xIndex = _xIndex;
		yIndex = _yIndex;
		playerNum = _playerNum;
	}
	
	public int getXIndex(){
		return xIndex;
	}
	
	public int getYIndex(){
		return yIndex;
	}
	
	public int getPlayerNum(){
		return playerNum;
	}
}
