<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop - 상품 관리</title>
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
    /* goodsList 전용 스타일: 상품 추가 버튼을 add-link와 유사하게 설정 */
    .btn-add-goods {
        display: inline-block;
        padding: 8px 15px;
        background-color: #c7b199; /* add-link 색상 사용 */
        color: white;
        text-decoration: none;
        font-weight: normal;
        font-size: 15px;
        border-radius: 5px; /* customerList 상단 버튼과 유사하게 */
        transition: background-color 0.3s;
    }
    .btn-add-goods:hover {
        background-color: #6c5d53;
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

	<h1>상품 관리</h1>
	
	<c:import url="/WEB-INF/view/inc/empMenu.jsp"></c:import>
	
	<div>
		<h2 style="display: inline-block; margin-right: 15px;">상품 정보 리스트</h2>
        <a href="${pageContext.request.contextPath}/emp/addGoods" class="btn-add-goods" style="float: right;">상품추가</a>
        
		<table class="list-table">
            <thead>
                <tr>
                    <th>goodsCode</th>
                    <th>상품이미지</th> <th>goodsName</th>
                    <th>goodsPrice</th>
                    <th>pointRate</th>
                    <th>soldout</th>
                    <th>createdate</th>
                    <th>수정/삭제</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty goodsList}">
                        <c:forEach var="goods" items="${goodsList}">
                            <tr>
                                <td>${goods.goodsCode}</td>
                                <td>
                                    <c:if test="${not empty goods.fileName}">
                                        <img src="${pageContext.request.contextPath}/upload/${goods.fileName}" 
                                             alt="${goods.goodsName} 이미지" 
                                             style="width: 50px; height: 50px; object-fit: cover;" />
                                    </c:if>
                                </td>
                                <td>${goods.goodsName}</td>
                                <td>${goods.goodsPrice}</td>
                                <td>${goods.pointRate}</td>
                                <td>${goods.soldout}</td>
                                <td>${goods.createdate}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/emp/modifyGoods?goodsCode=${goods.goodsCode}"
                                       class="active-btn active-1">수정</a>
                                    <a href="${pageContext.request.contextPath}/emp/removeGoods?goodsCode=${goods.goodsCode}"
                                       class="active-btn active-0">삭제</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7">등록된 상품이 없습니다.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
			</tbody>
		</table>
        
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/emp/goodsList?currentPage=${currentPage - 1}">이전</a>
            </c:if>
            <c:forEach var="p" begin="1" end="${lastPage}">
                <c:choose>
                    <c:when test="${p eq currentPage}">
                        <span class="current-page">${p}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/emp/goodsList?currentPage=${p}">${p}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${currentPage < lastPage}">
                <a href="${pageContext.request.contextPath}/emp/goodsList?currentPage=${currentPage + 1}">다음</a>
            </c:if>
        </div>
	</div>	
</body>
</html>