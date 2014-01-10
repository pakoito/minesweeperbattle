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
import com.zengate.minesweeperbattle.Notifications.Notification;
import com.zengate.minesweeperbattle.Notifications.NotificationManager;
import com.zengate.minesweeperbattle.UIButtons.ButtonAMatch;
import com.zengate.minesweeperbattle.UIButtons.ButtonCreateNewMatch;
import com.zengate.minesweeperbattle.UIButtons.ButtonLogout;
import com.zengate.minesweeperbattle.UIButtons.ButtonRefresh;

public class LobbyScene extends Scene {

	private WebCallback allMatches;
	
	private boolean waitingForMatches = false;
	
	private ScrollView scrollView;
	
	private Array<ButtonAMatch> buttonList;
	
	private float timer = 0;
	private float refreshTime = 30;
	
	private float scrollAmount = 0;
	
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
		
		ButtonRefresh refreshButton = new ButtonRefresh();
		refreshButton.setPosition(0,
				Renderer.getCameraSize().y - refreshButton.getSize().y);
		SceneManager.Scene().addEntity(refreshButton);
		
		allMatches = new WebCallback();
		DataSender aSender = new DataSender();
		aSender.getPlayerMatches(LocalValues.getUsername(),allMatches);
		waitingForMatches = true;
		
		createScrollView(0);
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
				if (allMatches.getResult()){
					addActiveMatches(allMatches.getMessage());
					waitingForMatches = false;
				}else{
					NotificationManager.addNotification(new Notification("Failed to retrieve match data"));
				}
			}
		}
		
		if (timer < refreshTime){
			timer += 1 * delta;
		}else{
			timer= 0;
			
			if (!waitingForMatches){
				
				scrollAmount = scrollView.scrolledAmount.y;
				RemoveEntity(scrollView);
				scrollView.Delete();
				
				createScrollView(scrollAmount);
				for(ButtonAMatch aButton : buttonList){
					RemoveEntity(aButton);
					aButton.Delete();
				}
		
				buttonList.clear();
				
				allMatches = new WebCallback();
				DataSender aSender = new DataSender();
				aSender.getPlayerMatches(LocalValues.getUsername(),allMatches);
				waitingForMatches = true;
				
				//sleep the thread so the list doesn't disappear and reappear
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void addActiveMatches(String _data){
		
		buttonList = new Array<ButtonAMatch>();
		
		String[] matches = _data.split("\\|");
			
		for (int i = 0; i < matches.length; i++){
			if (i != 0){
				String[] data = matches[i].split("-");
				
				int matchID = -1;
				String opponent = "";
				long seed = 0;
				int turn = 0;
				int playerNum = 0; //which number player this user is in this match
				String playerTurn = "";
				for (int j = 0; j < data.length; j++){
					if (j > 0){
						if (j == 1){
							matchID = Integer.parseInt(data[j]);
						}else{
							
							if (j <4){
								if (!LocalValues.getUsername().toLowerCase().equals(data[j].toLowerCase())){
									opponent = data[j];
									
									if ( j == 2){
										playerNum = 2;
									}else{
										playerNum = 1;
									}
								}
							}
							
							if (j == 4){
								seed = Long.parseLong(data[j]);
							}
							
							if (j == 5){
								turn = Integer.parseInt(data[j]);
							}
							
							if (j == 6){
								playerTurn = data[j].toLowerCase();
							}
						}
					}
				}
				ButtonAMatch button = new ButtonAMatch(matchID, opponent,seed,turn, playerNum, playerTurn);
				if (button.getYourTurn()){
					buttonList.insert(0, button);
				}else{
					buttonList.add(button);
				}
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
	
	private void createScrollView(float _startScroll){
		scrollView = new ScrollView();
		scrollView.setPosition(10,10);
		scrollView.setMinScrollVert(10);
		scrollView.setSize(1000,1000);
		scrollView.setVertScroll(_startScroll);
		scrollView.scrolledAmount.y = _startScroll;
		addEntity(scrollView);
	}


}
