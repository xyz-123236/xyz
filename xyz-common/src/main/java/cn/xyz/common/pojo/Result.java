package cn.xyz.common.pojo;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Result {
	private static Logger logger = Logger.getLogger(Result.class.getName());

	public static String success(String msg){
		return success(msg, null);
	}
	public static String success(String msg, Object data){
		return success(msg, data, null);
	}
	public static String success(String msg, Object data, Integer total){
		return result(msg, data, total, true, 200);
	}
	public static String error(String msg){
		return result(msg, null, null, false, 500);
	}
	public static String error(Exception e) {
		return error(e, "程序异常");
	}
	public static String error(Exception e, String msg) {
		logger.error("程序异常", e);
		return error(msg);
	}
	public static String result() {
		return result(null, new JSONArray(), 0, true, 200);
	}
	public static String result(String msg, Object data, Integer total, boolean status, Integer code) {
		JSONObject obj = new JSONObject();
		obj.put("status", status);
		obj.put("msg", msg);
		obj.put("rows", data);
		obj.put("total", total);
		obj.put("code", code);
		return obj.toJSONString();
	}
	
	//combobox使用url请求时需要的数据
	public static String data(Object data) throws Exception {
		return JSON.toJSONString(data);
	}
	
	//KindEditor返回码
	public static String successKE(String url){
		JSONObject obj = new JSONObject();
		obj.put("error", 0);
		obj.put("url", url);
		return obj.toJSONString();
	}
	public static String errorKE(String message){
		JSONObject obj = new JSONObject();
		obj.put("error", 1);
		obj.put("message", message);
		return obj.toJSONString();
	}
	
	
}
