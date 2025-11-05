<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop - 강제 탈퇴 처리</title>
<style>
body {
    font-family: Arial, sans-serif;
    padding: 20px;
}
.top-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
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

/* ★★★ removeCustomerByEmp.jsp에 필요한 스타일 추가/수정 ★★★ */

/* 폼 영역 스타일링 */
div {
    max-width: 600px;
    margin: 0 auto;
    margin-top: 20px;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

h2 {
    color: #9f8473;
    border-bottom: 2px solid #dfd3c3;
    padding-bottom: 10px;
    margin-top: 0;
    margin-bottom: 20px;
}

/* 레이블과 입력 필드 간격 */
label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
    color: #555;
}

/* 텍스트 영역 스타일 */
textarea {
    width: 100%;
    padding: 10px;
    margin-bottom: 20px;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box; /* 패딩이 너비에 포함되도록 설정 */
    resize: vertical; /* 수직 방향으로만 크기 조절 허용 */
}

/* 버튼 스타일 */
button[type="submit"], button[type="button"] {
    padding: 10px 15px;
    margin-right: 10px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-weight: bold;
    transition: background-color 0.3s;
}

/* 강제 탈퇴 처리 버튼 (위험 색상) */
button[type="submit"] {
    background-color: #dc3545; /* 빨간색 계열 */
    color: white;
}
button[type="submit"]:hover {
    background-color: #c82333;
}

/* 취소 버튼 (중립 색상) */
button[type="button"] {
    background-color: #6c757d; /* 회색 계열 */
    color: white;
}
button[type="button"]:hover {
    background-color: #5a6268;
}
</style>
</head>
<body>
	<h1>고객 강제 탈퇴</h1>
	
	<c:import url="/WEB-INF/view/inc/empMenu.jsp"></c:import>
	<div>
		<h2>고객 ${customerId} 강제 탈퇴 사유 입력</h2>
        
		<form method="post" action="${pageContext.request.contextPath}/emp/removeCustomerByEmp">
			<input type="hidden" name="customerId" value="${customerId}">
			
			<p>
				<label for="reason">탈퇴 사유:</label>
				<textarea id="reason" name="reason" rows="5" required></textarea>
			</p>
			
			<button type="submit" onclick="return confirm('정말로 고객 ${customerId}를 강제 탈퇴 시키겠습니까?');">강제 탈퇴 처리</button>
			<button type="button" onclick="location.href='${pageContext.request.contextPath}/emp/customerList'">취소</button>
		</form>
	</div>

</body>
</html>