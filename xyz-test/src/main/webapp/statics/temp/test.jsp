<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

</head>
<body>
	<form action="">
		<input id="aa" type="text" name="aa" value="">
		<input id="bb" type="text" name="bb" value="">
	</form>
	<div class="button">Button</div>
<script type="text/javascript">
function fireKeyEvent(el, evtType, keyCode) {
	var evtObj;
	if (document.createEvent) {
		if (window.KeyEvent) {//firefox 浏览器下模拟事件
		    evtObj = document.createEvent('KeyEvents');
		    evtObj.initKeyEvent(evtType, true, true, window, true, false, false, false, keyCode, 0);
		} else {//chrome 浏览器下模拟事件
		    evtObj = document.createEvent('UIEvents');//创建事件
		    evtObj.initUIEvent(evtType, true, true, window, 1);//初始化事件
		
		    delete evtObj.keyCode;
		    if (typeof evtObj.keyCode === "undefined") {//为了模拟keycode
		        Object.defineProperty(evtObj, "keyCode", { value: keyCode });                       
		    } else {
		        evtObj.key = String.fromCharCode(keyCode);
		    }
		
		    if (typeof evtObj.ctrlKey === 'undefined') {//为了模拟ctrl键
		        Object.defineProperty(evtObj, "ctrlKey", { value: true });
		    } else {
		        evtObj.ctrlKey = true;
		    }
		}
		el.dispatchEvent(evtObj);//触发事件
	} else if (document.createEventObject) {//IE 浏览器下模拟事件
	    evtObj = document.createEventObject();
	    evtObj.keyCode = keyCode
	    el.fireEvent('on' + evtType, evtObj);
	}
}
$(function(){
	$('#aa').bind('change',function(e) {
		fireKeyEvent(document.getElementById('aa'),'keydown',20);
	});
	//默认事件keycode改变，新增事件keycode无效
	$('#aa').bind('keydown',function(e) {
		if(e.keyCode == 20){
			alert(123);
		}
	});
	
	//创建一个事件并生效
	var btn = document.querySelector('.button');
    document.addEventListener('keyup', function (event) {
        console.log(event.keyCode);
    }, false);
    var ev = new KeyboardEvent('keyup', {
        keyCode: 13
    });
    document.dispatchEvent(ev);
	
    //回车执行tab键功能
	$(document).bind('keydown',function(evt) {
		//document.all可以判断浏览器是否是IE，是页面内所有元素的一个集合
		var isie = (document.all) ? true : false;
		var key;
		var srcobj;
		// if the agent is an IE browser, it's easy to do this.
		if (isie) {
			key = event.keyCode;
			srcobj=event.srcElement;//event.srcElement，触发这个事件的源对象
		}
		else {
			key = evt.which;
			srcobj=evt.target;//target是Firefox下的属性
		}
		if(key==13 && srcobj.type!='button' && srcobj.type!='submit' &&srcobj.type!='reset' && srcobj.type!='textarea' && srcobj.type!='') {
			if(isie)
				event.keyCode=9;//设置按键为tab键
			else {
				var el=getNextElement(evt.target);
				if (el.type!='hidden'){
					//nothing to do here.
				}else{
					while(el.type=='hidden'){
						el=getNextElement(el);
					}
				}
				if(!el){
					return false;
				}else{
					el.focus();
				}
			}
		}
	});
	function getNextElement(field) {
		var form = field.form;
		for(var e = 0; e < form.elements.length; e++) {
			if (field == form.elements[e]) break;
		}
		return form.elements[++e % form.elements.length];
	}
});
function a(){
	var parent = document.getElementById("parent");
    var child = document.getElementById("child");

    document.body.addEventListener("click",function(e){
        console.log("click-body");
    },false);
    
    parent.addEventListener("click",function(e){
        console.log("click-parent---事件传播");
    },false);
 
 //新增事件捕获事件代码
    parent.addEventListener("click",function(e){
        console.log("click-parent--事件捕获");
    },true);

    child.addEventListener("click",function(e){
        console.log("click-child");
    },false);

}
</script>
</body>
</html>