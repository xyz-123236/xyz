package cn.xyz.test.test;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.tools.ToolsDate;

public class T007 {

	public static void main(String[] args) {
		JSONObject obj = new JSONObject();
		obj.put("aa", "");
		System.out.println(1);
		System.out.println(obj.getDate("bb"));
		System.out.println(2);
		System.out.println(obj.getDate("aa"));
		System.out.println(3);
		System.out.println(obj.getInteger("aa"));
		System.out.println(obj.getIntValue("aa"));
		Date d = new Date();
		System.out.println(d.getTime());
		System.out.println(ToolsDate.getString("yyyyMMddHHmmssSSS"));
	}

}
