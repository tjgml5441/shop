package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import dao.NoticeDao;
import dto.Notice;

@WebServlet("/emp/noticeOne")
public class NoticeOneController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. noticeCode 파라미터 받기
		int noticeCode = 0;
		if (request.getParameter("noticeCode") != null) {
			try {
				noticeCode = Integer.parseInt(request.getParameter("noticeCode"));
			} catch (NumberFormatException e) {
				// 유효하지 않은 noticeCode 처리 (예: 목록 페이지로 리다이렉트 또는 오류 메시지 설정)
				// 여기서는 간단하게 0으로 유지하고 DAO에서 null 반환하도록 처리
			}
		}

		NoticeDao noticeDao = new NoticeDao();
		Notice notice = noticeDao.selectNoticeOne(noticeCode);

		// 2. request에 결과 저장 및 포워딩
		if (notice != null) {
			request.setAttribute("noticeOne", notice);
			request.getRequestDispatcher("/WEB-INF/view/emp/noticeOne.jsp").forward(request, response);
		} else {
			// 공지사항을 찾지 못했을 경우
			response.sendRedirect(request.getContextPath() + "/emp/noticeList"); // 목록 페이지로 리다이렉트
		}
	}


}