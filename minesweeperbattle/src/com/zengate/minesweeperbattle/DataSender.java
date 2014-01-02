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
	
	public boolean getHasData(){
		return hasData;
	}
	
	public String getFetchedData(){
		return fetchedData;
	}
}
