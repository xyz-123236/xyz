package cn.xyz.test.test;

import com.alibaba.fastjson.JSONObject;

public class T003 {

	public static void main(String[] args) {
		JSONObject obj = new JSONObject();
		System.out.println(obj.getDoubleValue("aa"));
	}

}
