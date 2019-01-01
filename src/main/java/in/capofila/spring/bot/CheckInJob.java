package in.capofila.spring.bot;

import javax.ws.rs.core.Response;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import in.capofila.spring.model.CheckinDetails;

public class CheckInJob implements Job {

	public CheckInJob() {
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		//JobKey key = context.getJobDetail().getKey();

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		CheckinDetails checkinDetails = (CheckinDetails) dataMap.get("checkinDetails");
		WebRobot rb = new WebRobot();
		try {
			Response submitResult = rb.submittingForm(checkinDetails);
			String result = parseResponse(submitResult);
			context.setResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//System.out.println("Executing job : " + checkinDetails.toString());
	}

	private String parseResponse(Response submitResult) {
		if(submitResult.getStatus()==200) {
			return submitResult.getEntity().toString();
		}
		
		if(submitResult.getStatus()==400) {
			return submitResult.getEntity().toString();
		}
		if(submitResult.getStatus()==500) {
			return submitResult.getEntity().toString();
		}
		
		return submitResult.getEntity().toString();
	}
		
}
