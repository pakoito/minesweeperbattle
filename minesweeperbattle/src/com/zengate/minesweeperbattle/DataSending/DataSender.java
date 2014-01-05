package com.zengate.minesweeperbattle.DataSending;

import java.util.HashMap;

import sun.awt.windows.ThemeReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpParametersUtils;

public class DataSender {
	
	public DataSender(){

	}
	
	public void sendPlayerMove(String _data, String _username, int _turn, int _matchID,final WebCallback _theCallback){
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put("user", _username);
		parameters.put("turn", ""+_turn);
		parameters.put("data", _data);
		parameters.put("matchID", "" +_matchID);
		HttpRequest httpPost = new HttpRequest(HttpMethods.POST);
		httpPost.setUrl("http://192.168.1.114/addPlayerMove.php");
		httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		
		 Gdx.net.sendHttpRequest (httpPost, new HttpResponseListener() {
		        public void handleHttpResponse(HttpResponse httpResponse) {
		        	String shttpResponse = httpResponse.getResultAsString();
	        		parseData(shttpResponse, _theCallback);
		        }
		 
		        public void failed(Throwable t) {
		        		System.out.println("Failed " + t.getMessage());
		        }
		 });
	}
	
	public void getPlayerMove(int _matchID,final WebCallback _theCallback){
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put("matchID", "" +_matchID);
		
		HttpRequest httpGet = new HttpRequest(HttpMethods.POST);
		httpGet.setUrl("http://192.168.1.114/getMove.php");
		httpGet.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		
		 Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
		        public void handleHttpResponse(HttpResponse httpResponse) {	
		        	String shttpResponse = httpResponse.getResultAsString();
		        	System.out.println("Response: " +shttpResponse);
	        		parseData(shttpResponse, _theCallback);
		        	
		        		//fetchedData = httpResponse.getResultAsString();
		        		//hasData = true;
		                
		        }
		 
		        public void failed(Throwable t) {
		        		System.out.println("failed");
		                //do stuff here based on the failed attempt
		        }
		 });
	}
	
	public void createNewMatch(String _player1, String _player2, long _seed,final WebCallback _theCallback){
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put("player1", _player1);
		parameters.put("player2", _player2);
		parameters.put("seed", ""+_seed);
		HttpRequest httpPost = new HttpRequest(HttpMethods.POST);
		httpPost.setUrl("http://192.168.1.114/createNewMatch.php");
		httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		
		 Gdx.net.sendHttpRequest (httpPost, new HttpResponseListener() {
		        public void handleHttpResponse(HttpResponse httpResponse) {
		        	String shttpResponse = httpResponse.getResultAsString();
	        		parseData(shttpResponse, _theCallback);
		        }
		 
		        public void failed(Throwable t) {
		        		System.out.println("Failed in Create new match " + t.getMessage());
		        }
		 });
	}
	
	public void createNewAccount(String _name, String _password, final WebCallback _theCallback){
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put("username", _name);
		parameters.put("password", _password);
		HttpRequest httpPost = new HttpRequest(HttpMethods.POST);
		httpPost.setUrl("http://192.168.1.114/createNewAccount.php");
		httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		
		 Gdx.net.sendHttpRequest (httpPost, new HttpResponseListener() {
		        public void handleHttpResponse(HttpResponse httpResponse) {
		        		String shttpResponse = httpResponse.getResultAsString();
		        		parseData(shttpResponse, _theCallback);
		        }
		 
		        public void failed(Throwable t) {
		        		System.out.println("Failed " + t.getMessage());
		        }
		 });
	}
	
	public void login(String _name, String _password, final WebCallback _theCallback){
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put("username", _name);
		parameters.put("password", _password);
		HttpRequest httpPost = new HttpRequest(HttpMethods.POST);
		httpPost.setUrl("http://192.168.1.114/accountLogin.php");
		httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		Gdx.net.sendHttpRequest (httpPost, new HttpResponseListener() {
		        public void handleHttpResponse(HttpResponse httpResponse) {
	        		String shttpResponse = httpResponse.getResultAsString();
	        		parseData(shttpResponse, _theCallback);

		        }
		 
		        public void failed(Throwable t) {
		        		System.out.println("Failed " + t.getMessage());
		        }
		 });
	}
	
	public void getPlayerMatches(String _name, final WebCallback _theCallback){
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put("player1", _name);
		HttpRequest httpPost = new HttpRequest(HttpMethods.POST);
		httpPost.setUrl("http://192.168.1.114/getPlayerMatches.php");
		httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		
		Gdx.net.sendHttpRequest (httpPost, new HttpResponseListener() {
		        public void handleHttpResponse(HttpResponse httpResponse) {
	        		String shttpResponse = httpResponse.getResultAsString();
	        		parseData(shttpResponse, _theCallback);
		        }
		 
		        public void failed(Throwable t) {
		        		System.out.println("Failed " + t.getMessage());
		        }
		 });
	}
	
	public void createNewMatchTable(long _matchID,final WebCallback _theCallback){
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put("matchID", ""+_matchID);
		HttpRequest httpPost = new HttpRequest(HttpMethods.POST);
		httpPost.setUrl("http://192.168.1.114/createMatchTable.php");
		httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		
		 Gdx.net.sendHttpRequest (httpPost, new HttpResponseListener() {
		        public void handleHttpResponse(HttpResponse httpResponse) {
		        	String shttpResponse = httpResponse.getResultAsString();
		        	System.out.println(shttpResponse);
	        		parseData(shttpResponse, _theCallback);
		        }
		 
		        public void failed(Throwable t) {
		        		System.out.println("Failed in Create new match " + t.getMessage());
		        }
		 });
	}
	
	private void parseData(String _theResult, WebCallback _theCallback){
		String[] response = _theResult.split(",");
		
		boolean result;
		
		if (response[0].equals("Success")){
			result = true;
		}else{
			result = false;
		}
		
		String message = "";
		
		if (response.length > 1){
			message = response[1];
		}
		
		_theCallback.setData(result, message);
	}
}
