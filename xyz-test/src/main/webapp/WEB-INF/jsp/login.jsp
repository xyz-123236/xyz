<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="/t1/t1" method="post" enctype="multipart/form-data">
	<input type="file" name="file" value="">
	<input type="submit">
</form>
<input type="button" onclick="test()" value="test">
<script type="text/javascript" src="${pageContext.request.contextPath}/statics/js/common/jquery.min-1.11.3.js"></script>
<script type="text/javascript">
	$(function(){
		
	})
	function test(){
		var dfd = $.Deferred();
		test2(dfd);
		
		dfd.done(function(val){ alert(val);})
		.fail(function(val){ alert(val);});
		alert("test");
	}
	function test2(dfd){
		for (var i = 0; i < 3; i++) {
			$.ajax({
				url:"/test/t1/t1",
				data:{aa:'123',bb:'234'}
			})
			.done(function(){ alert(i); dfd.resolve(true);})
			.fail(function(){ dfd.reject(false); })
			;
		}
		//dfd.promise();
		//alert("test");
	}
	/* function test(){
		test2().done(function(){ alert("成功");})
		.fail(function(){ alert("失败");});
		alert("test");
	}
	function test2(){
		return $.ajax({
			url:"/test/t1/t1",
			data:{aa:'123',bb:'234'}
		})
		//.done(function(){ return true})
		//.fail(function(){ return false })
		;
		//alert("test");
	} */
</script>
</body>
</html>