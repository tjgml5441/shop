<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop - 고객 관리</title>
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
    /* 1. 조회 버튼 CSS 추가 */
    .btn-search {
        padding: 6px 12px;
        background-color: #9f8473; /* Pagination active color */
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
        background-color: #6c5d53; /* Darker shade */
    }

    /* 2. 기능 탭의 버튼 CSS 추가 */
    .action-btn {
        display: inline-block;
        padding: 5px 10px;
        border-radius: 5px;
        text-decoration: none;
        color: white;
        font-size: 13px;
        font-weight: bold;
        transition: background-color 0.3s;
        margin: 2px; /* Added margin for spacing between buttons */
    }
    .btn-order {
        background-color: #6c757d; /* Neutral Gray */
    }
    .btn-order:hover {
        background-color: #5a6268;
    }
    .btn-force-out {
        background-color: #dc3545; /* Red from .btn-logout */
    }
    .btn-force-out:hover {
        background-color: #c82333;
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

	<h1>고객 관리</h1>
	
	<c:import url="/WEB-INF/view/inc/empMenu.jsp"></c:import>
	
	<div>
		<h2>고객 정보 리스트</h2>
        
        <c:if test="${not empty param.msg}">
            <p style="color: blue;">${param.msg}</p>
        </c:if>

		<form method="get" action="${pageContext.request.contextPath}/emp/customerList" style="margin-bottom: 10px;">
            <%-- 라디오 버튼 추가 --%>
            <label style="margin-right: 15px;">
                <%-- type=active: 활성 고객 (CUSTOMER 테이블) --%>
                <input type="radio" name="type" value="active" ${type eq 'active' ? 'checked' : ''}> 전체 회원 (활성)
            </label>
            <label style="margin-right: 15px;">
                <%-- type=force_out: 강제 탈퇴 고객 (OUTID 테이블) --%>
                <input type="radio" name="type" value="force_out" ${type eq 'force_out' ? 'checked' : ''}> 강제 탈퇴
            </label>
            <label style="margin-right: 15px;">
                <%-- 자진 탈퇴: 현재 DB 구조상 별도 테이블이 없어 조회 불가 --%>
                <input type="radio" name="type" value="sign_out" ${type eq 'sign_out' ? 'checked' : ''}> 탈퇴 회원
            </label>
            <button type="submit" class="btn-search">조회</button>
        </form>
		
		<table border="1">
			<thead>
                <%-- ★ type에 따라 헤더 변경 ★ --%>
                <c:if test="${type eq 'active'}">
                    <tr>
                        <td>고객코드</td>
                        <td>아이디</td>
                        <td>이름</td>
                        <td>전화번호</td>
                        <td>포인트</td>
                        <td>가입일</td>
                        <td>기능</td>
                    </tr>
                </c:if>
                <c:if test="${type eq 'force_out' || type eq 'sign_out'}">
                    <tr>
                        <td>탈퇴 아이디</td>
                        <td>탈퇴 사유</td>
                        <td>탈퇴일</td>
                    </tr>
                </c:if>
			</thead>
			<tbody>
                
                <%-- ★ type에 따라 내용 변경 ★ --%>
                <c:if test="${type eq 'active'}">
                    <c:forEach var="c" items="${list}">
                        <tr>
                            <td>${c.customerCode}</td>
                            <td>${c.customerId}</td>
                            <td>${c.customerName}</td>
                            <td>${c.customerPhone}</td>
                            <td>${c.point}</td>
                            <td>${c.createDate}</td>
                            <td>
                                <%-- 2. 기능 탭의 링크를 버튼 스타일로 변경 (action-btn, btn-order, btn-force-out 클래스 추가) --%>
                                <a href="${pageContext.request.contextPath}/emp/orderCustomer?customerCode=${c.customerCode}" 
                                   class="action-btn btn-order">주문</a>
                                <a href="${pageContext.request.contextPath}/emp/removeCustomerByEmp?customerId=${c.customerId}"
                                   class="action-btn btn-force-out">강제탈퇴</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                
                <c:if test="${type eq 'force_out' || type eq 'sign_out'}">
                    <c:forEach var="out" items="${list}">
                        <tr>
                            <td>${out.id}</td>
                            <td>${out.memo}</td>
                            <td>${out.createDate}</td>
                        </tr>
                    </c:forEach>
                </c:if>

                <%-- 결과가 없을 경우 메시지 출력 --%>
                <c:if test="${empty list}">
                    <tr>
                        <%-- colspan 값도 type에 따라 변경 --%>
                        <td colspan="${type eq 'active' ? '7' : '3'}">
                            <c:choose>
                                <c:when test="${type eq 'active'}">등록된 활성 고객이 없습니다.</c:when>
                                <c:when test="${type eq 'force_out'}">등록된 강제 탈퇴 회원이 없습니다.</c:when>
                                <c:when test="${type eq 'sign_out'}">등록된 탈퇴 회원이 없습니다.</c:when>
                                <c:otherwise>조회된 회원이 없습니다.</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:if>
			</tbody>
		</table>
	</div>
	
	<div class="pagination" style="margin-top: 20px;">
        <c:if test="${currentPage > 1}">
            <%-- 페이지네이션 링크에 type 파라미터 추가 --%>
            <a href="${pageContext.request.contextPath}/emp/customerList?currentPage=${currentPage - 1}&type=${type}">이전</a>
        </c:if>
        
        <c:forEach var="p" begin="1" end="${lastPage}">
            <c:choose>
                <c:when test="${p eq currentPage}">
                    <span class="current-page" style="font-weight: bold; color: white; margin: 0 5px;">${p}</span>
                </c:when>
                <c:otherwise>
                    <%-- 페이지네이션 링크에 type 파라미터 추가 --%>
                    <a href="${pageContext.request.contextPath}/emp/customerList?currentPage=${p}&type=${type}" style="margin: 0 5px;">${p}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${currentPage < lastPage}">
            <%-- 페이지네이션 링크에 type 파라미터 추가 --%>
            <a href="${pageContext.request.contextPath}/emp/customerList?currentPage=${currentPage + 1}&type=${type}">다음</a>
        </c:if>
	</div>
</body>
</html>