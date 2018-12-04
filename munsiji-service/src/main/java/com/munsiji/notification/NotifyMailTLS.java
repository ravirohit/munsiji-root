package com.munsiji.notification;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


//java-mail-1.4.4.jar
public class NotifyMailTLS {

	// reference link   https://support.google.com/accounts/answer/185833
	static final String username = "ravi.swd.rohit@gmail.com";
	static final String password = "exxaqjrtdejgufpi";
	public static void main(String[] args) {

		sendMailTLS(username, password);
	}
	public static boolean sendMailTLS(String username, String password){
	

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(username));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			//throw new RuntimeException(e);
			System.out.println("excpetion occur while sending mail:"+e);
			return false;
		}
		return true;
	}
}