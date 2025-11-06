<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop - 상품 추가</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<style>
/* customerList.jsp의 전체 스타일 적용 */
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

	th, thead td {
	    background-color: #dfd3c3;
	    border-bottom: 1.5px solid #a09483;
	    font-weight: bold;
	}
	
	table tbody tr:last-child td {
	    border-bottom: none;
	}
	
	table tbody tr:hover {
	    background-color: #f5f5f5;
	    cursor: pointer;
	}
	.pagination {
	    text-align: center;
	    margin-top: 20px;
	}
	.pagination a, .pagination span {
	    padding: 8px 12px;
	    margin: 0 4px;
	    border: 1px solid #ddd;
	    text-decoration: none;
	    color: #9f8473;
	    border-radius: 20px;
	}
	.pagination .current-page {
	    background-color: #9f8473;
	    color: white;
	    border-color: #9f8473;
	    font-weight: bold;
	}
	.active-btn {
	    color: #fff;
	    padding: 5px 10px;
	    border-radius: 3px;
	    text-decoration: none;
	    display: inline-block;
	    font-size: 14px;
	}
	.active-1 { /* 활성화 */
	    background-color: #9f8473;
	    transition: background-color 0.3s;
	}
	.active-1:hover {
	    background-color: #6c5d53;
	}
	.active-0 { /* 비활성화 */
	    background-color: #dc3545;
	    transition: background-color 0.3s;
	}
	.active-0:hover {
	    background-color: #8c1d28;
	}
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

    /* addGoods 전용 스타일: 폼 테이블 스타일 */
    .form-table-container {
        max-width: 600px;
        margin: 20px auto;
        padding: 20px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05); /* 테이블과 구분되는 약한 그림자 */
        border-radius: 8px;
        background-color: #fff;
    }
    .form-table tr td:first-child {
        background-color: #f2f2f2;
        font-weight: bold;
        text-align: left;
        width: 30%;
        border-right: 1px solid #ccc;
    }
    .form-table tr td:last-child {
        text-align: left;
    }
    input[type="text"], input[type="number"], input[type="file"] {
        width: 95%;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }
    .btn-submit {
        display: block;
        width: 100%;
        padding: 10px;
        margin-top: 20px;
        background-color: #9f8473; /* active-1 색상 사용 */
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-size: 16px;
        font-weight: bold;
        transition: background-color 0.3s;
    }
    .btn-submit:hover {
        background-color: #6c5d53; /* active-1 hover 색상 사용 */
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

	<h1>상품 추가</h1>
	
	<c:import url="/WEB-INF/view/inc/empMenu.jsp"></c:import>

    <div class="form-table-container">
	    <form enctype="multipart/form-data" action="${pageContext.request.contextPath}/emp/addGoods" method="post" >
	    	<table class="form-table">
	    		<tr>
	    			<td>goodsName</td>
	    			<td><input type="text" name="goodsName" required></td>
	    		</tr>
	    		<tr>
	    			<td>goodsPrice</td>
	    			<td><input type="number" name="goodsPrice" required min="100"></td>
	    		</tr>
	    		<tr>
	    			<td>pointRate</td>
	    			<td><input type="number" name="pointRate" required min="0" max="10"></td>
	    		</tr>
	    		<tr>
	    			<td>goodsImg (png / jpg / gif 확장자)</td>
	    			<td><input type="file" name="goodsImg" required accept=".png,.jpg,.jpeg,.gif"></td>
	    		</tr>
	    	</table>
	    	<button type="submit" class="btn-submit">상품등록</button>
	    </form>
	</div>
</body>
</html>