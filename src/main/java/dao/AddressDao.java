package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Address;

public class AddressDao {
	/**
	 * 배송지 추가 (5개 제한 트랜잭션 포함)
	 * @param address
	 */
	public void insertAddress(Address address) {
		Connection conn = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		ResultSet rs1 = null;
		
		String sql1 = """
			select count(*) from address where customer_code = ?
		""";
		
		String sql2 = """
				delete from address
				where address_code = (select min(address_code) from address);
				""";
		
		String sql3 = """
				insert into address(address_code, customer_code, address, createdate)
				values(seq_address.nextval,?,?, sysdate)
				"""; // 💡 수정: 'insert inito' -> 'insert into'
		try {
			conn = DBConnection.getConn(); // DBConnection은 존재하는 것으로 가정
			conn.setAutoCommit(false);
			stmt1 = conn.prepareStatement(sql1);
			// 💡 수정: customer_code 파라미터 바인딩
			stmt1.setInt(1, address.getCustomerCode()); 
			rs1 = stmt1.executeQuery();
			rs1.next();
			int cnt = rs1.getInt(1);
			
			if(cnt >= 5) { // 5개 이상이면 가장 오래된 주소 삭제 후 입력 sql2 쿼리 호출
				stmt2 = conn.prepareStatement(sql2);
				stmt2.executeUpdate();
			}
			
			//추가
			stmt3 = conn.prepareStatement(sql3);
			stmt3.setInt(1, address.getCustomerCode());
			stmt3.setString(2, address.getAddress());
			int row = stmt3.executeUpdate();
			
			conn.commit();
		} catch (SQLException e) {
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.err.println("AddressDao: 배송지 추가 중 DB 오류 발생: " + e.getMessage());
			e.printStackTrace();
		} finally { // finally 자원해지(close()) null 유무 확인 후 해지
			try {
				if(rs1 != null) rs1.close();
				if(stmt1 != null) stmt1.close();
				if(stmt2 != null) stmt2.close();
				if(stmt3 != null) stmt3.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 고객 코드를 이용해 해당 고객의 배송지 목록을 조회합니다.
	 * @param customerCode
	 * @return List<Address>
	 * @throws SQLException
	 */
	public List<Address> selectAddressList(int customerCode) throws SQLException {
		List<Address> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String sql = """
				SELECT address_code, customer_code, address, createdate
				FROM address 
				WHERE customer_code = ?
				ORDER BY createdate DESC
				"""; 
		
		try {
			conn = DBConnection.getConn(); // DBConnection은 존재하는 것으로 가정
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, customerCode);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				Address address = new Address();
				address.setAddressCode(rs.getInt("address_code"));
				address.setCustomerCode(rs.getInt("customer_code"));
				address.setAddress(rs.getString("address"));
				address.setCreatedate(rs.getString("createdate"));
				list.add(address);
			}
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