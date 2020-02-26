document.write("<script language='javascript' src='"+$('#path').val()+"/statics/js/common/common.js'></script>");
//import './common.js';
var _params = {
	num:		1,//模块数量
	
	type:		undefined,//处理类
	id: 		[],//表主键id
	edit: 		[],//存放编辑对象
	find: 		[],//查找路径
	save: 		[],//保存路径
	remove: 	[],//删除路径
	download: 	[],//下载路径
	
	element: 	[],//table的id选择器
	tb: 		[],//table的工具栏
	form: 		[],//table的表单
	
	//path: 	$('#path').val(),
	relate: 	true//默认多模块关联
}


function EditDg(index){		//element:table的id
	this.index = index;				//第几模块
	this.editIndex = undefined;		//编辑的行号
	this.originalRow = undefined;	//原行数据
	this.isEdit = true;				//禁用编辑，默认可以编辑，调用disableEditing()取消编辑
	if (typeof EditDg._initialized == 'undefined') {
		EditDg.prototype.checkData = function(row){//编辑后行数据验证
			return true;
		};
		EditDg.prototype.addBefore = function(row, type){//添加前处理初始row
			return row;
		};
		EditDg.prototype.addEdit = function(row){//删除前处理初始row

		};
		EditDg.prototype.removeBefore = function(_this, callback, row){//删除前处理初始row
			if (typeof(callback) === 'function'){
				callback(_this, row);
			}
		};
		EditDg.prototype.editAfter = function(){
			
		};
		EditDg.prototype.add = function(row){//新增   row:{index: '', init:{}}
			var _this = this;
			if(!_this.checkSave()) return;
			var obj = row ? (row.init || {}) : {};
			var index = row ? row.index : undefined;
			obj = _this.addBefore(obj,'add')
			if(_this.index > 0 && _params.relate){//relate默认是关联模块
				_this.task(_this, function(this2){//明细添加时需要主模块生成主键
					_row = $(_params.element[this2.index - 1]).datagrid('getSelected');
					if(_row){
						if(!isEmpty(_row[_params.id[this2.index - 1]])){
							obj[_params.id[this2.index - 1]] = _row[_params.id[this2.index - 1]];
							this2._add(index, obj);
							return true;
						}else{
							return false;
						}
					}else{
						$.messager.alert('信息','主模块没有选择记录!','info');
						return true;
					}
				});
			}else{
				_this._add(index, obj);
			}
		};
		EditDg.prototype._add = function(index, row){//添加的方法
			var _this = this;
			row.newRow = true;
			var index = index || $(_params.element[_this.index]).datagrid('getRows').length;
			_this.editIndex = index;
			_this.originalRow = $.extend(true, {}, row);
			$(_params.element[_this.index]).datagrid('insertRow',{index: _this.editIndex,'row': row});
			_this.beginEdit();
		};
		EditDg.prototype.copy = function(row){//复制:  row:{init:{},remove:[],retain:[]}  remove与retain不能同时用
			var _this = this;
			if(!_this.checkSave()) return;
			var _row = $(_params.element[_this.index]).datagrid('getSelected');
			if(_row){
				_this.addEdit(_row);
				var index = $(_params.element[_this.index]).datagrid('getRowIndex',_row);
				var obj = $.extend({},_row);
				var eds = $(_params.element[_this.index]).datagrid('options');
				obj = _this.handleCopyData(eds.columns[0], obj, row);
				obj = _this.handleCopyData(eds.frozenColumns[0], obj, row);
				delete obj[_params.id[_this.index]];
				if(row && row.init){
					obj = $.extend(true, obj, row.init);
				}
				_this._add(index + 1, obj);
			}else{
				$.messager.alert('信息','没有选择要复制的记录','info');
			}
		};
		EditDg.prototype.handleCopyData = function(columns, obj, row){
			if(columns){
				columns.forEach(function(element){
					if(element.editor != undefined){
						if(row){
							if(row.remove){
								if(row.remove.indexOf(element.field) >= 0) delete obj[element.field];
							}else if(row.retain){
								if(row.retain.indexOf(element.field) < 0) delete obj[element.field];
							}else{
								delete obj[element.field];
							}
						}else{
							delete obj[element.field];
						}
					}
				});
			}
			return obj;
		};
		EditDg.prototype.clickRow = function(index, field, edit){//edit强制编辑//点击单元格             需要回调？
			var _this = this;
			if(_this.isEdit && field){
				if(!_this.checkSave()) return false;
				var eds = $(_params.element[_this.index]).datagrid('options');
				for (var i = 0; i < eds.columns[0].length; i++) {//不用forEach,因为return会无效
					if(eds.columns[0][i].field == field && eds.columns[0][i].editor != undefined){
						_this.editIndex = index;
						_this.beginEdit(field);
						return;
					}
				}
				if(eds.frozenColumns[0]){
					for (var i = 0; i < eds.frozenColumns[0].length; i++) {
						if(eds.frozenColumns[0][i].field == field && eds.frozenColumns[0][i].editor != undefined){
							_this.editIndex = index;
							_this.beginEdit(field);
							return;
						}
					}
				}
			}else if(_this.isEdit && edit){
				_this.editIndex = index;
				_this.beginEdit();
			}
			if(_params.num > _this.index + 1 && _params.relate){
				$(_params.element[_this.index]).datagrid('selectRow', index);
				_params.edit[_this.index + 1].reload();
			}
		};
		EditDg.prototype.beginEdit = function(field){//开始编辑行，并聚焦
			var _this = this;
			$(_params.element[_this.index]).datagrid('selectRow', _this.editIndex).datagrid('beginEdit', _this.editIndex);
			if(_params.num > _this.index + 1 && _params.relate){
				_params.edit[_this.index + 1].reload();
			}
			var rows = $(_params.element[_this.index]).datagrid('getRows');
			var row = rows[_this.editIndex];
			_this.originalRow = $.extend(true, {}, row);
			var t;
			var editor = $(_params.element[_this.index]).datagrid('getEditor', {index: _this.editIndex, field: field});
			if (editor){
				t = editor.target;
			} else {
				var editors = $(_params.element[_this.index]).datagrid('getEditors', _this.editIndex);
				if (editors.length){
					t = editors[0].target;
				}
			}
			if (t){
				if ($(t).hasClass('textbox-f')){
					$(t).textbox('textbox').focus();
				} else {
					$(t).focus();
				}
			}
		};
		EditDg.prototype.save = function(index, row){//保存
			var _this = this;
			if(row){
				var para = ajaxParamData(_params.type, _params.save[_this.index], row);
				post(para, function(data){
						if(data.result){
							if(data.rows[0]){
								$(_params.element[_this.index]).datagrid('updateRow',{index: index,row: data.rows[0]});
							}
							if(data.msg.indexOf('info') == 0){
								$.messager.alert('提示',data.msg.substring(4,data.msg.length),'info');
							}
						}
					}, function(data){
						if(_this.editIndex != index && _this.editIndex != undefined){
							if(_this.originalRow.newRow){
								$(_params.element[_this.index]).datagrid('cancelEdit', _this.editIndex).datagrid('deleteRow', _this.editIndex);
							}else{
								$(_params.element[_this.index]).datagrid('endEdit', _this.editIndex);
							}
						}
						_this.clickRow(index, null, true);
						if(data.msg.indexOf('info') == 0){
							$.messager.alert('提示',data.msg.substring(4,data.msg.length),'info');
						}else{
							$.messager.alert('错误',data.msg,'error',function(){
								if(_this.editIndex != index) {
									if(_this.editIndex != undefined){
										if(_this.originalRow.newRow){
											$(_params.element[_this.index]).datagrid('cancelEdit', _this.editIndex).datagrid('deleteRow', _this.editIndex);
										}else{
											$(_params.element[_this.index]).datagrid('endEdit', _this.editIndex);
										}
									}
									_this.clickRow(index, null, true);
								}
							});
						}
						/*_this.task(_this, function(this2,row_){//明细添加时需要主模块生成主键
							if(this2.editIndex != row_.index || this2.editIndex == undefined){
								this2.beginEdit();
							}
						},100,{index: index});*/
					}
				);
			}else{
				_this.endEditing();
			}
		};
		EditDg.prototype.undo = function(){//取消编辑
			var _this = this;
			if(_this.editIndex != undefined){
				var row = $(_params.element[_this.index]).datagrid('getSelected');
				if (isEmpty(row[_params.id[_this.index]])){
					$(_params.element[_this.index]).datagrid('cancelEdit', _this.editIndex).datagrid('deleteRow', _this.editIndex);
					_this.editIndex = undefined;
					$(_params.element[_this.index]).datagrid('selectRow', 0);
				}else{
					$(_params.element[_this.index]).datagrid('cancelEdit', _this.editIndex);
					_this.editIndex = undefined;
				}
			}
		};
		EditDg.prototype.remove = function(){//删除行
			var _this = this;
			var row = $(_params.element[_this.index]).datagrid('getSelected');
			if (_this.editIndex != undefined && isEmpty(row[_params.id[_this.index]])){
				$(_params.element[_this.index]).datagrid('cancelEdit', _this.editIndex).datagrid('deleteRow', _this.editIndex);
				_this.editIndex = undefined;
				$(_params.element[_this.index]).datagrid('selectRow', 0);
			}if (_this.editIndex == undefined && isEmpty(row[_params.id[_this.index]])){
				$(_params.element[_this.index]).datagrid('deleteRow', $(_params.element[_this.index]).datagrid('getRowIndex', row));
				_this.editIndex = undefined;
				$(_params.element[_this.index]).datagrid('selectRow', 0);
			}else if(row){
				/*$.messager.confirm('提示', '确定删除吗？', function(r){
					if (r){
						_this.removeBefore(_this, function(this2, _row){
							var para = ajaxParamData(_params.type, _params.remove[this2.index], _row);
					    	post(para, function(data){
					   			if(data.result){
					   				$.messager.show({
					   					title: '信息', msg: data.msg, timeout: 3000, showType: 'slide',
					   				});
					   				$(_params.element[this2.index]).datagrid('deleteRow', $(_params.element[this2.index]).datagrid('getRowIndex', _row));
					   				$(_params.element[this2.index]).datagrid('selectRow', 0);
					   			}
					   		});
						}, row);
					}
				});*/
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
						   				$(_params.element[_this.index]).datagrid('deleteRow', $(_params.element[_this.index]).datagrid('getRowIndex', row));
						   				$(_params.element[_this.index]).datagrid('selectRow', 0);
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
		
		EditDg.prototype.endEditing = function(){//结束编辑
			var _this = this;
			if (_this.editIndex == undefined){
				return true
			}
			if ($(_params.element[_this.index]).datagrid('validateRow', _this.editIndex)){
				$(_params.element[_this.index]).datagrid('acceptChanges');
				var rows = $(_params.element[_this.index]).datagrid('getRows');
				var row = rows[_this.editIndex];
				if(_this.checkData(row)){
					
					$(_params.element[_this.index]).datagrid('endEdit', _this.editIndex);
					_this.editAfter();
					if(!row){
						
					}else if(row.newRow){
						delete row.newRow;
						_this.save(_this.editIndex, row);
					}else{
						var fields = $(_params.element[_this.index]).datagrid('getColumnFields',true).concat($(_params.element[_this.index]).datagrid('getColumnFields'));
						for(var i=0; i<fields.length; i++){
							var field = fields[i];
							var col = $(_params.element[_this.index]).datagrid('getColumnOption', field);
							if (col.editor && _this.originalRow[field] != row[field] && (!isEmpty(_this.originalRow[field]) || !isEmpty(row[field]))){
								_this.save(_this.editIndex, row);
								break;
							}
						}
					}
					_this.editIndex = undefined;
					return true;
				}else{
					_this.addEdit(row);
					_this.beginEdit();
					return false;
				}
			} else {
				//_this.beginEdit();
				return false;
			}
		};
		EditDg.prototype.reload = function(){//刷新数据
			var _this = this;
			var row = undefined;
			if(_this.index > 0 && _params.relate){
				_row = $(_params.element[_this.index - 1]).datagrid('getSelected');
				if(_row){
					row = _row;
				}else{
					$.messager.alert('信息','主模块没有选择记录!','info');
					return;
				}
			}else{
				if($(_params.form[_this.index]).form('validate')){
					row = getFormData(_params.form[_this.index]);
				}else{
					return;
				}
			}
			var length = _params.edit.length;
			if(length > 0){
				for (var i = 0; i < length; i++) {
					if(i > _this.index && _params.relate){
						$(_params.element[i]).datagrid("loadData", { total: 0, rows: [], result: true });
					}
				}
			}
			var para = gridParamData(_params.type, _params.find[_this.index], row);
			GridData(_params.element[_this.index], para);
			_this.editIndex = undefined;		//编辑的行号
			_this.originalRow = undefined;	//原行数据
		};
		EditDg.prototype.download = function(){//下载         如果只用明细模块导出，需要修改参数getFormData
			var _this = this;
			var para = ajaxParamData(_params.type, _params.download[_this.index], getFormData(_params.form[_this.index]));
			post(para, function(data){
				if(data.result){
					window.location=data.msg;
				}
			});
		};
		EditDg.prototype.disableEditing = function(row){//禁用:  row:{remove:[],retain:[]}  remove与retain不能同时用
			var _this = this;
			var eds = $(_params.element[_this.index]).datagrid('options');
			_this.handleDisableEditing(eds.columns[0], row);
			_this.handleDisableEditing(eds.frozenColumns[0], row);
			if(!row) _this.isEdit = false;
		};
		EditDg.prototype.handleDisableEditing = function(columns, row){
			if(columns){
				columns.forEach(function(element){
					if(element.editor != undefined){
						if(row){
							if(row.remove){
								if(row.remove.indexOf(element.field) >= 0) element.editor = null;
							}else if(row.retain){
								if(row.retain.indexOf(element.field) < 0) element.editor = null;
							}else{
								element.editor = null;
							}
						}else{
							element.editor = null;
						}
					}
				});
			}
		};
		EditDg.prototype.checkSave = function(){//检查其他模块保存没，自动保存，保存不了返回false
			var _this = this;
			for (var i = 0; i < _params.num; i++) {
				var edit = _params.edit[i];
				if(edit.editIndex != undefined){
					if(!edit.endEditing()){
						return false;
					}
				}
				if(i > _this.index && _params.relate){
					$(_params.element[i]).datagrid("loadData", { total: 0, rows: [], result: true });
				}
			}
			return true;
		};
		EditDg.prototype.task = function(_this, callback, time, row){//定时任务
			var i = 0;
			time = time || 100;
			var task_id = setInterval(function () {
				i++;
				if(i > 50){
					clearInterval(task_id);
					$.messager.alert('信息','请求超时!','info');
				}
				if (!typeof(callback) === 'function' || callback(_this, row)){
					clearInterval(task_id);
				}
			}, time);
		};
		EditDg._initialized = true;
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
		_params.element[i] = '#'+names[i];
		_params.tb[i] = '#'+names[i]+'-tb';
		_params.form[i] = '#'+names[i]+'-form';
	}
	$.extend(true, _params, obj);
}
function initDatagrid(no, opt){
	$(_params.element[no]).datagrid($.extend(true,{
		toolbar:_params.tb[no], pageSize:50, pageList:[10,20,50,100,200],
		pagination : true,fit : true, striped:true, nowrap:true, remoteSort: true, singleSelect:true,rownumbers:true,
		onClickCell: function(index,field,value){
			_params.edit[no].clickRow(index, field);
		}
	}, opt));
}