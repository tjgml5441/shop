package controller;

import java.io.IOException;

import dao.GoodsDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/customer/customerIndex")
public class CustomerIndexController extends HttpServlet {

	private GoodsDao goodsDao;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int currentPage = 1;
		if(request.getParameter("currentPage") != null) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		
		int rowPerPage = 20;
		int biginRow = (1-currentPage) * rowPerPage;
		
		goodsDao = new GoodsDao();
		request.setAttribute("goodsList", goodsDao.selectGoodsList(biginRow, rowPerPage));
		request.setAttribute("currentPage", currentPage);
		// request.setAttribute("lastPage", lastPage);
		// request.setAttribute("startPage", startPage);
		// request.setAttribute("endPage", endPage);
		// 만약에 페이지(첫번째 or 마지막)에 출력할 상품이 7개다 -> totalTd = 10
		// request.setAttribute("totalTd", totalTd);
		
		request.setAttribute("bestGoodsList", goodsDao.selectBestGoodsList());
		
		request.getRequestDispatcher("/WEB-INF/view/customer/customerIndex.jsp").forward(request, response);
	}
}