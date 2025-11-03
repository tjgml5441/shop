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
	<h1>login</h1>
	<form method="post" action="${papgeContext.request.contextPath}/out/login">
		<div>
			<div>
				<table>
					<tr>
						<td>id</td>
						<td><input type="text" name="id" id="id"></td>
					</tr>
					<tr>
						<td>pw</td>
						<td><input type="password" name="pw" id="pw"></td>
					</tr>
				</table>
				<button type="button">로그인</button>
			</div>
			<div>
				<input type="radio" name="customerOrEmpSel" class="customerOrEmpSel" value="customer" checked>customer
				<input type="radio" name="customerOrEmpSel" class="customerOrEmpSel" value="emp">emp
			</div>
		</div>
	</form>
	<a href="${pageContext.request.contextPath}/out/addMember">회원가입</a>
</body>
</html>