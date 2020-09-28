package cn.xyz.test.testJdbc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.orm.DbBase;
import cn.xyz.common.orm.DbTool;

public class T005 {

	public static void main(String[] args) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("table", "t6");
			JSONArray rows = new JSONArray();
			for (int i = 0; i < 10; i++) {
				JSONObject row = new JSONObject();
				row.put("field", "f"+i);
				if(i==0) {
					row.put("type", "bigint");
					row.put("auto_increment", true);
					row.put("not_null", true);
					row.put("primary", true);
				}else {
					row.put("type", "varchar");
				}
				if(i==2 || i== 3) {
					row.put("unique", true);
				}
				row.put("length", 10+i);
				if(i!=0) {
					row.put("default_value", 0);
				}
				
				row.put("comment", "备注"+i);
				rows.add(row);
			}
			obj.put("rows", rows);
			System.out.println(DbTool.getInstance().createTableSql(obj));

			//DbTool.getInstance().createTable(DbBase.getJdbc(), obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
