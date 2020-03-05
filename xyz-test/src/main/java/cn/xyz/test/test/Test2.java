package cn.xyz.test.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Test2 {
	public static void main(String[] args) {
		String regex = "^[1-9]\\d*$";
		String a = "a270009998";
		System.out.println(a.matches(regex));
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
