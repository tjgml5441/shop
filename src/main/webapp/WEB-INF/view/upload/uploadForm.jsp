<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>uploadForm.jsp</title>
</head>
<body>
	<!-- 
		method="post" : http body통해 전송 
		enctype="application/x-www-form-urlencoded" : 문자열만을 전송가능
	-->
	<form action="<%=request.getContextPath()%>/uploadAction.jsp" method="post" enctype="multipart/form-data">
		이름: <input type="text" name="name">
		<br>
		파일: <input type="file" name="myFile">
		<br>
		<button type="submit">파일업로드</button>
	</form>
</body>
</html>