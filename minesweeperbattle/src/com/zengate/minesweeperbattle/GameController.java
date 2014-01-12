package com.zengate.minesweeperbattle;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zengate.minesweeperbattle.Engine.ContentManager;
import com.zengate.minesweeperbattle.Engine.Input;
import com.zengate.minesweeperbattle.Engine.RenderableEntity;
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
	private boolean clickOkay = true;
	private float changeBuff = 32;
	
	private float oldScale;
	
	private boolean doubleTouch = false;
	private boolean zoomFirstTouch = false;
	private float startDistance;
	private Vector2 zoomCenter = new Vector2(0,0);
	
	private Vector2 zoomTouchPos = new Vector2(0,0);
	private Vector2 centerPos = new Vector2(0,0);
	
	public GameController(){
		theEventController = new EventController();
		theGameActions = new GameActions(theEventController, this);
		theEventController.setup(theGameActions);
		setUpGame(30, 16, 99);
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
		
		scroll();
		zoom();
		
		if (canClick && !turnOver){
			if (Input.getTouchedReleased() && clickOkay){
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
	}
	
	//handles camera scroll
	private void scroll(){
		if (Input.getTouchNumber() == 1 && !doubleTouch){
			if (Input.getTouched()){
				if (!firstTouched){
					clickOkay= true;
					touchDownPos.x = Input.getTouchedPosition().x;
					touchDownPos.y = Input.getTouchedPosition().y;
					cameraStartPos.x = cameraPosition.x;
					cameraStartPos.y = cameraPosition.y;
					
					firstTouched = true;
					
				}
				
				cameraChange.x = (touchDownPos.x - Input.getTouchedPosition().x);
				cameraChange.y = (touchDownPos.y - Input.getTouchedPosition().y);
				
				if (Math.abs(cameraChange.x) > changeBuff*gameScale || 
						Math.abs(cameraChange.y) > changeBuff*gameScale){
					clickOkay = false;
				}
				
				cameraPosition.x = cameraStartPos.x - (cameraChange.x);
				cameraPosition.y = cameraStartPos.y - (cameraChange.y);
				
				Renderer.setCameraPos(cameraPosition);
			}
		}
		
		if (!Input.getTouched()){
			if (firstTouched){
				firstTouched = false;
			}
		}
		
	}
	
	//zoom zoom zoom
	private void zoom(){
		if (Input.getTouchNumber() > 1){
			clickOkay = false;
			doubleTouch = true;
			if (!zoomFirstTouch){
				Vector2 touch1pos = new Vector2(Input.getTouchedPosition(0).x,Input.getTouchedPosition(0).y);
				Vector2 touch2pos = new Vector2(Input.getTouchedPosition(1).x,Input.getTouchedPosition(1).y);
				startDistance = touch1pos.dst(touch2pos);
				zoomFirstTouch = true;
				
				//mid point between both touches
				centerPos.x = (touch1pos.x + touch2pos.x)/2;
				centerPos.y = (touch1pos.y + touch2pos.y)/2;
	
				zoomTouchPos.x = ((cameraPosition.x - centerPos.x) / gameScale) +centerPos.x;
				zoomTouchPos.y = ((cameraPosition.y - centerPos.y) / gameScale) +centerPos.y;
				
				zoomCenter.x = ((cameraPosition.x - Renderer.getCameraSize().x/2) / gameScale) +Renderer.getCameraSize().x/2;
				zoomCenter.y = ((cameraPosition.y - Renderer.getCameraSize().y/2) / gameScale) +Renderer.getCameraSize().y/2;
			}
			
			//center of screen, position used to zoom out from
			zoomCenter.x = ((cameraPosition.x - Renderer.getCameraSize().x/2) / gameScale) +Renderer.getCameraSize().x/2;
			zoomCenter.y = ((cameraPosition.y - Renderer.getCameraSize().y/2) / gameScale) +Renderer.getCameraSize().y/2;
			
			Vector2 touch1pos = new Vector2(Input.getTouchedPosition(0).x,Input.getTouchedPosition(0).y);
			Vector2 touch2pos = new Vector2(Input.getTouchedPosition(1).x,Input.getTouchedPosition(1).y);
			float currentDistance = touch1pos.dst(touch2pos);
			
			if (currentDistance > 150){
				float factor = (currentDistance / startDistance);
				
				if (startDistance < currentDistance){
					if (gameScale + factor/20 < 5){
						gameScale += factor/20;
						cameraPosition.x = ((zoomTouchPos.x - centerPos.x) * gameScale) + (centerPos.x) ;
						cameraPosition.y = ((zoomTouchPos.y - centerPos.y) * gameScale) + (centerPos.y) ; 
					}
				}else if (startDistance > currentDistance){
					if (gameScale - factor/20 > 1){
						gameScale -= factor/20;
						cameraPosition.x = ((zoomCenter.x - Renderer.getCameraSize().x/2) * gameScale) + (Renderer.getCameraSize().x/2) ;
						cameraPosition.y = ((zoomCenter.y - Renderer.getCameraSize().y/2) * gameScale) + (Renderer.getCameraSize().y/2) ;
					}
				}
				Renderer.setCameraPos(cameraPosition);
				startDistance = currentDistance;
				
				if (gameScale < 1){
					gameScale = 1;
				}
				
				if (gameScale > 5){
					gameScale = 5;
				}
			}
			
		}else{
			if (!Input.getTouched()){
				zoomFirstTouch = false;
				if (doubleTouch){
					doubleTouch = false;
				}
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
