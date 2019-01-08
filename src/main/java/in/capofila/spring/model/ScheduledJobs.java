package in.capofila.spring.model;

public class ScheduledJobs {
	// System.out.println("[jobName] : " + jobName + " [groupName] : " + jobGroup +
	// " - " + nextFireTime);
	public ScheduledJobs() {
		// TODO Auto-generated constructor stub
	}
	private String jobName;
	private String jobGroup;
	private String jobTriggerName;
	private String jobStatus;
	private String jobTriggerGroup;
	private String scheduledTime;
	private CheckinDetails checkinDetails;

	
	public void setJobTriggerGroup(String jobTriggerGroup) {
		this.jobTriggerGroup = jobTriggerGroup;
	}
	
	public String getJobTriggerGroup() {
		return jobTriggerGroup;
	}
	
	public void setJobTriggerName(String jobTriggerName) {
		this.jobTriggerName = jobTriggerName;
	}
	
	public String getJobTriggerName() {
		return jobTriggerName;
	}
	
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	
	public String getJobStatus() {
		return jobStatus;
	}
	
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
	
	public CheckinDetails getCheckinDetails() {
		return checkinDetails;
	}
	public void setCheckinDetails(CheckinDetails checkinDetails) {
		this.checkinDetails = checkinDetails;
	}

	@Override
	public String toString() {
		return "ScheduledJobs [jobName=" + jobName + ", jobGroup=" + jobGroup + ", scheduledTime=" + scheduledTime
				+ ", jobStatus=" + jobStatus + ", jobTriggerName=" + jobTriggerName + ", jobTriggerGroup="
				+ jobTriggerGroup + ", checkinDetails=" + checkinDetails + "]";
	}
	
	
}
