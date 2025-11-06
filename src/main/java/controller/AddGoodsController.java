package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.UUID; // UUID 라이브러리 추가

import dto.Emp;
import dto.Goods; // Goods DTO 임포트 필요 (추정)
import dto.GoodsImg; // GoodsImg DTO 임포트 필요

// 파일 업로드를 위한 설정 추가
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 15    // 15MB
)
@WebServlet("/emp/addGoods")
public class AddGoodsController extends HttpServlet {

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.getRequestDispatcher("/WEB-INF/view/emp/addGoods.jsp").forward(request, response);
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
      // 1. 파라미터 받기 (멀티파트 폼 데이터)
      String goodsName = request.getParameter("goodsName");
      String goodsPrice = request.getParameter("goodsPrice");
      String pointRate = request.getParameter("pointRate");
      
      // 파일업로드는 Part 라이브러리 사용
      Part part = request.getPart("goodsImg");
      
      // 파일 정보 추출
      String originName = part.getSubmittedFileName();
      String contentType = part.getContentType();
      long filesize = part.getSize();
      
      // 2. 파일명 생성 (UUID를 사용하여 고유한 이름 생성 + 확장자)
      String filename = UUID.randomUUID().toString().replace("-", "");
      // filename = filename + originName의 확장자
      if (originName != null && originName.lastIndexOf(".") != -1) {
          filename += originName.substring(originName.lastIndexOf("."));
      }
      
      System.out.println("originName: "+originName);
      System.out.println("filename: "+filename);
      System.out.println("contentType: "+contentType);
      System.out.println("filesize: "+filesize);
      
      // 3. 이미지 타입 유효성 검사
      if(!(contentType.equals("image/png") || contentType.equals("image/jpeg") || contentType.equals("image/gif"))) {
         System.out.println("png, jpg, gif 파일만 허용");
         // 에러 메시지 처리가 필요하지만, 일단 리다이렉트
         response.sendRedirect(request.getContextPath() + "/emp/addGoods");
         return;
      }
      
      // 4. 상품 정보 및 이미지 정보 DTO 설정 (DB 로직은 생략된 상태)
      Emp loginEmp = (Emp)(request.getSession().getAttribute("loginEmp"));
      
      if (loginEmp == null) {
          response.sendRedirect(request.getContextPath() + "/out/login?msg=" + java.net.URLEncoder.encode("직원 로그인 후 이용 가능합니다.", "UTF-8"));
          return;
      }
      
      Goods goods = new Goods();
      goods.setGoodsName(goodsName);
      goods.setGoodsPrice(Integer.parseInt(goodsPrice));
      goods.setPointRate(Double.parseDouble(pointRate));
      goods.setEmpCode(loginEmp.getEmpCode());
      int goodsCode = 0; // insertGoods() 호출 후 반환받아야 함 (현재는 0으로 임시 설정)
      
      GoodsImg goodsImg = new GoodsImg();
      goodsImg.setGoodsCode(goodsCode);
      goodsImg.setFileName(filename);
      goodsImg.setOriginName(originName);
      goodsImg.setContentType(contentType); // Content Type 추가
      goodsImg.setFilesize(filesize);
      
      // 5. 이미지 파일을 서버의 실제 경로에 저장
      String realPath = request.getServletContext().getRealPath("upload");
      File saveFile = new File(realPath, filename); // realPath에 filename으로 저장할 파일 객체 생성
      
      // 디렉토리가 없으면 생성 (안전한 파일 저장)
      if (!saveFile.getParentFile().exists()) {
          saveFile.getParentFile().mkdirs();
      }
      
      try {
          part.write(saveFile.getAbsolutePath()); // 파일 저장 실행
      } catch (IOException e) {
          System.err.println("파일 저장 실패: " + e.getMessage());
          // 파일 저장 실패 시 예외 처리 로직 (DB 트랜잭션 롤백 등 필요)
          response.sendRedirect(request.getContextPath() + "/emp/addGoods?msg=" + java.net.URLEncoder.encode("파일 저장에 실패했습니다.", "UTF-8"));
          return;
      }
      
      // 6. DB에 상품(Goods) 및 이미지(GoodsImg) 정보 저장 (DAO 로직 생략)
      // GoodsDao goodsDao = new GoodsDao();
      // int goodsResult = goodsDao.insertGoods(goods); 
      // goodsCode = goods.getGoodsCode(); // PK 값 반환 (예상)
      // goodsImg.setGoodsCode(goodsCode);
      // int imgResult = goodsDao.insertGoodsImg(goodsImg);
      
      System.out.println("상품 등록 완료 (가정): " + goodsName + ", 파일명: " + filename);
      
      // 7. 성공 시 리다이렉트
      response.sendRedirect(request.getContextPath() + "/emp/goodsList?msg=" + java.net.URLEncoder.encode("상품이 성공적으로 등록되었습니다.", "UTF-8"));
   }
}