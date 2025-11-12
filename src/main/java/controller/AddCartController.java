package controller;

import java.io.IOException;

import dao.CartDao;
import dto.Cart;
import dto.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/customer/addCart")
public class AddCartController extends HttpServlet {
	CartDao cartDao;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Customer loginCustomer = (Customer)session.getAttribute("loginCustomer");
		
		String goodsCode = request.getParameter("goodsCode");
		String cartQuantity = request.getParameter("cartQuantity");
		int customerCode = loginCustomer.getCustomerCode();
		System.out.println(goodsCode);
		System.out.println(cartQuantity);
		
		Cart c = new Cart();
		c.setGoodsCode(Integer.parseInt(goodsCode));
		c.setCustomerCode(customerCode);
		c.setCartQuantity(Integer.parseInt(cartQuantity));
		
		cartDao.insertCart(c);
		
		response.sendRedirect(request.getContextPath()+"/customer/cartList");
	}

}