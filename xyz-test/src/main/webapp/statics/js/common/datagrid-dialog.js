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
	id: 		[],//表主键id
	edit: 		[],//存放编辑对象
	find: 		[],//查找路径
	save: 		[],//保存路径
	remove: 	[],//删除路径
	download: 	[],//下载路径
	upload: 	[],//
	syn: 		[],
	
	element: 	[],//table的id选择器
	tb: 		[],//table的工具栏
	form: 		[],//table的表单
	editDialog: 	[],
	editForm: 		[],
	uploadDialog: 	[],
	uploadForm: 	[],
	uploadFile: 	[],
	//path: 	$('#path').val(),
	relate: 	true//默认多模块关联
}

function EditDg(index){//element:table的id
	this.index = index;
	if (typeof EditDg._initialized == 'undefined') {
		EditDg.prototype.init = function(tbs, eds){
			if(tbs && tbs.length > 0){
				for (var i = 0; i < tbs.length; i++) {
					var tb = tbs[i];
					if(!$.isEmptyObject(tb)){//if(JSON.stringify(c) == "{}")
						if(tb.tool && tb.tool.length > 0){
							tb.tool.forEach(function(row){
								$a = '<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:\'icon-'+row.icon+'\'" onclick="'+names[i]+'.'+row.id+'()">'+row.text+'</a>';
							});
						}
						if(tb.from && tb.from.length > 0){
							tb.from.forEach(function(row){
								$input = '<input class="easyui-textbox" name="'+row.name+'" id="'+row.name+'" data-options="label:\''+row.label+'：\',width:160,labelWidth:60,labelAlign:\'right\'"/>';
							});
						}
					}
				}
			}
			if(eds && eds.length > 0){
				for (var i = 0; i < eds.length; i++) {
					var ed = eds[i];
					if(ed && ed.length > 0){
						ed.forEach(function(row){
							//
						});
					}
				}
			}
		};
		initjsp([{
			tool:[
				{icon:"add",id:"add",text:"添加"},
				{icon:"add",id:"add",text:"添加"}
			],
			from:[
				{type:"textbox",name:"user",option:{label:"添加",required:"true",validType: 'length[10,10]'}},
				{type:"combobox",name:"name",option:{label:"添加"}}
			]
		},{
			tool:[
				{icon:"add",id:"add",text:"添加"},
				{icon:"add",id:"add",text:"添加"}
			],
			from:[
				{type:"textbox",name:"add",text:"添加",required:"true",validType: 'length[10,10]'},
				{type:"combobox",name:"add",text:"添加"}
			]
		}]);
		initedit([[
			{type:"textbox",name:"add",text:"添加",required:"true",validType: 'length[10,10]'},
			{type:"combobox",name:"add",text:"添加"}
		],[
			{type:"add",id:"add",text:"添加"},
			{type:"add",id:"add",text:"添加"}
		]]);
		EditDg.prototype.add = function(row){
			var _this = this;
			var row = row || {};
			row.type = "add";
			if(_this.index != 0){
				var _row = $(_params.element[_this.index - 1]).datagrid('getSelected');
				if(_row){
					row[_params.id[_this.index - 1]] = _row[_params.id[_this.index - 1]];
					_this.open(row, '添加');
				}else{
					$.messager.alert('信息','主模块没有选择记录!','info');
				}
			}else{
				_this.open(row, '添加');
			}
		};
		EditDg.prototype.editBefore = function(row){
			return true;
		};
		EditDg.prototype.edit = function(){
			var _this = this;
			var row = $(_params.element[_this.index]).datagrid('getSelected');
			if(row){
				if(_this.editBefore(row)){
					if(!($(_params.element[_this.index]).datagrid('options').singleSelect)){//多选
						row = {};
					}
					row.type = "update";
					_this.open(row, '修改');
				}
			}else{
				$.messager.alert('信息','没有选择要修改的记录','info');
			}
		};
		EditDg.prototype.copy = function(deep, callback){
			var _this = this;
			var row = $(_params.element[_this.index]).datagrid('getSelected');
			if(row){
				if(!isEmpty(deep) && deep == 'deep'){
					row.type = "deep";
				}else{
					row.type = "copy";
					delete row[_params.id[_this.index]];
				}
				_this.open(row, '复制', callback);
			}else{
				$.messager.alert('信息','没有选择要复制的记录','info');
			}
		},
		EditDg.prototype.open = function(row, title, callback){
			var _this = this;
			$(_params.editDialog[_this.index]).dialog('open').dialog('center').dialog('setTitle', title);
			$(_params.editForm[_this.index]).form('clear');
			$(_params.editForm[_this.index]).form('load', _this.formatData(row));
			typeof(callback) === 'function' && callback();
		};
		EditDg.prototype.formatData = function(row){
			return row;
		};
		EditDg.prototype.save = function(){
			var _this = this;
			if($(_params.editForm[_this.index]).form('validate')){
				var data = getFormData(_params.editForm[_this.index]);
				if(!($(_params.element[_this.index]).datagrid('options').singleSelect)){//多选
					data.rows = $(_params.element[_this.index]).datagrid('getSelections');
				}
				var para = ajaxParamData(_params.type, _params.save[_this.index], data);
		    	post(para, function(data){
		   			if(data.result){
		   				$.messager.show({
		   					title: '信息', msg: data.msg, timeout: 3000, showType: 'slide',
		   				});
		   				$(_params.editDialog[_this.index]).dialog('close');
		   				_this.reload();
		   			}
		   		});
			}
		};
		
		EditDg.prototype.remove = function(){
			var _this = this;
			var row = $(_params.element[_this.index]).datagrid('getSelected');
			if(row){
				$.messager.confirm('提示', '删除后无法恢复，确定删除吗？', function(r){
					if (r){
						$.messager.confirm('提示', '请再次确定是否删除？', function(r){
							if (r){
								var para = ajaxParamData(_params.type, _params.remove[_this.index], row);
						    	post(para, function(data){
						   			if(data.result){
						   				$.messager.show({
						   					title: '信息', msg: data.msg, timeout: 3000, showType: 'slide',
						   				});
						   				_this.reload();
						   			}
						   		});
							}
						});
					}
				});
			}else{
				$.messager.alert('信息','没有选择要删除的记录!','info');
			}
		};
		
		EditDg.prototype.reload = function(){
			var _this = this;
			var row = undefined;
			if(_this.index != 0){
				var _row = $(_params.element[_this.index - 1]).datagrid('getSelected');
				if(_row){
					row = _row;
				}else{
					$.messager.alert('信息','主模块没有选择记录!','info');
					return;
				}
			}else{
				row = getFormData(_params.form[_this.index]);
			}
			var para = gridParamData(_params.type, _params.find[_this.index], row);
			GridData(_params.element[_this.index], para);
		};
		
		EditDg.prototype.download = function(){
			var _this = this;
			var para = ajaxParamData(_params.type, _params.download[_this.index], getFormData(_params.form[_this.index]));
			post(para, function(data){
				if(data.result){
					window.location=data.msg;
				}
			});
		};
		EditDg.prototype.upload = function(){
			$(_params.uploadDialog).dialog('open').dialog('center').dialog('setTitle', '上传');
			$(_params.uploadForm).form('clear');
			//$(_params['uploadForm']).form('load',row);
		};
		EditDg.prototype.uploadSave = function(type){
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
		EditDg.prototype.syn = function(){
			var para = ajaxParamData(_params.type, _params.syn[_this.index], getFormData(_params.form[_this.index]));
			post(para, function(data){
				if(data.result){
					$.messager.show({
	   					title: '信息', msg: data.msg, timeout: 3000, showType: 'slide',
	   				});
				}
			});
		};
		EditDg._initialized = true;
	}
}

var _one, _two, _three, _four, _five;
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
				_one = _params.edit[i]; break;
			case 1:
				_two = _params.edit[i]; break;
			case 2:
				_three = _params.edit[i]; break;
			case 3:
				_four = _params.edit[i]; break;
			case 4:
				_five = _params.edit[i]; break;
			default:
				break;
		}
		_params.find[i] = names[i]+'Find';
		_params.remove[i] = names[i]+'Remove';
		_params.save[i] = names[i]+'Save';
		_params.download[i] = names[i]+'Download';
		_params.upload[i] = names[i]+'Upload';
		_params.syn[i] = names[i]+'Syn';
		
		_params.element[i] = '#'+names[i];
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
