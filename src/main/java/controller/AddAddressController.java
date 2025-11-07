package controller;

import java.io.IOException;
import java.sql.SQLException; 

import dao.AddressDao;
import dto.Address;
import dto.Customer; 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; 


@WebServlet("/customer/addAddress")
public class AddAddressController extends HttpServlet {
	private AddressDao addressDao;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/view/customer/addAddress.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 로그인된 고객 정보 확인
		HttpSession session = request.getSession();
		// (가정: Customer DTO가 있고, 로그인 성공 시 loginCustomer 세션에 저장됨)
		Customer loginCustomer = (Customer)session.getAttribute("loginCustomer");
		
		if (loginCustomer == null) {
			response.sendRedirect(request.getContextPath() + "/out/login");
			return;
		}
		
		// 2. 주소 데이터 처리
		String[] addressArr = request.getParameterValues("address");
		// 우편번호, 도로명 주소, 지번 주소, 상세 주소, 참고 항목이 모두 'address' 이름으로 전달되므로
		// 이를 모두 공백으로 연결하여 하나의 주소 문자열로 만듭니다.
		String addressString = String.join(" ", addressArr); 
		
		// 3. Address DTO 설정
		Address addressDto = new Address();
		// (가정: Customer DTO에 getCustomerCode()가 있고, Address DTO에 setCustomerCode()가 있음)
		addressDto.setCustomerCode(loginCustomer.getCustomerCode()); 
		addressDto.setAddress(addressString);
		
		// 4. DAO를 이용해 DB에 주소 추가
		addressDao = new AddressDao();
		try {
			addressDao.insertAddress(addressDto);
		} catch (Exception e) { 
			System.err.println("AddAddressController: 배송지 추가 중 오류 발생 - " + e.getMessage());
			e.printStackTrace();
			
			request.setAttribute("errorMessage", "배송지 추가에 실패했습니다. 다시 시도해주세요.");
			request.getRequestDispatcher("/WEB-INF/view/customer/addAddress.jsp").forward(request, response);
			return;
		}
		
		// 5. 성공 시 배송지 목록 페이지로 리다이렉트
		response.sendRedirect(request.getContextPath()+"/customer/addressList");
	}

}