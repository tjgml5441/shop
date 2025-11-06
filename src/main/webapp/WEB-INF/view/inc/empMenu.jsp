<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
    /* 메뉴 박스 스타일: 중앙 정렬, 배경색 투명, 아래쪽 경계선만 표시 */
    .emp-menu-container {
        display: flex;
        justify-content: center; /* 메뉴 항목 중앙 정렬 */
        padding: 10px 0; /* 상하 패딩 유지, 좌우 패딩 제거 */
        
        /* 박스 배경색 투명하게 변경 */
        background-color: transparent; 
        
        /* 둥근 모서리 및 그림자 제거 */
        border-radius: 0; 
        box-shadow: none; 
        
        /* 아래쪽 경계선만 얇게 추가 (구분선 역할) */
        border-top: 2px solid #c7b199;
        border-bottom: 2px solid #c7b199; 
        
        width: 100%; 
        max-width: none; 
        margin: 10px 0; 
    }
    .emp-menu-container a {
        color: #4a4a4a; /* 링크 색상 */
        text-decoration: none;
        padding: 5px 15px;
        margin: 0 5px;
        font-weight: bold;
        transition: color 0.3s;
        
        /* 메뉴 항목 자체의 배경색과 경계선은 제거 */
        border: none; 
        border-radius: 0;
        background-color: transparent; 
    }
    .emp-menu-container a:hover {
        color: #000; /* 호버 시 텍스트 색상만 변경 */
        background-color: transparent; /* 호버 시 배경색도 투명으로 유지 */
    }
</style>
<div class="emp-menu-container">
	<!-- emp 메뉴 -->
	<a href="${pageContext.request.contextPath}/emp/empList">사원관리</a>
	<a href="${pageContext.request.contextPath}/emp/customerList">고객관리</a>
	<a href="${pageContext.request.contextPath}/emp/outidList">탈퇴ID관리</a>
	<a href="${pageContext.request.contextPath}/emp/goodsList">상품관리</a>
	<a href="${pageContext.request.contextPath}/emp/ordersList">주문관리</a>
	<a href="${pageContext.request.contextPath}/emp/noticeList">공지관리</a>
</div>