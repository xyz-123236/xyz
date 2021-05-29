package junit;


import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class Test4 {
	@Test
	public void t1() {
		String a = "0011";
		System.out.println(("0000"+(Integer.valueOf(a)+1)).substring((Integer.valueOf(a)+1+"").length()));
	}
	
	@Test
	public void t2() {
		JSONObject obj = new JSONObject();
		System.out.println(obj.isEmpty());
		System.out.println(obj.size());
	}
}
