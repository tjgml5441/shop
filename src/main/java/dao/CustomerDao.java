package dao;

import java.sql.*;
import dto.Customer;

public class CustomerDao {
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
     * 아이디 중복을 확인합니다.
     * @param id 확인할 고객 ID
     * @return 중복이면 true, 중복이 아니면 false
     */
    public boolean isIdDuplicate(String id) {
        String sql = "SELECT COUNT(*) FROM GDJ95.CUSTOMER WHERE CUSTOMER_ID = ?";
        
        try (Connection conn = DBConnection.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            
            pstmt.setString(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[CustomerDao] isIdDuplicate SQL Exception: " + e.getMessage());
            e.printStackTrace();
            return true; 
        }
        return false;
    }

    public int insertCustomer(Customer customer) {
        
        int result = 0;
        String sql = "INSERT INTO CUSTOMER (CUSTOMER_CODE, CUSTOMER_ID, CUSTOMER_PW, CUSTOMER_NAME, CUSTOMER_PHONE, POINT, CREATEDATE) "
                   + "VALUES (GDJ95.SEQ_CUSTOMER.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE)";
        try (Connection conn = DBConnection.getConn(); 
             PreparedStatement pstmt = conn.prepareStatement(sql);) { 
            conn.setAutoCommit(true); 

            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getCustomerPw());
            pstmt.setString(3, customer.getCustomerName());
            pstmt.setString(4, customer.getCustomerPhone());
            pstmt.setInt(5, customer.getPoint());

            result = pstmt.executeUpdate();
            
            System.out.println("[CustomerDao] insertCustomer: 삽입된 행 수 = " + result); 
            
        } catch (SQLException e) {
            System.out.println("[CustomerDao] insertCustomer SQL Exception: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("VendorError: " + e.getErrorCode());
            e.printStackTrace();
        } 
        
        return result;
    }
}