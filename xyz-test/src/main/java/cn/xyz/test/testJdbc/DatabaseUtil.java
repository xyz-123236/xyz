package cn.xyz.test.testJdbc;

import com.alibaba.fastjson.JSONArray;

import cn.xyz.orm.db.DbBase;
import cn.xyz.orm.db.DbJdbc;

public class DatabaseUtil {
    public static void main(String[] args) {
    	DbBase mysql = null;
        try {
        	mysql = DbJdbc.getInstance(DbBase.MYSQL);
        	JSONArray tables = mysql.getTables("fic_portal","fic_portal","attached",null);
			System.out.println("tableNames:" + tables);
		    System.out.println("ColumnTypes:" + mysql.getFiledType(tables.getJSONObject(0).getString("TABLE_NAME")));
		    System.out.println("ColumnComments:" + mysql.getTableFiled(tables.getJSONObject(0).getString("TABLE_NAME")));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
