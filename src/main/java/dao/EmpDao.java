package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.Emp;

public class EmpDao {
	
	/**
	 * 사원 목록 전체 개수 조회
	 * @return 전체 사원 수
	 * @throws SQLException
	 */
	public int selectEmpListCount() throws SQLException {
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        // GDJ95 스키마 명시
        String sql = "SELECT COUNT(*) FROM GDJ95.EMP";
        
        try {
            conn = DBConnection.getConn(); 
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
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
        return count;
	}
	
	/**
	 * 사원 목록 조회 (페이징 적용)
	 * @param beginRow 시작 행 번호 (0부터 시작)
	 * @param rowPerPage 페이지 당 행 수
	 * @return 페이지에 해당하는 Emp 리스트
	 * @throws SQLException
	 */
	public List<Emp> selectEmpListByPage(int beginRow, int rowPerPage) throws SQLException {
		// 자원 선언
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
		
        // SQL 구문 오류 수정 및 스키마 GDJ95 명시
		String sql = """
						SELECT EMP_CODE empCode, EMP_ID empId, EMP_NAME empName, ACTIVE active, CREATEDATE createdate
						FROM GDJ95.EMP
						ORDER BY EMP_CODE
						OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
					 """;
		
		// 리스트 객체 미리 생성
		List<Emp> list = new ArrayList<>();
		
		try {
			conn = DBConnection.getConn();
			pstmt = conn.prepareStatement(sql);
			
			// OFFSET 및 FETCH 파라미터 바인딩
			pstmt.setInt(1, beginRow); 
			pstmt.setInt(2, rowPerPage);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Emp e = new Emp();
				
				e.setEmpCode(rs.getInt("empCode"));
				e.setEmpId(rs.getString("empId"));
				e.setEmpName(rs.getString("empName"));
				e.setActive(rs.getInt("active"));
				e.setCreateDate(rs.getDate("createdate"));
				
				list.add(e);
			}
		} catch (SQLException e) {
            System.err.println("EmpDao: 사원 리스트 조회 중 오류 발생: " + e.getMessage());
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
		return list;
	}
    
    /**
     * ★★★ 오류 해결 메서드: 사원 로그인 처리 ★★★
     * @param id 사원 ID
     * @param password 사원 비밀번호
     * @return 로그인 성공 시 Emp 객체 (ACTIVE 포함), 실패 시 null
     * @throws SQLException
     */
    public Emp login(String id, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Emp employee = null;
        
        // 스키마 GDJ95 명시
        // EMP_PW는 암호화되지 않은 상태라고 가정하고 조회
        String sql = "SELECT EMP_CODE, EMP_ID, EMP_PW, EMP_NAME, ACTIVE, CREATEDATE FROM GDJ95.EMP WHERE EMP_ID = ? AND EMP_PW = ?";
        
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
                // ACTIVE 컬럼을 가져옵니다.
                employee.setActive(rs.getInt("ACTIVE")); 
                employee.setCreateDate(rs.getDate("CREATEDATE"));
            }
        } catch (SQLException e) {
            System.err.println("Employee 로그인 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e; // 예외를 호출한 곳으로 던짐
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
    
    /**
     * 사원 활성화/비활성화 상태 변경
     * @param empCode 변경할 사원의 코드
     * @param newActiveValue 새로운 활성화 상태 값 (0: 비활성화, 1: 활성화)
     * @return 변경된 행의 수 (1이면 성공)
     * @throws SQLException
     */
    public int updateEmpActive(int empCode, int newActiveValue) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        // ACTIVE 컬럼 업데이트 (GDJ95 스키마 명시)
        String sql = "UPDATE GDJ95.EMP SET ACTIVE = ? WHERE EMP_CODE = ?";

        try {
            conn = DBConnection.getConn();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, newActiveValue);
            pstmt.setInt(2, empCode);

            result = pstmt.executeUpdate();
            
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
}