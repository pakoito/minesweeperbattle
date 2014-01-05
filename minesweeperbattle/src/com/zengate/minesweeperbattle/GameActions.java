package com.zengate.minesweeperbattle;

import com.badlogic.gdx.math.Vector2;
import com.zengate.minesweeperbattle.EventSystem.CellClickedEvent;
import com.zengate.minesweeperbattle.EventSystem.EventController;

public class GameActions {

	private Cell[][] gameGrid;
	private Vector2 boardUnitSize;
	private EventController theEventController;
	private GameController theGameController;
	
	public GameActions(EventController _aEventController, GameController _theGameController){
		theEventController = _aEventController;
		theGameController = _theGameController;
	}
	
	public void setGameGrid(Cell[][] _aGameGrid, Vector2 _aBoardUnitSize){
		gameGrid = _aGameGrid;
		boardUnitSize = _aBoardUnitSize;
	}
	
	public void floodFillReveal(int _x, int _y){
		if (_x > 0){floodRoutine(_x -1, _y);}
		if (_x < boardUnitSize.x -1 ){floodRoutine(_x+1,_y);}
		if (_y > 0){floodRoutine(_x , _y-1);}
		if (_y < boardUnitSize.y -1 ){floodRoutine(_x,_y+1);}
		if (_x >0 && _y > 0){floodRoutine(_x -1, _y-1);}
		if (_x <boardUnitSize.x-1 && _y > 0){floodRoutine(_x +1, _y-1);}
		if (_x >0 && _y < boardUnitSize.y-1){floodRoutine(_x -1, _y+1);}
		if (_x < boardUnitSize.x-1 && _y < boardUnitSize.y-1){floodRoutine(_x +1, _y+1);}
	}
	
	public void floodRoutine(int _x, int _y){
		if (!gameGrid[_x][_y].getIsMine()){
			if (!gameGrid[_x][_y].getClicked()){
				gameGrid[_x][_y].clicked();
				
				if (gameGrid[_x][_y].getMineCount() == 0){
					floodFillReveal(_x,_y);
				}
			}
		}
	}
	
	public void revealAllCells() {
		for (int iX = 0; iX < boardUnitSize.x; iX++){
			for (int iY = 0; iY < boardUnitSize.y; iY++){
				gameGrid[iX][iY].reveal();
			}
		}
	}
	
	public void calculateMineCounts(){
		for (int iX = 0; iX < boardUnitSize.x; iX++){
			for (int iY = 0; iY < boardUnitSize.y; iY++){
				
				int count = 0;
				
				//There must be a better way to do this
				if (iX > 0){
					if (gameGrid[iX-1][iY].getIsMine()){count ++;}
				}
				
				if (iX < boardUnitSize.x-1){
					if (gameGrid[iX+1][iY].getIsMine()){count ++;}
				}
				
				if (iY > 0){
					if (gameGrid[iX][iY-1].getIsMine()){count ++;}
				}
				
				if (iY < boardUnitSize.y-1){
					if (gameGrid[iX][iY+1].getIsMine()){count ++;}
				}
				
				if (iX > 0 && iY > 0){
					if (gameGrid[iX-1][iY-1].getIsMine()){count ++;}
				}
				
				if (iX < boardUnitSize.x-1 && iY > 0){
					if (gameGrid[iX+1][iY-1].getIsMine()){count ++;}
				}
				
				if (iX > 0 && iY <boardUnitSize.y-1){
					if (gameGrid[iX-1][iY+1].getIsMine()){count ++;}
				}
				
				if (iX < boardUnitSize.x-1 && iY <boardUnitSize.y-1){
					if (gameGrid[iX+1][iY+1].getIsMine()){count ++;}
				}
				
				gameGrid[iX][iY].setMineCount(count);
			}
		}
	}
	
	public void cellClicked(int _x, int _y, boolean _track){
		if (_x < boardUnitSize.x && _y < boardUnitSize.y && _x >= 0 && _y >=0 ){
			if (!gameGrid[_x][_y].getClicked()){
				gameGrid[_x][_y].clicked();
				
				if (_track){
					theEventController.addOutGoingEvent(new CellClickedEvent(_x, _y));
				}
				
				if (!gameGrid[_x][_y].getIsMine()){
					if (gameGrid[_x][_y].getMineCount() == 0){
						floodFillReveal(_x, _y);
					}
				}else{
					//revealAllCells();
					//theGameController.reset();
				}
			}
		}
	}
	
}
