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
import dao.OutidDao;
import dto.Customer;
import dto.Outid;

@WebServlet("/emp/customerList")
public class CustomerListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        // 1. 파라미터 및 페이징 설정
		Integer currentPage = 1;
		if(request.getParameter("currentPage") != null) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) {
            type = "active"; // 기본값: 활성 고객
        }
		
		int rowPerPage = 10;
		int beginRow = (currentPage - 1) * rowPerPage;
		
		CustomerDao customerDao = new CustomerDao();
		OutidDao outidDao = new OutidDao();
        
		List<Customer> customerList = null;
        List<Outid> outidList = null;
        int totalCount = 0;
        int lastPage = 0;
		
		try {
            // 2. 고객 리스트 및 전체 개수 조회
            if ("active".equalsIgnoreCase(type)) {
                // 활성 고객 조회 (CUSTOMER 테이블)
                totalCount = customerDao.selectCustomerListCount(type);
			    customerList = customerDao.selectCustomerListByPage(beginRow, rowPerPage, type);
                
            } else if ("force_out".equalsIgnoreCase(type)) {
                // 강제 탈퇴 고객 조회 (OUTID 테이블)
                totalCount = outidDao.selectOutidListCount();
                outidList = outidDao.selectOutidListByPage(beginRow, rowPerPage);
            } else {
                // 유효하지 않은 type은 기본값으로 설정
                type = "active";
                totalCount = customerDao.selectCustomerListCount(type);
			    customerList = customerDao.selectCustomerListByPage(beginRow, rowPerPage, type);
            }
            
            // 3. 마지막 페이지 계산
            lastPage = totalCount / rowPerPage;
			if (totalCount % rowPerPage != 0) {
				lastPage++;
			}
            
		} catch (SQLException e) {
			System.err.println("고객 리스트 조회 중 오류: " + e.getMessage());
			e.printStackTrace();
            // DB 오류 시 빈 리스트와 1페이지로 설정
            totalCount = 0;
            lastPage = 1;
		}
		
		// 4. 모델 속성 설정
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("lastPage", lastPage);
        request.setAttribute("type", type); // 현재 조회 타입
        
        // 타입에 따라 적절한 리스트를 request에 저장
        if ("active".equalsIgnoreCase(type)) {
		    request.setAttribute("customerList", customerList);
        } else if ("force_out".equalsIgnoreCase(type)) {
            request.setAttribute("outidList", outidList);
        }
        
		// 5. 뷰 포워딩
		request.getRequestDispatcher("/WEB-INF/view/emp/customerList.jsp").forward(request, response);
	}
}