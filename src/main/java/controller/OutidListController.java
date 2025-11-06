package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
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
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		
		int rowPerPage = 10; // 페이지당 10개 행
		int beginRow = (currentPage - 1) * rowPerPage;
		
		OutidDao outidDao = new OutidDao();
		List<Outid> outidList = Collections.emptyList(); // 초기화
        int totalCount = 0;
        int lastPage = 1; // 최소 1페이지
		
		try {
            // 2. 탈퇴 ID 리스트 및 전체 개수 조회
            totalCount = outidDao.selectOutidListCount();
			outidList = outidDao.selectOutidListByPage(beginRow, rowPerPage);
            
            // 3. 마지막 페이지 계산
            lastPage = totalCount / rowPerPage;
			if (totalCount % rowPerPage != 0) {
				lastPage++;
			}
            
		} catch (SQLException e) {
			System.err.println("탈퇴 ID 리스트 조회 중 오류: " + e.getMessage());
			e.printStackTrace();
            // 오류 발생 시 리스트를 비우고 페이지는 1로 설정
		}
		
		// 4. 모델 속성 설정
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("lastPage", lastPage);
		request.setAttribute("outidList", outidList);
		
		// 5. 뷰 포워딩
		request.getRequestDispatcher("/WEB-INF/view/emp/outidList.jsp").forward(request, response);
	}
}