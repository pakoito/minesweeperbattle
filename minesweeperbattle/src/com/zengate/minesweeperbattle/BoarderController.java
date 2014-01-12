package com.zengate.minesweeperbattle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class BoarderController {
	
	private Vector2 boardUnitSize;
	private Vector2 cellSize;
	
	private Array<Boarder> topRow = new Array<Boarder>();
	private Array<Boarder> bottomRow = new Array<Boarder>();
	private Array<Boarder> leftRow = new Array<Boarder>();
	private Array<Boarder> rightRow = new Array<Boarder>();
	
	private Boarder topLeft;
	private Boarder topRight;
	private Boarder bottomLeft;
	private Boarder bottomRight;
	
	public BoarderController (Vector2 _boardSize, Vector2 _cellSize){
		boardUnitSize = _boardSize;
		cellSize = _cellSize;
		
		createBoarder();
	}
	
	private void createBoarder(){
		
		//top boarder
		for (int i = 0; i < boardUnitSize.x; i++){
			Boarder aBoarder;
			if (i < 1){
				aBoarder = new Boarder(0);
				aBoarder.setPosition(cellSize.x * i, -cellSize.y);
			}else if ( i < boardUnitSize.x-1){
				aBoarder = new Boarder(1);
			}else{
				aBoarder = new Boarder(2);
			}
			
			aBoarder.setPosition(cellSize.x * i, -cellSize.y);
			topRow.add(aBoarder);
		}
		
		//right boarder
		for (int i = 0; i < boardUnitSize.y; i++){
			Boarder aBoarder;
			if (i < 1){
				aBoarder = new Boarder(3);
			}else if ( i < boardUnitSize.y -1){
				aBoarder = new Boarder(4);
			}else{
				aBoarder = new Boarder(5);
			}
			
			aBoarder.setPosition(boardUnitSize.x * cellSize.x , cellSize.y * i);
			rightRow.add(aBoarder);
		}
		
		//bottom boarder
		for (int i = 0; i < boardUnitSize.x; i++){
			Boarder aBoarder;
			if (i < 1){
				aBoarder = new Boarder(6);
				aBoarder.setPosition(cellSize.x * i, -cellSize.y);
			}else if ( i < boardUnitSize.x -1){
				aBoarder = new Boarder(7);
			}else{
				aBoarder = new Boarder(8);
			}
			
			aBoarder.setPosition(cellSize.x * i, boardUnitSize.y * cellSize.y);
			bottomRow.add(aBoarder);
		}
		
		//left boarder
		for (int i = 0; i < boardUnitSize.y; i++){
			Boarder aBoarder;
			if (i < 1){
				aBoarder = new Boarder(9);
			}else if ( i < boardUnitSize.y -1){
				aBoarder = new Boarder(10);
			}else{
				aBoarder = new Boarder(11);
			}
			
			aBoarder.setPosition(-cellSize.x , cellSize.y * i);
			leftRow.add(aBoarder);
		}
		
		topLeft = new Boarder(12);
		topLeft.setPosition(-1 * cellSize.x,-1 * cellSize.y);
		
		topRight = new Boarder(13);
		topRight.setPosition(boardUnitSize.x * cellSize.x,-1 * cellSize.y);
		
		bottomLeft = new Boarder(14);
		bottomLeft.setPosition(-1 * cellSize.x,boardUnitSize.y * cellSize.y);
		
		bottomRight = new Boarder(15);
		bottomRight.setPosition( boardUnitSize.x * cellSize.x,boardUnitSize.y * cellSize.y);
		
	}
	
	public void updateBoarderScale(float _gameScale){
		Vector2 scaleCellSize = new Vector2(cellSize.x * _gameScale, 
				cellSize.y * _gameScale);
		
		for (int i = 0 ; i < topRow.size; i++){
			topRow.get(i).setPosition(i * scaleCellSize.x, -1  * scaleCellSize.y);
			topRow.get(i).setScale(_gameScale);
		}
		
		for (int i = 0 ; i < bottomRow.size; i++){
			bottomRow.get(i).setPosition(i * scaleCellSize.x, boardUnitSize.y   * scaleCellSize.y);
			bottomRow.get(i).setScale(_gameScale);
		}
		
		for (int i = 0 ; i < leftRow.size; i++){
			leftRow.get(i).setPosition(-1 * scaleCellSize.x, i  * scaleCellSize.y);
			leftRow.get(i).setScale(_gameScale);
		}
		
		for (int i = 0 ; i < rightRow.size; i++){
			rightRow.get(i).setPosition(boardUnitSize.x * scaleCellSize.x, i  * scaleCellSize.y);
			rightRow.get(i).setScale(_gameScale);
		}
		
		topLeft.setPosition(-1 * scaleCellSize.x,-1 * scaleCellSize.y);
		topLeft.setScale(_gameScale);
		
		topRight.setPosition(boardUnitSize.x * scaleCellSize.x,-1 * scaleCellSize.y);
		topRight.setScale(_gameScale);
		
		bottomLeft.setPosition(-1 * scaleCellSize.x,boardUnitSize.y * scaleCellSize.y);
		bottomLeft.setScale(_gameScale);
		
		bottomRight.setPosition( boardUnitSize.x * scaleCellSize.x,boardUnitSize.y * scaleCellSize.y);
		bottomRight.setScale(_gameScale);
	}

}
