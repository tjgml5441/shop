package ajax;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import dao.CustomerDao;

/**
 * AJAX 요청을 처리하여 ID 중복 여부를 JSON으로 응답합니다.
 */
@WebServlet("/out/idCkRestController")
public class IdCkRestController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
        // ID 중복 확인만 처리
		String id = request.getParameter("id"); 
		
		CustomerDao customerDao = new CustomerDao();
		// DAO의 ID 중복 확인 메서드 호출 (CUSTOMER, EMP, OUTID 통합 확인)
		String resultId = customerDao.checkDuplicationId(id); 
		
		Map<String, Object> map = new HashMap<>();
		
		if (resultId != null) {
			// 중복된 ID가 존재함
			map.put("result", "duplicate");
		} else {
			// 사용 가능한 ID임
			map.put("result", "available");
		}
		
		Gson gson = new Gson();
		String jsonResult = gson.toJson(map);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsonResult); 
		out.flush();
	}
}