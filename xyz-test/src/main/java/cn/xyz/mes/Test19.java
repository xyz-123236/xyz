package cn.xyz.mes;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;

import cn.xyz.common.orm.DbBase;
import com.alibaba.fastjson.JSONObject;


public class Test19 {

	public static void main(String[] args) {
		try {
			
			DbBase db = DbBase.getJdbc(DbBase.SYBASE);
			db.execute(" EXEC mes_abse_sum2 '2021', 1, '2021/01/02', '2021/01/02', '9931834', '9931834', '', 'ZZZZZZZZ', '', 'ZZZZZZZZ'");
			//JSONArray data = db.select(" EXEC mes_abse_sum3 '2021', 1, '2021/01/02', '2021/01/02', '9931834', '9931834', '', 'ZZZZZZZZ', '', 'ZZZZZZZZ'");
			//JSONArray data = db.select(" EXEC mes_abse_sum3 '2020', 12, '2020/12/01', '2020/12/31', '9932751', '9932751', '', 'ZZZZZZZZ', '', 'ZZZZZZZZ'",true,1);

			//System.out.println(data.size()+":"+data);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
