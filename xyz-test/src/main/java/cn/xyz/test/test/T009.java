package cn.xyz.test.test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.Date;

public class T009 {

	public static void main(String[] args) {
		Timestamp t = new Timestamp(System.currentTimeMillis());
		System.out.println(t);
		System.out.println(new Date().toInstant());
		LocalDate ld = LocalDate.now().plusDays(5);
		System.out.println(ld);
		LocalTime lt = LocalTime.now();
		System.out.println(lt);
		LocalDateTime ldt = LocalDateTime.now().minusHours(2).minusDays(5);
		System.out.println(ldt);
		System.out.println(Year.now());
		
		LocalDateTime ldt2 = LocalDateTime.parse("2020-09-08T07:31:35.333");
		System.out.println(ldt2);
		LocalDateTime ldt3 = ldt2.minusDays(5);
		System.out.println(ldt3);
		
		System.out.println(ldt3.isAfter(ldt2));
		System.out.println(ldt3.isBefore(ldt2));
		System.out.println(ldt3.isEqual(ldt2));
		System.out.println(LocalDateTime.MAX);
		System.out.println(LocalDateTime.MIN);
	}

}
