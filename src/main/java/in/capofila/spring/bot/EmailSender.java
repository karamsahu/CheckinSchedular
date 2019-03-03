package in.capofila.spring.bot;

import in.capofila.spring.commons.EmailUtil;

public class EmailSender {
	public static boolean sendEmail(String to, String subject, String body) {
		return EmailUtil.sendEmail(to, subject, body);
	}
}
