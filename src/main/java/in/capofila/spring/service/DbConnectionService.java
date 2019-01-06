package in.capofila.spring.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
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
		String sql = "INSERT INTO checkin_details(name,capacity) VALUES(?,?)";
		try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
			

			// set the corresponding param
			ps.setString(1, cd.getConfirmationNumber());
			ps.setString(2, cd.getFirstName());
			ps.setString(3, cd.getLastName());
			ps.setString(5, cd.getLastName());
			ps.setString(6, cd.getLastName());
			
			// update
			//pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static List<CheckinDetails> getCheckinDetails() {
		return null;
	}

	public static void addJobDetails() {

	}

	public static List<ScheduledJobs> getJobDetails() {
		return null;
	}

	public static void main(String[] args) {

	}
}
