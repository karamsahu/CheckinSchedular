<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
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
			<a>Login</a>
		</h1>
		<form id="form_42612" class="appnitro" method="post" action="validate">
			<div class="form_description">
				<h2>Login - Flight Check-in Planner</h2>
			</div>
			<ul>
				<li>
					<p>${loginError}</p>
				</li>
				<li id="li_1"><label class="description" for="password">Password
				</label>
					<div>
						<input id="element_1" type="password" name="password"
							class="element text medium" type="text" maxlength="255" value="" />
					</div></li>
				<li><input type="submit" value="Login"></li>
			</ul>
		</form>
		<div id="footer">
			Design and developed by <a href="https://capofila.in">Capofila
				Technologies</a>
		</div>
	</div>
	<img id="bottom" src="<c:url value="/resources/bottom.png" />" alt="">
</body>
</html>
