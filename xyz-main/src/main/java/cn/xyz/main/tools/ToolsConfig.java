package cn.xyz.main.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.tools.Tools;
import cn.xyz.orm.db.DbBase;
import cn.xyz.orm.db.DbTool;

public class ToolsConfig {
	public static JSONObject config = new JSONObject();
	
	public static String get(String key) throws Exception {
		if(Tools.isEmpty(config)) {
			DbBase db = DbBase.getDruid();
			String sql = DbTool.getInstance().select("sys_config").getSql();
			JSONArray data = db.find(sql);
			for (int i = 0; i < data.size(); i++) {
				config.put(data.getJSONObject(i).getString("key"), data.getJSONObject(i).getString("value"));
			}
		}
		return config.getString(key);
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(get("test1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
