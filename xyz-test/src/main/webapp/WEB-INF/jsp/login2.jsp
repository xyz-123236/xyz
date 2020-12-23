<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> --%>
<%@ taglib uri="http://cn.xyz/xyz" prefix="xyz" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="/t1/t1" method="post" enctype="multipart/form-data">
	<input type="file" name="file" value="">
	<xyz:if role="admin" permission="${name}">
		<input type="text" name="name" value="${name}">
	</xyz:if>
	<xyz:if2 role="${name}" permission="update">
		<input type="text" name="name" value="${name}">
	</xyz:if2>
	<input type="submit">
</form>
<input type="button" onclick="test()" value="test">
<input type="button" onclick="test3()" value="test3">
<script type="text/javascript" src="${pageContext.request.contextPath}/statics/js/common/jquery.min-1.11.3.js"></script>
<script type="text/javascript">
	$(function(){
		
	})
	function test3(){
		post("/test/t1/t2",{a:"a"}).done(function(result){
			alert(result.afa);
			//dfd.resolve(result);
		});
	}
	function post(url, data){
		return $.ajax({
			type:'POST',
			url: url,
			data: data,
			dataType:'json',
			success: function(result) {
				alert(result);
			},
			error: function (){
				$.messager.alert('错误', '服务器错误', 'error');
			}
		});
	}
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
				data:{aa:'123',bb:'234'},
				dataType:'json'
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