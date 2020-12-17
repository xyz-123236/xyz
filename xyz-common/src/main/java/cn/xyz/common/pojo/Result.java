package cn.xyz.common.pojo;

import cn.xyz.common.config.Config;
import cn.xyz.common.exception.CustomException;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.tools.Tools;

public class Result {
	private static final Logger logger = Logger.getLogger(Result.class.getName());
	public static String success(){
		return success(Config.EMPTY);
	}
	public static String success(String msg){
		return success(new JSONArray(), msg);
	}
	public static String success(Object data){
		return success(data, Config.EMPTY);
	}
	public static String success(Object data, String msg){
		return success(data, Config.ZERO, msg);
	}
	public static String success(Object data, Integer total){
		return success(data, total, Config.EMPTY);
	}
	public static String success(Object data, Integer total, String msg){
		return success(data, total, msg, 200);
	}
	public static String success(Object data, Integer total, String msg, Integer code){
		return result(data, total, msg, code, true);
	}

	public static String error(Exception e) {
		if(!Tools.isEmpty(e.getMessage()) && e.getMessage().contains("unique constraint violated")) {
			return error("主键重复");
		}
		if(e instanceof CustomException) {
			return error(e.getMessage());
		}
		logger.error("程序异常: ", e);//可发邮件，存数据库
		return error("程序异常: " + e.getMessage());
	}
	public static String error(String msg){
		return result(null, null, msg, 500, false);
	}

	/**
	 * 返回键值对
	 * @param data
	 * @param total
	 * @param msg
	 * @param status
	 * @param code
	 * @return
	 */
	public static String result(Object data, Integer total, String msg, Integer code, boolean status) {
		JSONObject obj = new JSONObject();
		obj.put("rows", data);
		obj.put("total", total);
		obj.put("msg", msg);
		obj.put("code", code);
		obj.put("status", status);
		return obj.toJSONString();
	}

	/**
	 * 返回数组
	 * @param data
	 * @return
	 */
	public static String result(Object data) {
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
