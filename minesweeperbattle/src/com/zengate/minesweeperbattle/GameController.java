package com.zengate.minesweeperbattle;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zengate.minesweeperbattle.Engine.Input;

public class GameController {
	
	private Cell[][] gameGrid;
	private int mineNumber;
	
	private boolean gameHasStarted = false;
	
	private Vector2 cellSize;
	private Vector2 boardUnitSize;
	
	private Vector2 firstClicked;
	
	Random aRandom;
	
	public GameController(){
		setUpGame(30, 16, 50);
		aRandom = new Random();
	}
	
	private void setUpGame(int _width, int _height, int _mineNum){
		gameGrid = new Cell[_width][_height];
		
		for (int iX = 0; iX < _width; iX++){
			for (int iY = 0; iY < _height; iY++){
				Cell aCell = new Cell();
				aCell.setPosition(iX * 32, iY * 32);
				gameGrid[iX][iY] = aCell;
				
				if (iX == 0){
					cellSize = new Vector2(aCell.getSize().x, aCell.getSize().y);
				}
			}
		}
		
		mineNumber = _mineNum; 
		boardUnitSize = new Vector2(_width, _height);
	}
	
	public void Update(){
		if (Input.getTouched()){
			if (!gameHasStarted){
				firstClicked = new Vector2(Input.getTouchedPosition().x,Input.getTouchedPosition().y);
				placeMines(mineNumber);
				gameHasStarted = true;
			}else{
				handleTouch();
			}
		}
	}
	
	private void handleTouch(){
		int xIndex = (int) (Input.getTouchedPosition().x /cellSize.x );
		int yIndex = (int) (Input.getTouchedPosition().y /cellSize.y);
		
		if (xIndex < boardUnitSize.x && yIndex < boardUnitSize.y && xIndex >= 0 && yIndex >=0 ){
			gameGrid[xIndex][yIndex].clicked();
			if (!gameGrid[xIndex][yIndex].getIsMine()){
				if (gameGrid[xIndex][yIndex].getMineCount() == 0){
					floodFillReveal(xIndex, yIndex);
				}
			}else{
				revealAllMines();
			}
		}
	}
	
	private void placeMines(int _mineCount){
		Array<Cell> cellArray = new Array<Cell>();
		
		int xIndex = (int) (firstClicked.x /cellSize.x );
		int yIndex = (int) (firstClicked.y /cellSize.y);
		
		for (int iX = 0; iX < boardUnitSize.x; iX++){
			for (int iY = 0; iY < boardUnitSize.y; iY++){
				if (!(iX == xIndex && iY == yIndex)){
					cellArray.add(gameGrid[iX][iY]);
				}
			}
		}

		for (int i= 0; i < mineNumber; i++){
			int choice = aRandom.nextInt(cellArray.size);
			cellArray.get(choice).setMine();
			cellArray.removeIndex(choice);
		}
		
		calculateMineCounts();
	}
	
	private void calculateMineCounts(){
		for (int iX = 0; iX < boardUnitSize.x; iX++){
			for (int iY = 0; iY < boardUnitSize.y; iY++){
				
				int count = 0;
				
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
	
	private void floodFillReveal(int _x, int _y){

		if (_x > 0){floodRoutine(_x -1, _y);}
		if (_x < boardUnitSize.x -1 ){floodRoutine(_x+1,_y);}
		if (_y > 0){floodRoutine(_x , _y-1);}
		if (_y < boardUnitSize.y -1 ){floodRoutine(_x,_y+1);}
		if (_x >0 && _y > 0){floodRoutine(_x -1, _y-1);}
		if (_x <boardUnitSize.x-1 && _y > 0){floodRoutine(_x +1, _y-1);}
		if (_x >0 && _y < boardUnitSize.y-1){floodRoutine(_x -1, _y+1);}
		if (_x < boardUnitSize.x-1 && _y < boardUnitSize.y-1){floodRoutine(_x +1, _y+1);}
	}
	
	private void floodRoutine(int _x, int _y){
		if (!gameGrid[_x][_y].getIsMine()){
			if (!gameGrid[_x][_y].getClicked()){
				gameGrid[_x][_y].clicked();
				
				if (gameGrid[_x][_y].getMineCount() == 0){
					floodFillReveal(_x,_y);
				}
			}
		}
	}
	
	private void revealAllMines() {
		for (int iX = 0; iX < boardUnitSize.x; iX++){
			for (int iY = 0; iY < boardUnitSize.y; iY++){
				gameGrid[iX][iY].reveal();
			}
		}
	}


}
