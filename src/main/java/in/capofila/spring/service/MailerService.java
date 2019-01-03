package in.capofila.spring.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("checkinSchedularEmailService")
public class MailerService {
//	@Autowired
	private JavaMailSenderImpl  mailSender; // MailSender interface defines a strategy
										// for sending simple mails

	public void sendMail(String toAddress, String subject, String msgBody) {

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(toAddress);
		email.setSubject(subject);
		email.setText(msgBody);
		mailSender.send(email);
	}
	
	public void  sendMimeMail( String to, String subject, String msg) {
        try {

            MimeMessage message = mailSender.createMimeMessage();

            message.setSubject(subject);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress("Flight Checkin <info@capofila.in>"));
            helper.setTo(to);
            helper.setText(msg, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            Logger.getLogger(MailerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
