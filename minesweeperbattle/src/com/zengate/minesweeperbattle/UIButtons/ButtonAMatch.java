package com.zengate.minesweeperbattle.UIButtons;

import com.badlogic.gdx.math.Vector2;
import com.zengate.minesweeperbattle.LocalValues;
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
	private String text1 = "";
	
	private float t1R = 0;
	private float t1G = 0;
	private float t1B = 0;
	
	private int playerNum = 0;
	
	private String playerTurn = "";
	
	public ButtonAMatch(int _matchID, String _opponent, long _matchSeed, int _roundNumber, int _playerNum,
			String _playerTurn) {
		super("sprButton","data/pack0/pack0.pack");
		matchID = _matchID;
		opponent = _opponent;
		matchSeed = _matchSeed;
		roundNumber = _roundNumber;
		playerNum = _playerNum;
		playerTurn = _playerTurn;
		
		text0 = "You VS " + opponent;
		
		if (LocalValues.getUsername().toLowerCase().equals(playerTurn.toLowerCase())){
			text1 = "Its your turn!";
			t1G = 1;
		}else{
			text1= "Waiting for move";
			t1R = 1;
		}
	}
	
	@Override
	public void Update(float dt){
		super.Update(dt);
		
		Renderer.drawText("testFont", text0, new Vector2(
				position.x + size.x/2 - ContentManager.getFont("testFont").getBounds(text0).width/2 ,
				position.y + 10),
				0, 0, 
				0, 1);
		
		Renderer.drawText("testFont", text1, new Vector2(
				position.x + size.x/2 - ContentManager.getFont("testFont").getBounds(text1).width/2 ,
				position.y +40),
				t1R, t1G, 
				t1B, 1);
		
	}
	
	@Override
	public void TouchedReleased(){
		super.TouchedReleased();
		MatchProperties.setData(matchSeed, matchID,roundNumber, false, playerNum,opponent);
		SceneManager.switchScene("TestScene");
	}

}
