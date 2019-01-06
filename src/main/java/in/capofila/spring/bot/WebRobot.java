package in.capofila.spring.bot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import in.capofila.spring.commons.CheckinConsts;
import in.capofila.spring.commons.SchedulerUtils;
import in.capofila.spring.model.CheckinDetails;
import in.capofila.spring.model.CheckinRequestEntity;

public class WebRobot {
	static Logger logger = Logger.getLogger(WebRobot.class);
	Map<String, JSONObject> checkinResponse = new HashMap<String, JSONObject>();
	String travellerIdentity;
	String tokenValue;

	public void visitWeb(String url) {

	}

	static DefaultHttpClient httpClient = new DefaultHttpClient();

	public Response submittingForm(CheckinDetails details) throws Exception {
		String message = null;
		int statusCode = 500;
		int maxAttempt = 3;
		int attemptMade = 0;
		String getJobStatus;
		HttpResponse externalCheckinResponse;
		JSONObject externalCheckinResponseJson = new JSONObject();
		try {
			for (int attempt = 1; attempt <= maxAttempt; attempt++) {
				
				externalCheckinResponse = externalCheckin(details);
				message = EntityUtils.toString(externalCheckinResponse.getEntity(), "UTF-8");
				logger.debug("Step 1 checkin response is "+message);
				statusCode = externalCheckinResponse.getStatusLine().getStatusCode();
				if(statusCode == 404) {
					attemptMade = attempt;
					logger.debug("Getting status : "+statusCode+ " Reattempting to checkin for "+attemptMade);
					details.setJobStatus(CheckinConsts.PENDING);
					boolean emailSent = EmailSender.sendEmail(details.getEmail(), "No checkin Information found",
							SchedulerUtils.emailFormatter(details));
					Thread.currentThread().sleep(CheckinConsts.REPEAT_SLEEP);
					if(emailSent) {
						logger.debug("Email sent to passenger about shceduling job status");
					}
					EntityUtils.consume(externalCheckinResponse.getEntity()); //this is requried
					continue;
				}
				

				if (statusCode == 200) {
					attemptMade = attempt;
					//message = EntityUtils.toString(externalCheckinResponse.getEntity(), "UTF-8");
					externalCheckinResponseJson = new JSONObject(message);
					JSONObject searchResultJson = externalCheckinResponseJson.getJSONObject("data")
							.getJSONObject("searchResults");
					travellerIdentity = searchResultJson.getJSONObject("reservation").getJSONArray("travelers")
							.getJSONObject(0).getString("travelerIdentity");
					tokenValue = searchResultJson.getString("token");
					break;
				}

				if (!externalCheckinResponseJson.getBoolean("success")) {
					logger.debug("Atemmpt " + attempt + "to checkin on website. Due to : "
							+ externalCheckinResponseJson.getJSONObject("notifications").getJSONArray("formErrors")
									.getJSONObject(0).getString("code"));
				}
			}//for ends
			details.setAttemptMade(attemptMade);
			
			if(attemptMade==3) {
				if (statusCode == 404) {
					System.err.println("http status : 404, Msg : No checkin Information found");
					details.setJobStatus(CheckinConsts.PENDING);
					EmailSender.sendEmail(details.getEmail(), "No checkin Information found",
							SchedulerUtils.emailFormatter(details));
					return Response.status(statusCode).entity("Checkin Pending or No checkin Information found").build();

				}
			}
			
			if (statusCode == 400) {
				System.err.println("http status : 400, Msg : API token is invalid or bad request");
				details.setJobStatus(CheckinConsts.FAILED);
				EmailSender.sendEmail(details.getEmail(), "Chekcin Failed, API token is invalid or bad request",
						SchedulerUtils.emailFormatter(details));
				return Response.status(statusCode).entity("API token is invalid or bad request").build();
			}

			// we have recieved staus 200 means checkin credentials were valid and now we
			// are in actual checkin page.We have to press checkin again here.
			if (statusCode == 200) {
				JSONObject jsonObj = new JSONObject(message);
				if (jsonObj.getBoolean("success")) {
					// starting process to do actual checkin here sending request to api.
					HttpResponse step2response = internalCheckin(details);
					int step2resCode = step2response.getStatusLine().getStatusCode();
					
					if(step2resCode != 200) {
						details.setJobStatus(CheckinConsts.FAILED);
						EmailSender.sendEmail(details.getEmail(), "Chekcin Failed, at step 2 check-in",
								SchedulerUtils.emailFormatter(details));
						return Response.status(statusCode).entity("API token is invalid or bad request").build();
					}
					
					if (step2resCode == 200) { // status of step 2 req is ok
						String responseString = EntityUtils.toString(step2response.getEntity());

						// call email setter
						boolean status = setEmailAddress(details, travellerIdentity, tokenValue);

						if (status) {
							details.setEmailStatus(CheckinConsts.SUBSCRIBED_YES);
							logger.debug("Successfully saved email id to recieve boarding pass on email");
						} else {
							details.setEmailStatus(CheckinConsts.SUBSCRIBED_NO);
						}
						details.setJobStatus(CheckinConsts.CONFIRMED);
						EmailSender.sendEmail(details.getEmail(),
								"Successfull! Auto Flight Checkin has been done just now.",
								SchedulerUtils.emailFormatter(details));
						return Response.status(statusCode).entity("Checkin successfully completed.").build();
					} else { // step 2 reqeust not ok
						details.setJobStatus(CheckinConsts.FAILED);
						EmailSender.sendEmail(details.getEmail(),
								"Unsuccessfull checkin, possible cause unknown, check logs.",
								SchedulerUtils.emailFormatter(details));
						return Response.status(statusCode).entity("Auto Chekcin Failed, you need to manual checkin")
								.build();
					}

				} else { // step 1 failed or response is not 200/201
					details.setJobStatus(CheckinConsts.FAILED);
					EmailSender.sendEmail(details.getEmail(),
							"Unsuccessfull checkin, possible cause unknown, check logs.",
							SchedulerUtils.emailFormatter(details));
					return Response.status(statusCode).entity("Auto Chekcin Failed, you need to manual checkin")
							.build();
				}
			}
		} catch(Exception e){
			e.printStackTrace();
			logger.error("Checkin failed due to and exception "+e.getCause()+e.getLocalizedMessage());
		}finally {
			// Important: Close the connect
			httpClient.getConnectionManager().shutdown();
		}
		return null;
	}

