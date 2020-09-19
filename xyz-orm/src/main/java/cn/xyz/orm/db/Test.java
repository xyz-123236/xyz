package cn.xyz.orm.db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test {

	public static void main(String[] args) {
		DbBase db = null;
		try {
			//System.out.println("159");
			db = DbBase.getJdbc("mysql");
			DbTool dt = DbTool.getInstance();
			
			JSONArray rows = new JSONArray();
			JSONObject row = new JSONObject();
			row.put("code", "2113");
			row.put("name", "xx");
			row.put("id", 2);
			rows.add(row);
			JSONObject b = new JSONObject();
			b.put("code", "8375");
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
		//
		
		
	}
	public static void update(DbBase db, DbTool dt, JSONArray rows, JSONObject row, JSONObject form) throws Exception {
		//
		
		
	}
	public static void select(DbBase db, DbTool dt, JSONArray rows, JSONObject row, JSONObject form) throws Exception {
		//
		
	}
}
