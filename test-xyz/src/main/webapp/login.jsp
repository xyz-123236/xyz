<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://cn.xyz/xyz" prefix="xyz" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- Meta, title, CSS, favicons, etc. -->
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="icon" href="data:;base64,=">
	<!-- <link rel="Shortcut Icon" href="../statics/img/logo.png" /> -->
	<script type="text/javascript" src="statics/js/jquery.min.js"></script>
	<script type="text/javascript" src="statics/common/common.js"></script>
</head>
<body>
<form action="/test1/test" method="post" enctype="multipart/form-data">
	<input type="file" name="file" value="">
	<xyz:if role="admin">
		test1
	</xyz:if>
	<xyz:if role="xx">
		test2
	</xyz:if>
	<input type="submit">
</form>
<script type="text/javascript">
	$(function(){
		if(isEmpty("")){
			console.log("1");
		}else{
			console.log("2");
		}
	})
</script>
</body>
</html>