package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException; 
import java.util.List; 

import dao.GoodsDao; 
import dto.Goods; 

@WebServlet("/emp/goodsList")
public class GoodsListController extends HttpServlet {
	
	private GoodsDao goodsDao = new GoodsDao(); 

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Goods> goodsList = null;
		
		try {
			// GoodsDao를 사용하여 상품 목록을 조회합니다.
			goodsList = goodsDao.selectGoodsList();
			
		} catch (SQLException e) {
			System.err.println("GoodsListController: 상품 목록 조회 중 DB 오류 발생 - " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("errorMessage", "상품 목록을 불러오는 데 실패했습니다.");
		}
		
		// 조회된 목록을 request에 담아 JSP로 포워딩
		request.setAttribute("goodsList", goodsList);
		request.getRequestDispatcher("/WEB-INF/view/emp/goodsList.jsp").forward(request, response);
	}
}