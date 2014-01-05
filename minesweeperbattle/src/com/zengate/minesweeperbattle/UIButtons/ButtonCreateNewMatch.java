package com.zengate.minesweeperbattle.UIButtons;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.math.Vector2;
import com.zengate.minesweeperbattle.LocalValues;
import com.zengate.minesweeperbattle.MatchProperties;
import com.zengate.minesweeperbattle.DataSending.DataSender;
import com.zengate.minesweeperbattle.DataSending.WebCallback;
import com.zengate.minesweeperbattle.Engine.ContentManager;
import com.zengate.minesweeperbattle.Engine.Renderer;
import com.zengate.minesweeperbattle.Engine.SceneManager;
import com.zengate.minesweeperbattle.Engine.UI.Button;

public class ButtonCreateNewMatch extends Button implements TextInputListener {

	private WebCallback accountCreateCallBack;
	private String opponent;
	private boolean beenClicked = false;
	private boolean textEntered = false;
	
	private boolean waitingForResponse = false;
	
	private Random aRandom = new Random();
	
	private long seed;
	
	public ButtonCreateNewMatch() {
		super("sprButton","data/pack0/pack0.pack");
	}
	
	@Override
	public void Update(float dt){
		super.Update(dt);
		
		Renderer.drawText("testFont", "New Match", new Vector2(
				position.x + size.x/2 - ContentManager.getFont("testFont").getBounds("New Match").width/2 ,
				position.y + size.y/2 - ContentManager.getFont("testFont").getBounds("New Match").height/2),
				0, 0, 
				0, 1);
		
		if (beenClicked){
			if (textEntered){
				DataSender aSender = new DataSender();
				accountCreateCallBack = new WebCallback();
				seed = aRandom.nextInt(99999);
				aSender.createNewMatch(LocalValues.getUsername(), opponent, seed, accountCreateCallBack);
				beenClicked = false;
				textEntered = false;
				waitingForResponse = true;
			}
		}
		
		if (waitingForResponse){
			if (accountCreateCallBack.getRecieved()){
				if (accountCreateCallBack.getResult()){
					waitingForResponse = false;
					MatchProperties.setData(seed, Integer.parseInt(accountCreateCallBack.getMessage()),0,
							true);
					
					DataSender aSender = new DataSender();
					aSender.createNewMatchTable(Integer.parseInt(accountCreateCallBack.getMessage()), new WebCallback());
					
					SceneManager.switchScene("TestScene");
				}
			}
		}
	}
	
	@Override
	public void TouchedReleased(){
		super.TouchedReleased();
		
		if (!waitingForResponse){
			beenClicked = true;
			
			Gdx.input.getTextInput(this, "Enter Username of opponent", "");
		}
		
		//SceneManager.switchScene("CreateAccountScene");
	}

	@Override
	public void input(String text) {
		opponent = text;
		textEntered = true;
		
	}

	@Override
	public void canceled() {
		// TODO Auto-generated method stub
		
	}
}
