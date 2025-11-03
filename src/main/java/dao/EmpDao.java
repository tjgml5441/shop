package dao;

import java.sql.*;
import dto.Emp;

public class EmpDao {
    
    public Emp login(String id, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Emp employee = null;
        
        String sql = "SELECT * FROM EMP WHERE EMP_ID = ? AND EMP_PW = ?";
        
        try {
            conn = DBConnection.getConn();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, password);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                employee = new Emp();
                employee.setEmpCode(rs.getInt("EMP_CODE"));
                employee.setEmpId(rs.getString("EMP_ID"));
                employee.setEmpPw(rs.getString("EMP_PW")); 
                employee.setEmpName(rs.getString("EMP_NAME"));
                employee.setActive(rs.getString("ACTIVE"));
                employee.setCreateDate(rs.getDate("CREATEDATE"));
            }
        } catch (SQLException e) {
            System.err.println("Employee 로그인 중 오류 발생: " + e.getMessage());
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
        return employee;
    }
}