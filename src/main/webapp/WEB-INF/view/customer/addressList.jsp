<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<style>
	/* 🎨 empList.jsp의 top-bar 스타일 */
	.top-bar {
	    display: flex;
	    justify-content: space-between;
	    align-items: center;
	    margin-bottom: 10px;
	    padding: 0 5px; /* 내부 여백 조절 */
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
	.btn-home {
	    background-color: #333;
	}
	.btn-home:hover {
	    background-color: #555;
	}
	.btn-logout {
	    background-color: #dc3545;
	}
	.btn-logout:hover {
	    background-color: #c82333;
	}
	.btn-login {
	    background-color: #007bff;
	}
	.btn-login:hover {
	    background-color: #0056b3;
	}
	
	body {
	    font-family: Arial, sans-serif;
	    padding: 20px;
	}
		
	h1 {
	    color: #333;
	    text-align: center;
	    margin-top: 0;
	}
	hr {
	    border: 0;
	    border-top: 1px solid #ccc;
	    margin: 20px 0;
	}
	table {
	    width: 100%;
	    border-collapse: collapse;
	    margin-top: 10px;
	    border-radius: 8px;
	    overflow: hidden;
	    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
	}
		
	th, td {
	    border: none;
	    border-bottom: 1px solid #ccc;
	    padding: 10px;
	    text-align: center;
	}
	th {
	    background-color: #dfd3c3;
	    border-bottom: 1.5px solid #a09483;
	}
		
	table tbody tr:last-child td {
	    border-bottom: none;
	}
		
	table tbody tr:hover {
	    background-color: #f5f5f5;
	    cursor: pointer;
	}
		
	/* 배송지 추가 링크 스타일 */
	.add-link {
	    display: inline-block;
	    margin-top: 10px;
	    padding: 8px 15px;
	    background-color: #c7b199;
	    color: white;
	    text-decoration: none;
	    font-weight: normal;
	    font-size: 15px;
	    border-radius: 10px;
	    transition: background-color 0.3s;
	}
	.add-link:hover {
	    background-color: #6c5d53;
	}
	
	/* 🎨 수정/삭제 버튼 스타일 */
	.action-btn-base {
	    padding: 5px 10px;
	    color: white;
	    border: none; /* 버튼 태그를 사용하기 때문에 border:none 추가 */
	    border-radius: 5px;
	    margin: 0 3px; 
	    font-size: 14px;
	    cursor: pointer; /* 버튼이므로 커서 변경 */
	    transition: background-color 0.3s;
	}
	
	/* 수정 버튼 스타일 (파란색) */
	.btn-edit {
	    background-color: #007bff;
	}
	.btn-edit:hover {
	    background-color: #0056b3;
	}
	
	/* 삭제 버튼 스타일 (빨간색) */
	.btn-delete {
	    background-color: #dc3545;
	}
	.btn-delete:hover {
	    background-color: #c82333;
	}
		
	/* 등록된 배송지가 없을 때 메시지 스타일 */
	p {
	    text-align: center;
	    padding: 20px;
	    color: #555;
	}
		
	/* 에러 메시지 스타일 */
	.error-message {
	    text-align: center;
	    color: red;
	    font-weight: bold;
	    margin-top: 15px;
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
                    <span>${sessionScope.loginCustomer.customerName}님</span>
                    <a href="${pageContext.request.contextPath}/customer/customerLogout" class="btn-logout">로그아웃</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/out/login" class="btn-login">로그인</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    
	<h1>배송지 관리</h1>
    <c:import url="/WEB-INF/view/inc/customerMenu.jsp"></c:import>
	
    <div style="text-align: right;">
		<a href="${pageContext.request.contextPath}/customer/addAddress" class="add-link">배송지추가</a>
	</div>

	<c:choose>
		<c:when test="${not empty addressList}">
			<table>
				<thead>
					<tr>
						<th>순번</th>
						<th>배송지코드</th>
						<th>주소</th>
						<th>등록일</th>
						<th>관리</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="addr" items="${addressList}" varStatus="status">
						<tr>
							<td>${status.count}</td>
							<td>${addr.addressCode}</td>
							<td>${addr.address}</td>
							<td>${addr.createdate}</td>
							<td>
								<button type="button" class="action-btn-base btn-edit">수정</button>
								<button type="button" class="action-btn-base btn-delete">삭제</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<p>등록된 배송지가 없습니다.</p>
		</c:otherwise>
	</c:choose>
	
	<c:if test="${not empty errorMessage}">
        <p class="error-message">${errorMessage}</p>
    </c:if>
	
</body>
</html>