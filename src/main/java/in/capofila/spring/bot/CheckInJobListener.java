package in.capofila.spring.bot;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

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
		String jobName = context.getJobDetail().getKey().toString();
		System.out.println("Job : " + jobName + " is finished...");
		if (!jobException.getMessage().equals("")) {
			System.out.println("Exception thrown by: " + jobName + " Exception: " + jobException.getMessage());
		}

	}

}
