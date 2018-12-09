package com.munsiji.notification;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PasswordResetNotif {
	static final ExecutorService executor = Executors.newFixedThreadPool(50);
	public static void mailPassword(String toEmail,String pwd){
        System.out.println("creating new thread to handle mail service");
		executor.execute(new MailThread(toEmail, pwd));
	}

}
class MailThread implements Runnable{
	String pwd, toEmail;
	NotifyMailTLS notifyMailTLS = null;
	
	public MailThread(String toEmail, String pwd){
		notifyMailTLS = new NotifyMailTLS(toEmail, pwd);
	}

	@Override
	public void run() {
		System.out.println("run method called;");
		notifyMailTLS.mailNewPassword();
	}
	
}
