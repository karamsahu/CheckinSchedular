package in.capofila.spring.model;

public class CheckinRequestEntity {
	// StringEntity("{\"confirmationNumber\":\"asa232\",\"passengerFirstName\":
	// \"asdf\",\"passengerLastName\":\"asdf\",\"application\":\"air-check-in\",\"site\":\"southwest\"}");

	private String confirmationNumber;
	private String passengerFirstName;
	private String passengerLastName;
	private final String application = "air-check-in";
	private final String site = "southwest";

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public String getPassengerFirstName() {
		return passengerFirstName;
	}

	public void setPassengerFirstName(String passengerFirstName) {
		this.passengerFirstName = passengerFirstName;
	}

	public String getPassengerLastName() {
		return passengerLastName;
	}

	public void setPassengerLastName(String passengerLastName) {
		this.passengerLastName = passengerLastName;
	}

	public String getApplication() {
		return application;
	}

	public String getSite() {
		return site;
	}

	@Override
	public String toString() {
		return "CheckinRequestEntity [confirmationNumber=" + confirmationNumber + ", passengerFirstName="
				+ passengerFirstName + ", passengerLastName=" + passengerLastName + ", application=" + application
				+ ", site=" + site + "]";
	}
	
	

}
