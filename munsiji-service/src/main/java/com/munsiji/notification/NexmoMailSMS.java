package com.munsiji.notification;

import java.io.IOException;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.SmsSubmissionResult;
import com.nexmo.client.sms.messages.TextMessage;

//https://dashboard.nexmo.com/getting-started/?firstLogin=true#/sms
public class NexmoMailSMS {

	public static void main(String[] args) throws IOException, NexmoClientException {
		// TODO Auto-generated method stub
		
		AuthMethod auth = new TokenAuthMethod("6e5dddb5", "60Z4DmEEfp3fj3yS");
		NexmoClient client = new NexmoClient(auth);

		SmsSubmissionResult[] responses = client.getSmsClient().submitMessage(
			new TextMessage(
		        "Nexmo",
		        "919176145194",
		        "Hello from Nexmo ....Rohit here"
		    )
		);

		for (SmsSubmissionResult response : responses) {
		    System.out.println(response);
		}

	}

}
