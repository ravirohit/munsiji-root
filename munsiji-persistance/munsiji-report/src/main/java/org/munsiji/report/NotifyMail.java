package org.munsiji.report;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class NotifyMail {
	
	public static void main(String[] args) {
		System.out.println("excuting mail method");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		System.out.println("excuting mail method");
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("rtes00000@gmail.com","Ravi@5417");
				}
			});

		try {
			System.out.println("excuting mail method123");
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("rtes00000@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("ravi.swd.rohit@gmail.com"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler," +
					"\n\n No spam to my email, please!");
			
			Transport.send(message);
			System.out.println("excuting mail method");
			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
