package com.zengate.minesweeperbattle;

import java.math.MathContext;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zengate.minesweeperbattle.Engine.ContentManager;
import com.zengate.minesweeperbattle.Engine.Input;
import com.zengate.minesweeperbattle.Engine.Renderer;
import com.zengate.minesweeperbattle.Engine.SceneManager;
import com.zengate.minesweeperbattle.EventSystem.EventController;

public class GameController {
	
	private Cell[][] gameGrid;
	private int mineNumber;
	private GameActions theGameActions;
	private EventController theEventController;
	
	private Vector2 cellSize;
	private Vector2 boardUnitSize;
	
	private Vector2 firstClicked;
	
	private Random aRandom;
	
	private boolean canClick = true;
	
	private int misses = 3;
	
	private String text0 = "Misses: ";
	
	private int localMineCount = 0;
	private int opponentMineCount = 0;
	
	private boolean turnOver = false;
	
	public GameController(){
		theEventController = new EventController();
		theGameActions = new GameActions(theEventController, this);
		theEventController.setup(theGameActions);
		setUpGame(30, 15, 80);
		placeMines(mineNumber);
		
		if (!MatchProperties.getNewMatch()){
			canClick = false;
		}
		
		if (!LocalValues.getPastMoves(MatchProperties.getMatchID()).equals("")){
			theEventController.reCreateInstant(LocalValues.getPastMoves(MatchProperties.getMatchID()));
		}
	}
	
	private void setUpGame(int _width, int _height, int _mineNum){
		gameGrid = new Cell[_width][_height];
		
		for (int iX = 0; iX < _width; iX++){
			for (int iY = 0; iY < _height; iY++){
				
				Cell aCell = new Cell();
				
				if (iX == 0){
					cellSize = new Vector2(aCell.getSize().x, aCell.getSize().y);
				}
				
				aCell.setPosition(iX * cellSize.x, 32 + iY * cellSize.y);
				gameGrid[iX][iY] = aCell;
			}
		}
		
		mineNumber = _mineNum; 
		boardUnitSize = new Vector2(_width, _height);
		
		aRandom = new Random(MatchProperties.getSeed());
		theGameActions.setGameGrid(gameGrid, boardUnitSize);
		
	}
	
	public void Update(float delta){
		
		if (theEventController.getIsReplaying()){
			Renderer.drawText("testFont", MatchProperties.getOpponent() + "'s turn, Please wait", new Vector2(
					Renderer.getCameraSize().x/2 - ContentManager.getFont("testFont").getBounds(MatchProperties.getOpponent() + "'s turn, Please wait").width/2 ,
					10),
					1, 0, 
					0, 1);	
		}else{
			Renderer.drawText("testFont", text0 + misses, new Vector2(
					Renderer.getCameraSize().x/2 - ContentManager.getFont("testFont").getBounds(text0 + misses).width/2 ,
					10),
					1, 1, 
					1, 1);
		}
		
		Renderer.drawText("testFont","You: " +localMineCount, new Vector2(
				0,
				10),
				1, 1, 
				1, 1);
		
		Renderer.drawText("testFont", MatchProperties.getOpponent() +": " +opponentMineCount, new Vector2(
				Renderer.getCameraSize().x - ContentManager.getFont("testFont").getBounds(MatchProperties.getOpponent() +": " +opponentMineCount).width,
				10),
				1, 1, 
				1, 1);
		
		if (canClick && !turnOver){
			if (Input.getTouched()){
				handleTouch();
			}
		}else{
			if(!theEventController.getIsReplaying()){
				canClick = true;
			}
		}
		
		theEventController.update(delta);
	}
	
	private void handleTouch(){
		int xIndex = (int) (Input.getTouchedPosition().x /cellSize.x );
		int yIndex = (int) ((Input.getTouchedPosition().y-32) /cellSize.y);
		boolean hasDied = false;
		if (xIndex < boardUnitSize.x && yIndex < boardUnitSize.y && xIndex >= 0 && yIndex >= 0 ){
			if (!gameGrid[xIndex][yIndex].getClicked()){
				if (!gameGrid[xIndex][yIndex].getIsMine()){
					if (misses > 0){
						misses--;
					}else{
						hasDied = true;
						turnOver = true;
					}
				}else{
					//scoring here
				}
			}
			
			theGameActions.cellClicked(xIndex, yIndex, true, MatchProperties.getPlayerNum());
			
			if (hasDied){
				theEventController.sendPlayerMove();
			}
		}
	}
	
	private void placeMines(int _mineCount){
		firstClicked = new Vector2(Input.getTouchedPosition().x,Input.getTouchedPosition().y);
		Array<Cell> cellArray = new Array<Cell>();
		
		//int xIndex = (int) (firstClicked.x /cellSize.x );
		//int yIndex = (int) (firstClicked.y /cellSize.y);
		
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
	
	public void addPoint(int _playerNum){
		if (_playerNum == MatchProperties.getPlayerNum()){
			localMineCount++;
		}else{
			opponentMineCount++;
		}
	}
}
