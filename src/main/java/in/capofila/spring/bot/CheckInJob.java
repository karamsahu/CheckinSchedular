package in.capofila.spring.bot;

import javax.ws.rs.core.Response;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import in.capofila.spring.model.CheckinDetails;
public class CheckInJob implements Job{
	private final CheckinDetails checkinDetails;
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		WebRobot rb = new WebRobot();
		try {
			Response submitResult = rb.submittingForm(checkinDetails);
			System.out.println(submitResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public CheckInJob(CheckinDetails checkinDetails) {
		this.checkinDetails= checkinDetails;
	}
}
