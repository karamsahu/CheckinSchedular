package in.capofila.spring.commons;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class EmailUtil {
	static Logger logger = Logger.getLogger(EmailUtil.class);

	/**
	 * Utility method to send simple HTML email
	 * 
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public EmailUtil() {
		// TODO Auto-generated constructor stub
	}
	public static boolean sendEmail(String toEmail, String subject, String body) {
		logger.debug("Send mail invoked....");
		boolean status = true;
		try {
			MimeMessage msg = new MimeMessage(getSession());
			// set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("flightcheckinbot@gmail.com", "Flight Check-INs"));

			msg.setReplyTo(InternetAddress.parse("flightcheckinbot@gmail.com", false));

			msg.setSubject(subject, "UTF-8");

			msg.setContent(body, "text/html; charset=utf-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("karamsahu@gmail.com", false));
			msg.saveChanges();
			logger.debug("Message is ready");
			Transport.send(msg);
			logger.debug("EMail Sent Successfully!!");
		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			return status;
		}
		return status;
	}

	/**
	 * Utility method to send email with attachment
	 * 
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendAttachmentEmail(String toEmail, String subject, String body) {
		try {
			MimeMessage msg = new MimeMessage(getSession());
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("flightcheckinbot@gmail.com", "Flight Check-INs"));

			msg.setReplyTo(InternetAddress.parse("flightcheckinbot@gmail.com", false));

			msg.setSubject(subject, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

			// Create the message body part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText(body);

			// Create a multipart message for attachment
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Second part is attachment
			messageBodyPart = new MimeBodyPart();
			String filename = "abc.txt";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			msg.setContent(multipart);

			// Send message
			Transport.send(msg);
			System.out.println("EMail Sent Successfully with attachment!!");
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Utility method to send image in email body
	 * 
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendImageEmail(String toEmail, String subject, String body) {
		try {
			MimeMessage msg = new MimeMessage(getSession());
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(new InternetAddress("flightcheckinbot@gmail.com", "Flight Check-INs"));

			msg.setReplyTo(InternetAddress.parse("flightcheckinbot@gmail.com", false));

			msg.setSubject(subject, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

			// Create the message body part
			BodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setText(body);

			// Create a multipart message for attachment
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Second part is image attachment
			messageBodyPart = new MimeBodyPart();
			String filename = "checkin.jpg";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
//			 Trick is to add the content-id header here
			messageBodyPart.setHeader("Content-ID", "image_id");
			multipart.addBodyPart(messageBodyPart);

			// third part for displaying image in the email body
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent("<h1>Attached Image</h1>" + "<img src='cid:image_id'>", "text/html");
			messageBodyPart.setContent(body, "text/html");

			multipart.addBodyPart(messageBodyPart);

			// Set the multipart message to the email message
			msg.setContent(multipart);

			// Send message
			Transport.send(msg);
			System.out.println("EMail Sent Successfully with image!!");
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	static Session session;

	public static Session getSession() {
		if (session == null) {
			session = createSession();
		}
		return session;
	}

	private static Session createSession() {
		final String fromEmail = "flightcheckinbot@gmail.com"; // requires valid gmail id
		final String password = "welcome2019"; // correct password for gmail id

		System.out.println("TLSEmail Start");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		props.put("mail.smtp.port", "587"); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

		// create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		return Session.getInstance(props, auth);
		// EmailUtil.sendEmail(session, toEmail, "TLSEmail Testing Subject", "TLSEmail
		// Testing Body");

	}
}
