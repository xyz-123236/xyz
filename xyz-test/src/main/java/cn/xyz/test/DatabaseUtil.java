package cn.xyz.test;

import com.alibaba.fastjson.JSONArray;

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
