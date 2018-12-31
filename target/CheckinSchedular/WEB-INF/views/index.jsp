<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Flight Check-in Planner</title>
<link rel="stylesheet" type="text/css" href="view.css" media="all">
<script type="text/javascript" src="view.js"></script>
<script type="text/javascript" src="calendar.js"></script>
</head>
<body id="main_body" >
	
	<img id="top" src="top.png" alt="">
	<div id="form_container">
	
		<h1><a>Flight Check-in Planner</a></h1>
		<form id="form_42612" class="appnitro"  method="post" action="">
					<div class="form_description">
			<h2>Flight Check-in Planner</h2>
			<p></p>
		</div>						
			<ul >
			
					<li class="section_break">
			<h3>Enter Passenger information.</h3>
			<p>You can plan your flight checkins in advance using this application. Just select the date and time you want this application to make checkin automatically.</p>
		</li>		<li id="li_1" >
		<label class="description" for="element_1">CONFIRMATION # * </label>
		<div>
			<input id="element_1" name="element_1" class="element text medium" type="text" maxlength="255" value=""/> 
		</div> 
		</li>		<li id="li_3" >
		<label class="description" for="element_3">FIRST NAME * </label>
		<div>
			<input id="element_3" name="element_3" class="element text medium" type="text" maxlength="255" value=""/> 
		</div> 
		</li>		<li id="li_4" >
		<label class="description" for="element_4">LAST NAME * </label>
		<div>
			<input id="element_4" name="element_4" class="element text medium" type="text" maxlength="255" value=""/> 
		</div> 
		</li>		<li class="section_break">
			<h3>Enter Checkin Details</h3>
			<p></p>
		</li>		<li id="li_5" >
		<label class="description" for="element_5">Date </label>
		<span>
			<input id="element_5_1" name="element_5_1" class="element text" size="2" maxlength="2" value="" type="text"> /
			<label for="element_5_1">MM</label>
		</span>
		<span>
			<input id="element_5_2" name="element_5_2" class="element text" size="2" maxlength="2" value="" type="text"> /
			<label for="element_5_2">DD</label>
		</span>
		<span>
	 		<input id="element_5_3" name="element_5_3" class="element text" size="4" maxlength="4" value="" type="text">
			<label for="element_5_3">YYYY</label>
		</span>
	
		<span id="calendar_5">
			<img id="cal_img_5" class="datepicker" src="calendar.gif" alt="Pick a date.">	
		</span>
		<script type="text/javascript">
			Calendar.setup({
			inputField	 : "element_5_3",
			baseField    : "element_5",
			displayArea  : "calendar_5",
			button		 : "cal_img_5",
			ifFormat	 : "%B %e, %Y",
			onSelect	 : selectDate
			});
		</script>
		<p class="guidelines" id="guide_5"><small>Select a day when you want to schedule the checkin.</small></p> 
		</li>		<li id="li_6" >
		<label class="description" for="element_6">Time </label>
		<span>
			<input id="element_6_1" name="element_6_1" class="element text " size="2" type="text" maxlength="2" value=""/> : 
			<label>HH</label>
		</span>
		<span>
			<input id="element_6_2" name="element_6_2" class="element text " size="2" type="text" maxlength="2" value=""/> : 
			<label>MM</label>
		</span>
		<span>
			<input id="element_6_3" name="element_6_3" class="element text " size="2" type="text" maxlength="2" value=""/>
			<label>SS</label>
		</span>
		<span>
			<select class="element select" style="width:4em" id="element_6_4" name="element_6_4">
				<option value="AM" >AM</option>
				<option value="PM" >PM</option>
			</select>
			<label>AM/PM</label>
		</span><p class="guidelines" id="guide_6"><small>Select Time when you want to schedule the checkin.</small></p> 
		</li>		<li id="li_8" >
		<label class="description" for="element_8">TIME ZONE </label>
		<div>
		<select class="element select large" id="element_8" name="element_8"> 
			<option value="1" >Eastern Standard Time (EST)</option>
<option value="2" >Indian Standard Time (IST)</option>
<option value="3" selected="selected">-- Select--</option>

		</select>
		</div><p class="guidelines" id="guide_8"><small>Select the timezone here. </small></p> 
		</li>		<li id="li_7" >
		<label class="description" for="element_7">Email </label>
		<div>
			<input id="element_7" name="element_7" class="element text medium" type="text" maxlength="255" value=""/> 
		</div><p class="guidelines" id="guide_7"><small>Provide an email address to receive boarding pass. </small></p> 
		</li>
			
					<li class="buttons">
			    <input type="hidden" name="form_id" value="42612" />
			    
				<input id="saveForm" class="button_text" type="submit" name="submit" value="Submit" />
		</li>
			</ul>
		</form>	
		<div id="footer">
			Design and developed by <a href="https://capofila.in">Capofila Technologies</a>
		</div>
	</div>
	<img id="bottom" src="bottom.png" alt="">
	</body>
</html>