package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

import dao.EmpDao;
import dao.CustomerDao; // 💡 추가: 고객 DAO
import dto.Emp;
import dto.Customer; // 💡 추가: 고객 DTO

@WebServlet("/out/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    // 로그인 폼 요청 (GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/out/login.jsp").forward(request, response);
    }

    // 로그인 처리 요청 (POST)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String password = request.getParameter("password");
        
        EmpDao empDao = new EmpDao();
        CustomerDao customerDao = new CustomerDao(); // 💡 CustomerDao 인스턴스 생성
        
        Emp emp = null;
        Customer customer = null; // 💡 Customer DTO 변수 선언
        String message = "";
        
        // **A. 직원 로그인 시도**
        try {
            emp = empDao.login(id, password);
        } catch (SQLException e) {
            System.err.println("LoginController: 직원 로그인 처리 중 DB 오류 발생 - " + e.getMessage());
            e.printStackTrace();
            message = "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
        }
        
        // DB 오류가 아닌 경우에만 다음 로직 실행
        if (message.isEmpty()) {
            if (emp != null) {
                // 1. 직원 로그인 성공
                if (emp.getActive() == 1) { 
                    // 1-1. 로그인 성공 및 활성 상태 (Active = 1)
                    emp.setEmpPw(null); 
                    HttpSession session = request.getSession();
                    session.setAttribute("loginEmp", emp);
                    
                    // 고객용 세션 충돌 방지
                    session.removeAttribute("loginCustomer"); 

                    response.sendRedirect(request.getContextPath() + "/emp/empIndex"); 
                    return;
                } else {
                    // 1-2. 비활성 상태 (Active = 0)
                    message = "비활성화된 계정입니다. 관리자에게 문의하세요.";
                }
            } else {
                // **B. 직원 로그인 실패 -> 고객 로그인 시도**
                try {
                    customer = customerDao.login(id, password); // 💡 고객 로그인 메서드 호출
                } catch (SQLException e) {
                    System.err.println("LoginController: 고객 로그인 처리 중 DB 오류 발생 - " + e.getMessage());
                    e.printStackTrace();
                    // 고객 로그인 중 DB 오류는 전체 메시지에 반영하지 않음 (직원 DB 오류가 우선)
                    if (message.isEmpty()) { 
                         message = "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
                    }
                }
                
                if (customer != null) {
                    // 2. 고객 로그인 성공
                    
                    customer.setCustomerPw(null); // 보안을 위해 비밀번호 제거 (Customer DTO에 setCustPw가 있다고 가정)
                    HttpSession session = request.getSession();
                    session.setAttribute("loginCustomer", customer); // 💡 고객 세션 저장
                    
                    // 직원용 세션 충돌 방지
                    session.removeAttribute("loginEmp"); 
                    
                    response.sendRedirect(request.getContextPath() + "/customer/customerIndex"); // 💡 고객 메인 페이지로 리다이렉트
                    return;
                    
                } else if (message.isEmpty()) { 
                    // 3. 모든 로그인 실패 (직원, 고객 모두 실패)
                    message = "아이디 또는 비밀번호를 확인해주세요.";
                }
            }
        }
        
        // 로그인 실패, 비활성 상태, 또는 DB 오류 발생 시, 에러 메시지와 함께 폼으로 포워딩
        request.setAttribute("message", message);
        request.setAttribute("id", id);
        request.getRequestDispatcher("/WEB-INF/view/out/login.jsp").forward(request, response);
    }
}