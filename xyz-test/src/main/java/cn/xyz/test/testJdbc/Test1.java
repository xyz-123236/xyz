package cn.xyz.test.testJdbc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.orm.db.DbBase;
import cn.xyz.orm.db.DbJdbc;
import cn.xyz.orm.db.DbTool;

public class Test1 {

	public static void main(String[] args) {
		DbBase db = null;
		try {
			db = DbBase.getJdbc();
			//System.out.println(db.find("select * from t1"));
			//System.out.println(db.insert("insert into sn_detail (batch_id,sn_detail) values (20,'ccc')"));
			//System.out.println(db.executeQueryJson("select * from sn_detail"));
			//System.out.println(db.executeUpdate("alter table t1 add code varchar(10) after id"));
			JSONArray data = new JSONArray();
			JSONObject a = new JSONObject();
			a.put("code", "62");
			a.put("name", "xx");
			a.put("id", 2);
			data.add(a);
			JSONObject b = new JSONObject();
			b.put("code", "63");
			b.put("name", "yy");
			data.add(b);
			System.out.println(db.getPrimaryKey("t4"));
			//System.out.println(DbTool.getInstance().insert("t4", a, null));
			//String str = "insert into sn_detail (batch_id,sn_detail) values";
			//System.out.println(str.replaceAll("values", ""));
			//System.out.println(db.insert("insert into sn_detail (batch_id,sn_detail) values (24,?)",a));
			//System.out.println(db.insert("insert into t3 (code,name) values (?,?) ",a));
			//System.out.println(db.insert("insert into sn_detail (batch_id,sn_detail) values",a));
			//System.out.println(db.insertBatch("insert into sn_detail (batch_id,sn_detail) values",data));
			//DbTool.getInstance(null).insertBatch(db,"t3", data, "tang.wu",true);
			//System.out.println(db.insertBatch(sql,data));
			//System.out.println(db.insertBatch("insert into sn_detail (batch_id,sn_detail) values",data));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
