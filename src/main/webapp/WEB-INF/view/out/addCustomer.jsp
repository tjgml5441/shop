<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop - 회원가입</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<style>
	body {
	    font-family: Arial, sans-serif;
	    background-color: #f0f0f0;
	    display: flex;
	    justify-content: center;
	    align-items: center;
	    height: 100vh;
	    margin: 0;
	}
	
	.login-container { 
	    background: white;
	    padding: 30px 50px; 
	    border-radius: 10px;
	    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
	    width: 500px; /* ★★★ 가로 길이를 500px로 수정 ★★★ */
        box-sizing: border-box;
	}
	
	h1 {
	    font-size: 24px;
	    color: #333;
	    margin-bottom: 5px; 
	    font-weight: bold;
	    display: block; 
	    padding-bottom: 0;
	    text-align: center;
	}

    .login-container > p {
        font-size: 14px;
        color: #777;
        margin-top: 0;
        margin-bottom: 30px; 
        text-align: center;
    }
	
	/* 메시지 스타일 */
	.message {
	    color: #d9534f;
	    text-align: center;
	    margin-bottom: 10px;
	    font-weight: bold;
	    padding: 8px;
	}
	.input-group {
	    margin-bottom: 15px; 
	    width: 100%;
        text-align: left; 
	}
    
    .input-group label {
        display: block; 
        font-size: 14px;
        color: #555;
        font-weight: bold;
        margin-bottom: 5px; 
        width: auto; 
    }

	.input-group input[type="text"],
	.input-group input[type="password"] {
	    width: 100%; /* 부모 컨테이너(500px)에 맞춰 100% 사용 */
	    padding: 12px 15px; 
	    border: 1px solid #ddd;
	    border-radius: 5px;
	    box-sizing: border-box; 
	    font-size: 13px;
        text-align: left; 
	}
    .button-group {
        display: flex;
        justify-content: space-between; 
        gap: 10px;
        margin-top: 25px; 
    }

	.signup-btn, .cancel-btn {
	    width: 50%; 
	    padding: 14px; 
	    color: white;
	    border: none;
	    border-radius: 40px; 
	    cursor: pointer;
	    font-size: 16px; 
	    font-weight: bold;
	    transition: background-color 0.3s;
	}

	.signup-btn {
	    background-color: #dfd3c3; 
	}
	
	.signup-btn:hover {
	    background-color: #c7b199;
	}
    
    .cancel-btn {
        background-color: #777; 
    }
    
    .cancel-btn:hover {
        background-color: #555;
    }
</style>
</head>
<body>
    <div class="login-container"> 
        <h1>SIGN IN</h1>
        <p>회원가입에 필요한 필수 정보를 입력하세요<p>

        <c:if test="${not empty message}">
            <p class="message">${message}</p>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/out/addCustomer" id="signupForm">
            
            <div class="input-group">
                <label for="id">아이디</label>
                <input type="text" name="id" id="id" required placeholder="아이디" value="${id}">
            </div>
            
            <div class="input-group">
                <label for="password">비밀번호</label>
                <input type="password" name="password" id="password" required placeholder="비밀번호">
            </div>

            <div class="input-group">
                <label for="passwordCheck">비밀번호 확인</label>
                <input type="password" name="passwordCheck" id="passwordCheck" required placeholder="비밀번호 확인">
            </div>

            <div class="input-group">
                <label for="name">이름</label>
                <input type="text" name="name" id="name" required placeholder="이름" value="${name}">
            </div>

            <div class="input-group">
                <label for="phone">전화번호</label>
                <input type="text" name="phone" id="phone" required placeholder="전화번호" value="${phone}">
            </div>
            
        </form>
        
        <div class="button-group">
            <button type="submit" form="signupForm" class="signup-btn">회원가입</button>
            <button type="button" class="cancel-btn" onclick="location.href='${pageContext.request.contextPath}/out/login'">취소</button>
        </div>

    </div>
</body>
</html>