package cn.xyz.main.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.orm.db.DbBase;
import cn.xyz.orm.db.DbTool;

public class ToolsConfig {
	public static JSONObject config = new JSONObject();
	public static final String DATE_PATTERN = "date_pattern";
	public static int getInt(String key) throws Exception {
		return config.getIntValue(key);
	}
	public static double Double(String key) throws Exception {
		return config.getDoubleValue(key);
	}
	public static String getString(String key) throws Exception {
		return config.getString(key);
	}
	public static JSONObject getObject(String key) throws Exception {
		return config.getJSONObject(key);
	}
	public static JSONArray getArray(String key) throws Exception {
		return config.getJSONArray(key);
	}
	//把create放到DispatcherServlet里初始化
	public static void create() throws Exception {
		config = new JSONObject();
		DbBase db = DbBase.getDruid();
		String sql = DbTool.getInstance().select("sys_config").getSql();
		JSONArray data = db.find(sql);
		for (int i = 0; i < data.size(); i++) {
			config.put(data.getJSONObject(i).getString("key"), data.getJSONObject(i).get("value"));
		}
	}
	public static void main(String[] args) {
		try {
			create();
			String test1 = getString(DATE_PATTERN);
			JSONArray test2 = getArray("test3");
			System.out.println(test2.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
