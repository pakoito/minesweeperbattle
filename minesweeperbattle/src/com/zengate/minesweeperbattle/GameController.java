package com.zengate.minesweeperbattle;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zengate.minesweeperbattle.Engine.Input;
import com.zengate.minesweeperbattle.Engine.SceneManager;
import com.zengate.minesweeperbattle.EventSystem.EventController;

public class GameController {
	
	private Cell[][] gameGrid;
	private int mineNumber;
	private GameActions theGameActions;
	private EventController theEventController;
	
	private Vector2 cellSize;
	private Vector2 boardUnitSize;
	
	private boolean gameHasStarted = false;
	private Vector2 firstClicked;
	
	private Random aRandom;
	
	private boolean canClick = true;
	
	public GameController(){
		theEventController = new EventController();
		theGameActions = new GameActions(theEventController, this);
		theEventController.setup(theGameActions);
		setUpGame(30, 16, 50);
		placeMines(mineNumber);
	}
	
	private void setUpGame(int _width, int _height, int _mineNum){
		gameGrid = new Cell[_width][_height];
		
		for (int iX = 0; iX < _width; iX++){
			for (int iY = 0; iY < _height; iY++){
				
				Cell aCell = new Cell();
				
				if (iX == 0){
					cellSize = new Vector2(aCell.getSize().x, aCell.getSize().y);
				}
				
				aCell.setPosition(iX * cellSize.x, iY * cellSize.y);
				gameGrid[iX][iY] = aCell;
			}
		}
		
		mineNumber = _mineNum; 
		boardUnitSize = new Vector2(_width, _height);
		
		aRandom = new Random(32);
		theGameActions.setGameGrid(gameGrid, boardUnitSize);
		
		gameHasStarted = false;
		
	}
	
	public void Update(float delta){

		if (Input.getTouched()){
			handleTouch();
		}
		
		theEventController.update(delta);
	}
	
	private void handleTouch(){
		int xIndex = (int) (Input.getTouchedPosition().x /cellSize.x );
		int yIndex = (int) (Input.getTouchedPosition().y /cellSize.y);
		
		theGameActions.cellClicked(xIndex, yIndex, true);
	}
	
	private void placeMines(int _mineCount){
		firstClicked = new Vector2(Input.getTouchedPosition().x,Input.getTouchedPosition().y);
		Array<Cell> cellArray = new Array<Cell>();
		
		int xIndex = (int) (firstClicked.x /cellSize.x );
		int yIndex = (int) (firstClicked.y /cellSize.y);
		
		for (int iX = 0; iX < boardUnitSize.x; iX++){
			for (int iY = 0; iY < boardUnitSize.y; iY++){
				//if (!(iX == xIndex && iY == yIndex)){
					cellArray.add(gameGrid[iX][iY]);
				//}
			}
		}

		for (int i= 0; i < mineNumber; i++){
			int choice = aRandom.nextInt(cellArray.size);
			cellArray.get(choice).setMine();
			cellArray.removeIndex(choice);
		}
		
		theGameActions.calculateMineCounts();
	}
	
	public void reset(){
		/*canClick = false;
		for (int iX = 0; iX < boardUnitSize.x; iX++){
			for (int iY = 0; iY < boardUnitSize.y; iY++){
				gameGrid[iX][iY].Delete();
			}
		}
		setUpGame(30, 16, 50);*/
		//theEventController.replay();
		theEventController.sendPlayerMove();
	}
}
