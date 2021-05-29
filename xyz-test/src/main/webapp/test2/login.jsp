<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- <link rel="Shortcut Icon" href="../statics/img/logo.png" /> -->
<link rel="icon" href="data:;base64,=">
<!-- <link rel="icon" href="data:image/ico;base64,aWNv"> -->
<title>Insert title here</title>
</head>
<body>
<form id="fm" action="/test1/test" method="post" enctype="multipart/form-data">
<!-- <form action="/test1/test" method="post">  -->
	<input type="file" name="file" value="">
	<input type="checkbox" name="xxx" value="1">
	<input type="checkbox" name="xxx" value="2">
	<input type="checkbox" name="xxx" value="3">
	<input type="checkbox" name="xxx" value="4">
	<!-- <input type="submit"> -->
	<input type="button" onclick="test()">
</form>
<!-- <img src="../statics/img/logo.png"> -->
<script type="text/javascript" src="../statics/js/jquery.min.js"></script>
<script>
	function test(){
		var formData = new FormData($('#uploadImg')[0]);
		var formData = new FormData($('#fm')[0]);
        //formData.append('file', file);
        //console.log(formData.get('file'));
        //console.log(formData.file);
        $.ajax({
            url: '/test1/test',
            type: 'POST',
            cache: false,
            data: formData,
            //dataType: 'json',
            //async: false,
            processData: false,
            contentType: false,
            success:function(data){
            	console.log(data);
            }
        });
	}
    $(function(){
       
    })
</script>  
</body>
</html>