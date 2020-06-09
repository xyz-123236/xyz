package cn.xyz.test.test;

import com.alibaba.fastjson.JSONObject;

public class Test001 {

	public static void main(String[] args) {
		JSONObject obj = new JSONObject();
		obj.put("aa", "");
		obj.put("bb", null);
		obj.put("cc", "123");
		for(String key: obj.keySet()){
			System.out.println(key);
		}
	}

}
