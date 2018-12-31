package in.capofila.spring.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import in.capofila.spring.bot.CheckInJob;
import in.capofila.spring.model.CheckinDetails;
import in.capofila.spring.model.ScheduledJobs;

public class CheckinServiceImpl implements CheckinService {
	SchedulerFactory sf = new StdSchedulerFactory();
	Scheduler sched = null;

	@Override
	public boolean doCheckin(CheckinDetails details) {
		// set the check in details in job object field
		new CheckInJob(details);
		try {
			sched = sf.getScheduler();
			// define the job and tie it to our CheckinDetails class
			StringBuilder schdueTimestamp = new StringBuilder();
			schdueTimestamp.append(details.getMonth()).append("/").append(details.getDateOfMonth()).append("/")
					.append(details.getYyyy());

			String triggerIdentity = "Checkin-" + details.getFirstName() + " on " + schdueTimestamp + " at "
					+ details.getHh() + ":" + details.getMm() + ":" + details.getSs();

			JobDetail checkinJob = JobBuilder.newJob(CheckInJob.class)
					.withIdentity(triggerIdentity, "south-west-boarding").build();

			// Second -- Minute -- Hour -- DayOfMonth -- Month -- DayOfWeek -- Year
			// +details.getYyyy()
			CronScheduleBuilder cb = CronScheduleBuilder
					.cronSchedule(new CronExpression(
							details.getSs() + " " + details.getMm() + " " + details.getHh() + " ? * * "))
					.inTimeZone(TimeZone.getTimeZone(details.getTimeZone()));

			final Trigger trigger = TriggerBuilder.newTrigger().withIdentity("CheckIn-Trigger").withSchedule(cb)
					.forJob(checkinJob).build();

			// Tell quartz to schedule the job using our trigger
			sched.scheduleJob(checkinJob, trigger);
			sched.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean isPortalRechable() {

		return false;
	}

	public Boolean isJobPaused(String jobName) throws SchedulerException {

		JobKey jobKey = new JobKey(jobName);
		JobDetail jobDetail = sched.getJobDetail(jobKey);
		List<? extends Trigger> triggers = sched.getTriggersOfJob(jobDetail.getKey());
		for (Trigger trigger : triggers) {
			TriggerState triggerState = sched.getTriggerState(trigger.getKey());
			if (TriggerState.PAUSED.equals(triggerState)) {
				return true;
			}
		}
		return false;
	}

	public boolean cancellJob(JobKey jobName) {
		boolean status;
		try {
			status = sched.deleteJob(jobName);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return status;
	}

	public List<ScheduledJobs> getAllJob() {
		List<ScheduledJobs> schdJobsList = new ArrayList<>();
		ScheduledJobs jobs = new ScheduledJobs();
		try {
			for (String groupName : sched.getJobGroupNames()) {
				for (JobKey jobKey : sched.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					String jobName = jobKey.getName();
					String jobGroup = jobKey.getGroup();
					// get job's trigger
					List<Trigger> triggers = (List<Trigger>) sched.getTriggersOfJob(jobKey);
					Date scheduledTime = triggers.get(0).getNextFireTime();
					jobs.setJobName(jobName);
					jobs.setJobGroup(jobGroup);
					jobs.setScheduledTime(scheduledTime.toString());
					//System.out.println("[jobName] : " + jobName + " [groupName] : " + jobGroup + " - " + nextFireTime);
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return schdJobsList;
	}
}
