package com.munsiji.customthread;

import com.munsiji.notification.NotifyMailTLS;

public class MailThread implements Runnable{
	String pwd, toEmail;
	NotifyMailTLS notifyMailTLS = null;
	
	public MailThread(String toEmail, String pwd){
		notifyMailTLS = new NotifyMailTLS(toEmail, pwd);
	}

	@Override
	public void run() {
		notifyMailTLS.mailNewPassword();
	}
	
}
