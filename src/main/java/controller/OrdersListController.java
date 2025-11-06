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
		int rowPerPage = 10;
		int beginRow = (1-currentPage)*rowPerPage;
		
		ordersDao = new OrdersDao();
		List<Map<String, Object>> list = null;
		try {
			list = ordersDao.selectOrdersList(beginRow, rowPerPage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("list", list);
		request.setAttribute("currentPage", currentPage);
		request.getRequestDispatcher("/WEB-INF/view/emp/ordersList.jsp").forward(request, response);
		
	}

}