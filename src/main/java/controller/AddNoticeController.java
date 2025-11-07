package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import dao.NoticeDao;
import dto.Emp;
import dto.Notice;

@WebServlet("/emp/addNotice")
public class AddNoticeController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 관리자 세션 체크
        HttpSession session = request.getSession();
        if (session.getAttribute("loginEmp") == null) {
            response.sendRedirect(request.getContextPath() + "/out/login");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/view/emp/addNotice.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        Emp loginEmp = (Emp)session.getAttribute("loginEmp");
        
        if (loginEmp == null) {
            response.sendRedirect(request.getContextPath() + "/out/login");
            return;
        }
        
        try {
            String noticeTitle = request.getParameter("noticeTitle");
            String noticeContent = request.getParameter("noticeContent");
            
            if (noticeTitle == null || noticeTitle.isEmpty() || noticeContent == null || noticeContent.isEmpty()) {
                throw new IllegalArgumentException("제목과 내용을 모두 입력해야 합니다.");
            }
            
            Notice notice = new Notice();
            notice.setNoticeTitle(noticeTitle);
            notice.setNoticeContent(noticeContent);
            notice.setEmpCode(loginEmp.getEmpCode()); 
            
            NoticeDao noticeDao = new NoticeDao();
            int row = noticeDao.insertNotice(notice); 
            
            if (row == 1) {
                // 등록 성공 시 목록 페이지로 리다이렉트
                response.sendRedirect(request.getContextPath() + "/emp/noticeList");
            } else {
                // DB 등록 실패 시 에러 메시지 처리 후 폼으로 포워드
                request.setAttribute("errorMessage", "공지사항 등록에 실패했습니다. (DB 처리 오류)");
                request.getRequestDispatcher("/WEB-INF/view/emp/addNotice.jsp").forward(request, response);
            }
            
        } catch (IllegalArgumentException e) {
            // 유효성 검사 오류
            request.setAttribute("errorMessage", "입력값 오류: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/emp/addNotice.jsp").forward(request, response);
        } catch (Exception e) {
            // 기타 서버 오류 (DB 등)
            System.err.println("AddNoticeController 처리 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "공지 등록 중 서버 오류가 발생했습니다.");
            request.getRequestDispatcher("/WEB-INF/view/emp/addNotice.jsp").forward(request, response);
        }
    }
}