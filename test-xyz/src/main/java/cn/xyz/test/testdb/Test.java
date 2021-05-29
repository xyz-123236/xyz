package cn.xyz.test.testdb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.orm.DbTool;

public class Test {

	public static void main(String[] args) {
		DbBase db = null;
		try {
			//System.out.println("123");
			db = DbBase.getJdbc("mysql");
			DbTool dt = DbTool.getInstance();
			
			JSONArray rows = new JSONArray();
			JSONObject row = new JSONObject();
			row.put("code", "62");
			row.put("name", "xx");
			row.put("id", 2);
			rows.add(row);
			JSONObject b = new JSONObject();
			b.put("code", "63");
			b.put("name", "yy");
			rows.add(b);
			
			JSONObject form = new JSONObject();
			form.put("page", "1");
			form.put("rows", "10");
			
			insert(db, dt, rows, row, form);
			delete(db, dt, rows, row, form);
			update(db, dt, rows, row, form);
			select(db, dt, rows, row, form);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void insert(DbBase db, DbTool dt, JSONArray rows, JSONObject row, JSONObject form) throws Exception {
		//dt.insert(db, "t3", row, null);
		//System.out.println(dt.createInsertSql(db, "t3", row, null));
		
		
	}
	public static void delete(DbBase db, DbTool dt, JSONArray rows, JSONObject row, JSONObject form) throws Exception {
		
		//System.out.println(dt.createInsertSql(db, "t3", row, null));
		
	}
	public static void update(DbBase db, DbTool dt, JSONArray rows, JSONObject row, JSONObject form) throws Exception {
		
		//System.out.println(dt.createInsertSql(db, "t3", row, null));
		
	}
	public static void select(DbBase db, DbTool dt, JSONArray rows, JSONObject row, JSONObject form) throws Exception {
		
		//System.out.println(dt.createInsertSql(db, "t3", row, null));
	}
}
