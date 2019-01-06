package in.capofila.spring.test;

import in.capofila.spring.bot.EmailSender;

public class ApplicationTest {
//	public static void main(String[] args) {
//		TimeZone.setDefault(TimeZone.getTimeZone("EST"));
//		System.out.println(new Date(System.currentTimeMillis()));
//	}
	
	public static void main(String args[]) {
	EmailSender.sendEmail("karamsahu@gmail.com", "ApplicationTestMail", "<h1>this is schedular Applicaiton test mail</h1>");

		
	}
	
	
}
