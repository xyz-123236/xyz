package cn.xyz.test.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.tools.ToolsExcel;
import cn.xyz.jdbc.dao.DbBase;
import cn.xyz.test.pojo.Config;

public class Test1 {

	public static void main(String[] args) {
		try {
			DbBase db = DbBase.getInstance("mysql");
			JSONArray data = db.find("select * from sn_detail");
			
			JSONObject obj = new JSONObject();
			obj.put("data", data);
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
			
			
			System.out.println("aaa");
			System.out.println(Config.SPACE+"aaa");
			LocalDateTime a = LocalDateTime.now();
			System.out.println(a);
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			LocalDateTime b = LocalDateTime.parse("2018-05-26 23:05:32");
			System.out.println(f.format(a));
			System.out.println(f.format(b));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
