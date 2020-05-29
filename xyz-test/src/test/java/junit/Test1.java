package junit;

import org.junit.Test;

public class Test1 {
	@Test
	public void t1() {
		String a = "'’'‘’";
		for (int i = 0; i < a.length(); i++) {
			if("'".equals(a.substring(i, i+1))) {
				System.out.println("y");
			}else if("‘".equals(a.substring(i, i+1)) || "’".equals(a.substring(i, i+1))) {
				System.out.println("z");
			}else {
				System.out.println("o");
			}
		}
	}
	@Test
	public void t2() {
		String a = "、`′";
	}
}
