package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.AddressDao;
import dao.CartDao;
import dao.GoodsDao;
import dao.OrdersDao;
import dto.Address;
import dto.Customer;
import dto.Orders;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/customer/addOrders")
public class AddOrderServlet extends HttpServlet {
	GoodsDao goodsDao;
	CartDao cartDao;
	AddressDao addressDao;
	OrdersDao ordersDao;
	// addOrders.jsp action
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Customer loginCustomer = (Customer)(session.getAttribute("loginCustomer"));
		String addressCode = request.getParameter("addressCode");
		String orderPrice = request.getParameter("orderPrice"); // 결제 모듈 추가 후 구현가능
		
		String[] goodsCodeList = request.getParameterValues("goodsCode");
		String[] orderQuantityList = request.getParameterValues("orderQuantity");
		String[] goodsPriceList = request.getParameterValues("goodsPrice");
		
		
		System.out.println("loginCustomer:"+ loginCustomer.getCustomerCode());
		System.out.println("addressCode:"+ addressCode);
		System.out.println("orderPrice:"+ orderPrice); // 결제 모듈 추가 후 구현가능
		
		System.out.println("goodsCodeList:"+ goodsCodeList.length);
		System.out.println("orderQuantityList:"+ orderQuantityList.length);
		System.out.println("goodsPriceList:"+ goodsPriceList.length);
		
		// 1) insert payment 테이블에 결제 행을 추가 - 테이블 추가 필요
		
		// 2) insert orders 각 상품별로 주문행 추가
		
		ordersDao = new OrdersDao();
		
		for(int i=0; i<goodsCodeList.length; i=i+1) { // goodsCodeList, orderQuantityList, goodsPriceList
			Orders o = new Orders();
			o.setCustomerCode(loginCustomer.getCustomerCode());
			o.setAddressCode(Integer.parseInt(addressCode));
			o.setGoodsCode(Integer.parseInt(goodsCodeList[i]));
			o.setOrderQuantity(Integer.parseInt(orderQuantityList[i]));
			o.setOrderPrice(Integer.parseInt(goodsPriceList[i]) * Integer.parseInt(orderQuantityList[i]));
			ordersDao.insertOrders(o);
		}
		
		// 1)번 테이블 추가후 구현 가능
		// 3) orders_payment 테이블에 payment pk와 orders pk를 연결(insert)
		
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// goodsOne action
		String goodsCode = request.getParameter("goodsCode");
		String cartQuantity = request.getParameter("cartQuantity");
		System.out.println("goodsCode: "+goodsCode);
		System.out.println("cartQuantity: "+cartQuantity);
				
		// cartList action
		String[] cartCodeList = request.getParameterValues("cartCodeList");
		System.out.println("cartCodeList: "+cartCodeList);
		
		List<Map<String, Object>> list = new ArrayList<>();
		
		
		// Map : 상품정보 + (이미지정보) + 수량
		if(goodsCode != null) { // goodsOne action
			// list.size() -> 하나의 상품
			// goodsCode를 사용하여 조인
			goodsDao = new GoodsDao();
			Map<String, Object> m = goodsDao.selectGoodsOne(Integer.parseInt(goodsCode));
			m.put("cartQuantity", cartQuantity);
			list.add(m);
		} else { // cartList action
			// list.size() -> 하나이상의 상품
			// cartCodeList -> goods정보 -> 조인
			cartDao = new CartDao(); 
			for(String cc : cartCodeList) {
				int cartCode = Integer.parseInt(cc);
				Map<String, Object> m = cartDao.selectCartListByKey(cartCode);
				// System.out.println(m);
				list.add(m);
				
				// cartDao.deleteCart(cc); 주문목록으로 이동 후 cart에서는 삭제
			}
		}	
		
		
		int orderPrice = 0;
		for(Map m : list) {
			orderPrice += (Integer)(m.get("goodsPrice"));
		}
		
		request.setAttribute("list", list);
		request.setAttribute("orderPrice", orderPrice);
		
		HttpSession session = request.getSession();
		Customer loginCustomer = (Customer)(session.getAttribute("loginCustomer"));
		addressDao = new AddressDao();
		List<Address> addressList = null;
		try {
			addressList = addressDao.selectAddressList(loginCustomer.getCustomerCode());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("addressList", addressList);
		
		request.getRequestDispatcher("/WEB-INF/view/customer/addOrders.jsp").forward(request, response);
	}
	
}