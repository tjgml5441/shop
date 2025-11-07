<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지 상세</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
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
	.detail-table-container {
        max-width: 800px; /* 상세 페이지이므로 너비를 조금 늘림 */
        margin: 20px auto;
        padding: 20px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        border-radius: 8px;
        background-color: #fff;
    }
    .detail-table {
        width: 100%;
        border-collapse: collapse;
    }
    .detail-table th, .detail-table td {
        border: 1px solid #ddd;
        padding: 10px;
        text-align: left;
    }
    .detail-table th {
        background-color: #f2f2f2;
        width: 15%;
        font-weight: bold;
    }
    .detail-content {
        white-space: pre-wrap; /* 내용에 줄바꿈 유지 */
        min-height: 200px;
    }
    
    .button-group {
        text-align: center;
        margin-top: 20px;
    }
    .button-group a {
        padding: 10px 20px;
        text-decoration: none;
        color: white;
        border-radius: 5px;
        margin: 0 5px;
        font-weight: bold;
        transition: background-color 0.3s;
        display: inline-block;
    }
    .btn-modify {
        background-color: #007bff;
    }
    .btn-modify:hover {
        background-color: #0056b3;
    }
    .btn-delete {
        background-color: #dc3545;
    }
    .btn-delete:hover {
        background-color: #c82333;
    }
    .btn-list {
        background-color: #6c757d;
    }
    .btn-list:hover {
        background-color: #5a6268;
    }
</style>
</head>
<body>
	<div class="top-bar">
	        <div class="btn-group">
	            <a href="${pageContext.request.contextPath}/emp/empIndex" class="btn-home">HOME</a>
	        </div>
	        <div class="btn-group">
	            <c:choose>
	                <c:when test="${sessionScope.loginEmp != null}">
	                    <span>${sessionScope.loginEmp.empName}님</span>
	                    <a href="${pageContext.request.contextPath}/emp/empLogout" class="btn-logout">로그아웃</a>
	                </c:when>
	                <c:otherwise>
	                    <a href="${pageContext.request.contextPath}/out/login" class="btn-login">로그인</a>
	                </c:otherwise>
	            </c:choose>
	        </div>
	    </div>

	<h1>공지 상세</h1>
	
	<c:import url="/WEB-INF/view/inc/empMenu.jsp"></c:import>
	
	<c:if test="${not empty noticeOne}">
	<div class="detail-table-container">
	    <table class="detail-table">
	        <tr>
	            <th>제목</th>
	            <td colspan="3">${noticeOne.noticeTitle}</td>
	        </tr>
	        <tr>
	            <th>공지 코드</th>
	            <td>${noticeOne.noticeCode}</td>
	            <th>작성자</th>
	            <td>${noticeOne.empCode}</td>
	        </tr>
	        <tr>
	            <th>작성일</th>
	            <td colspan="3">${noticeOne.createdate}</td>
	        </tr>
	        <tr>
	            <th>내용</th>
	            <td colspan="3" class="detail-content">${noticeOne.noticeContent}</td>
	        </tr>
	    </table>
	    
	    <div class="button-group">
	        <a href="${pageContext.request.contextPath}/emp/modifyNotice?noticeCode=${noticeOne.noticeCode}" class="btn-modify">수정</a>
	        <a href="${pageContext.request.contextPath}/emp/removeNotice?noticeCode=${noticeOne.noticeCode}" class="btn-delete" 
	           onclick="return confirm('정말로 이 공지사항을 삭제하시겠습니까?');">삭제</a>
	        <a href="${pageContext.request.contextPath}/emp/noticeList" class="btn-list">목록으로</a>
	    </div>
	</div>
	</c:if>
	<c:if test="${empty noticeOne}">
	    <div class="detail-table-container" style="text-align: center; padding: 50px;">
	        <p>요청하신 공지사항을 찾을 수 없습니다.</p>
	        <a href="${pageContext.request.contextPath}/emp/noticeList" class="btn-list">목록으로 돌아가기</a>
	    </div>
	</c:if>
</body>
</html>