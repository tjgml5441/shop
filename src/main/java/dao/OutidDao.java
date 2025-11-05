package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.Outid; // Outid DTO 사용

public class OutidDao {
    
    /**
     * 강제 탈퇴 ID 리스트 개수 조회
     * @return 전체 탈퇴 ID 수
     * @throws SQLException
     */
    public int selectOutidListCount() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        // GDJ95 스키마 명시
        String sql = "SELECT COUNT(*) FROM GDJ95.OUTID";
        
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
     * 강제 탈퇴 ID 리스트 조회 (페이징 포함)
     * @param beginRow 시작 행 번호
     * @param rowPerPage 페이지 당 행 수
     * @return 페이지에 해당하는 탈퇴 ID 리스트
     * @throws SQLException
     */
    public List<Outid> selectOutidListByPage(int beginRow, int rowPerPage) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Outid> outidList = new ArrayList<>();
        
        // ★★★ 수정된 부분: SQL에서 REASON 대신 MEMO 컬럼 사용 ★★★
        // GDJ95 스키마 명시
        String sql = """
                       SELECT ID, MEMO, CREATEDATE
                       FROM GDJ95.OUTID
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
                Outid outid = new Outid();
                outid.setId(rs.getString("ID"));
                // ★★★ 수정된 부분: rs.getString("MEMO")를 outid.setMemo()로 매핑 ★★★
                outid.setMemo(rs.getString("MEMO")); 
                outid.setCreateDate(rs.getDate("CREATEDATE"));
                outidList.add(outid);
            }
        } catch (SQLException e) {
            System.err.println("OutidDao: 강제 탈퇴 ID 리스트 조회 중 오류 발생: " + e.getMessage());
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
        return outidList;
    }
}