package com.zengate.minesweeperbattle.Engine.UI;

public class ButtonEvent {
	
	private Button hostButton;
	
	public ButtonEvent(Button _hostButton){
		hostButton = _hostButton;
	}
	
	public Button getButton(){
		return hostButton;
	}

}
