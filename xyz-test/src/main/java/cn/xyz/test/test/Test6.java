package cn.xyz.test.test;

import java.util.Date;

public class Test6 {
	public static void main(String[] args) {
		Object[] obj = new Object[5];
		obj[0] = 5;
		obj[1] = "abc";
		obj[2] = 5.6;
		obj[3] = new Date();
		for (int i = 0; i < obj.length; i++) {
			if(obj[i] instanceof String) {
				System.out.println("String");
			}else if(obj[i] instanceof Integer) {
				System.out.println("Integer");
			}else if(obj[i] instanceof Float) {
				System.out.println("Float");
			}else if(obj[i] instanceof Double) {
				System.out.println("Double");
			}else if(obj[i] instanceof Date) {
				System.out.println("Date");
			}else {
				System.out.println("other");
			}
		}
	}
}
