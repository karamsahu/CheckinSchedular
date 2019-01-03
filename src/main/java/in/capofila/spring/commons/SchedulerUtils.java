package in.capofila.spring.commons;

import in.capofila.spring.model.CheckinDetails;

public class SchedulerUtils {
	public static String emailFormatter(CheckinDetails details) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body><table style='border:1px solid black'>");
		sb.append("<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Confiramtion No</th><td>");
		sb.append(details.getConfirmationNumber());
		sb.append("</td></tr>");

		sb.append("<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>FirstName</th><td>");
		sb.append(details.getFirstName());
		sb.append("</td></tr>");

		sb.append("<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>LastName</th><td>");
		sb.append(details.getLastName());
		sb.append("</td></tr>");

		sb.append("<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Email</th><td>");
		sb.append(details.getEmail());
		sb.append("</td></tr>");

		sb.append("<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Checkin Date</th><td>");
		sb.append(details.getDateOfMonth()).append("/").append(details.getMonth()).append("/")
				.append(details.getYyyy());
		sb.append("</td></tr>");

		sb.append("<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Checkin Time</th><td>");
		sb.append(details.getHh()).append(":").append(details.getMm()).append(":").append(details.getSs()).append(" ")
				.append(details.getApmpm());
		sb.append("</td></tr>");

		sb.append("<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Status</th><td>");
		sb.append(details.getJobStatus());
		sb.append("</td></tr>");

		sb.append("<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Attempt Count</th><td>");
		sb.append("");
		sb.append("</td></tr>");
		sb.append("</table></body></html>");

		return sb.toString();
	}
}
