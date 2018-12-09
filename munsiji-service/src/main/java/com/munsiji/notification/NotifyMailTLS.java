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
	final String username = "ravi.swd.rohit@gmail.com";
	final String password = "exxaqjrtdejgufpi";
	String pwd, toEmail;
	
	public NotifyMailTLS(String toEmail, String pwd){
		this.toEmail = toEmail;
		this.pwd = pwd;
	}
	public static void main(String[] args) {

		//sendMailTLS(username, password);
	}
	public boolean mailNewPassword(){
	System.out.println("mail New password mail class method called");
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
				InternetAddress.parse(toEmail));
			message.setSubject("Password Reset");
			message.setText("Dear G user,"
				+ "\n\n Your password has been reset. "
				+ "\n\n Your new password is:"+ pwd
				+ "\nFYI...\n process to change the password, please follow the steps: "
				+ " \n\tlogin to your acccount with new password -> profile -> change password -> provide old password and new password -> "
				+ "click the submit button. ");

			Transport.send(message);

			System.out.println("---------------Done");

		} catch (MessagingException e) {
			//throw new RuntimeException(e);
			System.out.println("excpetion occur while sending mail:"+e);
			return false;
		}
		return true;
	}
}