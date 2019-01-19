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

	<img id="top" src="<c:url value="/resources/top.png" />" alt="">
	<div id="form_container">
		<h1>
			<a>Flight Check-in Planner</a>
		</h1>
		<nav> <a href="<c:url value='#' />">Refresh</a> <a
			href="<c:url value='/schedule/get' />">Jobs</a> </nav>
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
						<input style="text-transform: uppercase" required="ture"
							id="confirmationNumber" name="confirmationNumber"
							class="element text medium" type="text" pattern=".{6,6}" required
							title="Invalid confirmation number." maxlength="6" value="" />
					</div></li>
				<li id="li_3"><label class="description" for="firstName">FIRST
						NAME * </label>
					<div>
						<input required="ture" id="firstName" name="firstName"
							class="element text medium" type="text" maxlength="25" value="" />
					</div></li>
				<li id="li_4"><label class="description" for="lastName">LAST
						NAME * </label>
					<div>
						<input required="ture" id="lastName" name="lastName"
							class="element text medium" type="text" maxlength="25" value="" />
					</div></li>
				<li class="section_break">
					<h3>Enter Check-in Details</h3>
					<p></p>
				</li>
				<li id="li_5"><label class="description" for="element_5">Check-in
						Date </label> <span> <input required="ture" id="element_5_1"
						name="month" class="element text" size="2" maxlength="2" value=""
						type="text"> / <label for="month">MM</label>
				</span> <span> <input required="ture" id="element_5_2"
						name="dateOfMonth" class="element text" size="2" maxlength="2"
						value="" type="text"> / <label for="dateOfMonth">DD</label>
				</span> <span> <input required="ture" id="element_5_3" name="yyyy"
						class="element text" size="4" maxlength="4" value="" type="text">
						<label for="yyyy">YYYY</label>
				</span> <span id="element_5_calendar_5"> <img id="cal_img_5"
						class="datepicker" src="<c:url value="/resources/calendar.gif" />"
						alt="Pick a date.">
				</span> <script type="text/javascript">
					Calendar.setup({
						inputField : "element_5_3",
						baseField : "element_5",
						displayArea : "calendar_5",
						button : "cal_img_5",
						ifFormat : "%B %e, %Y",
						onSelect : selectDate
					});
				</script>
					<p class="guidelines" id="guide_5">
						<small>Select a day when you want to schedule the
							check-in.</small>
					</p></li>
				<li id="li_6"><label class="description" for="element_6">Check-In
						Time </label> <span> <select required="ture"
						class="element select small" id="hh" name="hh" style="width: 4em">
							<option value="" selected="selected">HH</option>
							<option value="01">01</option>
							<option value="02">02</option>
							<option value="03">03</option>
							<option value="04">04</option>
							<option value="05">05</option>
							<option value="06">06</option>
							<option value="07">07</option>
							<option value="08">08</option>
							<option value="09">09</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
					</select> : <label>HH</label>
				</span> <span> <select required="ture" class="element select small"
						id="mm" name="mm" style="width: 4em">
							<option value="" selected="selected">MM</option>
							<option value="00">00</option>
							<option value="01">01</option>
							<option value="02">02</option>
							<option value="03">03</option>
							<option value="04">04</option>
							<option value="05">05</option>
							<option value="06">06</option>
							<option value="07">07</option>
							<option value="08">08</option>
							<option value="09">09</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="19">19</option>
							<option value="20">20</option>
							<option value="21">21</option>
							<option value="22">22</option>
							<option value="23">23</option>
							<option value="24">24</option>
							<option value="25">25</option>
							<option value="26">26</option>
							<option value="27">27</option>
							<option value="28">28</option>
							<option value="29">29</option>
							<option value="30">30</option>
							<option value="31">31</option>
							<option value="32">32</option>
							<option value="33">33</option>
							<option value="34">34</option>
							<option value="35">35</option>
							<option value="36">36</option>
							<option value="37">37</option>
							<option value="38">38</option>
							<option value="39">39</option>
							<option value="40">40</option>
							<option value="41">41</option>
							<option value="42">42</option>
							<option value="43">43</option>
							<option value="44">44</option>
							<option value="45">45</option>
							<option value="46">46</option>
							<option value="47">47</option>
							<option value="48">48</option>
							<option value="49">49</option>
							<option value="50">50</option>
							<option value="51">51</option>
							<option value="52">52</option>
							<option value="53">53</option>
							<option value="54">54</option>
							<option value="55">55</option>
							<option value="56">56</option>
							<option value="57">57</option>
							<option value="58">58</option>
							<option value="59">59</option>
					</select> : <label>MM</label>
				</span> <span> <select required="ture" class="element select medium"
						id="ss" name="ss" style="width: 4em">
							<option value="" selected="selected">SS</option>
							<option value="00">00</option>
							<option value="01">01</option>
							<option value="02">02</option>
							<option value="03">03</option>
							<option value="04">04</option>
							<option value="05">05</option>
							<option value="06">06</option>
							<option value="07">07</option>
							<option value="08">08</option>
							<option value="09">09</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="19">19</option>
							<option value="20">20</option>
							<option value="21">21</option>
							<option value="22">22</option>
							<option value="23">23</option>
							<option value="24">24</option>
							<option value="25">25</option>
							<option value="26">26</option>
							<option value="27">27</option>
							<option value="28">28</option>
							<option value="29">29</option>
							<option value="30">30</option>
							<option value="31">31</option>
							<option value="32">32</option>
							<option value="33">33</option>
							<option value="34">34</option>
							<option value="35">35</option>
							<option value="36">36</option>
							<option value="37">37</option>
							<option value="38">38</option>
							<option value="39">39</option>
							<option value="40">40</option>
							<option value="41">41</option>
							<option value="42">42</option>
							<option value="43">43</option>
							<option value="44">44</option>
							<option value="45">45</option>
							<option value="46">46</option>
							<option value="47">47</option>
							<option value="48">48</option>
							<option value="49">49</option>
							<option value="50">50</option>
							<option value="51">51</option>
							<option value="52">52</option>
							<option value="53">53</option>
							<option value="54">54</option>
							<option value="55">55</option>
							<option value="56">56</option>
							<option value="57">57</option>
							<option value="58">58</option>
							<option value="59">59</option>
					</select> <label>SS</label>
				</span> <span> <select required="ture" class="element select medium"
						id="apmpm" name="apmpm" style="width: 4em">
							<option value="am" selected="selected">AM</option>
							<option value="pm">PM</option>
					</select> <label>AM PM</label>
				</span>
					<p class="guidelines" id="guide_6">
						<small>Select Time when you want to schedule the check-in.</small>
					</p></li>

				<li id="li_8"><label class="description" for="element_8">TIME
						ZONE </label>
					<div>
						<select required="true" class="element select large" id="timeZone"
							name="timeZone">
							<option value="" selected="selected">-- Select--</option>
							<option value="America/New_York">EST	EASTERN  STANDARD TIME 	UTC - 5</option>
							<option value="America/Chicago">CST	CENTRAL  STANDARD TIME	UTC - 6</option>
							<option value="America/Denver">MST	MOUNTAIN STANDARD TIME	UTC - 7</option>
							<option value="America/Los_Angeles">PST	PACIFIC  STANDARD TIME	UTC - 8</option>
							<option value="IST">IST	INDIAN   STANDARD TIME	GMT - 5.30</option>
						</select>
					</div>
					<p class="guidelines" id="guide_8">
						<small>Select the time-zone here. </small>
					</p></li>
				<li id="li_7"><label class="description" for="element_7">Email
				</label>
					<div>
						<input id="email" name="email" type="email"
							placeholder="you@example.com" class="element text medium"
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