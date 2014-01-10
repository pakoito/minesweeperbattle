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
	
	private Random aRandom;
	
	private boolean canClick = true;
	
	private int misses = 3;
	
	private String text0 = "Misses: ";
	
	private int localMineCount = 0;
	private int opponentMineCount = 0;
	
	private boolean turnOver = false;
	
	private float gameScale = 1;
	
	private Vector2 cameraPosition = new Vector2(0,0);
	
	private Vector2 touchDownPos = new Vector2(0,0);
	private Vector2 cameraStartPos = new Vector2(0,0);
	private boolean firstTouched = false;
	private Vector2 cameraChange  = new Vector2(0,0);
	private boolean cameraMoved = false;
	private float changeBuff = 32;
	
	private float oldScale;
	
	public GameController(){
		theEventController = new EventController();
		theGameActions = new GameActions(theEventController, this);
		theEventController.setup(theGameActions);
		setUpGame(30, 16, 80);
		placeMines(mineNumber);
		
		if (!MatchProperties.getNewMatch()){
			canClick = false;
		}
		
		if (!LocalValues.getPastMoves(MatchProperties.getMatchID()).equals("")){
			theEventController.reCreateInstant(LocalValues.getPastMoves(MatchProperties.getMatchID()));
		}
		
		Renderer.setCameraPos(cameraPosition);
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
		
		aRandom = new Random(MatchProperties.getSeed());
		theGameActions.setGameGrid(gameGrid, boardUnitSize);
		
	}
	
	public void Update(float delta){
		
		if (theEventController.getIsReplaying()){
			Renderer.drawScreenText("testFont", MatchProperties.getOpponent() + "'s turn, Please wait", new Vector2(
					Renderer.getCameraSize().x/2 - ContentManager.getFont("testFont").getBounds(MatchProperties.getOpponent() + "'s turn, Please wait").width/2 ,
					10),
					1, 0, 
					0, 1);	
		}else{
			Renderer.drawScreenText("testFont", text0 + misses, new Vector2(
					Renderer.getCameraSize().x/2 - ContentManager.getFont("testFont").getBounds(text0 + misses).width/2,
					10),
					1, 1, 
					1, 1);
		}
		
		Renderer.drawScreenText("testFont","You: " +localMineCount, new Vector2(0,
				10),
				1, 1, 
				1, 1);
		
		Renderer.drawScreenText("testFont", MatchProperties.getOpponent() +": " +opponentMineCount, new Vector2(
				Renderer.getCameraSize().x - ContentManager.getFont("testFont").getBounds(MatchProperties.getOpponent() +": " +opponentMineCount).width,
				10),
				1, 1, 
				1, 1);
		
		if (canClick && !turnOver){
			if (Input.getTouchedReleased() && !cameraMoved){
				handleTouch();
			}
		}else{
			if(!theEventController.getIsReplaying()){
				canClick = true;
			}
		}
		
		if (gameScale != oldScale){
			updateScale();
		}
		oldScale = gameScale;
		
		theEventController.update(delta);
		
		scroll();
	}
	
	//handles camera scroll
	private void scroll(){
		if (Input.getTouched()){
			if (!firstTouched){
				cameraMoved = false;
				touchDownPos.x = Input.getTouchedPosition().x;
				touchDownPos.y = Input.getTouchedPosition().y;
				cameraStartPos.x = cameraPosition.x;
				cameraStartPos.y = cameraPosition.y;
				
				firstTouched = true;
				
			}
			
			cameraChange.x = (touchDownPos.x - Input.getTouchedPosition().x);
			cameraChange.y = (touchDownPos.y - Input.getTouchedPosition().y);
			
			if (cameraChange.x > changeBuff*gameScale || cameraChange.y > changeBuff*gameScale){
				cameraMoved = true;
			}
			
			cameraPosition.x = cameraStartPos.x - (cameraChange.x);
			cameraPosition.y = cameraStartPos.y - (cameraChange.y);
			
			Renderer.setCameraPos(cameraPosition);
		}else{
			if (Input.getTouchedReleased()){
				firstTouched = false;
			}
		}
		
	}
	
	private void handleTouch(){
		int xIndex = (int)(( Input.getTouchedReleasedPos().x - cameraPosition.x) /(cellSize.x * gameScale));
		int yIndex = (int)(( Input.getTouchedReleasedPos().y - cameraPosition.y+(32*(gameScale -1))) /(cellSize.y * gameScale));
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
				}
			}
			
			theGameActions.cellClicked(xIndex, yIndex, true, MatchProperties.getPlayerNum());
			
			if (hasDied){
				theEventController.sendPlayerMove();
			}
		}
	}
	
	private void placeMines(int _mineCount){
		Array<Cell> cellArray = new Array<Cell>();
		
		for (int iX = 0; iX < boardUnitSize.x; iX++){
			for (int iY = 0; iY < boardUnitSize.y; iY++){
				cellArray.add(gameGrid[iX][iY]);
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
	
	private void updateScale(){
		
		Vector2 scaleCellSize = new Vector2(cellSize.x * gameScale, 
													cellSize.y * gameScale);
		
		for (int iX = 0; iX < boardUnitSize.x; iX++){
			for (int iY = 0; iY < boardUnitSize.y; iY++){
				gameGrid[iX][iY].setPosition(iX * scaleCellSize.x, iY * scaleCellSize.y);
				gameGrid[iX][iY].setScale(gameScale);
			}
		}
	}
}
