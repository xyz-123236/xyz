package cn.xyz.test.testJdbc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.orm.db.DbBase;
import cn.xyz.orm.db.DbTool;

public class Test1 {

	public static void main(String[] args) {
		DbBase db = null;
		try {
			db = DbBase.getInstance("mysql");
			System.out.println(db.find("select * from t1"));
			//System.out.println(db.insert("insert into sn_detail (batch_id,sn_detail) values (20,'ccc')"));
			//System.out.println(db.executeQueryJson("select * from sn_detail"));
			//System.out.println(db.executeUpdate("alter table t1 add code varchar(10) after id"));
			JSONArray data = new JSONArray();
			JSONObject a = new JSONObject();
			a.put("code", "23");
			a.put("name", "xx");
			data.add(a);
			JSONObject b = new JSONObject();
			b.put("code", "22");
			b.put("name", "yy");
			data.add(b);
			//String str = "insert into sn_detail (batch_id,sn_detail) values";
			//System.out.println(str.replaceAll("values", ""));
			//System.out.println(db.insert("insert into sn_detail (batch_id,sn_detail) values (24,?)",a));
			//System.out.println(db.insert("t3",a));
			//System.out.println(db.insert("insert into sn_detail (batch_id,sn_detail) values",a));
			//System.out.println(db.insertBatch("insert into sn_detail (batch_id,sn_detail) values",data));
			String sql = DbTool.getInstance(null).insertBatch("t3", data, "tang.wu").getSql();
			System.out.println(db.insertBatch(sql,data));
			//System.out.println(db.insertBatch("insert into sn_detail (batch_id,sn_detail) values",data));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			db.closeConnection();
		}

	}

}
