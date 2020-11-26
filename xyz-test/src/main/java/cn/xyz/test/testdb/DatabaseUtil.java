package cn.xyz.test.testdb;

import cn.xyz.common.orm.DbBase;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DatabaseUtil {
	public static JSONObject tables_info = new JSONObject();
	static {
		DbBase db = DbBase.getDruid();
		try {
			JSONArray tableNames = db.getTables(null,"xyz","%",null);
			for(int i = 0; i < tableNames.size(); i++){
				String table = tableNames.getJSONObject(i).getString("TABLE_NAME");
				JSONObject obj = new JSONObject();
				obj.put("filedType", db.getFiledType(table));
				obj.put("primaryKey", db.getPrimaryKey("xyz", table));
				tables_info.put(table, obj);
			}
			System.out.println(tables_info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//初始化数据字典，把表字段/类型添加到缓存
	};
	static {
		DbBase db = DbBase.getDruid();
		try {
			JSONArray tableNames = db.getTables(null,"xyz","%",null);
			for(int i = 0; i < tableNames.size(); i++){
				String table = tableNames.getJSONObject(i).getString("TABLE_NAME");
				JSONObject obj = new JSONObject();
				obj.put("filedType", db.getFiledType(table));
				obj.put("primaryKey", db.getPrimaryKey("xyz", table));
				tables_info.put(table, obj);
			}
			System.out.println(tables_info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//初始化数据字典，把表字段/类型添加到缓存
	}
    public static void main(String[] args) {
    	DbBase hana = null;
    	DbBase mysql = null;
        try {
        	String url = "jdbc:mysql://localhost:3306/xyz?useUnicode=true&characterEncoding=latin1&autoReconnect=true&failOverReadOnly=false&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true";
        	String database = url.split("\\/")[3].split("\\?")[0];
        	hana = DbBase.getJdbc(DbBase.HANA);
        	System.out.println("tableNames:" + hana.getCatalogs());
        	System.out.println("tableNames:" + hana.getSchemas());
			System.out.println("tableNames:" + hana.getTables(null,"MES",null, new String[]{"TABLE"}));
		    System.out.println("ColumnTypes:" + hana.getFiledType("PRODUCENUM"));
		    System.out.println("ColumnComments:" + hana.getTableFiled("MES","LOG"));
		    System.out.println("ColumnComments:" + hana.getPrimaryKey("MES","LOG"));
		    mysql = DbBase.getDruid();
		    System.out.println("tableNames:" + mysql.getCatalogs());
        	System.out.println("tableNames:" + mysql.getSchemas());
			System.out.println("tableNames:" + mysql.getTables(null,"xyz","%",null));
		    System.out.println("ColumnTypes:" + mysql.getFiledType("t1"));
		    System.out.println("ColumnComments:" + mysql.getTableFiled("xyz","t1"));
		    System.out.println("ColumnComments:" + mysql.getPrimaryKey("xyz","t1"));
		    mysql.getDataBaseInfo();
		    hana.getDataBaseInfo();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
