package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; 
import java.io.IOException;
import java.sql.SQLException;

import dao.CustomerDao; 
import dao.EmpDao; 
import dto.Customer; 
import dto.Emp; 

@WebServlet("/out/login")
public class LoginController extends HttpServlet {
	// 1. 로그인 폼으로
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/view/out/login.jsp").forward(request, response);
	}

	// 2. 로그인 처리 (액션)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8"); 
        
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        
        String message = "";
        
		String customerOrEmpSel = request.getParameter("customerOrEmpSel");
		
		if(customerOrEmpSel.equals("customer")) {
            CustomerDao customerDao = new CustomerDao();
            Customer loginCustomer = customerDao.login(id, password); 
            
            if (loginCustomer != null) {
                session.setAttribute("loginCustomer", loginCustomer);
                // 고객 인덱스 컨트롤러로
                response.sendRedirect(request.getContextPath()+"/customer/customerIndex");
                return; 
            } else {
                message = "고객 로그인 정보가 일치하지 않습니다.";
            }
            
		} else if(customerOrEmpSel.equals("emp")) {
            EmpDao empDao = new EmpDao();
            Emp loginEmp = null;
			try {
				loginEmp = empDao.login(id, password);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
            
            if (loginEmp != null) {
                session.setAttribute("loginEmp", loginEmp);
                // 직원 인덱스 컨트롤러로
                response.sendRedirect(request.getContextPath()+"/emp/empIndex");
                return; 
            } else {
                message = "직원 로그인 정보가 일치하지 않습니다.";
            }
		} else {
            message = "로그인 타입을 선택해주세요.";
        }
        
        // 로그인 실패 시: 메시지를 request에 login.jsp로 포워딩
        if (!message.isEmpty()) {
            request.setAttribute("message", message);
            request.getRequestDispatcher("/WEB-INF/view/out/login.jsp").forward(request, response);
        }
	}
}