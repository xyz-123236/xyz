obj = {
	isEmpty : function (value){
		/*if(typeof value == "undefined"){//用value == undefined，变量未定义,报错ReferenceError: str is not defined
			return true;
		}*/
		if (value == undefined || value == null) {
			return true;
		}
		var type = Object.prototype.toString.call(value).slice(8, -1);
		if(type == "String"){
			if(value.trim().length == 0){
				return true;
			}
		}
		if(type == "Number"){
			if(value == NaN){
				return true;
			}
		}
		if(type == "Array"){
			if(value.length < 1){
				return true;
			}
		}
		if(type == "Object"){
			if(value == "{}"){
				return true;
			}
		}
		return false;
	},
	isNumber : function(value){
		var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/ 
		if (re.test(value)) {
			return true;
		}
		if(!obj.isEmpty(value)){//isNaN会把""与" "当成0
			if(isNaN(value)){
				return false;
			}
		}
		if(parseFloat(value).toString() == "NaN"){
			return false;
		}
		return false;
	},
	downloadfile: function(){
		var a = document.createElement('a');
		    a.href = url;//连接的路径
		    a.download = url.substring(url.lastIndexOf("/")+1,url.length);//文件的名称
		    a.click();
	}
}

/*$(document).click(function (e) {
	var name = e.target.className;//e.target.id
	if (name.indexOf("calendar") < 0 && name.indexOf("textbox") <0 && name.indexOf("datebox") <0){
		alert(1);
	}
});*/

/*$("#toolbar").click(function(event){
	alert(1);
    event.stopPropagation();
    alert(2);
  });
$(document).click(function (e) {
	alert(3);
});*/