package in.capofila.spring.service;

import in.capofila.spring.model.CheckinDetails;

public interface CheckinService {
	boolean doCheckin(CheckinDetails details);
	
	boolean isPortalRechable();
}
