package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.OutidDao;
import dto.Outid;

@WebServlet("/emp/outidList")
public class OutidListController extends HttpServlet {
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
		
		int rowPerPage = 10; // 페이지당 행 수
		int beginRow = (currentPage - 1) * rowPerPage; // 시작 행 번호 (LIMIT/OFFSET용)
		
		OutidDao outidDao = new OutidDao();
		List<Outid> outidList = null;
        int totalCount = 0;
        int lastPage = 1; // 기본값은 1
		
		try {
            // 2. 강제 탈퇴 ID 목록 전체 개수 조회
            totalCount = outidDao.selectOutidListCount();
            
            // 3. 마지막 페이지 계산
            if (totalCount > 0) {
                lastPage = totalCount / rowPerPage;
    		    if (totalCount % rowPerPage != 0) {
    			    lastPage++;
    		    }
            }
            
            // 4. 페이지 범위 검사 및 데이터 조회
            if (currentPage > lastPage && lastPage > 0) {
                // 현재 페이지가 마지막 페이지를 초과하면 마지막 페이지로 재설정
                currentPage = lastPage;
                beginRow = (currentPage - 1) * rowPerPage;
            }
            
            // 데이터 조회
			outidList = outidDao.selectOutidListByPage(beginRow, rowPerPage);
            
		} catch (SQLException e) {
			System.err.println("OutidListController: 강제 탈퇴 ID 리스트 조회 중 오류: " + e.getMessage());
			e.printStackTrace();
            // 오류 발생 시 빈 리스트로 설정
            outidList = List.of(); 
		}
		
		// 5. 모델 속성 설정
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("lastPage", lastPage);
		request.setAttribute("outidList", outidList);
		
		// 6. 뷰로 포워딩
		request.getRequestDispatcher("/WEB-INF/view/emp/outidList.jsp").forward(request, response);
	}
}