<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<div style="height:200px;width:300px;background-color:red;margin:20px"></div>
<div style="height:200px;width:300px;background-color:red;margin:20px"></div>
<div style="height:200px;width:300px;background-color:red;margin:20px"></div>
<div style="height:200px;width:300px;background-color:red;margin:20px"></div>
<div id="greenid" style="height:200px;width:300px;background-color:green;margin:20px" tabindex="4"></div>
<div style="height:200px;width:300px;background-color:red;margin:20px"></div>
<div style="height:200px;width:300px;background-color:red;margin:20px"></div>
<div style="height:200px;width:300px;background-color:red;margin:20px"></div>
<div style="height:200px;width:300px;background-color:red;margin:20px"></div>
<a href="http://www.runoob.com//" tabindex="2"> runoob.com 菜鸟教程</a><br />
<a id="focusid" href="http://www.google.com/" tabindex="1">Google</a><br />
<a href="http://www.microsoft.com/" tabindex="3">Microsoft</a>
<script>
document.querySelector('#greenid').focus();
//document.querySelector('#focusid').focus();
//document.querySelector('.icon-cool').scrollIntoView();
//原理：利用tabindex和focus，因为focus只能聚焦在input，button，a之类可聚焦的标签上，所以要给div加上tabindex使其可以被focus。
//注意：如果元素已经被focus了那么再次focus不会触发滚动条滚动至此元素，记得在再次触发滚动到此focus元素之前先blur即可（demo是两个元素来回focus所以不存在这个问题）。
/* function 摇色子(){
    return new Promise((resolve, reject)=>{
        let sino = parseInt(Math.random() * 6 +1)
        setTimeout(()=>{
            resolve(sino)
        },3000)
    })
}
async function test(){
    let n =await 摇色子()
    console.log(n)
}
test(); */

</script>
</body>

</html>