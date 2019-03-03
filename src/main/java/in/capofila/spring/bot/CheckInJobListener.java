package in.capofila.spring.bot;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import in.capofila.spring.commons.CheckinConsts;
import in.capofila.spring.commons.SchedulerUtils;
import in.capofila.spring.model.CheckinDetails;
import in.capofila.spring.service.DbConnectionService;

public class CheckInJobListener implements JobListener {
	public static final String LISTENER_NAME = "SouthWestCheckinJobListner";
	Logger logger = Logger.getLogger(this.getClass());

	public CheckInJobListener() {
		// TODO Auto-generated constructor stub
	}

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
		String jobName = context.getJobDetail().getKey().toString();
		System.out.println("Job : " + jobName + " is vetoed...");

	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		try {
			JobDetail jobDetails = context.getJobDetail();
			String jobName = jobDetails.getKey().toString();	
			CheckinDetails checkinDetails = (CheckinDetails) jobDetails.getJobDataMap().get("checkinDetails");
			
			logger.debug("Job : " + jobName + " is finished..." + context.getRefireCount());
			
			Date actualFireTime = context.getFireTime();
			checkinDetails.setActualCheckinTime(actualFireTime.toString());
			checkinDetails.setSchedularStatus(CheckinConsts.COMPLETED);
			DbConnectionService.addCheckinDetails(checkinDetails); //this is to update the checkin details with status
			EmailSender.sendEmail(checkinDetails.getEmail(), "FINAL STATUS of "+checkinDetails.getConfirmationNumber(), SchedulerUtils.emailFormatter(checkinDetails));
			logger.debug("deleteing finished job"+jobDetails);
			context.getScheduler().deleteJob(jobDetails.getKey());
		} catch (Exception e) {
			logger.error(e);
		}

	}

}
