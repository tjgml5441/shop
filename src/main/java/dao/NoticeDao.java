package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.Notice;

// DBConnection.getConn() 메소드가 DB 연결 객체(Connection)를 반환한다고 가정합니다.
public class NoticeDao {
	// /emp/noticeList (페이지네이션을 위한 전체 개수 구하기)
	public int selectCount() {
		int count = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM notice";
		try {
			conn = DBConnection.getConn(); // DBConnection.getConn() 호출 가정
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1); // COUNT(*) 결과는 첫 번째 컬럼
			}
		} catch(Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		return count;
	}
	
	// /emp/noticeList
	public List<Notice> selectNoticeList(int beginRow, int rowPerPage) {
		List<Notice> list = new ArrayList<Notice>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = """
			SELECT
				notice_code noticeCode
				, notice_title noticeTitle
				, createdate
			FROM notice
			ORDER BY notice_code DESC
			OFFSET ? ROWS FETCH NEXT ? ROWS ONLY	
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, beginRow);
			stmt.setInt(2, rowPerPage);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Notice n = new Notice();
				n.setNoticeCode(rs.getInt("noticeCode"));
				n.setNoticeTitle(rs.getString("noticeTitle"));
				n.setCreatedate(rs.getString("createdate"));
				list.add(n);
			}
		} catch(Exception e1) {
			// conn.rollback();
			e1.printStackTrace(); // 콘솔에 예외메세지 출력
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		return list;
	}
	
	// /emp/addNotice (공지 등록)
	public int insertNotice(Notice n) {
	    int row = 0;
	    Connection conn = null;
	    // 시퀀스를 통해 nextval을 얻기 위한 별도의 PreparedStatement가 필요합니다.
	    PreparedStatement stmtNoticeCode = null; 
	    PreparedStatement stmtInsert = null;
	    ResultSet rs = null;
	    
	    // 1. 시퀀스에서 다음 공지 코드(noticeCode)를 얻는 쿼리
	    String sqlSelectCode = "SELECT SEQ_NOTICE.NEXTVAL FROM DUAL";
	    
	    // 2. 공지 데이터를 삽입하는 쿼리 (noticeCode를 포함)
	    String sqlInsert = """
	        INSERT INTO notice (notice_code, notice_title, notice_content, emp_code, createdate) 
	        VALUES (?, ?, ?, ?, SYSDATE)
	    """;
	    
	    try {
	        conn = DBConnection.getConn();
	        // conn.setAutoCommit(false); // 트랜잭션 관리가 필요할 경우 활성화

	        // A. 시퀀스에서 새로운 noticeCode 값 가져오기
	        stmtNoticeCode = conn.prepareStatement(sqlSelectCode);
	        rs = stmtNoticeCode.executeQuery();
	        int newNoticeCode = 0;
	        if (rs.next()) {
	            newNoticeCode = rs.getInt(1);
	        }
	        
	        // B. 새로운 noticeCode와 함께 데이터 삽입
	        stmtInsert = conn.prepareStatement(sqlInsert);
	        stmtInsert.setInt(1, newNoticeCode); // 1. 시퀀스에서 얻은 noticeCode
	        stmtInsert.setString(2, n.getNoticeTitle()); // 2. noticeTitle
	        stmtInsert.setString(3, n.getNoticeContent()); // 3. noticeContent
	        stmtInsert.setInt(4, n.getEmpCode()); // 4. empCode
	        
	        row = stmtInsert.executeUpdate();
	        
	        // conn.commit(); // 트랜잭션 관리가 필요할 경우 커밋
	    } catch(Exception e1) {
	        // conn.rollback();
	        e1.printStackTrace();
	    } finally {
	        // 리소스 해제는 열었던 순서의 역순으로
	        try {
	            if(rs != null) rs.close();
	            if(stmtNoticeCode != null) stmtNoticeCode.close();
	            if(stmtInsert != null) stmtInsert.close();
	            if(conn != null) conn.close();
	        } catch(Exception e2) {
	            e2.printStackTrace();
	        }
	    }
	    return row;
	}
	
	// /emp/noticeOne (공지 하나 상세보기)
	public Notice selectNoticeOne(int noticeCode) {
		Notice resultNotice = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = """
			SELECT
				notice_code noticeCode
				, notice_title noticeTitle
				, notice_content noticeContent
				, emp_code empCode
				, createdate
			FROM notice
			WHERE notice_code = ?
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, noticeCode);
			rs = stmt.executeQuery();
			if(rs.next()) {
				resultNotice = new Notice();
				resultNotice.setNoticeCode(rs.getInt("noticeCode"));
				resultNotice.setNoticeTitle(rs.getString("noticeTitle"));
				resultNotice.setNoticeContent(rs.getString("noticeContent"));
				resultNotice.setEmpCode(rs.getInt("empCode"));
				resultNotice.setCreatedate(rs.getString("createdate"));
			}
		} catch(Exception e1) {
			// conn.rollback();
			e1.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		return resultNotice;
	}
	
	// /emp/removeNotice (공지 삭제)
	public int deleteNotice(Notice n) {
		int row = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql = """
		    DELETE FROM notice WHERE notice_code = ?
		""";
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, n.getNoticeCode());
			row = stmt.executeUpdate();
		} catch(Exception e1) {
			// conn.rollback();
			e1.printStackTrace();
		} finally {
			try {
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		return row;
	}
}