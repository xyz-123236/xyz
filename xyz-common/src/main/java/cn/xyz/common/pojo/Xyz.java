package cn.xyz.common.pojo;

import com.alibaba.fastjson.JSONObject;

public class Xyz {
	public static JSONObject error(String msg) {
		JSONObject obj = new JSONObject();
		obj.put("status", false);
		obj.put("msg", msg);
		return obj;
	}
	public static JSONObject success(Object data) {
		JSONObject obj = new JSONObject();
		obj.put("status", true);
		obj.put("data", data);
		return obj;
	}
}
