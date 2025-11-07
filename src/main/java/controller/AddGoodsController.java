package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpSession; 

import java.io.File;
import java.io.IOException;
import java.util.UUID; 

import dao.GoodsDao; 
import dto.Emp;
import dto.Goods; 
import dto.GoodsImg; 

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 15    // 15MB
)
@WebServlet("/emp/addGoods")
public class AddGoodsController extends HttpServlet {

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      // 관리자 세션 체크 로직은 생략하고 바로 폼으로 포워딩
      request.getRequestDispatcher("/WEB-INF/view/emp/addGoods.jsp").forward(request, response);
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NumberFormatException {
      request.setCharacterEncoding("UTF-8");
      
      HttpSession session = request.getSession();
      Emp loginEmp = (Emp)session.getAttribute("loginEmp");
      
      // 관리자 세션 체크
      if (loginEmp == null) {
          response.sendRedirect(request.getContextPath() + "/out/login");
          return;
      }
      
      // 파일 저장 경로 설정 (웹 애플리케이션의 실제 경로)
      String path = request.getServletContext().getRealPath("/upload"); 
      File dir = new File(path);
      if (!dir.exists()) {
          dir.mkdirs(); // 디렉토리가 없으면 생성
      }
      
      File saveFile = null; // 물리적으로 저장된 파일 객체
      boolean fileSaved = false; // 파일 저장 성공 여부 플래그
      
      try {
          // 1. 요청 파라미터 및 파일 파트 받기
          String goodsName = request.getParameter("goodsName");
          String goodsPriceStr = request.getParameter("goodsPrice");
          String pointRateStr = request.getParameter("pointRate");
          Part filePart = request.getPart("goodsImg"); 
          
          // 2. 유효성 검사
          if (goodsName == null || goodsName.trim().isEmpty() || 
              goodsPriceStr == null || goodsPriceStr.trim().isEmpty() ||
              pointRateStr == null || pointRateStr.trim().isEmpty()) {
              throw new IllegalArgumentException("필수 입력값을 모두 입력해야 합니다.");
          }
          
          int goodsPrice = 0;
          double pointRate = 0.0;
          try {
              goodsPrice = Integer.parseInt(goodsPriceStr);
              pointRate = Double.parseDouble(pointRateStr);
          } catch (NumberFormatException e) {
              throw new IllegalArgumentException("가격 또는 포인트율이 올바른 숫자가 아닙니다.");
          }
          
          if (goodsPrice <= 0 || pointRate < 0) {
              throw new IllegalArgumentException("가격은 0보다 커야 하고, 포인트율은 0보다 크거나 같아야 합니다.");
          }
          
          // 3. 파일 처리 (물리적 저장)
          String originName = filePart.getSubmittedFileName();
          long fileSize = filePart.getSize();
          String contentType = filePart.getContentType();
          
          if (originName == null || originName.isEmpty() || fileSize == 0) {
              throw new IllegalArgumentException("상품 이미지를 등록해야 합니다.");
          }
          
          // 저장할 파일명 생성 (UUID 사용)
          String filename = UUID.randomUUID().toString().replace("-", "") + originName.substring(originName.lastIndexOf(".")); 
          saveFile = new File(path, filename);
          filePart.write(saveFile.getAbsolutePath()); 
          fileSaved = true; // 파일 저장 성공 플래그 설정
          
          // 4. DTO 생성 및 DAO 호출
          Goods goods = new Goods();
          goods.setGoodsName(goodsName);
          goods.setGoodsPrice(goodsPrice);
          goods.setPointRate(pointRate);
          goods.setEmpCode(loginEmp.getEmpCode()); 
          
          GoodsImg goodsImg = new GoodsImg();
          goodsImg.setFileName(filename);
          goodsImg.setOriginName(originName);
          goodsImg.setContentType(contentType);
          goodsImg.setFilesize(fileSize);
          
          // 상품(Goods) 및 이미지(GoodsImg) 정보 저장
          GoodsDao goodsDao = new GoodsDao();
          boolean success = goodsDao.insertGoodsAndImg(goods, goodsImg); 
          
          // 5. 결과 처리
          if (success) {
              // 성공 시 goodsList로 리다이렉트
              response.sendRedirect(request.getContextPath() + "/emp/goodsList");
          } else {
               // DB 트랜잭션 실패 시 물리적 파일 삭제 (롤백)
              if(fileSaved && saveFile != null && saveFile.exists()) saveFile.delete(); 
              throw new RuntimeException("상품 등록 트랜잭션이 실패했습니다. (DB 롤백됨)");
          }
          
      } catch (IllegalArgumentException e) {
          // 사용자 입력 오류 처리
          request.setAttribute("errorMessage", "입력값 오류: " + e.getMessage());
          request.getRequestDispatcher("/WEB-INF/view/emp/addGoods.jsp").forward(request, response);
      } catch (Exception e) {
          // 서버 측 오류 (DB, 파일 시스템 등) 처리
          System.err.println("AddGoodsController 처리 중 예외 발생: " + e.getMessage());
          e.printStackTrace();
          
          // DB 오류로 실패한 경우, 저장된 파일 삭제
          if(fileSaved && saveFile != null && saveFile.exists()) saveFile.delete(); 
          
          String userMessage = e.getMessage().contains("DB 오류") ? "상품 등록 중 서버 오류가 발생했습니다. (DAO SQL 오류)" : "상품 등록 중 알 수 없는 서버 오류가 발생했습니다.";
          request.setAttribute("errorMessage", userMessage);
          request.getRequestDispatcher("/WEB-INF/view/emp/addGoods.jsp").forward(request, response);
      }
   }
}