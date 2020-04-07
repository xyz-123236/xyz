package cn.xyz.test.testJdbc;

import com.alibaba.fastjson.JSONArray;

import cn.xyz.orm.db.DbBase;

public class Test2 {

	public static void main(String[] args) {
		try {
			DbBase db = DbBase.getJdbc().startTransaction();
			db.execute("lock tables t1 write;");
			JSONArray data = db.find("select * from t1");
			System.out.println(data);
			Thread.sleep(100000);
			db.commit();
			
			db.execute("unlock tables;");
			System.out.println("结束");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
