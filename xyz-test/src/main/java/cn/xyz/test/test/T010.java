package cn.xyz.test.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.xyz.common.tools.ToolsDate;

public class T010 {

	public static void main(String[] args) {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		int month = cal.get(Calendar.MONTH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd", Locale.ENGLISH);
		String month_s = sdf.format(cal.getTime());
		System.out.println(month_s.substring(5, 8).toUpperCase());
		System.out.println(ToolsDate.getLongString(date));
		java.sql.Timestamp d = new java.sql.Timestamp(date.getTime());
		System.out.println(d);
		System.out.println(ToolsDate.getLongString(d));
	}

}
