package cn.xyz.test.testdb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.tools.Tools;
import cn.xyz.common.orm.DbBase;

public class T003 {

	public static void main(String[] args) {
		try {
			DbBase db = DbBase.getDruid();
			JSONArray tables = db.getTables(null,"xyz","%",null);
			JSONObject obj = new JSONObject();
			for (int i = 0; i < tables.size(); i++) {
				String table = tables.getJSONObject(i).getString("TABLE_NAME");
				JSONObject fileds = db.getFiledType(table);
				JSONArray pk = db.getPrimaryKey(table);
				String primaryKey = "";
				if(!Tools.isEmpty(pk) && pk.size() == 1) {
					primaryKey = pk.getJSONObject(0).getString("COLUMN_NAME");
				}
				JSONArray arr = new JSONArray();
				arr.add(primaryKey);
				arr.add(fileds);
				obj.put(table, arr);
			}
			System.out.println(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
