package in.capofila.spring.bot;

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

public class Applicaiton {
	public static void main(String[] args)  {

		SchedulerFactory sf = new StdSchedulerFactory();

		Scheduler sched = null;
		try {
			sched = sf.getScheduler();
			// define the job and tie it to our CheckinDetails class
			JobDetail checkinJob = JobBuilder.newJob(CheckInJob.class).withIdentity("checkin", "south-west-boarding").build();
			
			// Second -- Minute --  Hour --  DayOfMonth -- Month -- DayOfWeek -- Year	
			CronScheduleBuilder cb = CronScheduleBuilder.cronSchedule(new CronExpression("25 32 08 ? * *"))
					.inTimeZone(TimeZone.getTimeZone("EST"));

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

	}
}
