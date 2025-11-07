package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.AddressDao;
import dto.Address;
import dto.Customer; 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; 

@WebServlet("/customer/addressList")
public class AddressListController extends HttpServlet {
	
	private AddressDao addressDao = new AddressDao(); 

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Customer loginCustomer = (Customer)session.getAttribute("loginCustomer");
		
		// 로그인 체크
		if (loginCustomer == null) {
			response.sendRedirect(request.getContextPath() + "/out/login");
			return;
		}
		
		List<Address> addressList = null;
		
		try {
			// 고객 코드를 사용하여 배송지 목록 조회
			// (가정: Customer DTO에 getCustomerCode()가 있음)
			int customerCode = loginCustomer.getCustomerCode(); 
			addressList = addressDao.selectAddressList(customerCode);
			
		} catch (SQLException e) {
			System.err.println("AddressListController: 배송지 목록 조회 중 오류 발생 - " + e.getMessage());
			e.printStackTrace();
			request.setAttribute("errorMessage", "배송지 목록을 불러오는 데 실패했습니다.");
		}
		
		// 조회된 목록을 request에 담아 JSP로 포워딩
		request.setAttribute("addressList", addressList);
		request.getRequestDispatcher("/WEB-INF/view/customer/addressList.jsp").forward(request, response);
	}
}