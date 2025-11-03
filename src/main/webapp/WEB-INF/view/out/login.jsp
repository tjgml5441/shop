<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<style>
	/* 1. 기본 레이아웃 및 중앙 정렬 */
	body {
	    font-family: Arial, sans-serif;
	    background-color: #f0f0f0;
	    display: flex;
	    justify-content: center;
	    align-items: center;
	    height: 100vh;
	    margin: 0;
	}
	
	/* 2. 로그인 창 (컨테이너) - 크기 및 패딩 재조정 */
	.login-container {
	    background: white;
	    padding: 35px 50px; /* 상하 35px, 좌우 50px로 패딩 늘림 */
	    border-radius: 10px;
	    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
	    width: 550px; /* ★★★ 가로 길이를 550px로 변경 ★★★ */
	    min-height: 330px; /* ★★★ 최소 높이를 330px로 추가 ★★★ */
	    text-align: center;
        box-sizing: border-box; /* 패딩이 전체 크기에 포함되도록 설정 */
	}
	
	/* 3. 제목 스타일 */
	h1 {
	    font-size: 30px;
	    color: #333;
	    margin-bottom: 5px; /* 제목과 설명 사이 간격 줄임 */
	    font-weight: bold;
	    /*border-bottom: 2px solid #5cb85c;*/ /* 이미지에 하단선이 보이지 않아 주석 처리 */
	    display: block; /* 블록 요소로 변경 */
	    padding-bottom: 0;
	    text-align: center;
	}
    /* 아이디와 비밀번호를 입력하세요 텍스트 */
    .login-container > p {
        font-size: 14px;
        color: #777;
        margin-top: 0;
        margin-bottom: 30px; /* 제목 그룹과 라디오 버튼 사이 간격 넓힘 */
    }
	
	/* 4. 라디오 버튼 그룹 */
	.user-type-group {
	    margin: 0 0 10px 0; /* 아래쪽 마진 늘림 */
	    padding: 0;
	    border: none; 
	    background-color: transparent; 
	    display: flex;
	    justify-content: flex-start; /* 중앙 정렬로 변경 */
	    gap: 30px; 
	}
	
	.user-type-group label {
	    font-size: 14px;
	    color: #555;
	    cursor: pointer;
	    font-weight: bold;
	}
	
	/* 5. 입력 필드 그룹 */
	.input-group {
	    /* 비밀번호 입력폼의 아래쪽 간격을 줄입니다. (15px -> 5px) */
	    margin-bottom: 5px; 
	    text-align: center; 
	    display: block; 
	    width: 100%;
	}
	
	/* 6. 회원가입 / 아이디&비밀번호 찾기 그룹 스타일 */
    .find-signup-group {
        width: 100%;
        display: flex;
        justify-content: space-between; /* 좌측, 우측으로 요소 분리 */
        align-items: center;
        /* 입력폼과 간격을 줄이기 위해 margin-top을 5px -> 0px으로 변경 */
        margin-top: 0px; 
        margin-bottom: 20px; /* 로그인 버튼 위 간격 유지 */
    }
	
    /* HTML에서 라벨을 제거했으므로, 라벨 관련 CSS는 더 이상 필요하지 않습니다. */
    .input-group label {
        display: none; 
    }
	
	.input-group input[type="text"],
	.input-group input[type="password"] {
	    /* 입력 필드 스타일 */
	    width: 100%; 
	    padding: 12px 15px; 
	    border: 1px solid #ddd;
	    border-radius: 5px;
	    box-sizing: border-box; 
	    font-size: 13px;
        text-align: left; 
	}
	
    .find-signup-group a {
        display: inline;
        color: black;
        text-decoration: none;
        font-size: 13px;
    }
    
    .find-signup-group a:hover {
        text-decoration: underline;
        color: #9f8473;
    }
	
	/* 7. 버튼 및 기타 요소 */
	.login-btn {
	    width: 100%;
	    padding: 14px; /* 버튼 패딩 유지 */
	    background-color: #dfd3c3; 
	    color: white;
	    border: none;
	    border-radius: 40px;
	    cursor: pointer;
	    font-size: 18px;
	    font-weight: normal;
	    margin-top: 10px; /* 버튼 상단 마진 유지 */
	    transition: background-color 0.3s;
	}
	
	.login-btn:hover {
	    background-color: #c7b199;
	}
	
	.message {
	    color: #d9534f;
	    text-align: center;
	    margin-bottom: 10px;
	    font-weight: bold;
	    padding: 8px;
	}
	
</style>
</head>
<body>
   <%-- WEB-INF/view/out/login.jsp (<body> 태그 내부 수정) --%>
    <div class="login-container"> 
        <h1>LOG IN</h1>
        <p>아이디와 비밀번호를 입력하세요<p>

        <c:if test="${not empty message}">
            <p class="message">${message}</p>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/out/login">
            
            <div class="user-type-group">
                <label>
                    <input type="radio" name="customerOrEmpSel" value="customer" checked> CUSTOMER
                </label>
                <label>
                    <input type="radio" name="customerOrEmpSel" value="emp"> EMPLOYEE
                </label>
            </div>
            
            <div class="input-group">
                <input type="text" name="id" id="id" required placeholder="아이디">
            </div>
            
            <div class="input-group">
                <input type="password" name="password" id="password" required placeholder="비밀번호">
            </div>
            
            <div class="find-signup-group">
            	<a href="${pageContext.request.contextPath}/out/addCustomer" class="signup-link">회원가입</a>
            	<div class="find-group">
            		<a href="#" class="find-link">아이디/비밀번호 찾기</a>
            	</div>
            </div>
            <button type="submit" class="login-btn">로그인</button>
            
        </form>

    </div>
</body>
</html>