	private static HttpResponse externalCheckin(CheckinDetails details) throws Exception {
		try {
			// Send the request; It will immediately return the response in HttpResponse
			// object if any
			HttpGet visitHomepage1 = new HttpGet("https://www.southwest.com");
			HttpResponse getCallRepons1 = httpClient.execute(visitHomepage1);
			getCallRepons1.getEntity().consumeContent();

			// Send the request; It will immediately return the response in HttpResponse
			// object if any
			HttpGet visitHomepage = new HttpGet("https://www.southwest.com/air/check-in/index.html?clk=GSUBNAV-CHCKIN");
			HttpResponse getCallReponse = httpClient.execute(visitHomepage);
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

			HttpResponse response = httpClient.execute(postRequest);
			return response;
		} finally {
			// Important: Close the connect
			// httpClient.getConnectionManager().shutdown();
		}
	}

	private static HttpResponse internalCheckin(CheckinDetails cd) throws ParseException, IOException {
		// Define a postRequest request
		HttpPost postRequest = new HttpPost(
				"https://www.southwest.com/api/content-delivery/v1/content-delivery/query/placements");

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
				"https://www.southwest.com/air/check-in/review.html?confirmationNumber=" + cd.getConfirmationNumber()
						+ "&passengerFirstName=" + cd.getFirstName() + "&passengerLastName=" + cd.getLastName());
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

		return httpClient.execute(postRequest);
	}

	private boolean setEmailAddress(CheckinDetails cd, String travellerIdentity, String tokenValue) {
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
			HttpResponse response = httpClient.execute(postRequest);
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
		cd.setDateOfMonth("06");
		cd.setMonth("01");
		cd.setYyyy("2019");
		cd.setHh("10");
		cd.setMm("27");
		cd.setSs("30");
		cd.setEmail("karamsahu@gmail.com");
		cd.setTimeZone("EST");
		cd.setApmpm("AM");
		cd.setHh(SchedulerUtils.to24hr(cd));
		WebRobot wr = new WebRobot();
		try {
			Response res = wr.submittingForm(cd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
