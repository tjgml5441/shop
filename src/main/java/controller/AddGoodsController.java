package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig; // 파일 업로드를 위해 추가
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part; // 파일 업로드를 위해 추가

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.sql.SQLException; // DB 처리를 위해 추가

import dao.GoodsDao; // 가정
import dto.Emp;
import dto.Goods;
import dto.GoodsImg;

@WebServlet("/emp/addGoods")
@MultipartConfig(
	fileSizeThreshold = 1024 * 1024 * 10, // 10MB
	maxFileSize = 1024 * 1024 * 50, // 50MB
	maxRequestSize = 1024 * 1024 * 100 // 100MB
)
public class AddGoodsController extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/view/emp/addGoods.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 파라미터 및 파일 정보 추출
		String goodsName = request.getParameter("goodsName");
		String goodsPrice = request.getParameter("goodsPrice");
		String pointRate = request.getParameter("pointRate");
		
		// 파일업로드는 Part 라이브러리 사용
		Part part = request.getPart("goodsImg");
		String originName = part.getSubmittedFileName();
		String contentType = part.getContentType();
		long filesize = part.getSize();
		
		// 2. 파일 이름 생성 및 확장자 추출
		String filename = UUID.randomUUID().toString().replace("-", "");
		// filename = filename + originName의 확장자
		if (originName != null && originName.lastIndexOf(".") > 0) {
		    filename += originName.substring(originName.lastIndexOf("."));
		}
		
		System.out.println("originName: "+originName);
		System.out.println("filename: "+filename);
		System.out.println("contentType: "+contentType);
		System.out.println("filesize: "+filesize);
		
		// 3. 파일 유효성 검사 (png, jpg, gif만 허용)
		if(!(contentType != null && (contentType.equals("image/png") || contentType.equals("image/jpeg") || contentType.equals("image/gif")))) {
			System.out.println("png, jpg, gif 파일만 허용");
			response.sendRedirect(request.getContextPath() + "/emp/addGoods?msg=" + java.net.URLEncoder.encode("png, jpg, gif 파일만 등록할 수 있습니다.", "UTF-8"));
			return;
		}
		
		// 4. 로그인 정보 확인
		Emp loginEmp = (Emp)(request.getSession().getAttribute("loginEmp"));
		if (loginEmp == null) {
		    response.sendRedirect(request.getContextPath() + "/out/login");
		    return;
		}
		
		// 5. Goods DTO 설정
		Goods goods = new Goods();
		goods.setGoodsName(goodsName);
		// 숫자 변환 실패 시 예외 처리가 필요하지만, 스니펫 기반으로 단순화
		try {
		    goods.setGoodsPrice(Integer.parseInt(goodsPrice));
		    goods.setPointRate(Double.parseDouble(pointRate));
		} catch (NumberFormatException e) {
		    response.sendRedirect(request.getContextPath() + "/emp/addGoods?msg=" + java.net.URLEncoder.encode("가격과 포인트 적립률을 올바르게 입력해주세요.", "UTF-8"));
		    return;
		}
		goods.setEmpCode(loginEmp.getEmpCode());
		
		// 6. GoodsImg DTO 설정
		GoodsImg goodsImg = new GoodsImg();
		// goodsImg.setGoodsCode(goodsCode); // goodsCode는 insertGoods() 후에 설정됨
		goodsImg.setFileName(filename);
		goodsImg.setOriginName(originName);
		goodsImg.setContentType(contentType); // ContentType 추가
		goodsImg.setFilesize(filesize);
		
		// 7. DAO를 통한 상품 및 이미지 등록 (트랜잭션 처리 필요)
		GoodsDao goodsDao = new GoodsDao();
        
		try {
		    // insertGoods 메서드는 상품을 삽입하고 생성된 goodsCode를 반환해야 합니다. (가정)
		    // 또는 트랜잭션으로 처리하는 단일 메서드를 호출해야 합니다.
		    boolean goodsCode = goodsDao.insertGoodsAndImg(goods, goodsImg);
		    
		    // 8. 이미지를 서버에 저장
		    String realPath = request.getServletContext().getRealPath("upload");
		    File saveDir = new File(realPath);
		    if (!saveDir.exists()) {
		        saveDir.mkdirs(); // 디렉토리가 없으면 생성
		    }
		    File saveFile = new File(realPath, filename);
		    part.write(saveFile.getAbsolutePath());
		    
		    // 9. 성공 시 리다이렉트
		    response.sendRedirect(request.getContextPath() + "/emp/goodsList?msg=" + java.net.URLEncoder.encode("상품 등록 성공 (코드: " + goodsCode + ")", "UTF-8"));
		    
		} catch (Exception e) {
		    System.err.println("상품 등록 중 오류 발생: " + e.getMessage());
		    e.printStackTrace();
		    // 실패 시 리다이렉트
		    response.sendRedirect(request.getContextPath() + "/emp/addGoods?msg=" + java.net.URLEncoder.encode("상품 등록에 실패했습니다. (DB 오류)", "UTF-8"));
		}
	}
}