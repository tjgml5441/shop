package dao;

import java.sql.*;
import java.util.List;

import org.apache.tomcat.dbcp.dbcp2.SQLExceptionList;

import dto.Customer;
import dto.Outid;

public class CustomerDao {
	// 직원에 의해 강제탈퇴
	public void deleteCustomerByEmp(Outid outid) {
		Connection conn = null;
        PreparedStatement pstmtCustomer = null;
        PreparedStatement pstmtOutid = null;
        String sqlCustomer = """
        	delete from customer where customer_id=?
        """;
        String sqlOutid = """
        	insert into outid(id, memo, createdate)
        	values(?,?,?)	
        """;
        
        // JDBC Connection의 기본 Commit 설정값 auto commit = true : false 변경 후 transaction 적용
        try {
        	conn = DBConnection.getConn(); 
			conn.setAutoCommit(false);  // 개발자가 commit 혹은 rollback 직접 구현이 필요
			pstmtCustomer = conn.prepareStatement(sqlCustomer);
			
			//param 설정 ? :outid.getId()
			
			int row = pstmtCustomer.executeUpdate(); // customer 삭제
			if(row == 1) {
				pstmtOutid = conn.prepareStatement(sqlOutid);
				
				//param 설정 : ? outid.getId() ? outid.getMemo() ? sysdate
				pstmtOutid.executeUpdate(); // outid 입력
			} else {
				throw new SQLException();
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				pstmtOutid.close();
				pstmtCustomer.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// 직원 로그인시 전체 고객 리스트 확인
	public List<Customer> selectCustomerList(int beginiRow, int rowPerPage) throws SQLException {
		return null;
	}
	
	
    
    /**
     * 고객 로그인 처리
     */
    public Customer login(String id, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Customer customer = null;
        
        String sql = "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID = ? AND CUSTOMER_PW = ?";
        
        try {
            conn = DBConnection.getConn(); 
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, password);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerCode(rs.getInt("CUSTOMER_CODE"));
                customer.setCustomerId(rs.getString("CUSTOMER_ID"));
                customer.setCustomerPw(rs.getString("CUSTOMER_PW")); 
                customer.setCustomerName(rs.getString("CUSTOMER_NAME"));
                customer.setCustomerPhone(rs.getString("CUSTOMER_PHONE"));
                customer.setPoint(rs.getInt("POINT"));
                customer.setCreateDate(rs.getDate("CREATEDATE"));
            }
        } catch (SQLException e) {
            System.err.println("Customer 로그인 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return customer;
    }
    
    /**
     * ID 중복 여부를 확인합니다. (CUSTOMER, EMP, OUTID 테이블 모두 확인)
     * @param id 확인할 아이디
     * @return 중복되는 ID가 존재하면 해당 ID 문자열, 존재하지 않으면 null
     */
    public String checkDuplicationId(String id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String resultValue = null; 
        
        try {
            conn = DBConnection.getConn(); 
            
            // 사용자 요청 쿼리 구조를 기반으로 통합 ID 중복 검사
            // ★주의: 이 쿼리는 DB에 CUSTOMER, EMP, OUTID 테이블이 존재하고 
            // 각각 customer_id, emp_id, id 컬럼을 가지고 있음을 가정합니다.
            String sql = "SELECT t.id FROM ("
                       + "  SELECT customer_id id FROM customer UNION ALL "
                       + "  SELECT emp_id id FROM emp UNION ALL "
                       + "  SELECT id FROM outid" 
                       + ") t WHERE t.id = ?"; 
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                resultValue = rs.getString("id"); 
            }
        } catch (SQLException e) {
            System.err.println("ID 중복 확인 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultValue;
    }
    
    /**
     * Name 또는 Phone 중복 여부를 확인합니다. (활성 회원 CUSTOMER만 대상)
     * (AddCustomerController의 서버 측 최종 검사에서 사용)
     */
    public String checkDuplicationNameOrPhone(String field, String value) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String resultValue = null; 
        String sql = "";
        
        try {
            conn = DBConnection.getConn(); 
            
            if (field.equals("name")) {
                sql = "SELECT CUSTOMER_NAME AS VALUE FROM CUSTOMER WHERE CUSTOMER_NAME = ?";
            } else if (field.equals("phone")) {
                sql = "SELECT CUSTOMER_PHONE AS VALUE FROM CUSTOMER WHERE CUSTOMER_PHONE = ?";
            } else {
                return null; // 유효하지 않은 필드
            }
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, value);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                resultValue = rs.getString("VALUE");
            }
        } catch (SQLException e) {
            System.err.println(field + " 중복 확인 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultValue;
    }
    
    /**
     * 고객 정보 삽입 (회원가입)
     */
    public int insertCustomer(Customer customer) {
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;
        
        String sql = "INSERT INTO CUSTOMER (CUSTOMER_CODE, CUSTOMER_ID, CUSTOMER_PW, CUSTOMER_NAME, CUSTOMER_PHONE, POINT, CREATEDATE) "
                   + "VALUES (CUSTOMER_SEQ.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE)";
        
        try {
            conn = DBConnection.getConn(); 
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getCustomerPw());
            pstmt.setString(3, customer.getCustomerName());
            pstmt.setString(4, customer.getCustomerPhone());
            pstmt.setInt(5, customer.getPoint()); 
            
            result = pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("[CustomerDao] insertCustomer SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }
}