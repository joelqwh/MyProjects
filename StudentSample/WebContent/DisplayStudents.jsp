<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>Student List</h1>

<table border = "2">
<tr>
<th>#</th>
<th>Student ID</th>
<th>Student Name</th>
<th>Nick Name</th>
<th>Course</th>
</tr>
<c:forEach var="student" varStatus="idx" items="${slist}">
<tr>
<td>#</td>
<td>${student.studentId}</td>
<td>${student.name}</td>
<td>${student.nick}</td>
<td>${student.course}</td>
</tr>
</c:forEach>
</table>

</body>
</html>