package in.capofila.spring.model;

public class CheckinDetails {
	private String confirmationNumber;
	private String firstName;
	private String lastName;
	private final String application = "air-check-in";
	private String site = "southwest"; 
	
	private String dateOfMonth,month,yyyy,timeZone,ampm,email, hh, mm, ss;
		
	
	public String getHh() {
		return hh;
	}
	public void setHh(String hh) {
		this.hh = hh;
	}
	public String getSs() {
		return ss;
	}
	public void setSs(String ss) {
		this.ss = ss;
	}
	public String getAmpm() {
		return ampm;
	}
	public void setAmpm(String ampm) {
		this.ampm = ampm;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
	}
	public String getYyyy() {
		return yyyy;
	}
	public void setYyyy(String yyyy) {
		this.yyyy = yyyy;
	}
	public String getDateOfMonth() {
		return dateOfMonth;
	}
	public void setDateOfMonth(String dateOfMonth) {
		this.dateOfMonth = dateOfMonth;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getConfirmationNumber() {
		return confirmationNumber;
	}
	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getSite() {
		return site;
	}
	
	public void setSite(String site) {
		this.site = site;
	}
	
	public String getApplication() {
		return application;
	}
	@Override
	public String toString() {
		return "CheckinDetails [confirmationNumber=" + confirmationNumber + ", firstName=" + firstName + ", lastName="
				+ lastName + ", application=" + application + ", site=" + site + ", month=" + month
				+ ", yyyy=" + yyyy + ", dateOfMonth=" + dateOfMonth + ", timeZone=" + timeZone
				+ ", ampm=" + ampm + ", email=" + email + ", hh=" + hh + ", mm=" + mm + ", ss=" + ss + "]";
	}
	
	
}
