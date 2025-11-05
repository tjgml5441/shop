package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.EmpDao;
import dto.Emp;

@WebServlet("/emp/empList")
public class EmpListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 1. 파라미터 및 페이징 설정
		Integer currentPage = 1;
		if(request.getParameter("currentPage") != null) {
			try {
			    currentPage = Integer.parseInt(request.getParameter("currentPage"));
			} catch (NumberFormatException e) {
			    // 파라미터가 유효하지 않은 경우 1페이지로 설정
			    currentPage = 1; 
			}
		}
		
		int rowPerPage = 10;
		int beginRow = (currentPage - 1) * rowPerPage;
		
		EmpDao empDao = new EmpDao();
		List<Emp> empList = null;
        int totalCount = 0;
		int lastPage = 0;
		
		try {
            // 2. 전체 사원 수 조회 및 마지막 페이지 계산
            totalCount = empDao.selectEmpListCount();
            lastPage = totalCount / rowPerPage;
			if (totalCount % rowPerPage != 0) {
				lastPage++;
			}
            
            // 페이지 범위를 벗어난 경우 조정
            if (currentPage > lastPage && lastPage > 0) {
                currentPage = lastPage;
                beginRow = (currentPage - 1) * rowPerPage;
            } else if (lastPage == 0) {
                // 사원이 없는 경우
                currentPage = 1;
                beginRow = 0;
            }
            
            // 3. 사원 활성화/비활성화 상태 변경 처리
            String empCodeStr = request.getParameter("empCode");
            String currentActiveStr = request.getParameter("currentActive");
            
            if (empCodeStr != null && currentActiveStr != null) {
                try {
                    int empCode = Integer.parseInt(empCodeStr);
                    int currentActive = Integer.parseInt(currentActiveStr);
                    
                    // 현재 상태의 반대 값으로 변경
                    int newActiveValue = (currentActive == 1) ? 0 : 1; 
                    
                    int result = empDao.updateEmpActive(empCode, newActiveValue);
                    
                    if (result == 1) {
                        System.out.println("EmpListController: 사원 활성화 상태 변경 성공. EmpCode: " + empCode + ", New Active: " + newActiveValue);
                        // DB 업데이트 후, 현재 페이지를 유지하기 위해 리다이렉트
                        response.sendRedirect(request.getContextPath() + "/emp/empList?currentPage=" + currentPage);
                        return; 
                    } else {
                        System.err.println("EmpListController: 사원 활성화 상태 변경 실패. EmpCode: " + empCode);
                    }
                } catch (NumberFormatException | SQLException e) {
                    System.err.println("EmpListController: 활성화 상태 변경 처리 중 오류 발생 - " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // 4. 페이지별 사원 리스트 조회
			empList = empDao.selectEmpListByPage(beginRow, rowPerPage);
            
		} catch (SQLException e) {
			System.err.println("EmpListController: 사원 리스트 조회 중 오류 발생 - " + e.getMessage());
			e.printStackTrace();
		}
		
		// 5. 모델 속성 설정 및 포워딩
		request.setAttribute("currentPage", currentPage);
        request.setAttribute("lastPage", lastPage);
        request.setAttribute("totalCount", totalCount);
		request.setAttribute("empList", empList);
		
		request.getRequestDispatcher("/WEB-INF/view/emp/empList.jsp").forward(request, response);
	}
    
    // POST 요청은 doGet으로 처리되도록 리다이렉트
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}