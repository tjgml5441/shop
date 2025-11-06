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
	<h1>customerIndex</h1>
	<!-- emp menu include -->
		<c:import url="/WEB-INF/view/inc/customerMenu.jsp"></c:import>
	<div>
		${loginCustomer.customerName}님 반갑습니다.
		(point : ${loginCustomer.point})
		<a href="${pageContext.request.contextPath}/customer/customerLogout">로그아웃</a>
	</div>
</body>
</html>