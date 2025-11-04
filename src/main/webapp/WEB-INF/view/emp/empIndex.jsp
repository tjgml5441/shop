<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
</head>
<body>
	<h1>empIndex</h1>
		<!-- emp menu include -->
		<c:import url="/WEB-INF/view/inc/empMenu.jsp"></c:import>
	<div>
		${loginEmp.empName}님 반갑습니다.
		<a href="${pageContext.request.contextPath}/emp/empLogout">로그아웃</a>
	</div>
</body>
</html>