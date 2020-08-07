package cn.xyz.common.pojo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface Config {
	public static final String MYSQL = "mysql";
	public static final String SPACE = " ";
	public static final String LOGIN_USER = "login_user";
	
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
	/*public static void create() throws Exception {
		config = new JSONObject();
		JSONArray data = DbTool.getInstance().select("sys_config").find();
		for (int i = 0; i < data.size(); i++) {
			config.put(data.getJSONObject(i).getString("key"), data.getJSONObject(i).get("value"));
		}
	}*/
}
