<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>    
<!DOCTYPE html>
<html>
<head>
<s:include value="../../head.jsp"></s:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
</head>
<body>
	<%-- <s:include value="../../include/head.jsp"></s:include>
	<div style="margin: 0; padding: 5px 5px;">
		<input class="easyui-textbox" name="userno" id="userno" data-options="label:'工号:',width:150,labelWidth:50,labelAlign:'right'"/>
		<input class="easyui-textbox" name="displayname" id="displayname" data-options="label:'姓名:',width:150,labelWidth:50,labelAlign:'right'"/>
		<input class="easyui-textbox" name="departmentno" id="departmentno" data-options="label:'部门:',width:150,labelWidth:50,labelAlign:'right'"/>
		<input class="easyui-textbox" name="workcenterNo" id="workcenterNo" data-options="label:'工作中心:',width:180,labelWidth:80,labelAlign:'right'"/>
	</div>
	<s:include value="../../include/foot.jsp"></s:include>
	</form></div></div></div></div> --%>
	
	<s:include value="../../include/loading.jsp"></s:include>
	<div class="easyui-layout" data-options="fit:true">
	    <div data-options="region:'center',width:'50%',split:true">
			<table id="master"></table>
		    <div id="master-tb">
			    <div style="width: 800px;">
		    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="editMaster.load()">刷新</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="editMaster.add()">新增</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editMaster.update()">修改</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="editMaster.remove()">删除</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="editMaster.upload()">上传</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="editMaster.download()">下载</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="editMaster.syn()">同步</a>
		    		<input class="easyui-checkbox" labelAlign="left" labelPosition="after" label="有证书" data-options="labelWidth:50,checked:true" style="width: 16px;height: 16px;" name="havework9" id="havework9" value=""/>
			    </div>
			    <div>
					<form id="master-form" method="post" style="margin: 0; padding: 5px 5px;overflow:auto;">
						<div style="width: 800px;">
							<input class="easyui-textbox" name="userno" id="userno" data-options="label:'工号:',width:150,labelWidth:50,labelAlign:'right'"/>
							<input class="easyui-textbox" name="displayname" id="displayname" data-options="label:'姓名:',width:150,labelWidth:50,labelAlign:'right'"/>
							<input class="easyui-textbox" name="departmentno" id="departmentno" data-options="label:'部门:',width:150,labelWidth:50,labelAlign:'right'"/>
							<input class="easyui-textbox" name="workcenterNo" id="workcenterNo" data-options="label:'工作中心:',width:180,labelWidth:80,labelAlign:'right'"/>
							<select class="easyui-combobox" name="off" id="off" style="width: 150px" labelAlign="right" label="离职:" data-options="labelWidth:50">
								<option value="">全部</option>
								<option selected="selected" value="N">正常</option>
								<option value="Y">离职</option>
							</select>
						</div>
						<input type="hidden" name="havework" id="havework" value="1">
					</form>
				</div>
			</div>
		</div>
		<div data-options="region:'east',width:'50%',split:true">
   			<table id="detaile"></table>
   			<div id="detaile-tb">
			    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="editDetaile.load()">刷新</a>
	    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="editDetaile.add()">新增</a>
	    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editDetaile.update()">修改</a>
	    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="editDetaile.remove()">删除</a>
	    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-large-shapes" plain="true" onclick="editDetaile.copy()">复制</a>
			</div>
	    </div>
    </div>
	<div id="master-edit-dialog" class="easyui-dialog" data-options="closed:true,buttons:'#master-edit-buttons'" style="width: 450px; height: 350px;">
		<form id="master-edit-form" method="post" enctype="multipart/form-data" style="margin: 0; padding: 5px 5px">
			<div style="margin: 15px;">
				<input name="workid" id="workid2" label="工号:" value="" required="true"/>
			</div>
			<div style="margin: 15px;">
				<input name="workid" id="workid2" label="姓名:" value="" required="true"/>
			</div>
			<div style="margin: 15px;">
				<input name="workcenterno" id="userworklevel2" label="工作中心:" value="" required="true"/>
			</div>
			<div style="margin: 15px;">
				<input class="mes-datebox-ym" name="begindate" id="begindate2" label="生效时间:" value=""  required="true"/>
			</div>
		</form>
	</div>
	<div id="master-edit-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="editMaster.save()" style="width: 90px">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#master-edit-dialog').dialog('close')" style="width: 90px">关闭</a>
	</div>
	
<input id="path" type="hidden" value="${pageContext.request.contextPath}">

