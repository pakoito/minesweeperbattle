package com.zengate.minesweeperbattle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zengate.minesweeperbattle.Engine.ContentManager;
import com.zengate.minesweeperbattle.Engine.RenderableEntity;
import com.zengate.minesweeperbattle.Engine.Renderer;
import com.zengate.minesweeperbattle.Engine.SceneManager;

public class Cell extends RenderableEntity {
	
	private boolean isMine = false;
	private boolean hasBeenClicked = false;
	private int mineCount = 0;
	
	private String mineCountTxt = "";
	private Vector2 textBounds;
	private Vector2 textPosition;
	
	private Array<Color> colorArray = new Array<Color>();
	
	private Vector2 oldPos = new Vector2();
	
	public Cell(){
		super("Cell","sprCell","data/pack0/pack0.pack");
		SceneManager.Scene().addEntity(this);
		
		colorArray.add(Color.CLEAR);
		colorArray.add(Color.BLUE);
		colorArray.add(Color.CYAN);
		colorArray.add(Color.DARK_GRAY);
		colorArray.add(Color.GREEN);
		colorArray.add(Color.MAGENTA);
		colorArray.add(Color.RED);
		colorArray.add(Color.YELLOW);
		colorArray.add(Color.PINK);
	}
	
	@Override
	public void Update(float dt){
		super.Update(dt);
		if (hasBeenClicked && !isMine){
			if (!mineCountTxt.equals("")){
				Renderer.drawText("testFont", mineCountTxt, textPosition,
						colorArray.get(mineCount).r, colorArray.get(mineCount).g, 
						colorArray.get(mineCount).b, 1);
			}
		}
		
		if (oldPos.x != position.x || oldPos.y != position.y){
			setMineCount(mineCount);
		}
		
		oldPos.x = position.x;
		oldPos.y = position.y;
	}
	
	/**
	 * @return if a click happened
	 */
	public boolean clicked(){
		if (!hasBeenClicked){
			if (!isMine){
				setUpTexture("sprCellClicked","data/pack0/pack0.pack");
			}else{
				setUpTexture("sprCellStar","data/pack0/pack0.pack");
			}
			hasBeenClicked = true;
			return true;
		}
		
		return false;
	}
	
	public void reveal(){
		if (!hasBeenClicked){
			if (!isMine){
				setUpTexture("sprCellClicked","data/pack0/pack0.pack");
			}else{
				setUpTexture("sprCellStar","data/pack0/pack0.pack");
			}
			hasBeenClicked = true;
		}
	}
	
	public boolean setMine(){
		if (!isMine){
			isMine = true;
			return true;
		}
		
		return false;
	}
	
	public void setMineCount(int _count){
		mineCount = _count;
		
		if (_count > 0){
			mineCountTxt = "" + mineCount;
			
			textBounds = new Vector2(ContentManager.getFont("testFont").getBounds(mineCountTxt).width,
					 ContentManager.getFont("testFont").getBounds(mineCountTxt).height);
			
			textPosition = new Vector2(position.x + ((size.x * scale.x) /2) - (textBounds.x/2), 
					position.y - size.y*(scale.y -1) + ((size.y * scale.y )  /2) - (textBounds.y/2) );
			
		}
	}
	
	
	public boolean getIsMine(){
		return isMine;
	}
	
	public boolean getClicked(){
		return hasBeenClicked;
	}
	
	public int getMineCount(){
		return mineCount;
	}

}
