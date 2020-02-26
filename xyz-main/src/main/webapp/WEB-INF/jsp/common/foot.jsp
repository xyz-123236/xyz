<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
						
					</form>
				</div>
			</div>
		</div>
		<div data-options="region:'east',width:'50%',split:true">
   			<table id="detaile"></table>
   			<div id="detaile-tb">
   				<div>
				    <a href="javascript:void(0)" class="easyui-linkbutton" id="master-load" iconCls="icon-reload" plain="true" onclick="editDetaile.load()">刷新</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" id="master-add" iconCls="icon-add" plain="true" onclick="editDetaile.add()">新增</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" id="master-upload" iconCls="icon-edit" plain="true" onclick="editDetaile.update()">修改</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" id="master-remove" iconCls="icon-remove" plain="true" onclick="editDetaile.remove()">删除</a>
		    		<a href="javascript:void(0)" class="easyui-linkbutton" id="master-copy" iconCls="icon-large-shapes" plain="true" onclick="editDetaile.copy()">复制</a>
				</div>
				<div>
					<form id="detaile-form" method="post" style="overflow:auto;">
						