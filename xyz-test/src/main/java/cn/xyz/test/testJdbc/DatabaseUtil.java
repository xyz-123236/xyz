package cn.xyz.test.testJdbc;

import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;

import cn.xyz.orm.db.DbBase;
import cn.xyz.orm.db.DbJdbc;

public class DatabaseUtil {
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
		    mysql = DbBase.getJdbc();
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
