package cn.xyz.test.test;

import cn.xyz.common.tools.ToolsDate;

public class T006 {

	public static void main(String[] args) {
		try {
			System.out.println(ToolsDate.getString("2019-12-29", "YYYY-MM-DD"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
