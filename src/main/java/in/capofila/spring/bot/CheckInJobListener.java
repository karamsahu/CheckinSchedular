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
			Trigger trigger = context.getTrigger();
			TriggerState triggerState = context.getScheduler().getTriggerState(trigger.getKey());
			if (triggerState.equals(TriggerState.NORMAL)) {
				ScheduledJobs jobs = new ScheduledJobs();
				jobs.setJobStatus(CheckinConsts.SCHEDULED);
				jobs.setJobName(jobName);
				jobs.setJobGroup(jobDetails.getKey().getGroup());
				jobs.setJobTriggerName(trigger.getKey().getName());
				jobs.setJobTriggerGroup(trigger.getKey().getGroup());
				context.setResult(jobs);
			}

			int refireCount = context.getRefireCount();
			Date actualFireTime = context.getFireTime();
			long jobExecutionDuration = context.getJobRunTime();
			Date plannedJobExecutionTime = context.getScheduledFireTime();

			logger.info("Job execution details " + checkinDetails + refireCount + actualFireTime + jobExecutionDuration
					+ plannedJobExecutionTime);
			/*
			 * EmailUtil.sendEmail(checkinDetails.getEmail(),
			 * "Flight Checkin Status Details",
			 * SchedulerUtils.emailFormatterVerbose(checkinDetails, refireCount,
			 * actualFireTime, jobExecutionDuration, plannedJobExecutionTime));
			 */
		} catch (Exception e) {
			logger.error(e);
		}

	}

}
