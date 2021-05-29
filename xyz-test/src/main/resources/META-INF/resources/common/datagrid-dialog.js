document.write("<script language='javascript' src='"+$('#path').val()+"/js/common/common.js'></script>");
/* load()查询
 * add()新增
 * update()修改
 * copy()复制
 * save()保存
 * remove()删除
 * copy('deep')深复制
 * download()导出文件
 * */
var _params = {
	type:		undefined,//处理类
	key: 		[],//表主键id
	edit: 		[],//存放编辑对象
	find: 		[],//查找路径
	save: 		[],//保存路径
	remove: 	[],//删除路径
	download: 	[],//下载路径
	
	id: 		[],//table的id选择器
	tb: 		[],//table的工具栏
	form: 		[],//table的表单
	//path: 	$('#path').val(),
	relate: 	true//默认多模块关联
}

function EditDatagrid(element){//element:table的id
	this.element = element || _params.master;
	if (typeof EditDatagrid._initialized == 'undefined') {
		EditDatagrid.prototype.add = function(row){
			var _this = this;
			var row = row || {};
			row.type = "add";
			if(_this.element == _params.detaile){
				var _row = $('#' + _params.master).datagrid('getSelected');
				if(_row){
					row[_params.masterId] = _row[_params.masterId];
					editDetaile.open(row, '添加');
				}else{
					$.messager.alert('信息','主模块没有选择记录!','info');
				}
			}else{
				editMaster.open(row, '添加');
			}
		};
		EditDatagrid.prototype.update = function(){
			var _this = this;
			var row = $('#' + _this.element).datagrid('getSelected');
			if(row){
				row.type = "update";
				_this.open(row, '修改');
			}else{
				$.messager.alert('信息','没有选择要修改的记录','info');
			}
		};
		EditDatagrid.prototype.copy = function(deep, callback){
			var _this = this;
			var row = $('#' + _this.element).datagrid('getSelected');
			if(row){
				if(!isEmpty(deep) && deep == 'deep'){
					row.type = "deep";
				}else{
					row.type = "copy";
					delete row[_params[_this.element + 'Id']];
				}
				_this.open(row, '复制', callback);
			}else{
				$.messager.alert('信息','没有选择要复制的记录','info');
			}
		},
		EditDatagrid.prototype.open = function(row, title, callback){
			var _this = this;
			$(_params[_this.element + 'EditDialog']).dialog('open').dialog('center').dialog('setTitle', title);
			$(_params[_this.element + 'EditForm']).form('clear');
			$(_params[_this.element + 'EditForm']).form('load', _this.formatData(row));
			typeof(callback) === 'function' && callback();
		};
		EditDatagrid.prototype.formatData = function(row){
			return row;
		};
		EditDatagrid.prototype.save = function(){
			var _this = this;
			if($(_params[_this.element + 'EditForm']).form('validate')){
				var para = ajaxParamData(_params.type, _params[_this.element + 'Save'], getFormData(_params[_this.element + 'EditForm']));
		    	post(para, function(data){
		   			if(data.result){
		   				$.messager.show({
		   					title: '信息', msg: data.msg, timeout: 3000, showType: 'slide',
		   				});
		   				$(_params[_this.element + 'EditDialog']).dialog('close');
		   				_this.load();
		   			}
		   		});
			}
		};
		
		EditDatagrid.prototype.remove = function(){
			var _this = this;
			var row = $('#' + _this.element).datagrid('getSelected');
			if(row){
				$.messager.confirm('提示', '确定删除吗？', function(r){
					if (r){
						var para = ajaxParamData(_params.type, _params[_this.element + 'Remove'], row);
				    	post(para, function(data){
				   			if(data.result){
				   				$.messager.show({
				   					title: '信息', msg: data.msg, timeout: 3000, showType: 'slide',
				   				});
				   				_this.load();
				   			}
				   		});
					}
				});
			}else{
				$.messager.alert('信息','没有选择要删除的记录!','info');
			}
		};
		
		EditDatagrid.prototype.load = function(){
			var _this = this;
			var row = undefined;
			if(_this.element == _params.detaile){
				var _row = $('#' + _params.master).datagrid('getSelected');
				if(_row){
					row = _row;
				}else{
					$.messager.alert('信息','主模块没有选择记录!','info');
					return;
				}
			}else{
				row = getFormData(_params.masterForm);
			}
			var para = gridParamData(_params.type, _params[_this.element + 'Find'], row);
			GridData('#' + _this.element, para);
		};
		
		EditDatagrid.prototype.download = function(){
			var _this = this;
			var para = ajaxParamData(_params.type, _params.download, getFormData(_params.masterForm));
			post(para, function(data){
				if(data.result){
					window.location=data.msg;
				}
			});
		};
		EditDatagrid.prototype.upload = function(){
			$(_params.uploadDialog).dialog('open').dialog('center').dialog('setTitle', '上传');
			$(_params.uploadForm).form('clear');
			//$(_params['uploadForm']).form('load',row);
		};
		EditDatagrid.prototype.uploadSave = function(type){
			var _this = this;
			var fileName = $(_params.uploadFile).textbox('getValue');
			if(!$(_params.uploadForm).form('validate')){
				$.messager.alert('信息','表单不能为空!','info');
			}else if (!checkFileType(fileName, type)) {
				$.messager.alert('信息','只能上传'+type+'文件!','info');
			}else{
				var formData = getFormData(_params.uploadForm);
		    	var para = ajaxParamData(_params.type, _params.upload, formData);
		    	postFile(_params.uploadForm, para, function(data){
					if(data.result){
						if(data.msg.indexOf('info') == 0){
							$.messager.alert('提示',data.msg.substring(4,data.msg.length),'info');
						}else{
							$.messager.show({
			   					title: '信息', msg: data.msg, timeout: 3000, showType: 'slide',
			   				});
						}
						$(_params.uploadDialog).dialog('close');
						_this.load();
					}else{
						$.messager.alert('信息',data.msg,'info');
					}
		    	});
			}
		};
		EditDatagrid.prototype.syn = function(){
			var para = ajaxParamData(_params.type, _params.syn, getFormData(_params.masterForm));
			post(para, function(data){
				if(data.result){
					$.messager.show({
	   					title: '信息', msg: data.msg, timeout: 3000, showType: 'slide',
	   				});
				}
			});
		};
		EditDatagrid._initialized = true;
	}
}

