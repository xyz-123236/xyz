<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="loading-mask"
	style="position:absolute;top:0px; left:0px; width:100%; height:100%; background:#D2E0F2; z-index:20000">
	<div id="pageloading"
		style="position:absolute; top:50%; left:50%; margin:-120px 0px 0px -120px; text-align:center;  border:2px solid #8DB2E3; width:200px; height:40px;  font-size:14px;padding:10px; font-weight:bold; background:#fff; color:#15428B;">
		<img src="${pageContext.request.contextPath}/images/loading.gif" align="absmiddle" /> 正在加载中,请稍候...
	</div>
</div>
<div class="easyui-layout" data-options="fit:true">
	    <div data-options="region:'center',width:'50%',split:true">
			<table id="master"></table>
		    <div id="master-tb">
			    <div>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" id="master-load" iconCls="icon-reload" plain="true" onclick="editMaster.load()">刷新</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" id="master-add" iconCls="icon-add" plain="true" onclick="editMaster.add()">新增</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" id="master-update" iconCls="icon-edit" plain="true" onclick="editMaster.update()">修改</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" id="master-remove" iconCls="icon-remove" plain="true" onclick="editMaster.remove()">删除</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" id="master-copy" iconCls="icon-large-shapes" plain="true" onclick="editMaster.copy()">复制</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" id="master-deep" iconCls="icon-large-shapes" plain="true" onclick="editMaster.copy('deep')">深复制</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" id="master-upload" iconCls="icon-save" plain="true" onclick="editMaster.upload()">上传</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" id="master-download" iconCls="icon-save" plain="true" onclick="editMaster.download()">导出</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" id="master-download" iconCls="icon-save" plain="true" onclick="editMaster.other()">其它</a>
			    </div>
			    <div>
					<form id="master-form" method="post" style="overflow:auto;">
						