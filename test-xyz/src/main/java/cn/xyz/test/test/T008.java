package cn.xyz.test.test;

import java.util.HashSet;
import java.util.Set;

public class T008 {

	public static void main(String[] args) {
		/*String a = "agagwete";
		System.out.println(a.substring(-1));*/
		String[] a = null;
		Set<String> set = new HashSet<>();
		set.add("aa");
		set.add("bb");
		a = new String[set.size()];
		a = set.toArray(a);
		System.out.println(a[0]);
	}

}
