package cn.xyz.test.test;

import java.util.Date;

import cn.xyz.common.tools.ToolsDate;

public class T010 {

	public static void main(String[] args) {
		Date date = new Date();
		System.out.println(ToolsDate.getLongString(date));
		java.sql.Timestamp d = new java.sql.Timestamp(date.getTime());
		System.out.println(d);
		System.out.println(ToolsDate.getLongString(d));
	}

}
