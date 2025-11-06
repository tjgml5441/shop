package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.Customer;
// import dto.Outid; // CustomerDao에서는 Outid DTO 사용을 최소화

public class CustomerDao {
	
	/**
	 * 고객 목록 전체 개수 조회 (활성 고객만)
	 * @param type 현재는 "active"만 가정
	 * @return 전체 활성 고객 수
	 * @throws SQLException
	 */
	public int selectCustomerListCount(String type) throws SQLException {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        // GDJ95 스키마 명시
        String sql = "SELECT COUNT(*) FROM GDJ95.CUSTOMER";
        
        // type="out"인 경우는 OutidDao에서 처리해야 합니다.
        // 여기서는 "active" 고객만 카운트합니다.
        
        try {
            conn = DBConnection.getConn(); 
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
	}
	
	/**
	 * 고객 목록 조회 (페이징 적용) - 활성 고객만
	 * @param beginRow 시작 행 번호
	 * @param rowPerPage 페이지 당 행 수
	 * @param type 현재는 "active"만 가정
	 * @return 페이지에 해당하는 고객 리스트
	 * @throws SQLException
	 */
	public List<Customer> selectCustomerListByPage(int beginRow, int rowPerPage, String type) throws SQLException {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Customer> customerList = new ArrayList<>();
        
        // GDJ95 스키마 명시 및 페이징 적용
        String sql = """
                       SELECT CUSTOMER_CODE, CUSTOMER_ID, CUSTOMER_NAME, CUSTOMER_PHONE, POINT, CREATEDATE
                       FROM GDJ95.CUSTOMER
                       ORDER BY CREATEDATE DESC
                       OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                     """;
        
        try {
            conn = DBConnection.getConn();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, beginRow);
            pstmt.setInt(2, rowPerPage);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerCode(rs.getInt("CUSTOMER_CODE"));
                customer.setCustomerId(rs.getString("CUSTOMER_ID"));
                customer.setCustomerName(rs.getString("CUSTOMER_NAME"));
                customer.setCustomerPhone(rs.getString("CUSTOMER_PHONE"));
                customer.setPoint(rs.getInt("POINT"));
                customer.setCreateDate(rs.getDate("CREATEDATE"));
                customerList.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("고객 리스트 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e; // 예외를 호출한 곳으로 던짐
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return customerList;
	}
	
	/**
	 * 고객 로그인
	 * @param id 고객 ID
	 * @param password 고객 비밀번호
	 * @return 일치하는 고객 DTO 또는 null
	 * @throws SQLException
	 */
	public Customer login(String id, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Customer customer = null;
        
        // GDJ95 스키마 명시
        String sql = """
                       SELECT CUSTOMER_CODE, CUSTOMER_ID, CUSTOMER_NAME, CUSTOMER_PHONE, POINT, CREATEDATE
                       FROM GDJ95.CUSTOMER
                       WHERE CUSTOMER_ID = ? AND CUSTOMER_PW = ?
                     """;
        
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
                customer.setCustomerName(rs.getString("CUSTOMER_NAME"));
                customer.setCustomerPhone(rs.getString("CUSTOMER_PHONE"));
                customer.setPoint(rs.getInt("POINT"));
                customer.setCreateDate(rs.getDate("CREATEDATE"));
            }
        } catch (SQLException e) {
            System.err.println("고객 로그인 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e; 
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
	 * ID 중복 검사 (CUSTOMER, EMP, OUTID 테이블 통합)
	 * @param id 검사할 ID
	 * @return 중복된 ID (있으면) 또는 null (없으면)
	 * @throws SQLException
	 */
	public String checkDuplicationId(String id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String duplicatedId = null;
        
        // CUSTOMER, EMP, OUTID 테이블에서 ID 중복 확인
        String sql = """
                     SELECT ID FROM (
                         SELECT CUSTOMER_ID AS ID FROM GDJ95.CUSTOMER WHERE CUSTOMER_ID = ?
                         UNION ALL
                         SELECT EMP_ID AS ID FROM GDJ95.EMP WHERE EMP_ID = ?
                         UNION ALL
                         SELECT ID FROM GDJ95.OUTID WHERE ID = ?
                     ) WHERE ROWNUM = 1
                     """;
        
        try {
            conn = DBConnection.getConn();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, id); // CUSTOMER
            pstmt.setString(2, id); // EMP
            pstmt.setString(3, id); // OUTID
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                duplicatedId = rs.getString("ID");
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return duplicatedId;
    }
    
    // 이름/전화번호 중복 검사 (단일 메서드)
    public String checkDuplicationNameOrPhone(String type, String value) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String duplicatedValue = null;
        
        // type에 따라 동적으로 컬럼 이름을 지정
        String column = "";
        if ("name".equalsIgnoreCase(type)) {
            column = "CUSTOMER_NAME";
        } else if ("phone".equalsIgnoreCase(type)) {
            column = "CUSTOMER_PHONE";
        } else {
            // 유효하지 않은 타입은 예외 발생
            throw new IllegalArgumentException("유효하지 않은 중복 검사 타입: " + type);
        }
        
        String sql = "SELECT " + column + " FROM GDJ95.CUSTOMER WHERE " + column + " = ?";
        
        try {
            conn = DBConnection.getConn();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, value);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                duplicatedValue = rs.getString(column);
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return duplicatedValue;
    }
	
	/**
	 * 고객 회원가입
	 * @param customer 고객 DTO
	 * @return 성공한 행의 수 (1이면 성공)
	 */
	public int insertCustomer(Customer customer) {
		Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;
        
        // GDJ95 스키마 명시
        String sql = """
                     INSERT INTO GDJ95.CUSTOMER 
                     (CUSTOMER_CODE, CUSTOMER_ID, CUSTOMER_PW, CUSTOMER_NAME, CUSTOMER_PHONE, POINT, CREATEDATE)
                     VALUES (SEQ_CUSTOMER.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE)
                     """;
        
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
            System.err.println("고객 등록 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
	}

	/**
	 * 직원(Emp)에 의한 고객 강제 탈퇴 (트랜잭션 적용)
	 * 1. OUTID 테이블에 ID와 사유 삽입
	 * 2. CUSTOMER 테이블에서 고객 삭제
	 * @param customerId 강제 탈퇴할 고객 ID
	 * @param reason 강제 탈퇴 사유 (memo)
	 * @return 성공 여부 (1: 성공, 0: 실패)
	 */
	public int deleteCustomerByEmp(String customerId, String reason) throws SQLException {
		Connection conn = null;
        PreparedStatement pstmtOutid = null;
        PreparedStatement pstmtDelete = null;
        int result = 0; // 0: 실패, 1: 성공
        
        // 1. OUTID 테이블 삽입 SQL (GDJ95 스키마 명시)
        String insertOutidSql = """
        	INSERT INTO GDJ95.OUTID (ID, REASON, CREATEDATE)
        	VALUES (?, ?, SYSDATE)
        """;
        
        // 2. CUSTOMER 테이블 삭제 SQL (GDJ95 스키마 명시)
        String deleteCustomerSql = """
        	DELETE FROM GDJ95.CUSTOMER WHERE CUSTOMER_ID = ?
        """;
        
        try {
            conn = DBConnection.getConn(); 
			conn.setAutoCommit(false); // 트랜잭션 시작
            
            // 1. OUTID에 삽입
            pstmtOutid = conn.prepareStatement(insertOutidSql);
            pstmtOutid.setString(1, customerId);
            pstmtOutid.setString(2, reason);
            int outidResult = pstmtOutid.executeUpdate();
            
            if (outidResult == 1) {
                // 2. CUSTOMER에서 삭제
                pstmtDelete = conn.prepareStatement(deleteCustomerSql);
                pstmtDelete.setString(1, customerId);
                int deleteResult = pstmtDelete.executeUpdate();
                
                if (deleteResult == 1) {
                    conn.commit(); // 커밋
                    result = 1; // 최종 성공
                } else {
                    conn.rollback(); // 롤백
                    System.err.println("CUSTOMER 테이블 삭제 실패. ID: " + customerId);
                }
            } else {
                 conn.rollback(); // 롤백
                 System.err.println("OUTID 테이블 삽입 실패. ID: " + customerId);
            }
            
        } catch (SQLException e) {
            conn.rollback(); // 오류 발생 시 롤백
            System.err.println("고객 강제 탈퇴 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e; // 예외를 호출한 곳으로 던짐
        } finally {
            try {
                if (pstmtDelete != null) pstmtDelete.close();
                if (pstmtOutid != null) pstmtOutid.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // 커넥션을 반환하기 전에 auto commit을 다시 true로 설정
                    conn.close(); 
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
	}
}