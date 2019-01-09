package in.capofila.spring.bot;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;

import in.capofila.spring.commons.CheckinConsts;
import in.capofila.spring.model.CheckinDetails;
import in.capofila.spring.model.ScheduledJobs;
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
			checkinDetails.setJobStatus(CheckinConsts.COMPLETED);
			DbConnectionService.addCheckinDetails(checkinDetails);
		} catch (Exception e) {
			logger.error(e);
		}

	}

}
