package in.capofila.spring.test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import in.capofila.spring.service.MailerService;

public class ApplicationTest {
//	public static void main(String[] args) {
//		TimeZone.setDefault(TimeZone.getTimeZone("EST"));
//		System.out.println(new Date(System.currentTimeMillis()));
//	}
	
	public static void main(String args[]) {
		 
		// Spring Bean file you specified in /src/main/resources folder
		String crunchifyConfFile = "checkinscheduler-bean.xml";
		ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(crunchifyConfFile);
 
		// @Service("crunchifyEmail") <-- same annotation you specified in CrunchifyEmailAPI.java
		MailerService crunchifyEmailAPI = (MailerService) context.getBean("checkinSchedularEmailService");
		String toAddr = "karansahu@gmail.com";
 
		// email subject
		String subject = "Hey.. This email sent by Flight Checkin Scheduler";
 
		// email body
		String body = "There you go.. You got an email.. Let's understand details on how Spring MVC works -- By Crunchify Admin";
		crunchifyEmailAPI.sendMail(toAddr, subject, body);
	}
	
	
}
