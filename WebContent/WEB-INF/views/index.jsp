<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Flight Check-in Planner</title>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/view.css"/>" media="all">
<script type="text/javascript" src="<c:url value="/resources/view.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/calendar.js"/>"></script>
</head>
<body id="main_body">

	<img id="top" src="top.png" alt="">
	<div id="form_container">
		<h1>
			<a>Flight Check-in Planner</a>
		</h1>
		<form id="form_42612" class="appnitro" method="post" action="schedule">
			<div class="form_description">
				<h2>Flight Check-in Planner</h2>
				<p></p>
			</div>
			<ul>

				<li class="section_break">
					<h3>Enter Passenger information.</h3>
					<p>You can plan your flight check-in in advance using this
						application. Just select the date and time you want this
						application to make check-in automatically.</p>
				</li>
				<li id="li_1"><label class="description"
					for="confirmationNumber">CONFIRMATION # * </label>
					<div>
						<input id="confirmationNumber" name="confirmationNumber"
							class="element text medium" type="text" maxlength="6" value="" />
					</div></li>
				<li id="li_3"><label class="description" for="firstName">FIRST
						NAME * </label>
					<div>
						<input id="firstName" name="firstName" class="element text medium"
							type="text" maxlength="255" value="" />
					</div></li>
				<li id="li_4"><label class="description" for="lastName">LAST
						NAME * </label>
					<div>
						<input id="lastName" name="lastName" class="element text medium"
							type="text" maxlength="255" value="" />
					</div></li>
				<li class="section_break">
					<h3>Enter Check-in Details</h3>
					<p></p>
				</li>
				<li id="li_5"><label class="description" for="element_5">Date
				</label> <span> <input id="month" name="month" class="element text"
						size="2" maxlength="2" value="" type="text"> / <label
						for="month">MM</label>
				</span> <span> <input id="dateOfMonth" name="dateOfMonth"
						class="element text" size="2" maxlength="2" value="" type="text">
						/ <label for="dateOfMonth">DD</label>
				</span> <span> <input id="yyyy" name="yyyy" class="element text"
						size="4" maxlength="4" value="" type="text"> <label
						for="yyyy">YYYY</label>
				</span> <span id="calendar_5"> <img id="cal_img_5"
						class="datepicker" src="calendar.gif" alt="Pick a date.">
				</span> <script type="text/javascript">
					Calendar.setup({
						inputField : "year",
						baseField : "element_5",
						displayArea : "calendar_5",
						button : "cal_img_5",
						ifFormat : "%B %e, %Y",
						onSelect : selectDate
					});
				</script>
					<p class="guidelines" id="guide_5">
						<small>Select a day when you want to schedule the checkin.</small>
					</p></li>
				<li id="li_6"><label class="description" for="element_6">Time
				</label> <span> <input id="hh" name="hh" class="element text "
						size="2" type="text" maxlength="2" value="" /> : <label>HH</label>
				</span> <span> <input id="mm" name="mm" class="element text "
						size="2" type="text" maxlength="2" value="" /> : <label>MM</label>
				</span> <span> <input id="ss" name="ss" class="element text "
						size="2" type="text" maxlength="2" value="" /> <label>SS</label>
				</span> <span> <select class="element select" style="width: 4em"
						id="ampm" name="ampm">
							<option value="AM">AM</option>
							<option value="PM">PM</option>
					</select> <label>AM/PM</label>
				</span>
					<p class="guidelines" id="guide_6">
						<small>Select Time when you want to schedule the checkin.</small>
					</p></li>
				<li id="li_8"><label class="description" for="element_8">TIME
						ZONE </label>
					<div>
						<select class="element select large" id="timeZone" name="timeZone">
							<option value="EST">Eastern Standard Time (EST)</option>
							<option value="IST">Indian Standard Time (IST)</option>
							<option value="" selected="selected">-- Select--</option>

						</select>
					</div>
					<p class="guidelines" id="guide_8">
						<small>Select the time-zone here. </small>
					</p></li>
				<li id="li_7"><label class="description" for="element_7">Email
				</label>
					<div>
						<input id="email" name="email" class="element text medium"
							type="text" maxlength="255" value="" />
					</div>
					<p class="guidelines" id="guide_7">
						<small>Provide an email address to receive boarding pass.
						</small>
					</p></li>

				<li class="buttons"><input id="saveForm" class="button_text"
					type="submit" name="submit" value="Submit" /></li>
			</ul>
		</form>
		<div id="footer">
			Design and developed by <a href="https://capofila.in">Capofila
				Technologies</a>
		</div>
	</div>
	<img id="bottom" src="<c:url value="/resources/bottom.png"/>" alt="">
</body>
</html>