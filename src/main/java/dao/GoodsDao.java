package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Goods;
import dto.GoodsImg;

// DBConnection 클래스는 외부에서 제공되었다고 가정합니다.
// DBConnection.getConn()이 Connection 객체를 반환한다고 가정합니다.

public class GoodsDao {
	
	/**
	 * 상품등록 + 이미지 등록 (트랜잭션)
	 * @param goods 상품 DTO
	 * @param img 상품 이미지 DTO
	 * @return 성공시 true, 실패시 false
	 */
	public boolean insertGoodsAndImg(Goods goods, GoodsImg img) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement stmtSeq = null; 
		PreparedStatement stmtGoods = null; 
		PreparedStatement stmtImg = null; 
		ResultSet rs = null;
		
		String sqlSeq = """
			select seq_goods.nextval from dual
		""";
		
		String sqlGoods = """
			insert into goods(goods_code, goods_name, goods_price, emp_id, point_rate, soldout, createdate)
			values(?, ?, ?, ?, ?, null, sysdate)	
		"""; // ✅ 수정됨: 'emp_code' -> 'emp_id'
		
		String sqlImg = """
			insert into goods_img(goods_code, filename, origin_name, content_type, filesize, createdate)
			values(?, ?, ?, ?, ?, sysdate)
		""";
		
		try {
			conn = DBConnection.getConn(); // DB 연결
			conn.setAutoCommit(false); // 트랜잭션 시작
			
			// 1. goods_code 시퀀스 값 가져오기
			stmtSeq = conn.prepareStatement(sqlSeq);
			rs = stmtSeq.executeQuery();
			int goodsCode = 0;
			if(rs.next()) {
				goodsCode = rs.getInt(1);
			} else {
				throw new SQLException("상품 코드 시퀀스 값을 가져오는데 실패했습니다.");
			}
			
			// 2. Goods 테이블에 삽입
			stmtGoods = conn.prepareStatement(sqlGoods);
			stmtGoods.setInt(1, goodsCode);
			stmtGoods.setString(2, goods.getGoodsName());
			stmtGoods.setInt(3, goods.getGoodsPrice());
			stmtGoods.setInt(4, goods.getEmpCode()); // DTO의 getEmpCode() 값을 emp_id 컬럼에 삽입
			stmtGoods.setDouble(5, goods.getPointRate());
			
			int rowGoods = stmtGoods.executeUpdate();
			if(rowGoods != 1) {
				throw new SQLException("Goods 테이블에 데이터 삽입 실패. (0건 처리)");
			}
			
			// 3. GoodsImg 테이블에 삽입
			stmtImg = conn.prepareStatement(sqlImg);
			stmtImg.setInt(1, goodsCode);
			stmtImg.setString(2, img.getFileName());
			stmtImg.setString(3, img.getOriginName());
			stmtImg.setString(4, img.getContentType());
			stmtImg.setLong(5, img.getFilesize());
			
			int rowImg = stmtImg.executeUpdate();
			if(rowImg != 1) {
				throw new SQLException("GoodsImg 테이블에 데이터 삽입 실패. (0건 처리)");
			}
			
			conn.commit(); // 커밋
			result = true;
			
		} catch (SQLException e) {
			try {
				if(conn != null) conn.rollback(); // 롤백
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			System.err.println("GoodsDao.insertGoodsAndImg DB 오류: " + e.getMessage());
			e.printStackTrace();
			// 상위 Controller에서 예외를 처리할 수 있도록 RuntimeException으로 변환하여 던집니다.
			throw new RuntimeException("상품등록 DB 오류 발생 (롤백됨)", e);
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmtSeq != null) stmtSeq.close();
				if(stmtGoods != null) stmtGoods.close();
				if(stmtImg != null) stmtImg.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 모든 상품의 목록과 대표 이미지 파일명을 조회합니다.
	 */
	public List<Goods> selectGoodsList() throws SQLException {
		List<Goods> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String sql = """
				SELECT 
				    g.goods_code, g.goods_name, g.goods_price, g.point_rate, g.soldout, g.createdate, 
				    i.filename AS goods_filename 
				FROM goods g
				JOIN goods_img i ON g.goods_code = i.goods_code
				ORDER BY g.createdate DESC
				"""; // emp_id 컬럼을 사용하지 않아 select 구문은 문제 없습니다.
		
		try {
			conn = DBConnection.getConn();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				Goods goods = new Goods();
				goods.setGoodsCode(rs.getInt("goods_code"));
				goods.setGoodsName(rs.getString("goods_name"));
				goods.setGoodsPrice(rs.getInt("goods_price"));
				goods.setPointRate(rs.getDouble("point_rate"));
				goods.setSoldout(rs.getString("soldout"));
				goods.setCreatedate(rs.getString("createdate"));
				// 조인하여 가져온 이미지 파일명을 Goods DTO의 fileName 필드에 설정
				goods.setFileName(rs.getString("goods_filename")); 
				list.add(goods);
			}
			
		} finally {
			// 자원 해제
			if(rs != null) try { rs.close(); } catch(SQLException e) { e.printStackTrace(); }
			if(stmt != null) try { stmt.close(); } catch(SQLException e) { e.printStackTrace(); }
			if(conn != null) try { conn.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
		
		return list;
	}
}