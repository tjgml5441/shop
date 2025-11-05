package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

//로그인 전 허용
@WebFilter("/out/*")
public class OutFilter extends HttpFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 요청 전
		System.out.println("pre OutFilter");
		HttpSession session = ((HttpServletRequest)request).getSession();
		if(session.getAttribute("loginEmp") != null) {
			((HttpServletResponse)response).sendRedirect(((HttpServletRequest)request).getContextPath() + "/emp/empIndex");
			return;
		} else if(session.getAttribute("loginCustomer") != null) {
			((HttpServletResponse)response).sendRedirect(((HttpServletRequest)request).getContextPath() + "/customer/customerIndex");
			return;
		}
		
		chain.doFilter(request, response);
	}


}
