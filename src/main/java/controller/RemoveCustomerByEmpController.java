package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import dao.CustomerDao;

@WebServlet("/emp/removeCustomerByEmp")
public class RemoveCustomerByEmpController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    // 강제 탈퇴 사유 입력 폼 요청 (GET)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String customerId = request.getParameter("customerId");
        
        if (customerId == null || customerId.isEmpty()) {
            // ID 누락 시 리스트로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/emp/customerList"); 
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
        
        // ★★★ 메시지 파라미터를 제외한 기본 리다이렉션 URL만 설정 ★★★
        String redirectUrl = request.getContextPath() + "/emp/customerList?type=active"; // 처리 후 활성 목록으로

        if (customerId == null || customerId.isEmpty() || reason == null || reason.isEmpty()) {
            // 실패 시 메시지를 서버 콘솔에 기록하고 기본 URL로 리다이렉트
            System.err.println("RemoveCustomerByEmpController: ID 또는 사유 누락.");
            response.sendRedirect(redirectUrl);
            return;
        }
        
        CustomerDao customerDao = new CustomerDao();
        int result = 0;
        try {
            // 트랜잭션 처리 (OUTID 삽입 후 CUSTOMER 삭제)
            result = customerDao.deleteCustomerByEmp(customerId, reason);
        } catch (Exception e) {
            // DB 오류 발생 시 로그를 명확하게 남깁니다.
            System.err.println("RemoveCustomerByEmpController: 강제 탈퇴 처리 중 심각한 오류 발생 - " + e.getMessage());
            e.printStackTrace();
        }
        
        // 리다이렉트 시 메시지 대신 파라미터를 사용하여 고객 목록 JSP에서 표시 (스니펫에서 msg 파라미터가 사용됨을 확인함)
        String msg = "";
        if (result == 1) {
            msg = customerId + " 고객을 강제 탈퇴 처리했습니다.";
        } else {
            msg = customerId + " 고객 강제 탈퇴 처리에 실패했습니다. (DB 오류)";
        }

        // 성공/실패 여부와 관계없이 메시지를 포함하여 리스트 화면으로 리다이렉트
        response.sendRedirect(redirectUrl + "&msg=" + java.net.URLEncoder.encode(msg, "UTF-8"));
    }
}