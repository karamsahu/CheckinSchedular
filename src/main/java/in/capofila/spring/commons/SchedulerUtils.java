package in.capofila.spring.commons;

import java.util.Date;

import in.capofila.spring.model.CheckinDetails;

public class SchedulerUtils {
	public SchedulerUtils() {
		// TODO Auto-generated constructor stub
	}
	public static String emailFormatter(CheckinDetails details) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body><table style='border:1px solid black'>");
		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Confiramtion No</th><td>");
		sb.append(details.getConfirmationNumber());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>FirstName</th><td>");
		sb.append(details.getFirstName());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>LastName</th><td>");
		sb.append(details.getLastName());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Email</th><td>");
		sb.append(details.getEmail());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Checkin Date</th><td>");
		sb.append(details.getDateOfMonth()).append("/").append(details.getMonth()).append("/")
				.append(details.getYyyy());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Checkin Time</th><td>");
		sb.append(details.getHh()).append(":").append(details.getMm()).append(":").append(details.getSs()).append(" ")
				.append(details.getApmpm());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Check-in Status</th><td>");
		sb.append(details.getJobStatus());
		sb.append("</td></tr>");
		
		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Schedular Status</th><td>");
		sb.append(details.getSchedularStatus());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Attempt Count</th><td>");
		sb.append(details.getAttemptMade());
		sb.append("</td></tr>");
		
		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>EMail Subscribed ?</th><td>");
		sb.append(details.getEmailStatus());
		sb.append("</td></tr>");
		
		sb.append("</table></body></html>");

		return sb.toString();
	}

	public static String emailFormatterVerbose(CheckinDetails details, Integer refireCount, Date actualFireTime, Long jobExecutionDuration, Date plannedJobExecutionTime) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body><table style='border:1px solid black'>");
		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Confiramtion No</th><td>");
		sb.append(details.getConfirmationNumber());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>FirstName</th><td>");
		sb.append(details.getFirstName());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>LastName</th><td>");
		sb.append(details.getLastName());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Email</th><td>");
		sb.append(details.getEmail());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Checkin Date</th><td>");
		sb.append(details.getDateOfMonth()).append("/").append(details.getMonth()).append("/")
				.append(details.getYyyy());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Checkin Time</th><td>");
		sb.append(details.getHh()).append(":").append(details.getMm()).append(":").append(details.getSs()).append(" ")
				.append(details.getApmpm());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Status</th><td>");
		sb.append(details.getJobStatus());
		sb.append("</td></tr>");

		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Attempt Count</th><td>");
		sb.append(refireCount);
		sb.append("</td></tr>");
		
		sb.append(
				"<tr><th style='bgcolor:#DCDCDC; border: 1px solid black;padding: 5px;text-align: left;'>Actual Checkin time </th><td>");
		sb.append(actualFireTime);
		sb.append("</td></tr>");
		
		
		
		sb.append("</table></body></html>");

		return sb.toString();
	}

	
	public static String to24hr(CheckinDetails details) {
		String hr = details.getHh();
		if (details.getApmpm().equalsIgnoreCase("pm")) {

			if (hr.equals("01")) {
				return hr = "13";
			}
			if (hr.equals("02")) {
				return hr = "14";
			}
			if (hr.equals("03")) {
				return hr = "15";
			}
			if (hr.equals("04")) {
				return hr = "16";
			}
			if (hr.equals("05")) {
				return hr = "17";
			}
			if (hr.equals("06")) {
				return hr = "18";
			}
			if (hr.equals("07")) {
				return hr = "19";
			}
			if (hr.equals("08")) {
				return hr = "20";
			}
			if (hr.equals("09")) {
				return hr = "21";
			}
			if (hr.equals("10")) {
				return hr = "22";
			}
			if (hr.equals("11")) {
				return hr = "23";
			}
		}
		if (details.getApmpm().equalsIgnoreCase("am")) {
			if (hr.equals("12")) {
				return hr = "00";
			}
		}
		return hr;
	}
}
