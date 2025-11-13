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
	<h1>cartList</h1>
	<c:import url="/WEB-INF/view/inc/customerMenu.jsp"></c:import>
	
	<form method="get" action="${pageContext.request.contextPath}/customer/addOrders">
		<table border="1">
			<tr>
				<th>선택</th>
				<th>goodsName</th>
				<th>goodsPrice</th>
				<th>cartQuantity</th>
				<th>totalPrice</th>
			</tr>
			<c:forEach var="m" items="${list}">
				<tr>
					<td>
						<c:if test="${m.soldout == 'soldout'}">
							soldout
						</c:if>
						<c:if test="${m.soldout != 'soldout'}">
							<input type="checkbox" name="ck" value="${m.cartCode}">
						</c:if>
					</td>
					<td>${m.goodName}</td>
					<td>${m.goodsPrice}</td>
					<td>
						${m.cartQuantity}
					</td>
					<td>${m.totalPrice}</td>
				</tr>
			</c:forEach>
		</table>
		<button type="submit">주문하기</button>
	</form>
</body>
</html>