// controller/OrdersListController.java

package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import dao.OrdersDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/emp/ordersList")
public class OrdersListController extends HttpServlet {
	private OrdersDao ordersDao;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int currentPage = 1;
		if(request.getParameter("currentPage") != null) { 
            try {
                currentPage = Integer.parseInt(request.getParameter("currentPage"));
            } catch (NumberFormatException e) {
                // Ignore and keep currentPage = 1 if parsing fails
            }
        }
		
		int rowPerPage = 10;
		// *수정된 부분*: beginRow 계산을 (currentPage - 1) * rowPerPage 로 수정 
		int beginRow = (currentPage-1) * rowPerPage;
		
		ordersDao = new OrdersDao();
		List<Map<String, Object>> list = null;
		
		// 페이지네이션 변수 추가
		int count = 0;
		int lastPage = 1;
		int startPage = 1;
		int endPage = 1;
		
		try {
			// 1. 전체 주문 건수 가져오기
			count = ordersDao.selectCount(); 
			
			// 2. lastPage 계산
			lastPage = (count % rowPerPage == 0) ? (count / rowPerPage) : (count / rowPerPage) + 1;
			
			// 3. 페이지네이션 블록 계산 (10개씩)
			int pagePerPage = 10;
			startPage = ((currentPage - 1) / pagePerPage * pagePerPage) + 1;
			endPage = startPage + (pagePerPage - 1);
			if (endPage > lastPage) {
				endPage = lastPage;
			}
			
			// 4. 주문 리스트 가져오기
			list = ordersDao.selectOrdersList(beginRow, rowPerPage);
			
		} catch (Exception e) {
			System.err.println("OrdersListController 주문 목록 조회 중 오류 발생:");
			e.printStackTrace();
			// 오류 발생 시 list는 null 또는 빈 리스트로 유지
		}
        
		// 5. View로 데이터 전달
		request.setAttribute("list", list);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("lastPage", lastPage);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
        
		request.getRequestDispatcher("/WEB-INF/view/emp/ordersList.jsp").forward(request, response);
		
	}

}