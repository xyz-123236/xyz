package cn.xyz.test.test;

import cn.xyz.common.tools.ToolsDate;

public class T006 {

	public static void main(String[] args) {
		try {
			//System.out.println(ToolsDate.getString("2019-12-29", "YYYY-MM-DD"));
			String pno = "A530_190000";
			if(pno.matches("^A930_19000[1-9]$") || pno.matches("^A[0-4].*$")) {
				System.out.println("ok");
			}else {
				System.out.println("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