var one, two, three, four, five;
var names = ['one', 'two', 'three', 'four', 'five'];
function initDg(obj){
	if(obj.num > 5) {
		$.messager.alert('信息','创建的模块太多!','info');
		return;
	}
	for (var i = 0; i < obj.num; i++) {
		_params.edit[i] = new EditDg(i);
		switch (i) {
			case 0:
				one = _params.edit[i]; break;
			case 1:
				two = _params.edit[i]; break;
			case 2:
				three = _params.edit[i]; break;
			case 3:
				four = _params.edit[i]; break;
			case 4:
				five = _params.edit[i]; break;
			default:
				break;
		}
		_params.find[i] = names[i]+'Find';
		_params.remove[i] = names[i]+'Remove';
		_params.save[i] = names[i]+'Save';
		_params.download[i] = names[i]+'Download';
		_params.upload[i] = names[i]+'Upload';
		_params.syn[i] = names[i]+'Syn';
		
		_params.id[i] = '#'+names[i];
		_params.tb[i] = '#'+names[i]+'-tb';
		_params.form[i] = '#'+names[i]+'-form';
		_params.editDialog[i] = '#'+names[i]+'-edit-dialog';
		_params.editForm[i] = '#'+names[i]+'-edit-form';
		_params.uploadDialog[i] = '#'+names[i]+'-upload-dialog';
		_params.uploadForm[i] = '#'+names[i]+'-upload-form';
		_params.uploadFile[i] = '#'+names[i]+'-upload-file';
	}
	$.extend(true, _params, obj);
}
