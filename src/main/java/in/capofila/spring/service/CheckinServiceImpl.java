package in.capofila.spring.service;

import java.text.ParseException;
import java.util.TimeZone;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import in.capofila.spring.bot.CheckInJob;
import in.capofila.spring.model.CheckinDetails;

public class CheckinServiceImpl implements CheckinService {

	@Override
	public boolean doCheckin(CheckinDetails details) {
		// set the checkin details in job obect field
		new CheckInJob(details);

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = null;
		try {
			sched = sf.getScheduler();
			// define the job and tie it to our CheckinDetails class
			JobDetail checkinJob = JobBuilder.newJob(CheckInJob.class).withIdentity("checkin", "south-west-boarding")
					.build();

			// Second -- Minute -- Hour -- DayOfMonth -- Month -- DayOfWeek -- Year +details.getYyyy()
			CronScheduleBuilder cb = CronScheduleBuilder
					.cronSchedule(new CronExpression(
							details.getSs() + " " + details.getMm() + " " + details.getHh() + " ? * * "))
					.inTimeZone(TimeZone.getTimeZone(details.getTimeZone()));

			final Trigger trigger = TriggerBuilder.newTrigger().withIdentity("Trigger-checkin").withSchedule(cb)
					.forJob(checkinJob).build();

			// Tell quartz to schedule the job using our trigger
			sched.scheduleJob(checkinJob, trigger);
			sched.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean isPortalRechable() {

		return false;
	}

}
