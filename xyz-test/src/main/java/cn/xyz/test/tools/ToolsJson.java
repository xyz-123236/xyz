package cn.xyz.test.tools;

import com.alibaba.fastjson.JSONObject;

import cn.xyz.main.dao.DbTool;

public class ToolsJson {
	public static JSONObject removeKey(JSONObject row, Object...keys) throws Exception {
		if(Tools.isEmpty(row)) return null;
		JSONObject obj = (JSONObject)row.clone();
		if(!Tools.isEmpty(keys)) {
			for (int i = 0; i < keys.length; i++) {
				Object key = keys[i];
				if(key.getClass().isArray()){//用于keys是：String,String,String...(最后一个是数组)
					String[] o = (String[])key;
					for (int j = 0; j < o.length; j++) {
						row.remove(o[j].trim());
					}
				}else {
					String[] s = ((String)key).split(",");
					for (int j = 0; j < s.length; j++) {
						row.remove(s[j].trim());
					}
				}
			}
		}
		return obj;
	}
	
	public static void main(String[] args) {
		JSONObject row = new JSONObject();
		row.put("a","111");
		row.put("b","222");
		String[] c = {"a","c"};
		try {
			removeKey(row,DbTool.DEFAULT_REMOVE_KEYS,c);
			System.out.println(row);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
