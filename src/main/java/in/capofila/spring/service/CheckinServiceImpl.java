package in.capofila.spring.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.KeyMatcher;

import in.capofila.spring.bot.CheckInJob;
import in.capofila.spring.bot.CheckInJobListener;
import in.capofila.spring.model.CheckinDetails;
import in.capofila.spring.model.ScheduledJobs;

public class CheckinServiceImpl implements CheckinService {
	final SchedulerFactory sf = new StdSchedulerFactory();
	Scheduler sched = null;
	{
		try {
			System.out.println("Schedular started...");
			sched = sf.getScheduler();
			sched.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
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

	public String checkJobState(Trigger trigger) throws SchedulerException {
		TriggerState triggerState = sched.getTriggerState(trigger.getKey());
		if (TriggerState.COMPLETE.equals(triggerState)) {
			return "COMPLETED";
		}
		if (TriggerState.BLOCKED.equals(triggerState)) {
			return "BLOCKED";
		}
		if (TriggerState.ERROR.equals(triggerState)) {
			return "ERROR";
		}
		if (TriggerState.NORMAL.equals(triggerState)) {
			return "NORMAL";
		}
		if (TriggerState.PAUSED.equals(triggerState)) {
			return "PAUSED";
		}
		return "NONE";

	}

	public boolean cancellJob(String jobName, String groupName) {
		boolean status = false;
		try {
			JobKey key = JobKey.jobKey(jobName, groupName);
			sched.getJobDetail(key);
			status = sched.deleteJob(key);

		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return status;
	}

	public List<ScheduledJobs> getAllJob() {
		List<ScheduledJobs> schdJobsList = new ArrayList<>();
		try {
			for (String groupName : sched.getJobGroupNames()) {
				for (JobKey jobKey : sched.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					String jobName = jobKey.getName();
					String jobGroup = jobKey.getGroup();
					// get job's trigger
					List<Trigger> triggers = (List<Trigger>) sched.getTriggersOfJob(jobKey);
					for (Trigger trigger : triggers) {
						ScheduledJobs jobs = new ScheduledJobs();
						Date scheduledTime = trigger.getNextFireTime();
						jobs.setJobName(jobName);
						jobs.setJobGroup(jobGroup);
						jobs.setScheduledTime(scheduledTime.toString());
						jobs.setJobTriggerName(trigger.getKey().getName());
						jobs.setJobTriggerGroup(trigger.getKey().getGroup());

						JobDetail details = sched.getJobDetail(jobKey);
						jobs.setJobStatus(checkJobState(trigger));
						JobDataMap jdp = details.getJobDataMap();
						CheckinDetails checkinDetails = (CheckinDetails) jdp.get("checkinDetails");
//						sched.getCurrentlyExecutingJobs()
						jobs.setCheckinDetails(checkinDetails);
						schdJobsList.add(jobs);
					}
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return schdJobsList;
	}

	public boolean createJob(CheckinDetails checkinDetails) {
		boolean status = true;
		try {
			TimeZone.setDefault(TimeZone.getTimeZone(checkinDetails.getTimeZone()));
			String detail = checkinDetails.getConfirmationNumber() + "-" + checkinDetails.getFirstName() + "-"
					+ checkinDetails.getLastName();

			String jobName = "Job-" + detail;
			String groupName = "SOUTHWEST";
			String triggerName = "Trigger-" + detail;

			JobKey jobKey = new JobKey(jobName, groupName);

			JobDataMap jdp = new JobDataMap();
			jdp.put("checkinDetails", checkinDetails);
			jdp.put("jobName", jobName);
			jdp.put("triggerName", triggerName);
			jdp.put("jobGroup", groupName);

			JobDetail jobDetail = JobBuilder.newJob(CheckInJob.class).withIdentity(jobKey).usingJobData(jdp)
					.storeDurably().build();

			Date startDate = new Date();

			Calendar cal1 = Calendar.getInstance();
			cal1.set(Integer.parseInt(checkinDetails.getYyyy()), Integer.parseInt(checkinDetails.getMonth()),
					Integer.parseInt(checkinDetails.getDateOfMonth()));
			//cal1.setTimeZone(TimeZone.getTimeZone(checkinDetails.getTimeZone()));
			startDate = cal1.getTime();

			Trigger jobTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, groupName)
					.withSchedule(CronScheduleBuilder
							.cronSchedule(new CronExpression(checkinDetails.getSs() + " " + checkinDetails.getMm() + " "
									+ checkinDetails.getHh() + " ? * * "))
							.inTimeZone(TimeZone.getTimeZone(checkinDetails.getTimeZone()))
							.withMisfireHandlingInstructionFireAndProceed())
					.startAt(startDate).usingJobData(jdp).forJob(jobDetail).build();

			// Listener attached to jobKey
			sched.getListenerManager().addJobListener(new CheckInJobListener(), KeyMatcher.keyEquals(jobKey));
			/*
			 * System.out.println("Added Job with Key"+jobDetail.getKey().getName());
			 * System.out.println("and Group "+jobDetail.getKey().getGroup());
			 * System.out.println("Added Trigger Key"+jobTrigger.getKey().getName());
			 * System.out.println("and group "+jobTrigger.getKey().getGroup());
			 */
			sched.addJob(jobDetail, false);
			sched.scheduleJob(jobTrigger);
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		} finally {
			return status;
		}
	}

	@Override
	public boolean doCheckin(CheckinDetails details) {
		// TODO Auto-generated method stub
		return false;
	}
}
