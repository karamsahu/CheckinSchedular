package in.capofila.spring.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
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
import org.quartz.impl.matchers.KeyMatcher;

import in.capofila.spring.bot.CheckInJob;
import in.capofila.spring.bot.CheckInJobListener;
import in.capofila.spring.bot.EmailSender;
import in.capofila.spring.commons.CheckinConsts;
import in.capofila.spring.commons.SchedulerUtils;
import in.capofila.spring.model.CheckinDetails;

public class CheckinServiceImpl implements CheckinService {
	final SchedulerFactory sf = new StdSchedulerFactory();
	Logger logger = Logger.getLogger(this.getClass());

	public CheckinServiceImpl() {
		// TODO Auto-generated constructor stub
	}

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
			DbConnectionService.deleteCheckinDetails(key.getName());
			status = sched.deleteJob(key);

		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return status;
	}

	/*public List<CheckinDetails> getAllJob() {
		List<CheckinDetails> schdChekcins = new ArrayList<>();
		try {
			for (String groupName : sched.getJobGroupNames()) {
				for (JobKey jobKey : sched.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					String jobName = jobKey.getName();
					String jobGroup = jobKey.getGroup();
					// get job's trigger
					List<Trigger> triggers = (List<Trigger>) sched.getTriggersOfJob(jobKey);
					for (Trigger trigger : triggers) {
						Date scheduledTime = trigger.getNextFireTime();
						JobDetail details = sched.getJobDetail(jobKey);
						JobDataMap jdp = details.getJobDataMap();
						CheckinDetails checkinDetails = (CheckinDetails) jdp.get("checkinDetails");
						
						CheckinDetails cd = new CheckinDetails();
						cd.setJobName(jobName);
						cd.setJobGroup(jobGroup);
						cd.setTriggerName(trigger.getKey().getName());
						cd.setTriggerGroup(trigger.getKey().getGroup());
						cd.setJobStatus(checkJobState(trigger));						
						cd.setSheduledTime(scheduledTime.toString());
						cd.setFirstName(checkinDetails.getFirstName());
						cd.setLastName(checkinDetails.getLastName());
						schdChekcins.add(cd);
						
					}
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return schdChekcins;
	}*/

	public boolean createJob(CheckinDetails checkinDetails) {
		logger.debug("Create job process satarted. with " + checkinDetails);
		boolean status = true;
		try {
			if (sched.isShutdown() || sched.isInStandbyMode()) {
				sched.start();
				logger.debug("Schedular is booting..");
			}
			String detail = checkinDetails.getConfirmationNumber() + "-" + checkinDetails.getFirstName() + "-"
					+ checkinDetails.getLastName();

			String jobName = "Job-" + detail;
			String groupName = "SOUTHWEST";
			String triggerName = "Trigger-" + detail;

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
			
			//adding more values to checkin data
			checkinDetails.setJobName(jobName);
			checkinDetails.setJobGroup(groupName);
			checkinDetails.setTriggerName(triggerName);
			checkinDetails.setTriggerGroup(groupName);
			checkinDetails.setSheduledTime(startDate.toString());
			
			JobKey jobKey = new JobKey(jobName, groupName);
			JobDataMap jdp = new JobDataMap();
			jdp.put("checkinDetails", checkinDetails);
			
			JobDetail jobDetail = JobBuilder.newJob(CheckInJob.class).withIdentity(jobKey).usingJobData(jdp)
					.storeDurably().build();

			
			logger.debug("Start date is " + startDate);

			
			  Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName,
			  groupName) .withSchedule(CronScheduleBuilder .cronSchedule(new
			  CronExpression(checkinDetails.getSs() + " " + checkinDetails.getMm() + " " +
			  SchedulerUtils.to24hr(checkinDetails) + " ? * * "))
			  .inTimeZone(TimeZone.getTimeZone(checkinDetails.getTimeZone()))
			  .withMisfireHandlingInstructionFireAndProceed()).usingJobData(jdp).forJob(
			  jobDetail).build();
			 
			// Listener attached to jobKey
			sched.getListenerManager().addJobListener(new CheckInJobListener(), KeyMatcher.keyEquals(jobKey));

			//sched.addJob(jobDetail, true);
			Date d = sched.scheduleJob(jobDetail, trigger);
			logger.debug(d.toString());
			checkinDetails.setJobStatus(CheckinConsts.SCHEDULED);
			checkinDetails.setSchedularStatus(CheckinConsts.CREATED);
			
			logger.info("New Job scheduled with details"+checkinDetails.toString());
			//store database in db
			DbConnectionService.addCheckinDetails(checkinDetails);
			//notify user about job creation
			logger.info("Notifying clients of job creations");
			EmailSender.sendEmail(checkinDetails.getEmail(), checkinDetails.getJobStatus(), SchedulerUtils.emailFormatter(checkinDetails));
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
	
	public List<CheckinDetails> getAllJobDetails(){
		return DbConnectionService.getCheckinDetails();
	}

	public static void main(String[] args) {
		CheckinServiceImpl csi = new CheckinServiceImpl();
		CheckinDetails checkinDetails = new CheckinDetails();
		checkinDetails.setConfirmationNumber("SFSAX2");
		checkinDetails.setFirstName("Ryan");
		checkinDetails.setLastName("Cortez");
		checkinDetails.setDateOfMonth("08");
		checkinDetails.setMonth("01");
		checkinDetails.setYyyy("2019");
		checkinDetails.setHh("09");
		checkinDetails.setMm("26");
		checkinDetails.setSs("00");
		checkinDetails.setEmail("karamsahu@gmail.com");
		checkinDetails.setTimeZone("IST");
		checkinDetails.setApmpm("PM");
		checkinDetails.setHh(SchedulerUtils.to24hr(checkinDetails));
		csi.createJob(checkinDetails);
//		System.out.println(csi.getAllJob().toString());

		/*
		 * Date startDate = new Date();
		 * 
		 * TimeZone.setDefault(TimeZone.getTimeZone("EST"));
		 * 
		 * Calendar cal1 = Calendar.getInstance(); // int year =
		 * Integer.parseInt(checkinDetails.getYyyy()); int month =
		 * Integer.parseInt(checkinDetails.getMonth())-1; int date =
		 * Integer.parseInt(checkinDetails.getDateOfMonth()); int hh =
		 * Integer.parseInt(checkinDetails.getHh()); int mm =
		 * Integer.parseInt(checkinDetails.getMm()); int ss =
		 * Integer.parseInt(checkinDetails.getSs()); cal1.set(year, month, date, hh, mm,
		 * ss); startDate = cal1.getTime();
		 * 
		 * System.out.println("Start date is " +startDate);
		 */
	}
	
	
}
