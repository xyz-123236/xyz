package cn.xyz.test.test;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;

import cn.xyz.orm.db.DbBase;

public class Test19 {

	public static void main(String[] args) {
		try {
			//System.out.println(Math.log(1024) / Math.log(2));
			try (DataOutputStream out = new DataOutputStream(new FileOutputStream("data"))) {
		           out.writeInt(666);
		           out.writeUTF("Hello");
		       }
			DbBase db = DbBase.getJdbc(DbBase.SYBASE);
			JSONArray data = db.find(" EXEC mes_abse_sum3 '2020', 3, '2020/03/27', '2020/03/27', '0133363', '0133363', '', 'ZZZZZZZZ', '', 'ZZZZZZZZ'");
			System.out.println(data.size()+":"+data);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
