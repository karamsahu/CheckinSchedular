package in.capofila.spring.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
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
import in.capofila.spring.commons.SchedulerUtils;
import in.capofila.spring.model.CheckinDetails;
import in.capofila.spring.model.ScheduledJobs;

public class CheckinServiceImpl implements CheckinService {
	final SchedulerFactory sf = new StdSchedulerFactory();
	Logger logger = Logger.getLogger(this.getClass());

	Scheduler sched = null;
	{
		try {
			System.out.println("Schedular started...");
			if (sched == null) {
				sched = sf.getScheduler();
				logger.debug("Initialzing Schedular..");
			}
			if (sched.isShutdown() || sched.isInStandbyMode()) {
				sched.start();
				logger.debug("starting scheudar now.s");
			}
		} catch (SchedulerException e) {
			logger.debug(e.getStackTrace());
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
			return "SCHEDULED";
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
						logger.debug("Next Schedule time is"+scheduledTime);
						jobs.setJobName(jobName);
						jobs.setJobGroup(jobGroup);

						jobs.setScheduledTime(scheduledTime.toString());//scheduledTime.toString());
						jobs.setJobTriggerName(trigger.getKey().getName());
						jobs.setJobTriggerGroup(trigger.getKey().getGroup());
						JobDetail details = sched.getJobDetail(jobKey);
						jobs.setJobStatus(checkJobState(trigger));

						JobDataMap jdp = details.getJobDataMap();
						CheckinDetails checkinDetails = (CheckinDetails) jdp.get("checkinDetails");
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
		logger.debug("Create job process satarted. with " + checkinDetails);
		boolean status = true;
		try {
			if (sched.isShutdown() || sched.isInStandbyMode()) {
				sched.start();
				logger.debug("starting scheudar now.s");
			}
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

			TimeZone.setDefault(TimeZone.getTimeZone(checkinDetails.getTimeZone()));

			Date startDate = new Date();

			Calendar cal1 = Calendar.getInstance();
			int year = Integer.parseInt(checkinDetails.getYyyy());
			int month = Integer.parseInt(checkinDetails.getMonth());
			int date = Integer.parseInt(checkinDetails.getDateOfMonth());
			int hh = Integer.parseInt(checkinDetails.getHh());
			int mm = Integer.parseInt(checkinDetails.getMm());
			int ss = Integer.parseInt(checkinDetails.getSs());
			cal1.set(year, month, date, hh, mm, ss);
			
			startDate = cal1.getTime();

			logger.debug("Start date is " + startDate);

			Trigger jobTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, groupName)
					.withSchedule(CronScheduleBuilder
							.cronSchedule(new CronExpression(checkinDetails.getSs() + " " + checkinDetails.getMm() + " "
									+ SchedulerUtils.to24hr(checkinDetails) + " ? * * "))
							.inTimeZone(TimeZone.getTimeZone(checkinDetails.getTimeZone()))
							.withMisfireHandlingInstructionFireAndProceed()).usingJobData(jdp).forJob(jobDetail).build();

			// Listener attached to jobKey
			sched.getListenerManager().addJobListener(new CheckInJobListener(), KeyMatcher.keyEquals(jobKey));
			sched.addJob(jobDetail, false);
			sched.scheduleJob(jobTrigger);
		} catch (Exception e) {
			status = false;
			logger.error(e.getStackTrace());
		} finally {
			logger.debug("Fillay job created");
		}
		return status;
	}

	@Override
	public boolean doCheckin(CheckinDetails details) {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) {
		CheckinServiceImpl csi = new CheckinServiceImpl();
		CheckinDetails checkinDetails = new CheckinDetails();
		checkinDetails.setConfirmationNumber("SFSAX3");
		checkinDetails.setFirstName("Ryan");
		checkinDetails.setLastName("Cortez");
		checkinDetails.setDateOfMonth("06");
		checkinDetails.setMonth("01");
		checkinDetails.setYyyy("2019");
		checkinDetails.setHh("03");
		checkinDetails.setMm("03");
		checkinDetails.setSs("30");
		checkinDetails.setEmail("karamsahu@gmail.com");
		checkinDetails.setTimeZone("EST");
		checkinDetails.setApmpm("AM");
		checkinDetails.setHh(SchedulerUtils.to24hr(checkinDetails));
//		 csi.createJob(checkinDetails);
//		System.out.println(csi.getAllJob().toString());

		Date startDate = new Date();

		TimeZone.setDefault(TimeZone.getTimeZone("EST"));

		Calendar cal1 = Calendar.getInstance(); //
		int year = Integer.parseInt(checkinDetails.getYyyy());
		int month = Integer.parseInt(checkinDetails.getMonth())-1;
		int date = Integer.parseInt(checkinDetails.getDateOfMonth());
		int hh = Integer.parseInt(checkinDetails.getHh());
		int mm = Integer.parseInt(checkinDetails.getMm());
		int ss = Integer.parseInt(checkinDetails.getSs());
		cal1.set(year, month, date, hh, mm, ss);
		startDate = cal1.getTime();

		System.out.println("Start date is " +startDate);

	}

}
