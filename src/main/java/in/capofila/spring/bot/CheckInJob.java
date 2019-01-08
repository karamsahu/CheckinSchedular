package in.capofila.spring.bot;

import javax.ws.rs.core.Response;

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
		String startTime = job.getJobDataMap().getString("startDate");

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		CheckinDetails checkinDetails = (CheckinDetails) dataMap.get("checkinDetails");
		WebRobot rb = new WebRobot();
		try {
			Response submitResult = rb.submittingForm(checkinDetails,startTime);
//			String result = parseResponse(submitResult);
//			context.setResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("Job executed completed for job "+job.getKey()+"with following details "+checkinDetails.toString());
	}

	private String parseResponse(Response submitResult) {
		if (submitResult.getStatus() == 200) {
			return submitResult.getEntity().toString();
		}

		if (submitResult.getStatus() == 400) {
			return submitResult.getEntity().toString();
		}
		if (submitResult.getStatus() == 500) {
			return submitResult.getEntity().toString();
		}

		return submitResult.getEntity().toString();
	}

}
