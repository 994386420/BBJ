package com.bbk.mail;

public class MailSenderFactory {
	private static SimpleMailSender serviceSMS = null;
	
	public static SimpleMailSender getMailSender() {
		if(serviceSMS == null) {
			serviceSMS = new SimpleMailSender("", "");
		}
		return serviceSMS;
	}
	
}
