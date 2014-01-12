package com.zengate.minesweeperbattle;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zengate.minesweeperbattle.Engine.ContentManager;
import com.zengate.minesweeperbattle.Engine.Input;
import com.zengate.minesweeperbattle.Engine.Renderer;
import com.zengate.minesweeperbattle.EventSystem.EventController;

public class GameController {
	
	private Cell[][] gameGrid;
	private int mineNumber;
	private GameActions theGameActions;
	private EventController theEventController;
	
	private Vector2 cellSize;
	private Vector2 boardUnitSize;
	
	private Random aRandom;
	
	private BoarderController theBoarderController;
	
	private boolean canClick = true;
	
	private int misses = 3;
	
	private String text0 = "Misses: ";
	
	private int localMineCount = 0;
	private int opponentMineCount = 0;
	
	private boolean turnOver = false;
	
	private float gameScale = 0.25f;
	
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
	
	private Vector2 oldTouch1Pos = new Vector2(0,0);
	private Vector2 oldTouch2Pos = new Vector2(0,0);
	
	private Vector2 currentTouch1Pos = new Vector2(0,0);
	private Vector2 currentTouch2Pos = new Vector2(0,0);
	
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
		
		//theBoarderController = new BoarderController(boardUnitSize, cellSize);
		
		
		
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
				
				
				//lock camera in place
				if (cameraPosition.x > 0 + Renderer.getCameraSize().x /2){
					cameraPosition.x = 0 + Renderer.getCameraSize().x /2;
				}
				
				if (cameraPosition.x < -(boardUnitSize.x * (cellSize.x * gameScale)) + Renderer.getCameraSize().x - Renderer.getCameraSize().x/2){
					cameraPosition.x = -(boardUnitSize.x * (cellSize.x * gameScale)) + Renderer.getCameraSize().x - Renderer.getCameraSize().x/2;
				}
				
				if (cameraPosition.y > cellSize.y*(gameScale -1) + Renderer.getCameraSize().y/2){
					cameraPosition.y = cellSize.y*(gameScale -1) + Renderer.getCameraSize().y/2;
				}
				
				if (cameraPosition.y < -(boardUnitSize.y * (cellSize.y * gameScale) - Renderer.getCameraSize().y) - Renderer.getCameraSize().y/2 +  cellSize.y*(gameScale -1)){
					cameraPosition.y = -(boardUnitSize.y * (cellSize.y * gameScale)) + Renderer.getCameraSize().y - Renderer.getCameraSize().y/2 +  cellSize.y*(gameScale -1);
				}
				
				
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
		Vector2 touch1pos;
		Vector2 touch2pos;
		if (Input.getTouchNumber() > 1){
			clickOkay = false;
			doubleTouch = true;
			if (!zoomFirstTouch){
				touch1pos = new Vector2(Input.getTouchedPosition(0).x,Input.getTouchedPosition(0).y);
				touch2pos = new Vector2(Input.getTouchedPosition(1).x,Input.getTouchedPosition(1).y);
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
			
			
			touch1pos = new Vector2(Input.getTouchedPosition(0).x,Input.getTouchedPosition(0).y);
			touch2pos = new Vector2(Input.getTouchedPosition(1).x,Input.getTouchedPosition(1).y);
			
			//check if either finger has moved past the dead zone allowance since last frame
			if (touch1pos.dst(oldTouch1Pos) > 5){
				currentTouch1Pos = touch1pos;
				oldTouch1Pos = currentTouch1Pos;
			}else{
				currentTouch1Pos = oldTouch1Pos;
			}
			
			if (touch2pos.dst(oldTouch2Pos) > 5){
				currentTouch2Pos = touch2pos;
				oldTouch2Pos = currentTouch2Pos;
			}else{
				currentTouch2Pos = oldTouch2Pos;
			}

			
			//calculate difference between fingers
			float currentDistance = currentTouch1Pos.dst(currentTouch2Pos);
			
			//make sure fingers arn't too close to each other
			if (currentDistance > 150){
				float factor = (currentDistance / startDistance);
				
				if (Math.abs(factor) >= 0.8f){
					if (startDistance < currentDistance){
						if (gameScale + factor/40 <= 1){
							gameScale += factor/40;
							cameraPosition.x = ((zoomTouchPos.x - centerPos.x) * gameScale) + (centerPos.x) ;
							cameraPosition.y = ((zoomTouchPos.y - centerPos.y) * gameScale) + (centerPos.y) ; 
						}
					}else if (startDistance > currentDistance){
						if (gameScale - factor/40 >= 0.25f){
							gameScale -= factor/40;
							cameraPosition.x = ((zoomCenter.x - Renderer.getCameraSize().x/2) * gameScale) + (Renderer.getCameraSize().x/2) ;
							cameraPosition.y = ((zoomCenter.y - Renderer.getCameraSize().y/2) * gameScale) + (Renderer.getCameraSize().y/2) ;
						}
					}
					Renderer.setCameraPos(cameraPosition);
					startDistance = currentDistance;
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
		int yIndex = (int)(( Input.getTouchedReleasedPos().y - cameraPosition.y+(cellSize.y*(gameScale -1))) /(cellSize.y * gameScale));
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
		
		//theBoarderController.updateBoarderScale(gameScale);
	}
}
