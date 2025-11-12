package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsDao {
	// 11개 chart 메서드

	// 1. 성별 주문 수량 : 파이 차트
	public List<Map<String, Object>> selectOrderCntByGender() {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = """
			select t.g gender, count(*) cnt
			from
			(select c.gender g, o.order_code oc
			from CUSTOMER c inner join ORDERS o
			on c.customer_code = o.customer_code) t
			group by t.g
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("gender", rs.getString("gender"));
				map.put("cnt", rs.getInt("cnt"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	// 2. 월 주문량 - 막대차트 (ORDERS 테이블)
	public List<Map<String, Object>> selectOrderCntByYM(String fromYM, String toYM) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = """
			select to_char(createdate, 'YYYY-MM') ym, count(*) cnt
			from ORDERS
			where createdate between to_date(?, 'YYYY-MM-DD') 
			and to_date(?, 'YYYY-MM-DD')
			group by to_char(createdate, 'YYYY-MM')
			order by ym asc
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, fromYM);
			stmt.setString(2, toYM);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ym", rs.getString("ym"));
				map.put("cnt", rs.getInt("cnt"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	// 3. 월별 주문 횟수 누적 : 선 차트 (ORDERS 테이블)
	public List<Map<String, Object>> selectOrderTotalCntByYM(String fromYM, String toYM) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = """
			select  t.ym ym
					, sum(t.cnt) over(order by t.ym asc) totalOrder
			from 
				  (select to_char(createdate, 'YYYY-MM') ym, count(*) cnt
				  from ORDERS
				  where createdate between to_date(?, 'YYYY-MM-DD') 
                  and to_date(?, 'YYYY-MM-DD')
				  group by to_char(createdate, 'YYYY-MM')) t	
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, fromYM);
			stmt.setString(2, toYM);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ym", rs.getString("ym"));
				map.put("totalOrder", rs.getString("totalOrder"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	// 4. 월별 주문 금액 누적 : 선 차트 (ORDERS 테이블)
	public List<Map<String, Object>> selectOrderTotalPriceByYM(String fromYM, String toYM) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = """
				select  t.ym ym
						, sum(t.total) over(order by t.ym asc) totalPrice
				from 
				    (select to_char(createdate, 'YYYY-MM') ym, sum(order_price) total
				    from ORDERS
				    where createdate between to_date(?, 'YYYY-MM-DD') 
                    and to_date(?, 'YYYY-MM-DD')
				    group by to_char(createdate, 'YYYY-MM')) t
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, fromYM);
			stmt.setString(2, toYM);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ym", rs.getString("ym"));
				map.put("totalPrice", rs.getString("totalPrice"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	// 5. 특정년도의 월별 주문금액 : 막대 차트 (ORDERS 테이블)
	public List<Map<String, Object>> selectOrderPriceByYM(String fromYM, String toYM) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = """
			select to_char(createdate, 'YYYY-MM') ym, sum(order_price) total
			from ORDERS
			where createdate between to_date(?, 'YYYY-MM-DD') 
			and to_date(?, 'YYYY-MM-DD')
			group by to_char(createdate, 'YYYY-MM')
			order by ym asc
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, fromYM);
			stmt.setString(2, toYM);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ym", rs.getString("ym"));
				map.put("total", rs.getInt("total"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	// 6. 고객별 주문횟수 1위 ~ 10위 : 막대 차트 (ORDERS 테이블)
	public List<Map<String, Object>> selectTop10CustomerOrderCnt() {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = """
			SELECT 
			    customer_code,
			    COUNT(order_code) AS cnt
			FROM ORDERS
			GROUP BY customer_code
			ORDER BY cnt DESC
			FETCH FIRST 10 ROWS ONLY
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("customer_code", rs.getInt("customer_code"));
				map.put("cnt", rs.getInt("cnt"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	// 7. 고객별 총금액 1위 ~ 10위 : 막대 차트 (ORDERS 테이블)
	public List<Map<String, Object>> selectTop10CustomerTotalPrice() {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = """
			SELECT 
			    customer_code,
			    SUM(order_price) AS total
			FROM ORDERS
			GROUP BY customer_code
			ORDER BY total DESC
			FETCH FIRST 10 ROWS ONLY
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("customer_code", rs.getInt("customer_code"));
				map.put("total", rs.getInt("total"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	// 8. 상품별 주문횟수 1위 ~ 10위 : 막대 차트
    // ORDERS.product_code -> ORDERS.goods_code 수정
	public List<Map<String, Object>> selectTop10ProductOrderCnt() {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = """
			SELECT 
			    g.goods_name productName,
			    COUNT(o.order_code) AS cnt
			FROM ORDERS o INNER JOIN GOODS g
			ON o.goods_code = g.goods_code 
			GROUP BY g.goods_name
			ORDER BY cnt DESC
			FETCH FIRST 10 ROWS ONLY
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("productName", rs.getString("productName"));
				map.put("cnt", rs.getInt("cnt"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	// 9. 상품별 주문금액 1위 ~ 10위 : 막대 차트
    // ORDERS.product_code -> ORDERS.goods_code 수정
	public List<Map<String, Object>> selectTop10ProductTotalPrice() {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = """
			SELECT 
			    g.goods_name productName,
			    SUM(o.order_price) AS total
			FROM ORDERS o INNER JOIN GOODS g
			ON o.goods_code = g.goods_code 
			GROUP BY g.goods_name
			ORDER BY total DESC
			FETCH FIRST 10 ROWS ONLY
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("productName", rs.getString("productName"));
				map.put("total", rs.getInt("total"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	// 10. 상품별 평균 리뷰평점 1위 ~ 10위 : 막대 차트
    // REVIEW.product_code -> REVIEW.goods_code 수정 (일관성을 위해 가정)
	public List<Map<String, Object>> selectTop10ProductAvgReview() {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = """
			SELECT 
			    g.goods_name productName,
			    AVG(r.review_score) AS avgScore
			FROM REVIEW r INNER JOIN GOODS g
			ON r.goods_code = g.goods_code
			GROUP BY g.goods_name
			ORDER BY avgScore DESC
			FETCH FIRST 10 ROWS ONLY
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("productName", rs.getString("productName"));
				map.put("avgScore", rs.getDouble("avgScore"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	// 11. 성별 총주문 금액 : 파이 차트 (CUSTOMER, ORDERS 테이블)
	public List<Map<String, Object>> selectOrderTotalPriceByGender() {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = """
			select t.g gender, sum(t.p) total
			from
			(select c.gender g, o.order_price p
			from CUSTOMER c inner join ORDERS o
			on c.customer_code = o.customer_code) t
			group by t.g
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("gender", rs.getString("gender"));
				map.put("total", rs.getInt("total"));
				list.add(map);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}