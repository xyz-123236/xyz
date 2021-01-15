Array.prototype.indexOf = function(item){
	for(let i = 0; i < this.length; i++){
		if(item === this[i]){
			return i;
		}
	}
	return -1;
}
function isEmpty(value) {
	if (value === undefined || value == null) {
		return true;
	}else{
		value += '';
		if(value.trim().length === 0){
			return true;
		}
	}
	return false;
}
function checkFileType(fileName, type){
	if(isEmpty(type)) return true;
	let obj = {
		txt: 'txt',
		excel: 'xls,xlsx',
		image: 'jpg,gif,png,pdf',
		media: 'mp3,mp4,avi,rm,rmvb',
		file: 'doc,docx,xls,xlsx,ppt,pdf,txt,rar,zip,gz,bz2',	
	};
	let types = type.split(',');
	let ext = fileName.substring(fileName.lastIndexOf(".")+1);
	for (let i = 0; i < types.length; i++) {
		if(obj[types[i]].indexOf(ext.toLowerCase()) >= 0){
			return true;
		}
	}
	return false;
}

function formatNumber0(value){
	if(isEmpty(value)) return 0;
	if(/^-?\d+\.\d+$/.test(value)){
		return value.toString().replace(/(\d)(?=(\d{3})+\.)/g,"$1,");
	}else{
		return value.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
	}
}
//返回2位小数/.xx
function formatNumber2(value){
	return formatNumber(value, 2);
}
//返回2位小数/.xx
function formatNumber3(value){
	return formatNumber(value, 3);
}
//返回4位小数/.xxx
function formatNumber4(value){
	return formatNumber(value, 4);
}
//返回4位小数/.xxx
function formatNumber5(value){
	return formatNumber(value, 8);
}
function formatNumber(value, scale){
	if(isEmpty(value)) value = 0;
	if(isEmpty(scale)) scale = 2;
	//if(/^[-0-9].*$/.test(value)){
	if(isNaN(value)){
		return value;
	}else{
		return (parseFloat(value).toFixed(scale) + '').toString().replace(/(\d)(?=(\d{3})+\.)/g,"$1,");
	}
}
//返回年月/yyyy-MM
function formatDateYM(value){
	if(!isEmpty(value)) return value.substr(0,7);
}
//返回年月/yyyy-MM-dd
function formatDateYMD(value){
	if(!isEmpty(value)) return value.substr(0,10);
}
function formatLevel(value){
	if(!isEmpty(value)){
		if(value === "1") return '初级';
		if(value === "2") return '中级';
		if(value === "3") return '高级';
	}
}
function formatBoolean(value){
	if(!isEmpty(value)){
		if(value === "Y") return '是';
	}else{
		return "";
	}
}
function formatBackgroundColorDateYMD(value){
	if(isEmpty(value)) return "";
	let str = value.replace(/-/g, '/');
	let date = new Date(str);
	if(date < new Date()){
		return '<span style="background-color:#ccc;">'+value.substr(0,10)+'</span>';
	}
	return value.substr(0,10);
}
//优先数字排序
function sortPriorityNumber(a,b){
	if(isEmpty(a)) return -1;
	if(isEmpty(b)) return 1;
	if(isNaN(parseInt(a)) && !isNaN(parseInt(b))) return 1;
	if(!isNaN(parseInt(a)) && isNaN(parseInt(b))) return -1;
	if(isNaN(parseInt(a)) && isNaN(parseInt(b))) {
		let a_s = '', a_u;
		for (let i = 0; i < a.length; i++) {
			if(isNaN(a.substring(i, i+1))){
				a_s += a.substring(i, i+1);
				a_u = a.substring(i+1, a.length);
			}else{
				break;
			}
		}
		let b_s = '', b_u;
		for (let i = 0; i < b.length; i++) {
			if(isNaN(b.substring(i, i+1))){
				b_s += b.substring(i, i+1);
				b_u = b.substring(i+1, b.length);
			}else{
				break;
			}
		}
		if(a_s === b_s){
			return sortPriorityNumber(a_u, b_u);
		}else{
			return a_s > b_s ? 1 : -1;
		}
	}
	if(!isNaN(parseInt(a)) && !isNaN(parseInt(b))) {
		let a_s = '', a_u;
		for (let i = 0; i < a.length; i++) {
			if(!isNaN(a.substring(i, i+1))){
				a_s += a.substring(i, i+1);
				a_u = a.substring(i+1, a.length);
			}else{
				break;
			}
		}
		let b_s = '', b_u;
		for (let i = 0; i < b.length; i++) {
			if(!isNaN(b.substring(i, i+1))){
				b_s += b.substring(i, i+1);
				b_u = b.substring(i+1, b.length);
			}else{
				break;
			}
		}
		if(a_s === b_s){
			return sortPriorityNumber(a_u, b_u);
		}else{
			return parseInt(a_s) > parseInt(b_s) ? 1 : -1;
		}
	}
}
/*let _input = {
	width: 360, height: 30, labelAlign: 'right', labelWidth: 90
}

function initInput(){
	let textboxs = $('.mes-textbox');
	textboxs.each(function(index,element){
	 	$(element).textbox(_input);
	});
	let numberboxs = $('.mes-numberbox');
	numberboxs.each(function(index,element){
	 	$(element).numberbox(_input);
	});
	let fileboxs = $('.mes-filebox');
	fileboxs.each(function(index,element){
	 	$(element).filebox(_input);
	});
	let dateboxs = $('.mes-datebox');
	dateboxs.each(function(index,element){
	 	$(element).datebox(_input);
	});
	let dateboxYms = $('.mes-datebox-ym');
	dateboxYms.each(function(index,element){
	 	$(element).datebox({
	 		width: _input.width, height: _input.height, labelAlign: _input.labelAlign, labelWidth: _input.labelWidth,
	 		onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
				let _this = this;
				let p = $(_this).datebox('panel'); //日期选择对象
                let tds = false; //日期选择对象中月份
                let yearIpt = p.find('input.calendar-menu-year');//年份输入框
                let span = p.find('span.calendar-text'); //显示月份层的触发控件
                span.trigger('click'); //触发click事件弹出月份层
                if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                    tds = p.find('div.calendar-menu-month-inner td');
                    tds.click(function (e) {
                        e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
                        let year = /\d{4}/.exec(span.html())[0]//得到年份
                        , month = parseInt($(this).attr('abbr'), 10); //月份，这里不需要+1
                        $(_this).datebox('hidePanel')//隐藏日期对象
                        .datebox('setValue', year + '-' + month); //设置日期的值
                    });
                }, 0);
                yearIpt.unbind();//解绑年份输入框中任何事件
            },
            parser: function (s) {
                if (!s) return new Date();
                let arr = s.split('-');
                return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
            },
            formatter: function (d) {
                let a = parseInt(d.getMonth())<parseInt('9')?'0'+parseInt(d.getMonth()+ 1):d.getMonth() + 1;
                return d.getFullYear() + '-' +a;
            }
	 	});
	});
}
*/

