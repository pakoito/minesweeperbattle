package com.zengate.minesweeperbattle;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpParametersUtils;

public class PostTest {
	
	public PostTest(){
		HashMap<String, String> parameters = new HashMap<String,String>();
		parameters.put("username", "Android");
		parameters.put("password", "ImANexus7");
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
}
