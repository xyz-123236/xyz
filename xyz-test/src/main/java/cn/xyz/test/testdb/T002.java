package cn.xyz.test.testdb;

import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.orm.DbTool;
import cn.xyz.common.orm.DbBase;

public class T002 {

	public static void main(String[] args) {
		
		JSONObject row = new JSONObject();
		row.put("dat", "9''h");
		row.put("num", "9");
		row.put("ok", "9\'9");
		row.put("ng", "9\"99");
		row.put("pid", "9");
		//row.put("dat", DbTool.escape(row.getString("dat")));
		row.put("ok", DbTool.escape(row.getString("ok")));
		row.put("ng", DbTool.escape(row.getString("ng")));
		DbTool d = DbTool.getInstance();
		try {
			System.out.println(d.insert(DbBase.getJdbc(DbBase.DEFAULT_DB), "test3", row, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(d.getSql());
	}

}
