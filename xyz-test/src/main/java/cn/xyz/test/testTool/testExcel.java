package cn.xyz.test.testTool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.orm.DbBase;
import cn.xyz.common.pojo.Excel;
import cn.xyz.common.tools.ToolsExcel;

public class testExcel {
	public static void main(String[] args) {
		try {
			DbBase db = DbBase.getJdbc();
			JSONArray data = db.select("select * from sn_detail");
			Excel excel = new Excel();
			//excel.setFile_name("test1");
			//excel.setFile_path("");
			//excel.setSheet_name("");
			//excel.setTitle("");
			excel.setData(data);
			excel.setHeads(new String[] {"用户名","姓名","密码"});
			excel.setFileds(new String[] {"id","batch_id","sn_detail"});
			excel.setLocks(new Integer[] {2,1});
			excel.setFormats(new Integer[] {12,12,12});
			excel.setWidths(new Integer[] {4000,4000,4000});
			ToolsExcel.create(excel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
