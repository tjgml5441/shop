package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import dao.CustomerDao;

@WebServlet("/emp/removeCustomerByEmp")
public class RemoveCustomerByEmpController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    // 강제 탈퇴 사유 입력 폼 요청 (GET)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String customerId = request.getParameter("customerId");
        
        if (customerId == null || customerId.isEmpty()) {
            // ID가 없으면 고객 리스트로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/emp/customerList?type=active"); 
            return;
        }
        
        request.setAttribute("customerId", customerId);
		request.getRequestDispatcher("/WEB-INF/view/emp/removeCustomerByEmp.jsp").forward(request, response);
	}

    // 강제 탈퇴 처리 요청 (POST)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
		String customerId = request.getParameter("customerId");
        String reason = request.getParameter("reason"); // 강제 탈퇴 사유
        
        // 성공 시 리다이렉션 URL (탈퇴 목록)
        String redirectUrlSuccess = request.getContextPath() + "/emp/customerList?type=out"; 
        // 실패/오류 시 리다이렉션 URL (활성 목록)
        String redirectUrlFailure = request.getContextPath() + "/emp/customerList?type=active"; 
        
        if (customerId == null || customerId.isEmpty() || reason == null || reason.isEmpty()) {
            // ID 또는 사유 누락 시 활성 목록으로 리다이렉트
            System.err.println("RemoveCustomerByEmpController: ID 또는 사유 누락. ID: " + customerId + ", Reason: " + reason);
            response.sendRedirect(redirectUrlFailure);
            return;
        }
        
        CustomerDao customerDao = new CustomerDao();
        int result = 0;
        try {
            result = customerDao.deleteCustomerByEmp(customerId, reason);
        } catch (SQLException e) {
            // DB 오류 발생 시 로그를 명확하게 남기고 실패 URL로 설정
            System.err.println("RemoveCustomerByEmpController: 강제 탈퇴 처리 중 심각한 오류 발생 - " + e.getMessage());
            e.printStackTrace();
            result = 0; // 명시적 실패 처리
        }
        
        if (result == 1) {
            // 성공 시, 탈퇴 목록으로 리다이렉트
            response.sendRedirect(redirectUrlSuccess); 
        } else {
            // 실패 시, 활성 목록으로 리다이렉트
            System.err.println("RemoveCustomerByEmpController: 강제 탈퇴 처리 실패. DB 업데이트 결과: " + result);
            response.sendRedirect(redirectUrlFailure); 
        }
	}
}