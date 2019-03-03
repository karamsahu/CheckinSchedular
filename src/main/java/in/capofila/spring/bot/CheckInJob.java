package in.capofila.spring.bot;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import in.capofila.spring.model.CheckinDetails;

public class CheckInJob implements Job {
	Logger logger = Logger.getLogger(CheckInJob.class);
	public CheckInJob() {
		//let it be here
	}
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDetail job = context.getJobDetail();
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		CheckinDetails checkinDetails = (CheckinDetails) dataMap.get("checkinDetails");
		WebRobot rb = new WebRobot();
		try {
			CheckinDetails details = rb.submittingFormV2(context);
			logger.debug("Result : "+details);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("Job execution completed for job "+job.getKey()+" with following details "+checkinDetails.toString());
	}
}
