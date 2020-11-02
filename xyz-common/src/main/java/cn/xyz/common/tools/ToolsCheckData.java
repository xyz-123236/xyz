package cn.xyz.common.tools;

import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

public class ToolsCheckData {
	public static String check(JSONObject obj) {
		for(String key:obj.keySet()){
			String msg = check(key, obj.getString(key));
			if(!Tools.isEmpty(msg)) {
				return msg;
			}
		}
		return "";
	}
	public static String check(String key, String value) {
		if("usercode".equals(key)) {
			if(!Pattern.matches("[a-zA-Z]([0-9a-zA-Z]{5}|[0-9a-zA-Z]{15})",value)) {
				return "用户名不能小于6位，大于16位";
			}
		}else if("password".equals(key)) {
			if(!Pattern.matches("([0-9a-zA-Z]{6}|[0-9a-zA-Z]{16})",value)) {
				return "密码不能小于6位，大于16位";
			}
		}
		return "";
	}
	public static void main(String[] args) {
		System.out.println(check(new JSONObject()));
		System.out.println(check("usercode","a23456"));
	}
}
