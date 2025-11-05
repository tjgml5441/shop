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
// import java.io.InputStream; // 기존 코드에 있었으나, part.write() 사용 시 불필요
// import java.io.OutputStream; // 기존 코드에 있었으나, part.write() 사용 시 불필요
// import java.nio.file.Files;  // 기존 코드에 있었으나, part.write() 사용 시 불필요
import java.util.UUID;

// 임시 Mock DTO (실제 프로젝트에 맞게 import 하세요)
import dto.Emp;
import dto.Goods;
import dto.GoodsImg;

// ★ 수정 1: 파일 업로드를 처리하기 위해 @MultipartConfig 어노테이션 추가
@WebServlet("/emp/addGoods")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class AddGoodsController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/emp/addGoods.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String goodsName = request.getParameter("goodsName");
        String goodsPrice = request.getParameter("goodsPrice");
        String pointRate = request.getParameter("pointRate");
        
        // 파일업로드는 Part 라이브러리 사용
        Part part = request.getPart("goodsImg");
        
        // ★ 파일 정보를 먼저 추출합니다.
        String originName = part.getSubmittedFileName();
        String filename = UUID.randomUUID().toString().replace("-", "");
        
        // filename = filename + originName의 확장자
        if (originName != null && originName.contains(".")) {
            filename += originName.substring(originName.lastIndexOf("."));
        } else {
             // 파일 확장자가 없는 경우 처리 (예: 파일명만 있는 경우)
            System.err.println("경고: 파일 확장자를 찾을 수 없습니다.");
        }
        
        String contentType = part.getContentType();
        long filesize = part.getSize();
        
        // ★ 디버깅 정보 출력
        System.out.println("★디버그: originName: " + originName);
        System.out.println("★디버그: filename: " + filename);
        System.out.println("★디버그: contentType: " + contentType);
        System.out.println("★디버그: filesize: " + filesize + " bytes");
        
        // 파일 유효성 검사
        if(!(contentType.equals("image/png") || contentType.equals("image/jpeg") || contentType.equals("image/gif"))) {
            System.out.println("png, jpg, gif 파일만 허용");
            response.sendRedirect(request.getContextPath() + "/emp/addGoods?msg=imageTypeFail");
            return;
        }
        
        Emp loginEmp = (Emp)(request.getSession().getAttribute("loginEmp"));
        if (loginEmp == null) {
            response.sendRedirect(request.getContextPath() + "/out/login"); // 세션 만료 시 로그인 페이지로
            return;
        }
        
        // --- 1. 상품(Goods) DB 등록 처리 ---
        Goods goods = new Goods();
        goods.setGoodsName(goodsName);
        goods.setGoodsPrice(Integer.parseInt(goodsPrice));
        goods.setPointRate(Double.parseDouble(pointRate));
        goods.setEmpCode(loginEmp.getEmpCode());
        
        int goodsCode = 0; 
        
        try {
            // [Mock Code] 실제 GoodsDao.insertGoods(goods) 호출하고 goodsCode를 반환받아야 합니다.
            // goodsCode = new GoodsDao().insertGoods(goods); 
            goodsCode = 123; // 임시 Mock 코드 (성공적으로 DB에 저장 후 발급받은 PK라고 가정)
            if (goodsCode == 0) {
                 throw new Exception("상품 등록(Goods DB) 실패: goodsCode가 반환되지 않음");
            }
        } catch (Exception e) {
            System.err.println("★에러: 상품 등록 중 DB 오류: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/emp/addGoods?msg=dbError");
            return;
        }
        // ---------------------------------
        
        
        // --- 2. 이미지 파일 저장 및 GoodsImg DB 처리 ---
        // 이미지를 저장할 서버의 실제 경로를 얻어옵니다. (웹 컨텍스트 경로)
        String realPath = request.getServletContext().getRealPath("upload");
        
        // ★★★ 디버그: 파일 저장 시도 경로 출력 ★★★
        System.out.println("★디버그: 파일 저장 시도 경로 (realPath): " + realPath);
        
        // ★ 수정 2: 'upload' 폴더가 없으면 생성하는 로직 추가
        File uploadDir = new File(realPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs(); // 디렉토리가 없으면 생성
            System.out.println("★디버그: 업로드 디렉토리 생성 시도 결과: " + created);
            
            // 디렉토리 생성에 실패했을 경우 (권한 문제 등)
            if (!created) {
                System.err.println("★에러: 업로드 디렉토리 생성 실패. 권한을 확인하세요: " + realPath);
                response.sendRedirect(request.getContextPath() + "/emp/addGoods?msg=dirCreateFail");
                return;
            }
        } else {
            System.out.println("★디버그: 업로드 디렉토리 이미 존재함.");
        }
        
        File saveFile = new File(realPath, filename); // 저장될 파일 객체 생성
        System.out.println("★디버그: 최종 저장될 파일 경로: " + saveFile.getAbsolutePath()); 

        try {
            // ★ 수정 3: part.write()를 사용하여 파일 저장 (파일 쓰기가 여기서 일어남)
            part.write(saveFile.getAbsolutePath());
            System.out.println("★디버그: 파일 저장 성공! 파일명: " + filename);
            
            // --- GoodsImg DB 처리 ---
            GoodsImg goodsImg = new GoodsImg();
            goodsImg.setGoodsCode(goodsCode);
            goodsImg.setFileName(filename);
            goodsImg.setOriginName(originName);
            goodsImg.setContentType(contentType); 
            goodsImg.setFilesize(filesize);
            
            // [Mock Code] 실제 GoodsImgDao.insertGoodsImg(goodsImg) 호출
            // int imgResult = new GoodsImgDao().insertGoodsImg(goodsImg); 
            int imgResult = 1; // 임시 Mock 코드
            
            if (imgResult == 0) {
                 throw new Exception("상품 이미지 DB 등록 실패");
            }
            // --------------------------
            
            // 최종 성공 시 상품 목록 페이지로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/emp/goodsList"); 
            
        } catch (Exception e) {
            // 파일 저장 또는 이미지 DB 등록 실패 시
            System.err.println("★에러: 파일 저장 또는 DB 등록 실패 상세 메시지: " + e.getMessage());
            e.printStackTrace();
            
            // 파일 저장이 실패했으므로, 앞서 등록된 상품(Goods) 정보는 롤백(삭제)하는 것이 좋습니다.
            // [Mock Code] new GoodsDao().deleteGoods(goodsCode); 
            System.err.println("★경고: DB에 등록된 상품(Code: " + goodsCode + ") 정보 롤백(삭제) 필요.");
            
            response.sendRedirect(request.getContextPath() + "/emp/addGoods?msg=fileSaveFail");
        }
    }
}