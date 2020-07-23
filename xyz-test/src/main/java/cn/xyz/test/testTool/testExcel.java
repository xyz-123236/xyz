package cn.xyz.test.testTool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.tools.ToolsExcel;
import cn.xyz.orm.db.DbBase;

public class testExcel {
	public static void main(String[] args) {
		try {
			DbBase db = DbBase.getJdbc();
			JSONArray data = db.find("select * from sn_detail");
			
			JSONObject obj = new JSONObject();
			//obj.put("data", data);
			obj.put("file_name", "test1");
			obj.put("sheet_name", "sheet1");
			obj.put("title", "title");
			obj.put("prohibits", ",2,1,");
			String[][] cells = {{"用户名","姓名","密码"}
			,{"id","batch_id","sn_detail"}};
			Integer[][] formats = {
					{}
					,{4000,4000,4000}	
			};
			
			ToolsExcel.export(obj, cells, formats);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
