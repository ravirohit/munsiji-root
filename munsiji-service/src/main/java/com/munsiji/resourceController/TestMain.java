package com.munsiji.resourceController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class TestMain {
	 static int a =0;
	public static void main(String[] args) {
	ExecutorService es = Executors.newFixedThreadPool(300);
	for(int i = 0;i < 10; i++){
		es.execute(() -> {
			RestConsumeMethodImpl.apiCall(a);
		});
	}
	es.shutdown();
		
	}
}

class RestConsumeMethodImpl {
	public static void apiCall(int i){
		try {
		System.out.println("==== start:"+i++);
		// create HTTP Client
		RequestConfig.Builder requestBuilder = RequestConfig.custom();
		//requestBuilder.setConnectTimeout(2000);
		//requestBuilder.setConnectionRequestTimeout(2000);
	//	requestBuilder.setSocketTimeout(2000);     // default value could be infinite even in case of browser .. time is in milli second

		HttpClientBuilder builder = HttpClientBuilder.create();     
		builder.setDefaultRequestConfig(requestBuilder.build());
		HttpClient httpClient = builder.build();
	
     // Create new getRequest with below mentioned URL
       // System.out.println("create url");
       //HttpGet getRequest = new HttpGet("http://localhost:8080/munsiji-service/rest/myapp/holdreqthread");
       //HttpGet getRequest = new HttpGet("http://localhost:8080/munsiji-service/rest/myapp/holdreqthreadinfite");
	   // HttpGet getRequest = new HttpGet("http://localhost:8080/AsysnchServletApi/myservlet");
		HttpGet getRequest = new HttpGet("http://localhost:8080/AsysnchServletApi/myasyncservlet");
	
    // Add additional header to getRequest which accepts application/xml data
       getRequest.addHeader("accept", "application/xml");
       
    // Execute your request and catch response
       //System.out.println("making api call");
       HttpResponse response = httpClient.execute(getRequest);
       System.out.println("print response ojbbect:"+response);
	// Check for HTTP response code: 200 = success
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}
 
			// Get-Capture Complete application/xml body response
		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		String output;
			// Simply iterate through XML response and show on console.
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}
	  } catch (Exception e) {
		e.printStackTrace();
	  }
	}

}


	