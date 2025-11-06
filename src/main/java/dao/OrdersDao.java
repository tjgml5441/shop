package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersDao {
	public List<Map<String, Object>> selectOrdersList(int beginRow, int rowPerPage) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
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
			m.put("goodsName", rs.getString("goodsName"));
			m.put("goodsPrice", rs.getInt("goodsPrice"));
			m.put("customerName", rs.getString("customerName"));
			m.put("customerPhone", rs.getString("customerPhone"));
			m.put("address", rs.getString("address"));
			list.add(m);
		}
		return list;
	}
}