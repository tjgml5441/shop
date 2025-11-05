<%@page import="java.io.InputStream"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.UUID" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.nio.file.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>uploadAction.jsp</title>
</head>
<body>
<%
	String name = request.getParameter("name");
	// 파일을 받는 Part API
	// part : 파일바이너리(stream) + 파일메타정보(이름, 사이즈, ....)
	Part part = request.getPart("myFile");
	
	// 파일저장시 원본이름을 사용하면 덮어쓰기 이슈 - 저장에 사용할 파일이름 생성
	UUID uuid = UUID.randomUUID();
	System.out.println(uuid.toString());
	String filename = uuid.toString();
	filename = filename.replace("-", "");
	System.out.println(filename);
	
	// 업로된 원본이름에서 파일 확장자만 분리
	String originName = part.getSubmittedFileName();
	System.out.println(originName);
	int lastIndex = originName.lastIndexOf(".");
	String dotext = originName.substring(lastIndex);//파일의 확장자
	filename = filename + dotext;
	System.out.println(filename);
	
	// 웹프로젝트안의 폴더의 실제경로(path)를 구하는 api
	String path = request.getServletContext().getRealPath("upload");
	// 빈파일을 생성
	File saveFile = new File(path, filename); // 0byte 빈파일
	//part의 바이너리(스트림) inputstream -> outputstream saveFile 이동
	// 보내는
	InputStream is = part.getInputStream();
	// 받는
	OutputStream os = Files.newOutputStream(saveFile.toPath());
	// is -> os 전송
	is.transferTo(os); // copy
%>
</body>
</html>