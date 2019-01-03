package in.capofila.spring.bot;

import javax.ws.rs.core.Response;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import in.capofila.spring.commons.SchedulerUtils;
import in.capofila.spring.model.CheckinDetails;

public class CheckInJobListener implements JobListener {

	public static final String LISTENER_NAME = "SouthWestCheckinJobListner";

	@Override
	public String getName() {
		return LISTENER_NAME;
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		String jobName = context.getJobDetail().getKey().toString();
		System.out.println("Job : " + jobName + " is going to start...");
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		JobDetail jobDetails = context.getJobDetail();
		String jobName = jobDetails.getKey().toString();
		CheckinDetails details = (CheckinDetails)jobDetails.getJobDataMap().get("checkinDetails");
		Response res = (Response) context.getResult();
		System.out.println("Job : " + jobName + " is finished...");
		EmailSender.sendMimeEmail(details.getEmail(), "FLIGHT CHECKIN STATUS", SchedulerUtils.emailFormatter(details));
		
		if (!jobException.getMessage().equals("")) {
			System.out.println("Exception thrown by: " + jobName + " Exception: " + jobException.getMessage());
		}

	}

}
