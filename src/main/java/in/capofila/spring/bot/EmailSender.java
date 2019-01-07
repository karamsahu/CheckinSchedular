package in.capofila.spring.bot;

import in.capofila.spring.commons.EmailUtil;

public class EmailSender {
	public EmailSender() {
		// TODO Auto-generated constructor stub
	}
	public static boolean sendEmail(String to, String subject, String body) {
		return EmailUtil.sendEmail(to, subject, body);
	}
	
}
