package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        
        // 1. 파라미터 받기
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        String passwordCheck = request.getParameter("passwordCheck");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        
        String message = "";
        CustomerDao customerDao = new CustomerDao();
        
        // 2.입력값 및 유효성 검사 로직 추가
        if (id == null || id.isEmpty() || password == null || password.isEmpty() || name == null || name.isEmpty() || phone == null || phone.isEmpty()) {
            message = "모든 항목을 입력해야 합니다.";
        } else if (!password.equals(passwordCheck)) {
            message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
        } else if (customerDao.isIdDuplicate(id)) {
            message = "이미 사용 중인 아이디입니다. 다른 아이디를 사용해 주세요.";
        }
        
        // 3. DAO를 통한 회원가입 처리
        if (message.isEmpty()) {
            Customer customer = new Customer();
            customer.setCustomerId(id);
            customer.setCustomerPw(password);
            customer.setCustomerName(name);
            customer.setCustomerPhone(phone);
            customer.setPoint(0);

            int result = customerDao.insertCustomer(customer);
            
            if (result == 1) {
                // 회원가입 성공 시 로그인 페이지로
                response.sendRedirect(request.getContextPath() + "/out/login");
                return;
            } else {
                // DB 삽입 실패 시
                message = "회원가입에 실패했습니다. (DB 삽입 오류)";
            }
        }

        // 4. 실패 시: 메시지와 기존 입력값을 request에 담아 폼으로 포워딩
        if (!message.isEmpty()) {
            request.setAttribute("message", message);
            request.setAttribute("id", id);
            request.setAttribute("name", name);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("/WEB-INF/view/out/addCustomer.jsp").forward(request, response);
        }
	}
}