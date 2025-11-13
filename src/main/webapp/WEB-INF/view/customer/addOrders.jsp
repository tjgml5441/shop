<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
</head>

<body>
	<h1>addOrders</h1>
	<!-- customer meun include -->
	<c:import url="/WEB-INF/view/inc/customerMenu.jsp"></c:import>
	<hr>
	
	<div>
		<form method="post" action="${pageContext.request.contextPath}/customer/addOrders">
			<table border="1">
				<tr>
					<th>filename</th>
					<th>goodsName</th>
					<th>goodsPrice</th>
					<th>pointRate</th>
					<th>cartQuantity</th>
				</tr>
				<c:forEach var="m" items="${list}">
					<input type="hidden" name="goodsCode" value="${m.goodsCode}">
					<input type="hidden" name="orderQuantity" value="${m.cartQuantity}">
					<input type="hidden" name="goodsPrice" value="${m.goodsPrice}">
					<tr>
						<td><img src="${pageContext.request.contextPath}/upload/${m.filename}"></td>
						<td>${m.goodsName}</td>
						<td>${m.goodsPrice}</td>
						<td>${m.pointRate}</td>
						<td>${m.cartQuantity}</td>
					</tr>
				</c:forEach>
			</table>
			
			<div>
				배송지 선택:
				<select id="addressList" size="5">
					<c:forEach var="addr" items="${addressList}">
						<option class="addrOpt" value="${addr.addressCode}">${addr.address}</option>
					</c:forEach>
				</select>
				
				<input type="text" id="addressCode" name="addressCode" id="addressCode" readonly>
				<input type="text" id="address" readonly>
			</div>
			<div>
				<div>
					결제금액 : 
					<input type="number" name="orderPrice" value="${orderPrice}" readonly>
					<!-- 결제 테이블을 별도로 생성해서 결제코드/결제일자/결제금액/포인트사용금액/..... -->
				</div>
			</div>
			<div>
				<!-- 3) -->
				<button type="submit">결제하기(주문완료)</button>
			</div>
		</form>
	</div>
	<script>
		$('#addressList').dblclick(function() {
			$('#addressCode').val($('#addressList').val());
			$('#address').val($('.addrOpt:selected').text());
		});
	</script>
</body>
</html>