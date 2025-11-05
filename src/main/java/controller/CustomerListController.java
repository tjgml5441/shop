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
import dto.Customer;


@WebServlet("/customerList")
public class CustomerListController extends HttpServlet {
	private CustomerDao customerDao;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// request
		int currentPage = 1;
		int beginRow = 0;
		int rowPerPage = 10;
		
		this.customerDao = new CustomerDao();
		List<Customer> customerList = null;
		try {
			customerList = customerDao.selectCustomerList(beginRow, rowPerPage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// 속성에 모델 저장
		
		request.getRequestDispatcher("WEB-INF/view/emp/customerList.jsp").forward(request, response);
	}

}
