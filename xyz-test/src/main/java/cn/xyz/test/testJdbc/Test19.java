package cn.xyz.test.testJdbc;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;

import cn.xyz.common.orm.DbBase;


public class Test19 {

	public static void main(String[] args) {
		try {
			
			DbBase db = DbBase.getJdbc(DbBase.SYBASE);
			JSONArray data = db.select(" EXEC mes_abse_sum3 '2020', 3, '2020/05/20', '2020/05/20', '0108427', '0108427', '', 'ZZZZZZZZ', '', 'ZZZZZZZZ'");
			System.out.println(data.size()+":"+data);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
