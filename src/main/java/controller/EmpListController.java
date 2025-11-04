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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        EmpDao empDao = new EmpDao(); // DAO 인스턴스는 한 번만 생성
        
        // ★★★ 상태 변경 로직 (기존 컨트롤러 재활용) ★★★
        String empCodeParam = request.getParameter("empCode");
        String currentActiveParam = request.getParameter("currentActive");

        if (empCodeParam != null && currentActiveParam != null) {
            // 사원 상태 변경 요청 처리 (PRG 패턴)
            try {
                int empCode = Integer.parseInt(empCodeParam);
                int currentActive = Integer.parseInt(currentActiveParam);
                
                // 현재 상태의 반대값(1 -> 0, 0 -> 1)을 새로운 상태로 설정하여 토글
                int newActive = (currentActive == 1) ? 0 : 1; 
                
                // 수정된 EmpDao의 메서드 호출
                int result = empDao.updateEmpActive(empCode, newActive); 
                
                if (result == 1) {
                    System.out.println("사원 CODE: " + empCode + " 상태가 " + (newActive == 1 ? "활성화" : "비활성화") + "으로 변경되었습니다.");
                } else {
                    System.err.println("사원 상태 변경 실패");
                }

                // 상태 변경 후, 목록 페이지로 리다이렉트 (PRG 패턴)
                response.sendRedirect(request.getContextPath() + "/emp/empList?currentPage=" + request.getParameter("currentPage"));
                return; // 리다이렉트 후 메서드 종료
                
            } catch (NumberFormatException e) {
                System.err.println("파라미터 변환 오류: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("DB 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
            // 오류 발생 시에도 목록 페이지로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/emp/empList"); 
            return;
        }
        
        // ★★★ 목록 조회 로직 ★★★
		Integer currentPage = 1;
		if(request.getParameter("currentPage") != null) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
        
		int rowPerPage = 10;
		int beginRow = (currentPage-1) * rowPerPage;
		
		int lastPage = 0; // 페이징 관련 로직 완성
        int totalRow = 0; // 전체 레코드 수
		
		List<Emp> empList = null;
		try {
            // 전체 레코드 수 조회
            totalRow = empDao.selectEmpTotalCount();
            
            // 마지막 페이지 계산
            lastPage = totalRow / rowPerPage;
            if (totalRow % rowPerPage != 0) {
                lastPage++; // 나머지가 있으면 페이지 하나 추가
            }
            
			empList = empDao.selectEmpListByPage(beginRow, rowPerPage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// 모델 속성
        request.setAttribute("totalRow", totalRow);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("lastPage", lastPage);
		request.setAttribute("empList", empList);
		
		request.getRequestDispatcher("/WEB-INF/view/emp/empList.jsp").forward(request, response);
	}

}