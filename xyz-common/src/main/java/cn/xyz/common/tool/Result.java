package cn.xyz.common.tool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Result {
	
	public static String success(Object rows){
		return result(rows, null, 0, null, null);
	}
	public static String error(String message){
		return result(null, null, 1, null, message);
	}
	public static String errorEasyui() {
		return result(new JSONArray(), 0, 1, null, "没有找到记录");
	}
	public static String successEasyui(Object rows, Integer total) {
		return result(rows, total, 0, null, null);
	}
	public static String successKindEditor(String url){
		return result(null, null, 0, url, null);
	}
	public static String errorKindEditor(String message){
		return result(null, null, 1, null, message);
	}
	public static String result(Object rows, Integer total, Integer error, String url, String message) {
		JSONObject obj = new JSONObject();
		obj.put("rows", rows);//记录：easyui需要
		obj.put("total", total);//总记录数：easyui需要
		obj.put("error", error);//状态码：KindEditor需要       0成功，1失败
		obj.put("url", url);//url:KindEditor需要
		obj.put("message", message);//消息：KindEditor需要
		return obj.toJSONString();
	}
}
