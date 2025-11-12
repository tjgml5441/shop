<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.5.0"></script>
<style>
	/* 🎨 공통 스타일 */
	body {
	    font-family: Arial, sans-serif;
	    padding: 20px;
	}
	
	/* 🎨 Top-Bar 스타일 (empList.jsp 기반) */
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

    /* 🎨 버튼 스타일 (goodsOne.jsp에서 사용된 btn-cart 계열 색상 활용) */
    button {
        padding: 8px 15px;
        color: white;
        border: none;
        border-radius: 5px;
        font-size: 15px;
        cursor: pointer;
        transition: background-color 0.3s;
        font-weight: bold;
        margin: 5px 5px 5px 0; /* 버튼 간격 조정 */
        background-color: #9f8473; /* 기본 버튼 색상 */
    }
    button:hover {
        background-color: #6c5d53;
    }
    
    /* 🎨 입력 필드 스타일 */
    input[type="text"] {
        padding: 8px 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        margin-right: 5px;
        font-size: 15px;
    }
    
    /* 🎨 차트 컨테이너 스타일 */
    #myChart {
        margin-top: 30px;
        padding: 15px;
        border: 1px solid #eee;
        border-radius: 10px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        height: 350px; 
        width: 100% !important;
        max-width: 700px !important;
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
    
	<h1>stats</h1>
	<c:import url="/WEB-INF/view/inc/empMenu.jsp"></c:import>


	<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
	
	<input type="text" id="fromYM" value="2025-01-01">
	~
	<input type="text" id="toYM" value="2025-12-31">
	
	<br>
	
	<button type="button" id="totalOrderBtn">특정년도의 월별 주문횟수(누적) : 선 차트</button>	
	<button type="button" id="totalPriceBtn">특정년도의 월별 주문금액(누적) : 선 차트</button>
	<button type="button" id="orderBtn">특정년도의 월별 주문수량 : 막대 차트</button>	
	<button type="button" id="orderPriceBtn">특정년도의 월별 주문금액 : 막대 차트</button>	
	<button type="button" id="top10CustomerOrderCntBtn">고객별 주문횟수 1위 ~ 10위 : 막대 차트</button>	
	<button type="button" id="top10CustomerTotalPriceBtn">고객별 총금액 1위 ~ 10위 : 막대 차트</button>
	<button type="button" id="top10ProductOrderCntBtn">상품별 주문횟수 1위 ~ 10위 : 막대 차트</button>
	<button type="button" id="top10ProductTotalPriceBtn">상품별 주문금액 1위 ~ 10위 : 막대 차트</button>
	<button type="button" id="top10ProductAvgReviewBtn">상품별 평균 리뷰평점 1위 ~ 10위 : 막대 차트</button>	
	<button type="button" id="genderTotalPriceBtn">성별 총주문 금액 : 파이 차트</button>	
	<button type="button" id="genderOrderBtn">성별 총주문 수량 : 파이 차트</button>

	<canvas id="myChart"></canvas>
	
	<script>
		let myChart = null;
		function drawChart(type, url, title, xKey, yKey, colors, isAccumulated = false) {
			let data = {};
			if (isAccumulated) {
				data = {
					fromYM: $('#fromYM').val(),
					toYM: $('#toYM').val()
				}
			}
			
			$.ajax({
				url: $('#contextPath').val() + url
				, type: 'get'
				, data: data
				, success: function (result) {
					
					let xValues = [];
					let yValues = [];
					
					result.forEach(function (m) {
						xValues.push(m[xKey]);
						yValues.push(m[yKey])
					});
					
					const ctx = document.getElementById('myChart');
					
					if(myChart != null) {
						myChart.destroy();
						console.log('canvas 초기화');
					}
					
					let datasets = [];
					if (type === "line") {
						// 선 차트 (누적)
						datasets.push({
					      label: $('#fromYM').val() + '~' + $('#toYM').val() + title,
					      data: yValues,
					      borderColor: colors[0],
					      fill: false
					    });
					} else {
						// 파이/막대 차트
						datasets.push({
					      backgroundColor: colors,
					      data: yValues
					  
					  });
					}
					
					myChart = new Chart(ctx, {
					  type: type,
					  data: {
					    labels: xValues,
					    datasets: datasets
					  },
					  options: {
                          // 모든 차트 유형에 대해 고정된 레이아웃 패딩을 적용하여 여백을 일정하게 유지
                          layout: {
                              padding: 10 
                          },
					    plugins: {
					      legend: {display: (type === 'pie' ||
					      type === 'line') ? true : false},
					      title: {
					        display: true,
					        text: title,
					        font: {size:16}
					      }
					    }
					  }
					});
				}
			});
		}
		
		// [기존] 성별 총주문 수량 : 파이 차트
		$('#genderOrderBtn').click(function () {
			const barColors = [ "#b91d47", "#00aba9" ];
			drawChart("pie", '/emp/genderOrder', "남/여 전체 주문량", "gender", "cnt", barColors);
		});
		// [기존] 특정년도의 월별 주문수량 : 막대 차트
		$('#orderBtn').click(function () {
			const barColors = ["red", "green","blue","orange","brown", "yellow"];
			drawChart("bar", '/emp/order', "20250101 ~ 현재 월별 판매량", "ym", "cnt", barColors, true);
		});
		// [기존] 특정년도의 월별 주문금액(누적) : 선 차트
		$('#totalPriceBtn').click(function(){
			drawChart("line", '/emp/totalPrice', "총판매금액 추이(누적)", "ym", "totalPrice", ["#0000FF"], true);
		});
		// [기존] 특정년도의 월별 주문횟수(누적) : 선 차트
		$('#totalOrderBtn').click(function(){
			drawChart("line", '/emp/totalOrder', "주문량 추이(누적)", "ym", "totalOrder", ["red"], true);
		});
		// --- [추가된 버튼 처리] ---
		
		// [추가] 특정년도의 월별 주문금액 : 막대 차트
		$('#orderPriceBtn').click(function () {
			const barColors = ["#8e5ea2","#3cba9f","#e8c3b9","#c45850","#ffc107", "#0d6efd"];
			drawChart("bar", '/emp/orderPrice', "특정년도의 월별 주문금액", "ym", "total", barColors, true);
		});
		// [추가] 고객별 주문횟수 1위 ~ 10위 : 막대 차트
		$('#top10CustomerOrderCntBtn').click(function () {
			const barColors = ["#8e5ea2","#3cba9f","#e8c3b9","#c45850","#ffc107", "#0d6efd", "#6610f2", "#fd7e14", "#20c997", "#adb5bd"];
			drawChart("bar", '/emp/top10CustomerOrderCnt', "고객별 주문횟수 Top 10", "customer_code", "cnt", barColors);
		});
		// [추가] 고객별 총금액 1위 ~ 10위 : 막대 차트
		$('#top10CustomerTotalPriceBtn').click(function () {
			const barColors = ["#8e5ea2","#3cba9f","#e8c3b9","#c45850","#ffc107", "#0d6efd", "#6610f2", "#fd7e14", "#20c997", "#adb5bd"];
			drawChart("bar", '/emp/top10CustomerTotalPrice', "고객별 총 주문금액 Top 10", "customer_code", "total", barColors);
		});
		// [추가] 상품별 주문횟수 1위 ~ 10위 : 막대 차트
		$('#top10ProductOrderCntBtn').click(function () {
			const barColors = ["#8e5ea2","#3cba9f","#e8c3b9","#c45850","#ffc107", "#0d6efd", "#6610f2", "#fd7e14", "#20c997", "#adb5bd"];
			drawChart("bar", '/emp/top10ProductOrderCnt', "상품별 주문횟수 Top 10", "productName", "cnt", barColors);
		});
		// [추가] 상품별 주문금액 1위 ~ 10위 : 막대 차트
		$('#top10ProductTotalPriceBtn').click(function () {
			const barColors = ["#8e5ea2","#3cba9f","#e8c3b9","#c45850","#ffc107", "#0d6efd", "#6610f2", "#fd7e14", "#20c997", "#adb5bd"];
			drawChart("bar", '/emp/top10ProductTotalPrice', "상품별 주문금액 Top 10", "productName", "total", barColors);
		});
		// [추가] 상품별 평균 리뷰평점 1위 ~ 10위 : 막대 차트
		$('#top10ProductAvgReviewBtn').click(function () {
			const barColors = ["#8e5ea2","#3cba9f","#e8c3b9","#c45850","#ffc107", "#0d6efd", "#6610f2", "#fd7e14", "#20c997", "#adb5bd"];
			drawChart("bar", '/emp/top10ProductAvgReview', "상품별 평균 리뷰평점 Top 10", "productName", "avgScore", barColors);
		});
		// [추가] 성별 총주문 금액 : 파이 차트
		$('#genderTotalPriceBtn').click(function () {
			const barColors = [ "#b91d47", "#00aba9" ];
			drawChart("pie", '/emp/genderTotalPrice', "남/여 전체 주문금액", "gender", "total", barColors);
		});
	</script>
</body>
</html>