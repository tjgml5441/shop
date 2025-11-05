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
     * 고객 목록 조회 (페이징 적용, 활성 고객만)
     * @param beginRow 시작 행 번호
     * @param rowPerPage 페이지 당 행 수
     * @param type 현재는 "active"만 가정
     * @return 페이지에 해당하는 고객 목록
     * @throws SQLException
     */
    public List<Customer> selectCustomerListByPage(int beginRow, int rowPerPage, String type) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Customer> list = new ArrayList<>();
        
        // SQL 구문: CREATEDATE를 createDate로 별칭 부여
        String sql = """
                        SELECT CUSTOMER_CODE customerCode, CUSTOMER_ID customerId, CUSTOMER_NAME customerName, CUSTOMER_PHONE customerPhone, POINT point, CREATEDATE createDate
                        FROM GDJ95.CUSTOMER
                        ORDER BY CUSTOMER_CODE DESC
                        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                     """;
        
        // type="out"인 경우는 OutidDao에서 처리해야 합니다. 여기서는 "active"만 처리합니다.
        
        try {
            conn = DBConnection.getConn();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, beginRow); 
            pstmt.setInt(2, rowPerPage);
            
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                Customer c = new Customer();
                
                c.setCustomerCode(rs.getInt("customerCode"));
                c.setCustomerId(rs.getString("customerId"));
                c.setCustomerName(rs.getString("customerName"));
                c.setCustomerPhone(rs.getString("customerPhone"));
                c.setPoint(rs.getInt("point"));
                c.setCreateDate(rs.getDate("createDate")); 
                
                list.add(c);
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
        
        return list;
    }

    /**
     * 직원에 의한 고객 강제 탈퇴 (CUSTOMER 삭제 및 OUTID 삽입을 트랜잭션 처리)
     * @param customerId 탈퇴할 고객 ID
     * @param reason 탈퇴 사유 (OUTID 테이블의 MEMO 컬럼에 해당)
     * @return 성공 시 1, 실패 시 0
     * @throws SQLException
     */
    public int deleteCustomerByEmp(String customerId, String reason) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmtDelete = null;
        PreparedStatement pstmtOutid = null;
        int result = 0;
        
        // 트랜잭션 처리를 위해 Connection 획득
        try {
            conn = DBConnection.getConn();
            conn.setAutoCommit(false); // 수동 커밋 모드 설정

            String deleteCustomerSql = "DELETE FROM GDJ95.CUSTOMER WHERE CUSTOMER_ID = ?";
            // ★★★ 수정된 부분: OUTID 테이블에 MEMO 컬럼을 사용하도록 수정 ★★★
            String insertOutidSql = "INSERT INTO GDJ95.OUTID(ID, MEMO, CREATEDATE) VALUES(?, ?, SYSDATE)";
            
            // 1. OUTID에 탈퇴 기록 삽입
            pstmtOutid = conn.prepareStatement(insertOutidSql);
            pstmtOutid.setString(1, customerId);
            pstmtOutid.setString(2, reason); // 사유를 MEMO 컬럼에 삽입
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
            if (conn != null) {
                try {
                    conn.rollback(); // 오류 시 롤백
                } catch (SQLException rb) {
                    rb.printStackTrace();
                }
            }
            System.err.println("고객 강제 탈퇴 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e; // 예외를 호출한 곳으로 던집니다.
        } finally {
            // 자원 해제
            try {
                if (pstmtDelete != null) pstmtDelete.close();
                if (pstmtOutid != null) pstmtOutid.close();
                if (conn != null) conn.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    // 고객 정보 삽입 (회원가입)
    public int insertCustomer(Customer customer) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;
        
        // 시퀀스 사용, 포인트는 0으로 고정
        String sql = """
                     INSERT INTO GDJ95.CUSTOMER 
                     (CUSTOMER_CODE, CUSTOMER_ID, CUSTOMER_PW, CUSTOMER_NAME, CUSTOMER_PHONE, POINT, CREATEDATE)
                     VALUES (SEQ_CUSTOMER.NEXTVAL, ?, ?, ?, ?, 0, SYSDATE)
                     """;
        
        try {
            conn = DBConnection.getConn();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getCustomerPw());
            pstmt.setString(3, customer.getCustomerName());
            pstmt.setString(4, customer.getCustomerPhone());
            
            result = pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("고객 회원가입 중 오류 발생: " + e.getMessage());
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
    
    // ID 중복 검사 (단일 메서드)
    public String checkDuplicationId(String id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String duplicatedId = null;
        
        String sql = "SELECT CUSTOMER_ID FROM GDJ95.CUSTOMER WHERE CUSTOMER_ID = ?";
        
        try {
            conn = DBConnection.getConn();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                duplicatedId = rs.getString("CUSTOMER_ID");
            }
        } catch (SQLException e) {
            System.err.println("ID 중복 검사 중 오류 발생: " + e.getMessage());
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

}