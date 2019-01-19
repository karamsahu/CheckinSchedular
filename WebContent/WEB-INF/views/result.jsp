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
	<style>
table.paleBlueRows {
	font-family: "Times New Roman", Times, serif;
	border: 1px solid #FFFFFF;
	width: auto;
	height: 200px;
	text-align: center;
	border-collapse: collapse;
}

table.paleBlueRows td, table.paleBlueRows th {
	border: 1px solid #FFFFFF;
	padding: 3px 2px;
}

table.paleBlueRows tbody td {
	font-size: 13px;
}

table.paleBlueRows tr:nth-child(even) {
	background: #D0E4F5;
}

table.paleBlueRows thead {
	background: #0B6FA4;
	border-bottom: 5px solid #FFFFFF;
}

table.paleBlueRows thead th {
	font-size: 17px;
	font-weight: bold;
	color: #FFFFFF;
	text-align: center;
	border-left: 2px solid #FFFFFF;
}

table.paleBlueRows thead th:first-child {
	border-left: none;
}

table.paleBlueRows tfoot {
	font-size: 14px;
	font-weight: bold;
	color: #333333;
	background: #D0E4F5;
	border-top: 3px solid #444444;
}

table.paleBlueRows tfoot td {
	font-size: 14px;
}
</style>
	<img id="top" src="<c:url value="/resourecs/top.png"/>" alt="">
	<div id="form_container">
		<h1>
			<a>Result</a>
		</h1>
		<nav> <a href="<c:url value='/schedule/get' />">Refresh</a> <a
			href="<c:url value='/index' />">Back</a> </nav>

		<div>
			<table class="paleBlueRows">
				<thead>
					<tr>
						<th>CONFIRMATION#</th>
						<th>NAME</th>
						<th>SCHEDULED TIME</th>
						<th>EMAIL</th>
						<th>CHECKIN STATUS</th>
						<th>JOB STATUS</th>
						<th>ACTION</th>
					</tr>
				</thead>
				<tfoot>
					<tr>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
				</tfoot>
				<tbody>
					<c:if test="${not empty lists}">
						<c:forEach var="checkinDetails" items="${lists}">
							<tr>
								<td>${checkinDetails.getConfirmationNumber()}</td>
								<td>${checkinDetails.getFirstName()} ${jobs.getLastName()}</td>
								<td>${checkinDetails.getSheduledTime() }</td>
								<td>${checkinDetails.getEmail() }</td>
								<td>${checkinDetails.getJobStatus() }</td>
								<td>${checkinDetails.getSchedularStatus() }</td>
								<td><a href="<c:url value='/schedule/delete/${checkinDetails.getJobName()}/${checkinDetails.getJobGroup()}'/>"><input
										type="button" value="Delete"></a></td>
							</tr>
						</c:forEach>


					</c:if>
				</tbody>
			</table>
		</div>
		<div id="footer">
			Design and developed by <a href="https://capofila.in">Capofila
				Technologies</a>
		</div>
	</div>
	<img id="bottom" src="<c:url value="/resources/bottom.png" />" alt="">
</body>
</html>
