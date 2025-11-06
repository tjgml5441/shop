<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop - 주문 관리</title>
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
	.active-1 { /* 활성화/성공 색상 */
	    background-color: #9f8473;
	    transition: background-color 0.3s;
	}
	.active-1:hover {
	    background-color: #6c5d53;
	}
	.active-0 { /* 비활성화/위험 색상 */
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
    .btn-search {
        padding: 6px 12px;
        background-color: #9f8473;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-size: 14px;
        font-weight: bold;
        transition: background-color 0.3s;
        margin-left: 5px; 
    }
    .btn-search:hover {
        background-color: #6c5d53;
    }
    .action-btn {
        display: inline-block;
        padding: 5px 10px;
        border-radius: 5px;
        text-decoration: none;
        color: white;
        font-size: 13px;
        font-weight: bold;
        transition: background-color 0.3s;
        margin: 2px;
    }
    .btn-order {
        background-color: #6c757d;
    }
    .btn-order:hover {
        background-color: #5a6268;
    }
    .btn-force-out {
        background-color: #dc3545;
    }
    .btn-force-out:hover {
        background-color: #c82333;
    }
</style>
</head>

<body>
    <%-- customerList.jsp와 동일한 상단바 구조 추가 --%>
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

	<h1>주문 관리</h1>
	
	<c:import url="/WEB-INF/view/inc/empMenu.jsp"></c:import>
	
	<div>
		<h2>전체 주문 리스트</h2>
	    <%-- border="1" 제거 --%>
		<table>
			<thead>
                <%-- th 대신 td 사용 --%>
                <tr>
                    <td>orderCode</td>
                    <td>goodsName</td>
                    <td>goodsPrice</td>
                    <td>orderQuantity</td>
                    <td>orderPrice</td>
                    <td>customerName</td>
                    <td>customerPhone</td>
                    <td>address</td>
                    <td>createdate</td>
                    <td>orderState</td>
                </tr>
			</thead>
			<tbody>
				<c:forEach var="m" items="${list}">
					<tr>
						<td>${m.orderCode}</td>
						<td>${m.goodsName}</td>
						<td>${m.goodsPrice}</td>
						<td>${m.orderQuantity}</td>
						<td>${m.orderPrice}</td>
						<td>${m.customerName}</td>
						<td>${m.customerPhone}</td>
						<td>${m.address}</td>
						<td>${m.createdate}</td>
						<td>
                            <%-- 주문 상태 링크에 active-btn 스타일 적용 --%>
                            <a href="${pageContext.request.contextPath}/emp/modifyOrderState?orderCode=${m.orderCode}&orderState=${m.orderState}" 
                               class="active-btn active-1">
                               ${m.orderState}
                            </a>
                        </td>
					</tr>
				</c:forEach>
                <c:if test="${empty list}">
                    <tr>
                        <td colspan="10">조회된 주문 내역이 없습니다.</td>
                    </tr>
                </c:if>
			</tbody>
		</table>
	</div>
    
    <%-- 페이지네이션 영역도 customerList와 동일하게 임시 적용 --%>
    <div class="pagination">
        <c:if test="${currentPage > 1}">
            <a href="${pageContext.request.contextPath}/emp/ordersList?currentPage=${currentPage - 1}">이전</a>
        </c:if>
        <c:forEach var="p" begin="1" end="${lastPage}">
            <c:choose>
                <c:when test="${p eq currentPage}">
                    <span class="current-page">${p}</span>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/emp/ordersList?currentPage=${p}">${p}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${currentPage < lastPage}">
            <a href="${pageContext.request.contextPath}/emp/ordersList?currentPage=${currentPage + 1}">다음</a>
        </c:if>
    </div>
</body>
</html>