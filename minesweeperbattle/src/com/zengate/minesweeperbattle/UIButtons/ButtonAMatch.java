package com.zengate.minesweeperbattle.UIButtons;

import com.badlogic.gdx.math.Vector2;
import com.zengate.minesweeperbattle.MatchProperties;
import com.zengate.minesweeperbattle.Engine.ContentManager;
import com.zengate.minesweeperbattle.Engine.Renderer;
import com.zengate.minesweeperbattle.Engine.SceneManager;
import com.zengate.minesweeperbattle.Engine.UI.Button;

public class ButtonAMatch extends Button {

	private int matchID;
	private String opponent;
	private long matchSeed;
	
	private int roundNumber;
	
	private String text0 = "";
	
	public ButtonAMatch(int _matchID, String _opponent, long _matchSeed, int _roundNumber) {
		super("sprButton","data/pack0/pack0.pack");
		matchID = _matchID;
		opponent = _opponent;
		matchSeed = _matchSeed;
		roundNumber = _roundNumber;
		
		text0 = "You VS " + opponent;
	}
	
	@Override
	public void Update(float dt){
		super.Update(dt);
		
		Renderer.drawText("testFont", text0, new Vector2(
				position.x + size.x/2 - ContentManager.getFont("testFont").getBounds(text0).width/2 ,
				position.y + size.y/2 - ContentManager.getFont("testFont").getBounds(text0).height/2),
				0, 0, 
				0, 1);
	}
	
	@Override
	public void TouchedReleased(){
		super.TouchedReleased();
		MatchProperties.setData(matchSeed, matchID,roundNumber, false);
		SceneManager.switchScene("TestScene");
	}

}
