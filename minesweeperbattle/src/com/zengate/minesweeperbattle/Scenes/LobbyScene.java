package com.zengate.minesweeperbattle.Scenes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.zengate.minesweeperbattle.LocalValues;
import com.zengate.minesweeperbattle.DataSending.DataSender;
import com.zengate.minesweeperbattle.DataSending.WebCallback;
import com.zengate.minesweeperbattle.Engine.Renderer;
import com.zengate.minesweeperbattle.Engine.Scene;
import com.zengate.minesweeperbattle.Engine.SceneManager;
import com.zengate.minesweeperbattle.Engine.UI.ScrollView;
import com.zengate.minesweeperbattle.UIButtons.ButtonAMatch;
import com.zengate.minesweeperbattle.UIButtons.ButtonCreateNewMatch;
import com.zengate.minesweeperbattle.UIButtons.ButtonLogout;

public class LobbyScene extends Scene {

	private WebCallback allMatches;
	
	private boolean waitingForMatches = false;
	
	private ScrollView scrollView;
	
	public LobbyScene() {
		super("LobbyScene");
	}
	
	@Override
	public void SceneBegin(){
		super.SceneBegin();
		
		ButtonLogout logoutButton = new ButtonLogout();
		logoutButton.setPosition(Renderer.getCameraSize().x - logoutButton.getSize().x,0);
		SceneManager.Scene().addEntity(logoutButton);
		
		ButtonCreateNewMatch createMatchButton = new ButtonCreateNewMatch();
		createMatchButton.setPosition(Renderer.getCameraSize().x - createMatchButton.getSize().x,
				Renderer.getCameraSize().y - createMatchButton.getSize().y);
		SceneManager.Scene().addEntity(createMatchButton);
		
		allMatches = new WebCallback();
		DataSender aSender = new DataSender();
		aSender.getPlayerMatches(LocalValues.getUsername(),allMatches);
		waitingForMatches = true;
		
		scrollView = new ScrollView();
		scrollView.setPosition(10,10);
		scrollView.setMinScrollVert(10);
		scrollView.setSize(1000,1000);
		addEntity(scrollView);
	}
	
	@Override
	public void SceneEnd(){
		super.SceneEnd();
		this.reset();
	}
	
	@Override
	public void Update(float delta){
		super.Update(delta);
		
		Renderer.drawText("testFont", "Welcome, " +LocalValues.getUsername(), new Vector2(0,0),
				1, 1, 1, 1);
		
		if (waitingForMatches){
			if (allMatches.getRecieved()){
				addActiveMatches(allMatches.getMessage());
				waitingForMatches = false;
			}
		}
	}
	
	private void addActiveMatches(String _data){
		
		Array<ButtonAMatch> buttonList = new Array<ButtonAMatch>();
		
		String[] matches = _data.split("\\|");
			
		for (int i = 0; i < matches.length; i++){
			if (i != 0){
				String use = matches[i].replace("","");
				String[] data = use.split("-");
				
				int matchID = -1;
				String opponent = "";
				long seed = 0;
				for (int j = 0; j < data.length; j++){
					if (j > 0){
						if (j == 1){
							matchID = Integer.parseInt(data[j]);
						}else{
							
							if (j <4){
								if (!LocalValues.getUsername().toLowerCase().equals(data[j].toLowerCase())){
									opponent = data[j];
								}
							}
							
							if (j == 4){
								seed = Long.parseLong(data[j]);
							}
						}
					}
				}
				
				ButtonAMatch button = new ButtonAMatch(matchID, opponent,seed,0);
				buttonList.add(button);
			}
		}
		
		int scrollLength = 0;
		for(int k = 0; k < buttonList.size; k++){
			buttonList.get(k).setPosition(Renderer.getCameraSize().x/2 - buttonList.get(k).getSize().x/2,
					100 + (96 * k));
			
			scrollLength += 96;
			scrollView.add(buttonList.get(k));
			scrollView.setMaxScrollVert(-scrollLength);
		}
	}


}
