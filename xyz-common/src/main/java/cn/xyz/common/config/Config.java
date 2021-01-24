package cn.xyz.common.config;

import java.util.Properties;

public interface Config extends Op, Key, Value {

	String NUMBER_TYPES = ",INTEGER,BIGINT,FLOAT,INT,DOUBLE,";
	String DEFAULT_REMOVE_KEYS = "page,rows,sort,order,jsp_name";
	Properties properties = null;
	//public static JSONObject config = new JSONObject();
	//加载系统配置文件
//	static {
//		try {
//			properties = ToolsProperties.load("xyz.properties");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	/*private static JSONObject config = new JSONObject();
	public static String NUMBER_TYPES = ",INTEGER,BIGINT,FLOAT,INT,DOUBLE,";
	public static String DEFAULT_REMOVE_KEYS = "page,rows,sort,order,jsp_name";
	private static Properties properties = null;

	//加载系统配置文件
	static {
		try {
			properties = ToolsProperties.load("config.properties");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Integer getInt(String key) {
		return config.getInteger(key);
	}
	public static Double getDouble(String key) {
		return config.getDouble(key);
	}
	public static String getString(String key) {
		return config.getString(key);
	}
	public static Object getObject(String key) {
		return config.get(key);
	}
	public static JSONObject getJSONObject(String key) {
		return config.getJSONObject(key);
	}
	public static JSONArray getJSONArray(String key) {
		return config.getJSONArray(key);
	}
	//把create放到DispatcherServlet里初始化
	/*public static void create() throws Exception {
		config = new JSONObject();
		JSONArray data = DbTool.getInstance().select("sys_config").find();
		for (int i = 0; i < data.size(); i++) {
			config.put(data.getJSONObject(i).getString("key"), data.getJSONObject(i).get("value"));
		}
	}
	public static void main(String[] args) {
		System.out.println(Config.NUMBER_TYPES);
		System.out.println(Config.getString("number_types"));
	}*/
}
