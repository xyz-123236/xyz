package cn.xyz.orm.old;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbJdbc extends DbBase{
	private static Properties properties = null;
	private static final String DB_FILE_NAME = "db.properties";
	//加载配置文件
	static {
	    try(InputStream is = DbJdbc.class.getClassLoader().getResourceAsStream(DB_FILE_NAME)) {
	    	properties = new Properties();
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private DbJdbc(String db_name) {
		this.db_name = db_name;
	}
	//必须通过此方法创建对象
	public static DbJdbc getInstance() throws Exception {
		return new DbJdbc(DbBase.DEFAULT_DB);
	}
	public static DbJdbc getInstance(String dbName) throws Exception {
		return new DbJdbc(dbName);
	}
	public Connection getConnection() throws Exception {
		if(this.conn == null || this.conn.isClosed()) {
			Class.forName(properties.getProperty(this.db_name+"_driver"));
			this.conn = DriverManager.getConnection(properties.getProperty(this.db_name+"_url"), properties.getProperty(this.db_name+"_user"), properties.getProperty(this.db_name+"_password"));
		}
		return this.conn;
	}
	public static void main(String[] args) {
		DbJdbc db = null;
		try {
			db = DbJdbc.getInstance(DbBase.MYSQL);
			System.out.println(db.find("select passWord,userName from user"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
