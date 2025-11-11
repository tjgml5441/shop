<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.5.0"></script>
</head>

<body>
	<h1>stats</h1>
	<!-- customer meun include -->
	<c:import url="/WEB-INF/view/inc/empMenu.jsp"></c:import>
	<hr>


	<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
	
	<input type="text" id="fromYM" value="2025-01-01">
	~
	<input type="text" id="toYM" value="2025-12-31">
	
	<br>
	
	<button type="button" id="order">특정년도의 월별 주문횟수(누적) : 선 차트</button>
	
	<button type="button" id="price">특정년도의 월별 주문금액(누적) : 선 차트</button>

	<button type="button" id="">특정년도의 월별 주문수량 : 막대 차트</button>
	<button type="button" id="">특정년도의 월별 주문금액 : 막대 차트</button>
	<button type="button" id="">고객별 주문횟수 1위 ~ 10위 : 막대 차트</button>
	<button type="button" id="">고객별 총금액 1위 ~ 10위 : 막대 차트</button>
	<button type="button" id="">상품별 주문횟수 1위 ~ 10위 : 막대 차트</button>
	<button type="button" id="">상품별 주문금액 1위 ~ 10위 : 막대 차트</button>
	<button type="button" id="">상품별 평균 리뷰평점 1위 ~ 10위 : 막대 차트</button>
	<button type="button" id="">성별 총주문 금액 : 파이 차트</button>
	<button type="button" id="">성별 총주문 수량 : 파이 차트</button>

	<canvas id="myChart" style="width:100%;max-width:700px"></canvas>
	
	<script>
		let myChart = null;
		
		$('#price').click(function(){
			$.ajax({
				url: $('#contextPath').val()+'/totalPrice'
				, type: 'get'
				, data: {
							fromYM: $('#fromYM').val()
							, toYM: $('#toYM').val()
						}
				, success: function(result){ // result --> list
					console.log(result);					
					$('#myChart').empty();
					
					let x = [];
					let y = [];
					
					console.log(x, x);
					console.log(y, x);
					
					result.forEach(function(m){
						x.push(m.ym);
						y.push(m.totalPrice);
					});
					
					if(myChart != null) {
						myChart.destroy();
					} 
					
					myChart = new Chart("myChart", {
					  type: "line",
					  data: {
					    labels: x,
					    datasets: [{
					      label: $('#fromYM').val() + '~' + $('#toYM').val() + '총판매금액 추이(누적)',
					      data: y,
					      borderColor: "#0000FF",
					      fill: false
					    }]
					  },
					  options: {
					    legend: {display: false}
					  }
					});
				} 
			});
		});
	
	
		$('#order').click(function(){
			// alert('orderAndPrice 클릭');
			$.ajax({
				url: $('#contextPath').val()+'/totalOrder'
				, type: 'get'
				, data: {
							fromYM: $('#fromYM').val()
							, toYM: $('#toYM').val()
						}
				, success: function(result){ // result --> list
					console.log(result);					
					$('#myChart').empty();
					
					let x = [];
					let y = [];
					
					console.log(x, x);
					console.log(y, x);
					
					result.forEach(function(m){
						x.push(m.ym);
						y.push(m.totalOrder);
					});
					
					if(myChart != null) {
						myChart.destroy();
					} 
					
					myChart = new Chart("myChart", {
					  type: "line",
					  data: {
					    labels: x,
					    datasets: [{
					      label: $('#fromYM').val() + '~' + $('#toYM').val() + '주문량 추이(누적)',
					      data: y,
					      borderColor: "red",
					      fill: false
					    }]
					  },
					  options: {
					    legend: {display: false}
					  }
					});
				} 
			});
		});
	</script>
</body>
</html>