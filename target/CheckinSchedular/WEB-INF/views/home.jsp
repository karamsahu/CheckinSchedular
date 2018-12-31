<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Flight Check-in Planner</title>
</head>
<body>
	<h1>Welcome to file check in planner</h1>
	<P>The time on the server is ${serverTime}.</p>
	<p>${loginError}</p>
	<form action="validate" method="post">
		<input type="password" name="password"><br> 
		<input type="submit" value="Login">
	</form>
</body>
</html>
