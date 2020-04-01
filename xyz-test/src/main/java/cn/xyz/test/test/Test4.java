package cn.xyz.test.test;

import java.net.URL;

import cn.xyz.test.et.ClassA;
import cn.xyz.test.et.ClassB;

public class Test4 {

	public static void main(String[] args) {
		//URL url  =Test4.class.getClassLoader().getResource("/"+packageName.replaceAll("\\.", "/"));
        /*ClassA a = new ClassA();
        a.test1();
        ClassB b = new ClassB();
        b.test1();
        b.test2();
        ClassA c = new ClassB();
        c.test1();
        c.test2();*/
		System.out.println(Test4.class.getClassLoader().getResource("/cn/xyz/test"));
	}

}
