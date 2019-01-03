package in.capofila.spring.bot;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import in.capofila.spring.service.MailerService;

public class EmailSender {

	public static void sendEmail(String to, String subject, String body) {
		String crunchifyConfFile = "checkinscheduler-bean.xml";
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(crunchifyConfFile);

		MailerService crunchifyEmailAPI = (MailerService) context.getBean("checkinSchedularEmailService");
		String toAddr = "karansahu@gmail.com";

		crunchifyEmailAPI.sendMail(toAddr, subject, body);
	}

	public static void sendMimeEmail(String to, String subject, String body) {
		String crunchifyConfFile = "checkinscheduler-bean.xml";
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(crunchifyConfFile);

		MailerService crunchifyEmailAPI = (MailerService) context.getBean("checkinSchedularEmailService");
		String toAddr = "karansahu@gmail.com";

		crunchifyEmailAPI.sendMimeMail(toAddr, subject, body);
	}
}
