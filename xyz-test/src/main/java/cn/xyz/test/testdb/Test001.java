package cn.xyz.test.testdb;

import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.orm.DbTool;
import cn.xyz.common.orm.DbBase;
import cn.xyz.common.pojo.Config;


public class Test001 {

	public static void main(String[] args) {
		JSONObject row = new JSONObject();
		row.put("dat", "9h");
		row.put("num", "9");
		row.put("ok", "99");
		row.put("ng", "999");
		row.put("pid", "9");
		DbTool d = DbTool.getInstance();
		try {
			System.out.println(DbBase.getJdbc(DbBase.DEFAULT_DB).getUnique("t2"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Config.config = new JSONObject();
		
	}

}
