package in.capofila.spring.model;

public class ScheduledJobs {
	// System.out.println("[jobName] : " + jobName + " [groupName] : " + jobGroup +
	// " - " + nextFireTime);
	
	private String jobName;
	private String jobGroup;
	private String scheduledTime;
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	public String getScheduledTime() {
		return scheduledTime;
	}
	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
}
