package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException; // 추가된 import

import dao.CustomerDao;
import dto.Customer;

@WebServlet("/out/addCustomer")
public class AddCustomerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/view/out/addCustomer.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
        
        // 1. 파라미터 받기 (입력 값 및 중복 확인 플래그)
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String passwordCheck = request.getParameter("passwordCheck");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        
        // 아이디 중복 확인 플래그만 받음
        String idCheck = request.getParameter("idCheck");
        
        String message = "";
        CustomerDao customerDao = new CustomerDao();
        
        // 2. 기본 유효성 검사 및 아이디 중복 확인 플래그 검사
        if (id == null || id.isEmpty() || password == null || password.isEmpty() || 
            passwordCheck == null || passwordCheck.isEmpty() || name == null || name.isEmpty() ||
            phone == null || phone.isEmpty()) {
            
            message = "모든 필드를 입력해야 합니다.";
        } else if (!password.equals(passwordCheck)) {
            message = "비밀번호가 일치하지 않습니다.";
        } else if (!"Y".equals(idCheck)) { // 아이디 중복 확인 플래그 검사
            message = "아이디 중복 확인이 필요합니다.";
        }
        
        // 3. 2차 중복 검사 (이름/전화번호)
        if (message.isEmpty()) {
			try {
				// 이름 중복 검사
				if (customerDao.checkDuplicationNameOrPhone("name", name) != null) {
				     message = "이미 사용 중인 이름입니다.";
				} 
				// 전화번호 중복 검사
				else if (customerDao.checkDuplicationNameOrPhone("phone", phone) != null) {
				     message = "이미 사용 중인 전화번호입니다.";
				}
			} catch (SQLException e) {
				System.err.println("회원가입 중 2차 중복 검사 오류: " + e.getMessage());
				e.printStackTrace();
				message = "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
			} 
        }

        // 4. DAO를 통한 회원가입 처리
        if (message.isEmpty()) {
            Customer customer = new Customer();
            customer.setCustomerId(id);
            customer.setCustomerPw(password);
            customer.setCustomerName(name);
            customer.setCustomerPhone(phone);
            customer.setPoint(0);

            try {
                int result = customerDao.insertCustomer(customer);
                
                if (result == 1) {
                    response.sendRedirect(request.getContextPath() + "/out/login");
                    return;
                } else {
                    message = "회원가입에 실패했습니다. (DB 오류)";
                }
            } catch (SQLException e) {
                System.err.println("회원가입 중 DB 삽입 오류: " + e.getMessage());
                e.printStackTrace();
                message = "회원가입에 실패했습니다. (DB 오류)";
            }
        }

        // 5. 실패 시: 메시지와 기존 입력값, 플래그를 request에 담아 폼으로 포워딩
        if (!message.isEmpty()) {
            request.setAttribute("message", message);
            request.setAttribute("id", id);
            request.setAttribute("name", name);
            request.setAttribute("phone", phone);
            
            // 아이디 플래그만 유지
            request.setAttribute("idCheck", idCheck); 
            
            request.getRequestDispatcher("/WEB-INF/view/out/addCustomer.jsp").forward(request, response);
        }
	}
}