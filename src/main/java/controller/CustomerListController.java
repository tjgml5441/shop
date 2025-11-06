package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.CustomerDao;
import dao.OutidDao; // ★ OutidDao 추가
import dto.Customer;
import dto.Outid;    // ★ Outid DTO 추가

@WebServlet("/emp/customerList")
public class CustomerListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        // 1. 파라미터 및 페이징 설정
		Integer currentPage = 1;
		if(request.getParameter("currentPage") != null) {
			try {
				currentPage = Integer.parseInt(request.getParameter("currentPage"));
			} catch (NumberFormatException e) {
				// 유효하지 않은 페이지 번호는 무시하고 기본값 1 사용
			}
		}
		
        // type 파라미터 확인: "active" (활성 고객), "force_out" (강제 탈퇴)
        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) {
            type = "active"; // 기본값: 활성 고객 (전체 회원)
        }
		
		int rowPerPage = 10;
		int beginRow = (currentPage - 1) * rowPerPage;
		
		CustomerDao customerDao = new CustomerDao();
        OutidDao outidDao = new OutidDao();
		List<?> list = null; // List<Customer> 또는 List<Outid>를 담기 위한 와일드카드 리스트
        int totalCount = 0;
        int lastPage = 1; 
		
		try {
            // 2. 선택된 타입에 따라 다른 DAO 호출
            if (type.equals("active")) {
                // 2-1. 활성 고객 (CUSTOMER 테이블) 조회
                // CustomerDao의 selectCustomerListCount 메서드는 현재 모든 고객 수를 반환한다고 가정
                totalCount = customerDao.selectCustomerListCount(type); 
                list = customerDao.selectCustomerListByPage(beginRow, rowPerPage, type);

            } else if (type.equals("force_out")) {
                // 2-2. 강제 탈퇴 고객 (OUTID 테이블) 조회
                totalCount = outidDao.selectOutidListCount();
                list = outidDao.selectOutidListByPage(beginRow, rowPerPage);
            }
            
            // 3. 마지막 페이지 계산
            if (totalCount > 0) {
                lastPage = totalCount / rowPerPage;
    		    if (totalCount % rowPerPage != 0) {
    			    lastPage++;
    		    }
            }
            
		} catch (SQLException e) {
			System.err.println("고객/탈퇴 리스트 조회 중 오류: " + e.getMessage());
			e.printStackTrace();
            list = List.of(); 
		}
		
		// 4. 모델 속성 설정
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("lastPage", lastPage);
		request.setAttribute("list", list); // 조회된 리스트
        request.setAttribute("type", type); // 현재 조회 타입
		
		// 5. 뷰로 포워딩
		request.getRequestDispatcher("/WEB-INF/view/emp/customerList.jsp").forward(request, response);
	}
}