function stylerBackgroundByDate(index, row){
	if(!isEmpty(row.enddate)){
		if(new Date(row.enddate.replace(/-/g, '/')) < new Date()){
			return 'background-color:#eee;'
		}
	}
}
//日期控件只显示年月
let date_ym_options = {
	onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
		let _this = this;
		let p = $(_this).datebox('panel'); //日期选择对象
        let tds = false; //日期选择对象中月份
        let yearIpt = p.find('input.calendar-menu-year');//年份输入框
        let span = p.find('span.calendar-text'); //显示月份层的触发控件
        span.trigger('click'); //触发click事件弹出月份层
        if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
            tds = p.find('div.calendar-menu-month-inner td');
            tds.click(function (e) {
                e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
                let year = /\d{4}/.exec(span.html())[0]//得到年份
                , month = parseInt($(this).attr('abbr'), 10); //月份，这里不需要+1
                $(_this).datebox('hidePanel')//隐藏日期对象
                .datebox('setValue', year + '-' + month); //设置日期的值
            });
        }, 0);
        yearIpt.unbind();//解绑年份输入框中任何事件
    },
    parser: function (s) {
        if (!s) return new Date();
        let arr = s.split('-');
        return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
    },
    formatter: function (d) {
        let a = parseInt(d.getMonth())<parseInt('9')?'0'+parseInt(d.getMonth()+ 1):d.getMonth() + 1;
        return d.getFullYear() + '-' +a;
    }	
}
$.extend($.fn.validatebox.defaults.rules, {//options:{validType: 'workcenterNo',}
	workcenterNo: {  
		validator: function (value) {  
			return /^12[A-Z]([0-9]{2}|[0-9]{4})$/.test(value);  
		},  
		message: '工作中心只能是5位或7位'  
	},
	workcenterNoSeven: {  
		validator: function (value) {  
			return /^12[A-Z][0-9]{4}$/.test(value);  
		},  
		message: '工作中心只能是7位'  
	},
	workorderNo: {  
		validator: function (value) {  
			return /^[0-9]{10}$/.test(value);  
		},  
		message: '工单为10位数字'  
	},
	userno: {  
		validator: function (value) {  
			return /^[0-9]{7}$/.test(value);  
		},  
		message: '工号为7位数字'  
	},
	minute: {  
		validator: function (value) {  
			return value >= 0 && value <= 60;  
		},  
		message: '只能是0到60'  
	},
	gtZero: {  
		validator: function (value) {  
			return value > 0;  
		},  
		message: '值必须大于0'  
	},
	lgZero: {  
		validator: function (value) {  
			return value < 0;  
		},  
		message: '值必须小于0'  
	},
	num: {
		validator: function (value) {
			return /^-?([1-9][0-9]*|0)([\.][0-9]+)?$/.test(value) && value > 0;
		},
		message: '数字不合法'
	},
});
//清除表单数据
function clearForm(elements){
	elements.forEach(function (element) {
		$("#"+element).textbox('setValue','');
	});
}
//post("/test/t1/t2",{a:"a"}).done(function(result){alert(result.afa);});
function post(url, data){
	return $.ajax({
		type:'POST',
		url: url,
		data: data,
		dataType:'json',
		success: function(result) {
			if(!result.status){
				$.messager.alert('错误', result.msg, 'error');
			}
		},
		error: function (){
			$.messager.alert('错误', '服务器错误', 'error');
		}
	});
}
