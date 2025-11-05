package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/emp/*")
public class EmpFilter extends HttpFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// 요청 전
		System.out.println("pre EmpFilter");
		HttpSession session = ((HttpServletRequest)request).getSession();
		if(session.getAttribute("loginEmp") == null) {
			((HttpServletResponse)response).sendRedirect(((HttpServletRequest)request).getContextPath() + "/out/login");
			return;
		}		
		
		
		chain.doFilter(request, response);
	}


}
