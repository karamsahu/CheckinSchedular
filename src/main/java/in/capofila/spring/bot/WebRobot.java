package in.capofila.spring.bot;


import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.capofila.spring.commons.SchedulerUtils;
import in.capofila.spring.model.CheckinDetails;
import in.capofila.spring.model.CheckinRequestEntity;

public class WebRobot {
	public void visitWeb(String url) {

	}

	public Response submittingForm(CheckinDetails details) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		int statusCode;
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
			System.out.println(jsonDetails);

			StringEntity userEntity = new StringEntity(jsonDetails);
			postRequest.setEntity(userEntity);

			HttpResponse response = httpClient.execute(postRequest);
			String message = EntityUtils.toString(response.getEntity(), "UTF-8");

			// verify the valid error code first
			statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == 404) {
				System.err.println("http status : 404, Msg : No checkin Information found");				
				EmailSender.sendMimeEmail(details.getEmail(), "Successfull! Auto Flight Checkin has been done just now.", SchedulerUtils.emailFormatter(details));

				return Response.status(statusCode).entity("No checkin Information found").build();
				
			}
			
			if (statusCode == 400) {
				System.err.println("http status : 400, Msg : API token is invalid or bad request");
				return Response.status(statusCode).entity("API token is invalid or bad request").build();
			}

			if (statusCode == 201 || statusCode == 200) {
				System.err.println("http status : 200/201, Msg : Checkin successfully completed");
				return Response.status(statusCode).entity("Checkin successfully completed.").build();
			}
		} finally {
			// Important: Close the connect
			httpClient.getConnectionManager().shutdown();
		}
		return null;
	}

	public static void main(String[] args) {
		CheckinDetails cd = new CheckinDetails();
		cd.setConfirmationNumber("asa232");
		cd.setFirstName("karam");
		cd.setLastName("sahi");
		cd.setDateOfMonth("01");
		cd.setMonth("01");
		cd.setYyyy("2019");
		cd.setHh("22");
		cd.setMm("27");
		cd.setSs("30");
		cd.setEmail("karamsahu@gmail.com");
		WebRobot wr = new WebRobot();
		try {
			Response res = wr.submittingForm(cd);
			System.out.println(res.getEntity().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
