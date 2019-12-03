package cn.xyz.test.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test1 {

	public static void main(String[] args) {
		LocalDateTime a = LocalDateTime.now();
		System.out.println(a);
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime b = LocalDateTime.parse("2018-05-26 23:05:32");
		System.out.println(f.format(a));
		System.out.println(f.format(b));
	}

}
