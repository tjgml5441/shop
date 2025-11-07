<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지관리</title>
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
    
    table { 
    	width: 100%;
    	border-collapse: collapse; 
    	margin-top: 10px; 
    	border-radius: 8px; /* 모서리 둥글게 */
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
    
    /* 기존 스타일은 유지 */
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
</style>
<script>
    $(document).ready(function(){
        // 테이블 행(tr) 클릭 시 공지 상세 페이지로 이동
        $("tbody tr").click(function() {
            // 해당 행의 noticeCode를 가져옵니다. (첫 번째 td의 텍스트)
            // 혹은 <a> 태그의 href 속성을 사용하여 이동할 수도 있습니다.
            
            // 공지 제목(두 번째 td) 내의 <a> 태그의 href를 찾아서 이동
            var href = $(this).find("td:nth-child(2) a").attr("href");
            
            if (href) {
                window.location.href = href;
            }
        });
        
        // 앵커 태그(<a>)가 포함된 td를 클릭해도 이벤트가 중복 발생하지 않도록 방지
        $("tbody tr td:nth-child(2) a").click(function(e) {
            e.stopPropagation(); // <tr>에 걸린 이벤트가 전파되는 것을 막음
        });
    });
</script>
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
    
    <h1>공지관리</h1>
	
	<c:import url="/WEB-INF/view/inc/empMenu.jsp"></c:import>
	
	<div style="text-align: right; margin-bottom: 10px;">
		<a href="${pageContext.request.contextPath}/emp/addNotice" class="add-link">공지추가</a>
	</div>
	
	<table>
		<thead>
			<tr>
				<th>noticeCode</th>
				<th>noticeTitle</th>
				<th>createdate</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="n" items="${list}">
				<tr>
					<td>${n.noticeCode}</td>
					<td><a href="${pageContext.request.contextPath}/emp/noticeOne?noticeCode=${n.noticeCode}">${n.noticeTitle}</a></td>
					<td>${n.createdate}</td>
				</tr>
			</c:forEach>
			<c:if test="${empty list}">
				<tr>
					<td colspan="3">등록된 공지사항이 없습니다.</td>
				</tr>
			</c:if>
		</tbody>
	</table>
	
	<div class="pagination">
        
        <%-- [이전] 버튼: startPage가 1보다 클 때 --%>
        <c:if test="${startPage > 1}">
            <a href="${pageContext.request.contextPath}/emp/noticeList?currentPage=${startPage - 1}">이전</a>
        </c:if>
        
      
        <%-- 페이지 번호 출력: startPage부터 endPage까지 --%>
        <c:forEach var="p" begin="${startPage}" end="${endPage}">
            <c:choose>
                <c:when test="${p eq currentPage}">
                    <span class="current-page">${p}</span> </c:when>
                <c:otherwise>
            
                 <a href="${pageContext.request.contextPath}/emp/noticeList?currentPage=${p}">${p}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        
        <%-- [다음] 버튼: endPage가 lastPage보다 작을 때 --%>
        <c:if test="${endPage < lastPage}">
            <a href="${pageContext.request.contextPath}/emp/noticeList?currentPage=${endPage + 1}">다음</a>
      
        </c:if>
	</div>
</body>
</html>