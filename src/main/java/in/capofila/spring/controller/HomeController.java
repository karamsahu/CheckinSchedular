package in.capofila.spring.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import in.capofila.spring.model.CheckinDetails;
import in.capofila.spring.model.ScheduledJobs;
import in.capofila.spring.model.User;
import in.capofila.spring.service.CheckinServiceImpl;

@Controller
public class HomeController {
	CheckinServiceImpl checkinservice = new CheckinServiceImpl();
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	public HomeController() {
		// TODO Auto-generated constructor stub
	}
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		System.out.println("Home Page Requested, locale = " + locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate);
		return "home";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String getIndex(@Validated User user, Model model) {
		return "index";
	}

	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public String user(@Validated User user, Model model) {
		System.out.println(user.getPassword());
		if (user.getPassword().equals("welcome2019")) {
			return "index";
		} else {
			model.addAttribute("loginError", "<p>Invalid Password</p>");
			return "home";
		}
	}

	@RequestMapping(value = "/schedule", method = RequestMethod.POST)
	public ModelAndView addCheckinEvent(CheckinDetails checkinDetails, Model model) {
		checkinservice = new CheckinServiceImpl();
		boolean status = checkinservice.createJob(checkinDetails);

		List<ScheduledJobs> allScheduledJobs = checkinservice.getAllJob();
		System.out.println(allScheduledJobs.toString());
		// model.addAttribute(allScheduledJobs);
		ModelAndView modelview = new ModelAndView("result");
		modelview.addObject("lists", allScheduledJobs);

		return modelview;
	}

	@RequestMapping(value = "/schedule/get", method = RequestMethod.GET)
	public ModelAndView listCheckinEvent(CheckinDetails checkinDetails, Model model) {
		List<ScheduledJobs> allScheduledJobs = checkinservice.getAllJob();
		ModelAndView modelview = new ModelAndView("result");
		modelview.addObject("lists", allScheduledJobs);
		return modelview;
	}

	@RequestMapping(value = "/schedule/delete/{jobName}/{groupName}", method = RequestMethod.GET)
	public ModelAndView deleteCheckinEvent(@PathVariable String jobName, @PathVariable String groupName) {
		checkinservice.cancellJob(jobName, groupName);
		List<ScheduledJobs> allScheduledJobs = checkinservice.getAllJob();
		ModelAndView modelview = new ModelAndView("result");
		modelview.addObject("lists", allScheduledJobs);
		return modelview;
	}

}