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
	.table-container {
        margin-top: 20px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        border-radius: 8px;
        overflow: hidden;
    }
	table {
	    width: 100%;
	    border-collapse: collapse;
	}
	th, td {
	    border: none;
	    border-bottom: 1px solid #ddd;
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
        display: inline-block;
        padding: 5px 10px;
        border-radius: 5px;
        text-decoration: none;
        color: white;
        font-weight: bold;
        transition: background-color 0.3s;
    }
    /* 주문 상태별 색상 (예시) */
    .active-1 { background-color: #007bff; } /* 주문 완료 */
    .active-2 { background-color: #ffc107; color: #333; } /* 배송 준비중 */
    .active-3 { background-color: #28a745; } /* 배송 중 */
    .active-4 { background-color: #6c757d; } /* 배송 완료 */
    .active-5 { background-color: #dc3545; } /* 취소 */
</style>
</head>
<body>
	
	<div class="top-bar">
        <div></div> 
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

	<div class="table-container">
		<table>
			<thead>
				<tr>
					<th>주문코드</th>
					<th>상품명</th>
					<th>주문 수량</th>
					<th>주문 가격</th>
					<th>주문자</th>
					<th>연락처</th>
					<th>배송지 주소</th>
					<th>주문일자</th>
					<th>주문상태</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="m" items="${list}">
					<tr>
						<td>${m.orderCode}</td>
						<td>${m.goodsName}</td>
						<td>${m.orderQuantity}</td>
						<td>${m.orderPrice}</td>
						<td>${m.customerName}</td>
						<td>${m.customerPhone}</td>
						<td>${m.address}</td>
						<td>${m.createdate}</td>
						<td>
							<%-- 주문 상태 변경 링크에 active-btn 스타일 적용 --%>
                            <a href="${pageContext.request.contextPath}/emp/modifyOrderState?orderCode=${m.orderCode}&orderState=${m.orderState}" 
                               class="active-btn active-1">
                               ${m.orderState}
                            </a>
						</td>
					</tr>
				</c:forEach>
                <c:if test="${empty list}">
                    <tr>
                        <td colspan="9">조회된 주문 내역이 없습니다.</td>
                    </tr>
                </c:if>
			</tbody>
		</table>
	</div>
    
    <%-- 페이지네이션 영역 --%>
    <div class="pagination">
        <%-- [이전] 버튼: startPage가 1보다 클 때 --%>
        <c:if test="${startPage > 1}">
            <a href="${pageContext.request.contextPath}/emp/ordersList?currentPage=${startPage - 1}">이전</a>
        </c:if>
        
        <%-- 페이지 번호 출력: startPage부터 endPage까지 --%>
        <c:forEach var="p" begin="${startPage}" end="${endPage}">
            <c:choose>
                <c:when test="${p eq currentPage}">
                    <span class="current-page">${p}</span>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/emp/ordersList?currentPage=${p}">${p}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        
        <%-- [다음] 버튼: endPage가 lastPage보다 작을 때 --%>
        <c:if test="${endPage < lastPage}">
            <a href="${pageContext.request.contextPath}/emp/ordersList?currentPage=${endPage + 1}">다음</a>
        </c:if>
	</div>
</body>
</html>