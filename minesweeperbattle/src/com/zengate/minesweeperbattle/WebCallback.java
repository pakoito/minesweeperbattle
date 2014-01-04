package com.zengate.minesweeperbattle;

public class WebCallback {
	
	private boolean isRecieved = false;
	
	private boolean success;
	
	private String message;
	
	public WebCallback(){
		
	}
	
	public boolean getRecieved(){
		return isRecieved;
	}
	
	public boolean getResult(){
		return success;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setData(boolean _result, String _message){
		isRecieved = true;
		success = _result;
		message = _message;
	}

}
