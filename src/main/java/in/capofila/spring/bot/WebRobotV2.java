package in.capofila.spring.bot;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import in.capofila.spring.commons.CheckinConsts;
import in.capofila.spring.model.CheckinDetails;

public class WebRobotV2 {
	static Logger logger = Logger.getLogger(WebRobotV2.class);
	
	public static void main(String[] args) {
		CheckinDetails dd = new CheckinDetails();
		dd.setFirstName("Ryan");
		dd.setLastName("Coretz");
		dd.setEmail("karamsahu@gmail.com");
		dd.setConfirmationNumber("RU66D7");
		CheckinDetails d = doCheckIn(dd);
		System.out.println(d.toString());
	}
	
	// browser simulation
	public static CheckinDetails doCheckIn(CheckinDetails details) {
		logger.info("Initializing websimulator..");
//		String chromeDriverPath = "C:\\Users\\Karam\\Documents\\driver\\chromedriver.exe";
		String chromeDriverPath = "/var/lib/chromedriver";
		
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors");
		WebDriver driver = new ChromeDriver(options);
		return doActualWork(driver, details);
	}

	private static CheckinDetails doActualWork(WebDriver driver, CheckinDetails details) {
		String firstName = details.getFirstName();
		String lastName = details.getLastName();
		String confirmationNumber = details.getConfirmationNumber();
		String email = details.getEmail();
		logger.info("Actual checkin call invoked..");
		details.setJobStatus(CheckinConsts.PENDING);
		details.setEmailStatus(CheckinConsts.SUBSCRIBED_NO);

		// Get the login page
		driver.get("https://www.southwest.com/air/check-in/index.html?clk=GSUBNAV-CHCKIN");

		try {
			// Search for username / password input and fill the inputs
			driver.findElement(By.xpath("//input[@id='confirmationNumber']")).sendKeys(confirmationNumber);
		} catch (Exception e) {
			logger.error("Error ouccured while searching confirmationnumber text box" + e.getMessage());
			return details;
		}

		try {
			driver.findElement(By.xpath("//input[@id='passengerFirstName']")).sendKeys(firstName);
		} catch (Exception e) {
			logger.error("Error ouccured while searching confirmationnumber text box" + e.getMessage());
			return details;
		}

		try {
			driver.findElement(By.xpath("//input[@id='passengerLastName']")).sendKeys(lastName);
		} catch (Exception e) {
			logger.error("Error ouccured while searching passengerLastName text box" + e.getMessage());
			return details;
		}

		// Locate the login button and click on it
		try {
			driver.findElement(By.xpath("//*[@id=\"form-mixin--submit-button\"]")).click();
		} catch (Exception e) {
			logger.error("Error ouccured while searching checkin button" + e.getMessage());
			return details;
		}

		try {
			Thread.sleep(1000 * 2);
			logger.debug("wating for 2 second before next step");
		} catch (InterruptedException e) {
			logger.error("error occured while trying to add pause for 2 minutes");
		}

		if (driver.getCurrentUrl().equals("https://www.southwest.com/air/check-in/review.html?confirmationNumber="
				+ confirmationNumber + "&passengerFirstName=" + firstName + "&passengerLastName=" + lastName + "")) {
			logger.info("We are able to successfully checkin in step 1");

			logger.info("Checkin login success at step 1");
			try {
				driver.findElement(By.xpath("//*[@id=\"swa-content\"]/div/div[2]/div/section/div/div/div[3]/button"))
						.click();
				details.setJobStatus(CheckinConsts.CONFIRMED);
			} catch (Exception e) {
				logger.error("Error occured while searching ");
			}
			try {
				Thread.sleep(1000 * 2);
				logger.debug("wating for 2 second before next step");
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error("error occured while trying to add pause for 2 minutes");
			}

			// click email button to bring popup
			try {
				driver.findElement(By.xpath(
						"//*[@id=\"swa-content\"]/div/div[2]/div/section/div/div/section/table/tbody/tr/td[2]/button"))
						.click();
			} catch (Exception e) {
				logger.error("error occured trying to bring email input popup box");
			}

			try {
				Thread.sleep(1000 * 2);
			} catch (InterruptedException e) {
				logger.error("error occured while trying to add pause for 2 minutes");
			}

			// input email inside popup
			logger.info("sending email to input fields");
			try {
				driver.findElement(By.xpath("//*[@id='emailBoardingPass']")).sendKeys(email);
			} catch (Exception e) {
				logger.error("error occured trying to input email in input popup box");
			}

			// click send on pop up
			try {
				driver.findElement(By.xpath("//*[@id=\"form-mixin--submit-button\"]")).click();
				details.setEmailStatus(CheckinConsts.SUBSCRIBED_YES);
			} catch (Exception e) {
				logger.error("error occured trying to click email button in popup box");
			}
			logger.info("clicking send boarding pass button");
		}
		driver.quit();
		return details;
	}
}
