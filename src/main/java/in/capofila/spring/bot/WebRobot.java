package in.capofila.spring.bot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.quartz.JobDetail;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.capofila.spring.commons.CheckinConsts;
import in.capofila.spring.commons.SchedulerUtils;
import in.capofila.spring.model.CheckinDetails;
import in.capofila.spring.model.CheckinRequestEntity;

public class WebRobot {
	public WebRobot() {

	}

	static Logger logger = Logger.getLogger(WebRobot.class);
	Map<String, JSONObject> checkinResponse = new HashMap<String, JSONObject>();
	String travellerIdentity;
	String tokenValue;
	private CheckinDetails details;

	public void visitWeb(String url) {

	}

	static DefaultHttpClient httpClient = new DefaultHttpClient();

	private static DefaultHttpClient getCleint() {
		if (httpClient == null) {
			logger.debug("Creating http client now");
			httpClient = new DefaultHttpClient();
		}
		return httpClient;
	}

	public Response submittingForm(CheckinDetails details, String startTime) throws Exception {
		logger.debug("Loading process to perform auto checkin...");
		String message = null;
		int statusCode = 500;
		int maxAttempt = 3;
		int attemptMade = 1;
		HttpResponse externalCheckinResponse;
		JSONObject externalCheckinResponseJson = new JSONObject();
		try {
			this.details = details;
			this.details.setSheduledTime(startTime);
			for (int attempt = 1; attempt <= maxAttempt; attempt++) {
				attemptMade = attempt;
				externalCheckinResponse = externalCheckin(details);
				message = EntityUtils.toString(externalCheckinResponse.getEntity(), "UTF-8");
				logger.debug("Step 1 checkin response is " + message);
				statusCode = externalCheckinResponse.getStatusLine().getStatusCode();
				if (statusCode == 404) {
					logger.debug("Getting status : " + statusCode + " Reattempting to checkin for " + attemptMade);
					if (attemptMade < 3) {
						this.details.setJobStatus(CheckinConsts.PENDING);
					} else {
						this.details.setJobStatus(CheckinConsts.FAILED);
					}
					EntityUtils.consume(externalCheckinResponse.getEntity()); // this is requried
					continue;
				}

				if (statusCode == 400) {
					logger.debug("http status : 400, Msg : API token is invalid or bad request");
					if (attemptMade < 3) {
						this.details.setJobStatus(CheckinConsts.PENDING);
					} else {
						this.details.setJobStatus(CheckinConsts.FAILED);
					}
					continue;
				}

				if (statusCode == 200) {
					externalCheckinResponseJson = new JSONObject(message);
					JSONObject searchResultJson = externalCheckinResponseJson.getJSONObject("data")
							.getJSONObject("searchResults");
					travellerIdentity = searchResultJson.getJSONObject("reservation").getJSONArray("travelers")
							.getJSONObject(0).getString("travelerIdentity");
					tokenValue = searchResultJson.getString("token");
					break;
				}
				Thread.currentThread().sleep(CheckinConsts.REPEAT_SLEEP);

			} // for ends
			this.details.setAttemptMade(attemptMade);

			if (statusCode == 404) {
				if (attemptMade < 3) {
					this.details.setJobStatus(CheckinConsts.PENDING);
				} else {
					this.details.setJobStatus(CheckinConsts.FAILED);
				}
				logger.debug("No checkin Information found for " + details);
				return Response.status(statusCode).entity("No checkin Information found").build();
			}

			// we have recieved staus 200 means checkin credentials were valid and now we
			// are in actual checkin page.We have to press checkin again here.
			if (statusCode == 200) {
				logger.info(
						"we have recieved staus 200 means checkin credentials were valid and now we are in actual checkin page.We have to press checkin again here.");
				JSONObject jsonObj = new JSONObject(message);
				if (jsonObj.getBoolean("success")) {
					// starting process to do actual checkin here sending request to api.
					HttpResponse step2response = internalCheckin(details);
					int step2resCode = step2response.getStatusLine().getStatusCode();

					if (step2resCode != 200) {
						if (attemptMade < 3) {
							this.details.setJobStatus(CheckinConsts.PENDING);
						} else {
							this.details.setJobStatus(CheckinConsts.FAILED);
						}
						logger.debug("It seems API token is not valid see :" + message);
						return Response.status(statusCode).entity("API token is invalid or bad request").build();
					}

					if (step2resCode == 200) { // status of step 2 req is ok
						logger.info("Congratulations ! We have got 200 Ok meaning checkin is done.");
						this.details.setJobStatus(CheckinConsts.CONFIRMED);
						String responseString = EntityUtils.toString(step2response.getEntity());

						logger.info("Now trying to subscribe email id to recieve boarding pass on email"
								+ details.getEmail());
						// call email setter
						boolean status = emailSubscription(details, travellerIdentity, tokenValue);

						if (status) {
							details.setEmailStatus(CheckinConsts.SUBSCRIBED_YES);
							logger.debug("Successfully saved email id to recieve boarding pass on email");
						} else {
							logger.debug("Failed to sav email id to recieve boarding pass on email");
							details.setEmailStatus(CheckinConsts.SUBSCRIBED_NO);
						}

						this.details.setActualCheckinTime(new Date(System.currentTimeMillis()).toString());
						return Response.status(statusCode).entity("Checkin successfully completed.").build();
					} else { // step 2 reqeust not ok
						logger.debug("Failed at step 2 checkin possible due to unknow response");
						details.setJobStatus(CheckinConsts.FAILED);
						return Response.status(statusCode)
								.entity("Auto Check-in has failed, you need to do a manual checkin.").build();
					}

				} else { // step 1 failed or response is not 200/201
					details.setJobStatus(CheckinConsts.FAILED);
					return Response.status(statusCode).entity("Auto Chekcin Failed, you need to manual checkin")
							.build();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Checkin failed due to and exception " + e.getCause() + e.getLocalizedMessage());
		} finally {
			//send final status to user
			EmailSender.sendEmail(details.getEmail(), this.details.getJobStatus(), SchedulerUtils.emailFormatter(this.details));
			getCleint().getConnectionManager().shutdown();
		}
		return null;
	}

	private static HttpResponse externalCheckin(CheckinDetails details) throws Exception {
		HttpResponse responseMain = null;
		try {
			// Send the request; It will immediately return the response in HttpResponse
			// object if any
			HttpGet visitHomepage1 = new HttpGet("https://www.southwest.com");
			HttpResponse getCallRepons1 = getCleint().execute(visitHomepage1);
			getCallRepons1.getEntity().consumeContent();

			// Send the request; It will immediately return the response in HttpResponse
			// object if any
			HttpGet visitHomepage = new HttpGet("https://www.southwest.com/air/check-in/index.html?clk=GSUBNAV-CHCKIN");
			HttpResponse getCallReponse = getCleint().execute(visitHomepage);
			getCallReponse.getEntity().consumeContent();

			// Define a postRequest request
			HttpPost postRequest = new HttpPost(
					"https://www.southwest.com/api/air-checkin/v1/air-checkin/page/air/check-in/review");

			// Set the API media type in http content-type header
			postRequest.addHeader("authority", "www.southwest.com");
			postRequest.addHeader("path", "/api/air-checkin/v1/air-checkin/page/air/check-in/review");
			postRequest.addHeader("scheme", "https");
			postRequest.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			postRequest.addHeader("encoding", "gzip, deflate, br");
			postRequest.addHeader("accept-language", "en-US,en;q=0.9");
			postRequest.addHeader("authorization", "null null");
			postRequest.addHeader("Content-type", "application/json");
			postRequest.addHeader("origin", "https://www.southwest.com");
			postRequest.addHeader("user-agent",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
			postRequest.addHeader("x-api-key", "l7xx944d175ea25f4b9c903a583ea82a1c4c");

			// Set the request post body
			// StringEntity userEntity = new
			// StringEntity("{\"confirmationNumber\":\"asa232\",\"passengerFirstName\":\"asdf\",\"passengerLastName\":\"asdf\",\"application\":\"air-check-in\",\"site\":\"southwest\"}");

			ObjectMapper mapper = new ObjectMapper();

			CheckinRequestEntity cre = new CheckinRequestEntity();
			cre.setConfirmationNumber(details.getConfirmationNumber());
			cre.setPassengerFirstName(details.getFirstName());
			cre.setPassengerLastName(details.getLastName());

			String jsonDetails = mapper.writeValueAsString(cre);
			logger.debug("Step 1 checkin request payload data " + jsonDetails);

			StringEntity userEntity = new StringEntity(jsonDetails);
			postRequest.setEntity(userEntity);

			HttpResponse response = getCleint().execute(postRequest);
			responseMain = response;
		} catch (Exception e) {
			EmailSender.sendEmail(CheckinConsts.DEV_EMAIL, "Checkin schedular failed at step 1", e.getMessage());
			logger.error("Checkin failed at step 2 caused by ", e);
		}
		return responseMain;
	}

	private static HttpResponse internalCheckin(CheckinDetails cd) throws ParseException, IOException {
		HttpPost postRequest = new HttpPost(
				"https://www.southwest.com/api/content-delivery/v1/content-delivery/query/placements");

		try {
			// Define a postRequest request

			// Set the API media type in http content-type header
			postRequest.addHeader("authority", "www.southwest.com");
			postRequest.addHeader("path", "/api/content-delivery/v1/content-delivery/query/placements");
			postRequest.addHeader("scheme", "https");
			postRequest.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			postRequest.addHeader("encoding", "gzip, deflate, br");
			postRequest.addHeader("accept-language", "en-US,en;q=0.9");
			postRequest.addHeader("authorization", "null null");
			postRequest.addHeader("Content-type", "application/json");
			postRequest.addHeader("origin", "https://www.southwest.com");
			postRequest.addHeader("referer",
					"https://www.southwest.com/air/check-in/review.html?confirmationNumber="
							+ cd.getConfirmationNumber() + "&passengerFirstName=" + cd.getFirstName()
							+ "&passengerLastName=" + cd.getLastName());
			postRequest.addHeader("user-agent",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
			postRequest.addHeader("x-api-key", "l7xx944d175ea25f4b9c903a583ea82a1c4c");

			// Set the request post body
			// {"lang":"en","pageId":"air-check-in-review","appId":"air-check-in","site":"southwest","application":"air-check-in"}

			JSONObject entityData = new JSONObject();
			entityData.put("lang", "en");
			entityData.put("pageId", "air-check-in-review");
			entityData.put("appId", "air-check-in");
			entityData.put("site", "southwest");
			entityData.put("application", "air-check-in");

			StringEntity userEntity = new StringEntity(entityData.toString());
			postRequest.setEntity(userEntity);
		} catch (Exception e) {
			EmailSender.sendEmail(CheckinConsts.DEV_EMAIL, "Checkin schedular failed at step 1", e.getMessage());
			logger.debug("Exception occured at step 1 ", e);
		}

		return getCleint().execute(postRequest);
	}

	private boolean emailSubscription(CheckinDetails cd, String travellerIdentity, String tokenValue) {
		String email = cd.getEmail();
		boolean status = false;

		// Define a postRequest request
		HttpPost postRequest = new HttpPost(
				"https://www.southwest.com/api/air-checkin/v1/air-checkin/feature/mobile-boarding-pass");

		// Set the API media type in http content-type header
		postRequest.addHeader("authority", "www.southwest.com");
		postRequest.addHeader("path", "/api/air-checkin/v1/air-checkin/feature/mobile-boarding-pass");
		postRequest.addHeader("scheme", "https");
		postRequest.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		postRequest.addHeader("encoding", "gzip, deflate, br");
		postRequest.addHeader("accept-language", "en-US,en;q=0.9");
		postRequest.addHeader("authorization", "null null");
		postRequest.addHeader("Content-type", "application/json");
		postRequest.addHeader("origin", "https://www.southwest.com");
		postRequest.addHeader("referer", "ttps://www.southwest.com/air/check-in/confirmation.html");
		postRequest.addHeader("user-agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
		postRequest.addHeader("x-api-key", "l7xx944d175ea25f4b9c903a583ea82a1c4c");

		// Set the request post body
		// {"lang":"en","pageId":"air-check-in-review","appId":"air-check-in","site":"southwest","application":"air-check-in"}

		// Set the request post body
		JSONObject entityData = new JSONObject();
		entityData.put("application", "air-check-in-review");
		entityData.put("confirmationNumber", cd.getConfirmationNumber());
		entityData.put("deliveryMethod", "EMAIL");
		entityData.put("destination", cd.getEmail());
		entityData.put("drinkCouponSelected", "false");
		entityData.put("site", "southwest");
		entityData.put("token", tokenValue);
		entityData.put("travelerIdentity", travellerIdentity);

		StringEntity userEntity = null;
		JSONObject resObj;
		try {
			userEntity = new StringEntity(entityData.toString());
			HttpResponse response = getCleint().execute(postRequest);
			String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
			try {
				resObj = new JSONObject(responseString);
			} catch (Exception e) {
				logger.error("400, Bad request. this might be happening due to alrady subscribed");
				return status = false;

			}

			status = resObj.getBoolean("success");
//			String result = resObj.getJSONObject("notifications").getJSONArray("formErrors").getJSONObject(0)
//					.getString("code");
			logger.debug("Setting email response " + resObj.toString());

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		postRequest.setEntity(userEntity);
		return status;

	}

	public static void main(String[] args) {
		CheckinDetails cd = new CheckinDetails();
		cd.setConfirmationNumber("SFAAX3");
		cd.setFirstName("Ryan");
		cd.setLastName("Cortez");
		cd.setDateOfMonth("07");
		cd.setMonth("01");
		cd.setYyyy("2019");
		cd.setHh("10");
		cd.setMm("29");
		cd.setSs("30");
		cd.setEmail("karamsahu@gmail.com");
		cd.setTimeZone("IST");
		cd.setApmpm("PM");
		cd.setHh(SchedulerUtils.to24hr(cd));
		WebRobot wr = new WebRobot();
		try {
			Response res = wr.submittingForm(cd, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
