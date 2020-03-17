package cn.xyz.test.test;

import java.util.regex.Pattern;

public class Test3 {

	public static void main(String[] args) {
		String a = "2020-03-16 12:30:52";
		String pattern ="";
		for (int i = 0; i < a.length(); i++) {
			String item = a.substring(i,i+1);
			if(Pattern.matches("[0-9]",item)) {
				pattern += "[0-9a-zA-Z]";
			}else {
				pattern += item;
			}
		}
		System.out.println(pattern);
		if(Pattern.matches(pattern,a)) {
			System.out.println("匹配成功");
		}else {
			System.out.println("匹配失败");
		}
		if(Pattern.matches(pattern,"yyyy/MM/dd HH:mm:ss")) {
			System.out.println("匹配成功");
		}else {
			System.out.println("匹配失败");
		}
	}

}
