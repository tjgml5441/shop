<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<style>
	/* 🎨 공통 스타일 */
	body {
	    font-family: Arial, sans-serif;
	    padding: 20px;
	}
	
	/* 🎨 Top-Bar 스타일 */
	.top-bar {
	    display: flex;
	    justify-content: space-between;
	    align-items: center;
	    margin-bottom: 10px;
	    padding: 0 5px;
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
	.btn-home { background-color: #333; }
	.btn-home:hover { background-color: #555; }
	.btn-logout { background-color: #dc3545; }
	.btn-logout:hover { background-color: #c82333; }
	.btn-login { background-color: #007bff; }
	.btn-login:hover { background-color: #0056b3; }
	
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
	
	/* 🎨 상품 상세 레이아웃 */
	.goods-detail-container {
	    display: flex;
	    margin-top: 30px;
	}
	.goods-image {
	    /* 영역 크기 조정 */
        max-width: 450px; 
	    flex: none; /* max-width를 존중하도록 flex: none 설정 */
	    text-align: center;
	    /* margin을 사용하여 우측 정보표와의 간격 조절 */
	    margin-right: 20px; 
	}
	.goods-image img {
	    border-radius: 8px;
	    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
	}
	.goods-form-area {
	    flex: 1; /* 나머지 공간을 모두 차지하도록 설정 */
	}
	
	/* 🎨 상품 상세 테이블 스타일 */
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
	    text-align: left; /* 상세 정보이므로 왼쪽 정렬 */
	}

    /* 📌 TH 가로 길이 수정 */
    .goods-form-area table th {
        width: 120px; /* th 영역의 너비를 명시적으로 줄여서 테이블을 콤팩트하게 만듭니다. */
        white-space: nowrap; /* 텍스트가 줄바꿈되지 않도록 설정 (선택 사항) */
    }
	th {
	    background-color: #dfd3c3;
	    border-bottom: 1.5px solid #a09483;
	}
	table tbody tr:last-child td {
	    border-bottom: none;
	}
	table tbody tr:hover {
	    background-color: white; /* 상세 정보 테이블은 hover 효과 제거 */
	    cursor: default;
	}
	
	/* 🎨 버튼 스타일 */
	.btn-style {
	    padding: 8px 15px;
	    color: white;
	    border: none; 
	    border-radius: 5px;
	    font-size: 15px;
	    cursor: pointer;
	    transition: background-color 0.3s;
	    font-weight: bold;
	    margin-top: 15px;
	}
	.btn-cart {
	    background-color: #9f8473; /* 장바구니: 브라운 계열 */
	    margin-right: 10px;
	}
	.btn-cart:hover {
	    background-color: #6c5d53;
	}
	.btn-order {
	    background-color: #007bff; /* 바로주문: 블루 계열 */
	}
	.btn-order:hover {
	    background-color: #0056b3;
	}
</style>
</head>

<body>
    <div class="top-bar">
        <div class="btn-group">
            <a href="${pageContext.request.contextPath}/customer/customerIndex" class="btn-home">HOME</a>
        </div>
        <div class="btn-group">
            <c:choose>
                <c:when test="${sessionScope.loginCustomer != null}">
                    <span>${loginCustomer.customerName}님 (point : ${loginCustomer.point})</span>
                    <a href="${pageContext.request.contextPath}/customer/customerLogout" class="btn-logout">로그아웃</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/out/login" class="btn-login">로그인</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

	<h1>상품 상세</h1>
	<c:import url="/WEB-INF/view/inc/customerMenu.jsp"></c:import>
	
	<div class="goods-detail-container">
		<div class="goods-image">
			<img src="${pageContext.request.contextPath}/upload/${goods.filename}" width="400" height="400">
		</div>
		<div class="goods-form-area">
			<form>
				<table>
					<tr>
						<th>상품명</th>
						<td>${goods.goodsName}</td>
					</tr>
					<tr>
						<th>가격</th>
						<td>${goods.goodsPrice}</td>
					</tr>
					<tr>
						<th>포인트 적립률</th>
						<td>${goods.pointRate} %</td>
					</tr>
					<tr>
						<th>판매 상태</th>
						<td>${goods.soldout}</td>
					</tr>
					<tr>
						<th>수량</th>
						<td>
							<select name="cartQuantity">
								<c:forEach var="n" begin="1" end="10">
									<option value="${n}">${n}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
				</table>
				<button type="button" class="btn-style btn-cart">장바구니</button>
				<button type="button" class="btn-style btn-order">바로주문</button>
			</form>
		</div>
	</div>
	<script>
		$('#cartBtn').click(function(){
			$('#myForm').attr('method', 'post');
			$('#myForm').attr('action', $('#contextPath').val()+'/customer/addCart');
			alert('cartBtn:' + $('#myForm').attr('method') + ',' + $('#myForm').attr('action')); // cart 액션
			$('#myForm').submit();
		});
		
		$('#orderBtn').click(function(){
			$('#myForm').attr('method', 'get');
			$('#myForm').attr('action', $('#contextPath').val()+'/customer/addOrders');
			alert('orderBtn: ' + $('#myForm').attr('method') + ',' + $('#myForm').attr('action')); // orders 화면
			$('#myForm').submit();
		});
	</script>
</body>
</html>