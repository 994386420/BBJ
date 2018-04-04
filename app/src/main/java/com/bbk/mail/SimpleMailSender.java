package com.bbk.mail;

import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SimpleMailSender {
	private final transient Properties props = System.getProperties();
	private transient MailAuthenticator authenticator;
	private transient Session session;

	public SimpleMailSender(final String smtpHostName, final String username, final String password) {
		init(username, password, smtpHostName);
	}

	public SimpleMailSender(final String username, final String password) {
		final String smtpHostName = "smtp." + username.split("@")[1];
		init(username, password, smtpHostName);
	}

	private void init(String username, String password, String smtpHostName) {
		// 初始化props
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", smtpHostName);
		// 验证
		authenticator = new MailAuthenticator(username, password);
		// 创建session
		session = Session.getInstance(props, authenticator);
	}

	public void send(String recipient, String subject, Object content){
		try {
			// 创建mime类型邮件
			final MimeMessage message = new MimeMessage(session);
			// 设置发信人
			message.setFrom(new InternetAddress(authenticator.getUserName()));
			// 设置收件人
			message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipient));
			// 设置主题
			message.setSubject(subject);
			// 设置邮件内容
			message.setContent(content.toString(), "text/html;charset=utf-8");
			// 发送
			Transport.send(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void send(List<String> recipients, String subject, Object content){
		try {
			// 创建mime类型邮件
			final MimeMessage message = new MimeMessage(session);
			// 设置发信人
			message.setFrom(new InternetAddress(authenticator.getUserName()));
			// 设置收件人们
			final int num = recipients.size();
			InternetAddress[] addresses = new InternetAddress[num];
			for (int i = 0; i < num; i++) {
				addresses[i] = new InternetAddress(recipients.get(i));
			}
			message.setRecipients(javax.mail.Message.RecipientType.TO, addresses);
			// 设置主题
			message.setSubject(subject);
			// 设置邮件内容
			message.setContent(content.toString(), "text/html;charset=utf-8");
			// 发送
			Transport.send(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void send(String recipient, SimpleMail mail) throws AddressException, MessagingException {
		send(recipient, mail.getSubject(), mail.getContent());
	}

	public void send(List<String> recipients, SimpleMail mail) throws AddressException, MessagingException {
		send(recipients, mail.getSubject(), mail.getContent());
	}
}
