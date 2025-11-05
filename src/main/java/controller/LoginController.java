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
import dto.Emp;

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
        Emp emp = null;
        String message = "";
        
        try {
            emp = empDao.login(id, password);
        } catch (SQLException e) {
            System.err.println("LoginController: 로그인 처리 중 DB 오류 발생 - " + e.getMessage());
            e.printStackTrace();
            message = "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
        }

        if (emp != null) {
            // ★★★ 수정된 부분: 사원의 활성화 상태(active) 체크 ★★★
            if (emp.getActive() == 1) { 
                // 1. 로그인 성공 및 활성 상태 (Active = 1)
                // 세션에 사원 정보 저장 (보안을 위해 비밀번호는 제외)
                emp.setEmpPw(null); 
                HttpSession session = request.getSession();
                session.setAttribute("loginEmp", emp);
                
                // 고객용 세션 충돌 방지를 위해 고객 세션은 무효화 (필요 시)
                session.removeAttribute("loginCustomer"); 

                response.sendRedirect(request.getContextPath() + "/emp/empIndex"); 
                return;
            } else {
                // 2. 로그인 성공, 하지만 비활성 상태 (Active = 0)
                message = "비활성화된 계정입니다. 관리자에게 문의하세요.";
            }
        } else {
            // 3. 로그인 실패 (ID 또는 PW 불일치)
            message = "아이디 또는 비밀번호를 확인해주세요.";
        }

        // 로그인 실패 또는 비활성 상태일 경우, 에러 메시지와 함께 폼으로 포워딩
        request.setAttribute("message", message);
        request.setAttribute("id", id);
        request.getRequestDispatcher("/WEB-INF/view/out/login.jsp").forward(request, response);
    }
}