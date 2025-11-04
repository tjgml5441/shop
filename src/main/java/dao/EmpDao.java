package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.Emp;

public class EmpDao {
    public int updateEmpActive(int empCode, int newActive) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        String sql = "UPDATE GDJ95.EMP SET ACTIVE = ? WHERE EMP_CODE = ?";
        
        try {
            conn = DBConnection.getConn();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, newActive); 
            pstmt.setInt(2, empCode); 
            
            result = pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("[EmpDao] updateEmpActive SQL Exception: " + e.getMessage());
            e.printStackTrace();
            throw e; 
        } finally {
            // 자원 해제
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public int selectEmpTotalCount() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int totalCount = 0;
        
        String sql = "SELECT COUNT(*) FROM GDJ95.EMP";
        
        try {
            conn = DBConnection.getConn();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
        } finally {
            // 자원 해제
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return totalCount;
    }

	public List<Emp> selectEmpListByPage(int beginRow, int rowPerPage) throws SQLException {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

		String sql = """
						SELECT EMP_CODE empCode, EMP_ID empId, EMP_NAME empName, ACTIVE active, CREATEDATE createDate
						FROM GDJ95.EMP
						ORDER BY EMP_CODE
						OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
					 """;
		
		List<Emp> list = new ArrayList<>();
		
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, beginRow); 
			pstmt.setInt(2, rowPerPage);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Emp e = new Emp();
				
				e.setEmpCode(rs.getInt("empCode"));
				e.setEmpId(rs.getString("empId"));
				e.setEmpName(rs.getString("empName"));
				e.setActive(rs.getInt("active")); 
				e.setCreateDate(rs.getDate("createDate"));
				
				list.add(e);
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

    public Emp login(String id, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Emp employee = null;
        
        String sql = "SELECT * FROM GDJ95.EMP WHERE EMP_ID = ? AND EMP_PW = ?";
        
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
                employee.setActive(rs.getInt("ACTIVE")); // NUMBER/INT로 가정
                employee.setCreateDate(rs.getDate("CREATEDATE"));
            }
        } catch (SQLException e) {
            System.err.println("Employee 로그인 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            // 자원 해제
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