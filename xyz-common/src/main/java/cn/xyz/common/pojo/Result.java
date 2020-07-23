package cn.xyz.common.pojo;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Result {
	private static Logger logger = Logger.getLogger(Result.class.getName());

	public static String success(String msg){
		return result(200, msg, null, null);
	}
	public static String success(Object data){
		return result(200, null, data, null);
	}
	public static String success(String msg, Object data){
		return result(200, msg, data, null);
	}
	public static String success(Object data, Integer total){
		return result(200, null, data, total);
	}
	public static String success(String msg, Object data, Integer total){
		return result(200, msg, data, total);
	}
	public static String error(String msg){
		return result(500, msg, null, null);
	}
	public static String error(Exception e) {
		logger.error("程序异常", e);
		return error("程序异常");
	}
	public static String error(Exception e, String msg) {
		logger.error("程序异常", e);
		return error(msg);
	}
	public static String easyuiNull() {
		return result(200, null, new JSONArray(), 0);
	}
	//combobox使用url请求时需要的数据
	public static String toJson(Object data) throws Exception {
		return JSON.toJSONString(data);
	}
	public static String result(Integer status, String msg, Object data, Integer total) {
		JSONObject obj = new JSONObject();
		obj.put("status", status);
		obj.put("msg", msg);
		obj.put("rows", data);
		obj.put("total", total);
		return obj.toJSONString();
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
