package in.capofila.spring.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import in.capofila.spring.model.CheckinDetails;
import in.capofila.spring.model.ScheduledJobs;

public class DbConnectionService {
	static Logger logger = Logger.getLogger(DbConnectionService.class);
	private static Connection conn = null;

	/**
	 * Connect to a sample database
	 * 
	 * @return
	 */
	private static Connection connect() {
		try {
			// db parameters
			String url = "jdbc:sqlite:application.db";
			// create a connection to the database
			logger.info("Connection to SQLite has been established.");
			return conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public static void createDb() throws SQLException {
		Statement stmt = connect().createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS job_details (\n" + " id integer PRIMARY KEY,\n"
				+ " job_name text NOT NULL,\n" + " capacity real\n" + ");";
		stmt.execute(sql);
	}

	public static void addCheckinDetails(CheckinDetails cd) {

		CheckinDetails cdResult = getJobDetailsByJobName(cd.getJobName());
		logger.info("entry form database" + cdResult.toString());
		SimpleDateFormat dateTimeInGMT = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss aa");
		dateTimeInGMT.setTimeZone(TimeZone.getTimeZone(cd.getTimeZone()));
		String insert = "INSERT OR REPLACE INTO checkin_details(confirmation_number, first_name, last_name, timestamp, application, "
				+ "site, job_name, job_group, trigger_name, trigger_group,"
				+ "job_status, attempt_made, email_status, actual_checkin_time, scheduled_time,email) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		String update = "UPDATE checkin_details SET confirmation_number = ?, " + "first_name = ?, " + "last_name = ?, "
				+ "timestamp = ?, " + "application = ?, "
				+ "site = ?, job_name = ? , job_group = ?, trigger_name = ?, trigger_group = ?,"
				+ "job_status = ?, attempt_made = ?, email_status = ?, actual_checkin_time = ?, scheduled_time = ? ,email = ? "
				+ "WHERE id="+cdResult.getId()+"";
		String sql;
		
		if(cdResult.getId() ==null) {
			sql = insert;
			logger.debug("No exsisting job found for Job Name = "+cd.getJobName());
			logger.debug("Inserting record"+cd.toString());
		}else {
			sql = update;
			logger.debug("One exsisting job found for Job Name = "+cd.getJobName());
			logger.debug("Updating record"+cdResult.toString());
			cd = cdResult;
			logger.debug(sql);
		}
		try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, cd.getConfirmationNumber());
			ps.setString(2, cd.getFirstName());
			ps.setString(3, cd.getLastName());
			ps.setString(4, dateTimeInGMT.format(new Date(System.currentTimeMillis())));
			ps.setString(5, cd.getApplication());
			ps.setString(6, cd.getSite());
			ps.setString(7, cd.getJobName());
			ps.setString(8, cd.getJobGroup());
			ps.setString(9, cd.getTriggerName());
			ps.setString(10, cd.getTriggerGroup());
			ps.setString(11, cd.getJobStatus());
			ps.setString(12, ""); // cd.getAttemptMade().toString());
			ps.setString(13, cd.getEmailStatus());
			ps.setString(14, cd.getActualCheckinTime());
			ps.setString(15, cd.getSheduledTime());
			ps.setString(16, cd.getEmail());
			int result = ps.executeUpdate();

			if (result > 0) {
				logger.debug("saved one scheduled " + cd.toString());
			}
		} catch (SQLException e) {
			logger.error("Exception occured while adding new checkin details", e);
		}
	}

	public static List<CheckinDetails> getCheckinDetails() {
		return null;
	}

	public static void addJobDetails() {

	}

	public static CheckinDetails getJobDetails(CheckinDetails cd) {
		String sql = "select * from checkin_details where job_name = '" + cd.getJobName() + "'";
		CheckinDetails checkinDetails = new CheckinDetails();
		try (Connection conn = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				checkinDetails.setId(rs.getInt("id"));
				checkinDetails.setConfirmationNumber(rs.getString("confirmation_number"));
				checkinDetails.setFirstName(rs.getString("first_name"));
				checkinDetails.setLastName(rs.getString("last_name"));
//				checkinDetails.
				checkinDetails.setSite(rs.getString("site"));
				checkinDetails.setJobName(rs.getString("job_name"));
				checkinDetails.setJobGroup(rs.getString("job_group"));
				checkinDetails.setTriggerName(rs.getString("trigger_name"));
				checkinDetails.setTriggerGroup(rs.getString("trigger_group"));
				checkinDetails.setJobStatus(rs.getString("job_status"));
//				checkinDetails
				checkinDetails.setAttemptMade(rs.getInt("attempt_made"));
				checkinDetails.setEmailStatus(rs.getString("email_status"));
				checkinDetails.setActualCheckinTime(rs.getString("actual_checkin_time"));
				checkinDetails.setSheduledTime(rs.getString("scheduled_time"));
				checkinDetails.setEmail(rs.getString("email"));
			}
		} catch (SQLException e) {
			logger.error("Exception occured while adding new checkin details", e);
		}
		return checkinDetails;
	}

	public static CheckinDetails getJobDetailsByJobName(String jobName) {
		String sql = "select * from checkin_details where job_name = '" + jobName + "'";
		CheckinDetails checkinDetails = new CheckinDetails();
		try (Connection conn = connect();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				checkinDetails.setId(rs.getInt("id"));
				checkinDetails.setConfirmationNumber(rs.getString("confirmation_number"));
				checkinDetails.setFirstName(rs.getString("first_name"));
				checkinDetails.setLastName(rs.getString("last_name"));
//				checkinDetails.
				checkinDetails.setSite(rs.getString("site"));
				checkinDetails.setJobName(rs.getString("job_name"));
				checkinDetails.setJobGroup(rs.getString("job_group"));
				checkinDetails.setTriggerName(rs.getString("trigger_name"));
				checkinDetails.setTriggerGroup(rs.getString("trigger_group"));
				checkinDetails.setJobStatus(rs.getString("job_status"));
//				checkinDetails
				checkinDetails.setAttemptMade(rs.getInt("attempt_made"));
				checkinDetails.setEmailStatus(rs.getString("email_status"));
				checkinDetails.setActualCheckinTime(rs.getString("actual_checkin_time"));
				checkinDetails.setSheduledTime(rs.getString("scheduled_time"));
				checkinDetails.setEmail(rs.getString("email"));
			}
		} catch (SQLException e) {
			logger.error("Exception occured while adding new checkin details", e);
		}
		return checkinDetails;
	}

	public static void main(String[] args) {

	}
}
