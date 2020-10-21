package com.srm.rta.validation;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import com.srm.coreframework.config.MailMessages;
import com.sun.mail.util.MailSSLSocketFactory;


public class MailInstance {

	private static MailInstance single_instance = null;
	
	private static MailMessages msg;

	public static MailInstance getInstance(MailMessages mailMessages) {
		if (single_instance == null){
			single_instance = new MailInstance();
		}
		
		msg = mailMessages;
		
		return single_instance;
	}
	
	public Session getSession() throws GeneralSecurityException{
		
		Properties props = new Properties();
		props.put("mail.smtp.host", msg.getSmtpHost());
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "true");

		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		props.put("mail.smtp.ssl.socketFactory", sf);
		Authenticator auth = new SMTPAuthenticator();
		Session mailSession = Session.getInstance(props, auth);
	
		return mailSession;
	}
	
	private class SMTPAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(msg.getSmtpUserName(), msg.getSmtpPassword());
		}
	}

}
