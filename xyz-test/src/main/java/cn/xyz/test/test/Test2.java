package cn.xyz.test.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class Test2 {
	public static void main(String[] args) {
		String a = "chengxin396@126.com";
		String b = "chengxin396@126.com";
		System.out.println(a.length());
		System.out.println(a.length());
		//String a = "02";
		//System.out.println(Integer.parseInt(a));
		/*JSONObject obj = new JSONObject();
		obj.put("from", "2020-10-01");
		obj.put("to", "2020-10-11");
		System.out.println(obj.getDate("from").compareTo(obj.getDate("to")));*/
		/*String regex = "^[1-9]\\d*$";
		String a = "a270009998";
		System.out.println(a.matches(regex));*/
		/*List<String> list = new ArrayList<>();
		list.add("aa");
		list.add("bb");
		list.add(1, "cc");
		list.add(1, "dd");
		String[] aa = {"aa","bb","cc"};
		System.out.println(StringUtils.join(aa,","));
		System.out.println(list.toString());*/
	}
}
