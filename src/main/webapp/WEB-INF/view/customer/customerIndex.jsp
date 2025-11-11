<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<style>
/* 🎨 공통 스타일 */
body {
    font-family: Arial, sans-serif;
    padding: 20px;
}

/* 🎨 Top-Bar 스타일 */
.top-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    padding: 0 5px;
}
.top-bar .btn-group a {
    padding: 5px 10px;
    text-decoration: none;
    color: white;
    border-radius: 5px;
    margin-left: 10px;
    font-size: 14px;
    transition: background-color 0.3s;
}
.btn-home { background-color: #333; }
.btn-home:hover { background-color: #555; }
.btn-logout { background-color: #dc3545; }
.btn-logout:hover { background-color: #c82333; }
.btn-login { background-color: #007bff; }
.btn-login:hover { background-color: #0056b3; }

h1 {
    color: #333;
    text-align: center;
    margin-top: 0;
}

h2 {
    color: #333;
    margin-top: 25px;
    border-bottom: 2px solid #dfd3c3;
    padding-bottom: 5px;
}

hr {
    border: 0;
    border-top: 1px solid #ccc;
    margin: 20px 0;
}

/* 🎨 상품 목록 테이블 스타일 */
.goods-list-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 10px;
    table-layout: fixed;
}
.goods-item-td {
    padding: 10px;
    vertical-align: top;
    text-align: center;
    width: 20%; /* 5개씩 배치 */
    border: none;
    transition: background-color 0.3s;
}
.goods-item-td:hover {
    background-color: #f5f5f5;
    cursor: pointer;
}
.goods-info {
    padding: 5px 0;
    font-size: 14px;
}
.goods-info a {
    color: #9f8473; /* empList의 통일된 색상 */
    text-decoration: none;
    font-weight: bold;
    display: block;
    margin-bottom: 3px;
}
.goods-info a:hover {
    color: #6c5d53;
    text-decoration: underline;
}

</style>
</head>
<body>
    <div class="top-bar">
        <div class="btn-group">
            <a href="${pageContext.request.contextPath}/customer/customerIndex" class="btn-home">HOME</a>
        </div>
        <div class="btn-group">
            <c:choose>
                <c:when test="${sessionScope.loginCustomer != null}">
                    <span>${loginCustomer.customerName}님 (point : ${loginCustomer.point})</span>
                    <a href="${pageContext.request.contextPath}/customer/customerLogout" class="btn-logout">로그아웃</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/out/login" class="btn-login">로그인</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
	<h1>customerIndex</h1>
	<c:import url="/WEB-INF/view/inc/customerMenu.jsp"></c:import>
	
	<h2>베스트 상품목록</h2>
		${bestGoodsList}
	<div>
		베스트상품(5개)
	</div>
	
	<h2>상품목록</h2>
	<div>
		<table class="goods-list-table">
			<tr>
				<c:forEach var="m" items="${goodsList}" varStatus="state">					
					<td class="goods-item-td">
						<div>
							<img src="${pageContext.request.contextPath}/upload/${m.filename}" width="200" height="200">
						</div>
						<div class="goods-info">
							<a href="${pageContext.request.contextPath}/customer/goodsOne?goodsCode=${m.goodsCode}">
							${m.goodsName}<br>
							</a>
							${m.goodsPrice}원
							<br>
							<c:if test="${state.last}">
							</c:if>
						</div>
					</td>
					<c:if test="${state.last == false && state.count % 5 == 0}">
						</tr><tr>
					</c:if>
				</c:forEach>
				
			</tr>
		</table>
	</div>
</body>
</html>