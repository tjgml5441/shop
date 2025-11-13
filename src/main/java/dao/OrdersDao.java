package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.Orders;

// DBConnection.getConn()이 Connection 객체를 반환한다고 가정
public class OrdersDao {
	
	public int insertOrders(Orders o) {
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql = """
				insert into orders(
					order_code, goods_code, customer_code, address_code, order_quantity
					, order_price, order_state, createdate
				) values (
					seq_order.nextval, ?, ?, ?, ?, ?, '주문완료', sysdate
				)
				""";
		int row = 0;
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, o.getGoodsCode());
			stmt.setInt(2, o.getCustomerCode());
			stmt.setInt(3, o.getAddressCode());
			stmt.setInt(4, o.getOrderQuantity());
			stmt.setInt(5, o.getOrderPrice());
			row = stmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
		}
 		return row;
	}
	
	// goodsOne -> 주문완료
	
	// cartList -> 주문완료
	
	
	// 전체 주문 건수 조회 (페이지네이션을 위해 추가)
	public int selectCount() throws Exception {
		int count = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT COUNT(*) FROM orders"; 
		try {
			conn = DBConnection.getConn(); // DBConnection.getConn() 호출 가정
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} finally {
			// 리소스 정리
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return count;
	}
	
	public List<Map<String, Object>> selectOrdersList(int beginRow, int rowPerPage) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		// *참고: 이 쿼리는 orders, goods, customer, address 테이블에 모두 유효한 데이터가 있어야만 결과를 반환합니다.
		String sql = """
			select 
				o.order_code orderCode, o.goods_code goodsCode, o.customer_code customerCode
				, o.address_code addressCode, o.order_quantity orderQuantity, o.order_state orderState
				, o.order_price orderPrice, o.createdate createdate
		        , g.goods_name goodsName, g.goods_price goodsPrice
		        , c.customer_name customerName, c.customer_phone customerPhone
		        , a.address address
			from orders o inner join goods g
			on o.goods_code = g.goods_code
			    inner join customer c
			    on o.customer_code = c.customer_code
			        inner join address a
			        on o.address_code = a.address_code
			order by o.order_code desc
			offset ? rows fetch next ? rows only
		""";
		
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, beginRow);
			stmt.setInt(2, rowPerPage);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("orderCode", rs.getInt("orderCode"));
				m.put("goodsCode", rs.getInt("goodsCode"));
				m.put("customerCode", rs.getInt("customerCode"));
				m.put("addressCode", rs.getInt("addressCode"));
				m.put("orderQuantity", rs.getInt("orderQuantity"));
				m.put("orderPrice", rs.getInt("orderPrice"));
				m.put("orderState", rs.getString("orderState"));
				m.put("createdate", rs.getString("createdate"));
				
				// 조인된 테이블의 정보
				m.put("goodsName", rs.getString("goodsName"));
				m.put("goodsPrice", rs.getInt("goodsPrice"));
				m.put("customerName", rs.getString("customerName"));
				m.put("customerPhone", rs.getString("customerPhone"));
				m.put("address", rs.getString("address"));
	
				list.add(m);
			}
		} finally {
			// 리소스 정리
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
}