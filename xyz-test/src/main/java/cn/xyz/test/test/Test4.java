package cn.xyz.test.test;

import java.net.URL;

public class Test4 {

	public static void main(String[] args) {
		//URL url  =Test4.class.getClassLoader().getResource("/"+packageName.replaceAll("\\.", "/"));
        
		System.out.println(Test4.class.getClassLoader().getResource("/cn/xyz/test"));
	}

}
