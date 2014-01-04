package com.zengate.minesweeperbattle;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpParametersUtils;

public class DataSender {
	
	private String fetchedData;
	private boolean hasData = false;
	
	public DataSender(){

	}
	
	public void sendPlayerMove(String _data){
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put("user", "Nathy");
		parameters.put("turn", "0");
		parameters.put("data", _data);
		HttpRequest httpPost = new HttpRequest(HttpMethods.POST);
		httpPost.setUrl("http://192.168.1.114/databaseTest.php");
		httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		
		 Gdx.net.sendHttpRequest (httpPost, new HttpResponseListener() {
		        public void handleHttpResponse(HttpResponse httpResponse) {
		        		System.out.println("Response: " +httpResponse.getResultAsString());
		        }
		 
		        public void failed(Throwable t) {
		        		System.out.println("Failed " + t.getMessage());
		        }
		 });
	}
	
	public void getPlayerMove(){
		HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setUrl("http://192.168.1.114/getMove.php");
		hasData = false;
		
		 Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
		        public void handleHttpResponse(HttpResponse httpResponse) {
		        		fetchedData = httpResponse.getResultAsString();
		        		hasData = true;
		                
		        }
		 
		        public void failed(Throwable t) {
		        		System.out.println("failed");
		                //do stuff here based on the failed attempt
		        }
		 });
	}
	
	public void createNewMatch(String _player1, String _player2){
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put("player1", _player1);
		parameters.put("player2", _player2);
		HttpRequest httpPost = new HttpRequest(HttpMethods.POST);
		httpPost.setUrl("http://192.168.1.114/createNewMatch.php");
		httpPost.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		
		 Gdx.net.sendHttpRequest (httpPost, new HttpResponseListener() {
		        public void handleHttpResponse(HttpResponse httpResponse) {
		        		System.out.println("Response: " +httpResponse.getResultAsString());
		        }
		 
		        public void failed(Throwable t) {
		        		System.out.println("Failed " + t.getMessage());
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
		        		System.out.println(shttpResponse);
		        		String[] response = shttpResponse.split(",");
		        		
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
		 
		        public void failed(Throwable t) {
		        		System.out.println("Failed " + t.getMessage());
		        }
		 });
	}
	
	public boolean getHasData(){
		return hasData;
	}
	
	public String getFetchedData(){
		return fetchedData;
	}
}