<script type="text/javascript" src="${pageContext.request.contextPath}/js/datagrid-common.js"></script>
<script type="text/javascript">
	$(function(){
		_params.type = 'allowanceUser';//处理的类
		_params.masterId = 'userid';//一级表的主键id
		_params.detaileId = 'userworkid';//一级表的主键id
		/* $('#master-copy').hide();
		$('#master-deep').hide();
		$('#master-download').hide(); */
		loadUser();
		initjsp([{
			tool:[
				{icon:"add",id:"add",text:"添加"},
				{icon:"add",id:"add",text:"添加"}
			],
			from:[
				{type:"textbox",name:"user",option:{text:"添加",required:"true",validType: 'length[10,10]'}},
				{type:"combobox",name:"name",option:{text:"添加"}}
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
	});
	
	var users = undefined;
	var works = undefined;

	function loadUser(){
		var para = ajaxParamData("allowanceUser", "findUserByPass");
		post(para, function(data){
			if(data.result){
				users = data.rows;
				loadWork();
			}
		});
	}
	function loadWork(){
		var para = ajaxParamData("allowanceWork", "masterFind", {workname:"",master: "master"});
		post(para, function(data){
			if(data.result){
				works = data.rows;
				init();
			}
		});
	}
	function formatWork(value) {
		if (isEmpty(value))
			return "";
		for (var i = 0; i < works.length; i++) {
			if (value == works[i].workid) {
				return works[i].workname;
			}
		}
	}
	function init(){
		initInput();
		$('#workcenterNo2').textbox('textbox').keyup(function(){
	        $(this).val( $(this).val().toUpperCase());
	    });
		$('#havework9').checkbox({
			onChange: function(checked){
				if(checked){
					$('#havework').val(1);
				}else{
					$('#havework').val(0);
				}
			}
		});
		$('#userno2').combobox({
			width: _input.width, height: _input.height, labelAlign: _input.labelAlign, labelWidth: _input.labelWidth,
			data: users,
			valueField:'userno',
		    textField:'userno',
			formatter: function(row){
				return row.userno + "--" + row.displayname + "--" + row.workcenterNo;
			},
			filter: function(q, row){
				return row.userno.indexOf(q) >= 0 || row.displayname.indexOf(q) >= 0 || row.workcenterNo.indexOf(q) >= 0;
			},
			onSelect: function(record){
				//$('#userno2').textbox('setValue',record.userno);
				$('#displayname2').textbox('setValue',record.displayname);
				$('#departmentno2').textbox('setValue',record.departmentno);
				$('#departmentname2').textbox('setValue',record.departmentname);
				$('#workcenterNo2').textbox('setValue',record.workcenterNo);
			},
			onChange: function(newValue, oldValue){
				$('#userno2').textbox('setValue',newValue);
			},
		});
		$('#workid2').combobox({
			width: _input.width, height: _input.height, labelAlign: _input.labelAlign, labelWidth: _input.labelWidth,
			data: works,
			valueField:'workid',
		    textField:'workname',
			formatter: function(row){
				return row.workname;
			},
			filter: function(q, row){
				return row.workname.indexOf(q) >= 0;
			},
		});
		$('#userworklevel2').combobox({
			width: _input.width, height: _input.height, labelAlign: _input.labelAlign, labelWidth: _input.labelWidth,
			data: [{text:'初级',value:'1'},{text:'中级',value:'2'},{text:'高级',value:'3'}],
			valueField:'value',
		    textField:'text',
		});
		$('#' + _params.master).datagrid({
			title:'员工列表', toolbar:_params.masterTb, iconCls:'icon-save',pageSize:50, pageList:[10,20,50,100,200],
			pagination : true,fit : true, striped:true, nowrap:true, rownumbers:true, remoteSort: true, singleSelect:true,
			columns:[[
				{ field:'ck', align:'center', checkbox:true},
				{ field:'userno', title:'工号', align:'left',sortable: true,},
				{ field:'displayname', title:'姓名', align:'right',sortable: true,},
				{ field:'departmentno', title:'部门编号', align:'right',sortable: true,},
				{ field:'departmentname', title:'部门名称', align:'right',sortable: true,},
				{ field:'workcenterNo', title:'工作中心', align:'right',sortable: true,},
				{ field:'off', title:'离职', align:'right',sortable: true,formatter: formatBoolean,},
				{ field: 'entby', title: '创建用户', align: 'center', sortable: true,},
				{ field: 'entdate', title: '创建日期', align: 'center', sortable: true,},
				{ field: 'modifyby', title: '修改用户', align: 'center', sortable: true,},
				{ field: 'modifydate', title: '修改日期', align: 'center', sortable: true,},
			]],
			onSelect: function (index, row) {
				editDetaile.load();
			},
			onLoadSuccess: function (data) {
				$(this).datagrid('selectRow', 0);
	        },
		});
		
		$('#' + _params.detaile).datagrid({
			title:'员工岗位明细', toolbar:_params.detaileTb, iconCls:'icon-save',//pageSize:50, pageList:[10,20,50,100,200],pagination : true,
			fit : true, striped:true, nowrap:true, rownumbers:true, remoteSort: false,multiSort:true, singleSelect:true,
			sortName: 'workid',sortOrder: 'asc',
			columns:[[
				{ field:'ck', align:'center', checkbox:true},
				{ field:'workid', title:'特殊岗位', align:'left',sortable: true,formatter: formatWork,},
				{ field:'userworklevel', title:'特殊岗位等级', align:'right',sortable: true,formatter: formatLevel,},
				{ field:'begindate', title:'生效时间', align:'center',sortable: true,formatter: formatDateYMD,},
				{ field:'enddate', title:'结束时间', align:'center',sortable: true,formatter: formatBackgroundColorDateYMD,},
				{ field: 'entby', title: '创建用户', align: 'center', sortable: true,},
				{ field: 'entdate', title: '创建日期', align: 'center', sortable: true,},
				{ field: 'modifyby', title: '修改用户', align: 'center', sortable: true,},
				{ field: 'modifydate', title: '修改日期', align: 'center', sortable: true,},
			]],
		});
		
	    editMaster.load();
	}  
	
</script>
</body>
</html>
