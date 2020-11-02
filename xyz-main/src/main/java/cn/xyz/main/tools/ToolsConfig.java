package cn.xyz.main.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.orm.DbTool;

public class ToolsConfig {
	public static JSONObject config = new JSONObject();
	public static final String DATE_PATTERN = "date_pattern";
	public static Integer getInt(String key) throws Exception {
		return config.getInteger(key);
	}
	public static Double getDouble(String key) throws Exception {
		return config.getDouble(key);
	}
	public static String getString(String key) throws Exception {
		return config.getString(key);
	}
	public static Object getObject(String key) throws Exception {
		return config.get(key);
	}
	public static JSONObject getJSONObject(String key) throws Exception {
		return config.getJSONObject(key);
	}
	public static JSONArray getJSONArray(String key) throws Exception {
		return config.getJSONArray(key);
	}
	//把create放到DispatcherServlet里初始化
	public static void create() throws Exception {
		config = new JSONObject();
		JSONArray data = DbTool.getInstance().createSelectSql("sys_config").select();
		for (int i = 0; i < data.size(); i++) {
			config.put(data.getJSONObject(i).getString("key"), data.getJSONObject(i).get("value"));
		}
	}
	public static void main(String[] args) {
		try {
			create();
			String test1 = getString(DATE_PATTERN);
			JSONArray test2 = getJSONArray("date_patterns");
			System.out.println(test2.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
