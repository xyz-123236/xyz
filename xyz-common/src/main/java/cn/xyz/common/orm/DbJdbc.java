package cn.xyz.common.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import cn.xyz.common.tools.ToolsProperties;

public class DbJdbc extends DbBase{
	private static Properties properties = null;
	private static final String DB_FILE_NAME = "db.properties";
	//加载配置文件
	static {
		try {
			properties = ToolsProperties.load(DB_FILE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	DbJdbc(String db_name) {
		this.db_name = db_name;
	}
	//必须通过此方法创建对象
	public static DbJdbc getInstance() {
		return new DbJdbc(DbBase.DEFAULT_DB);
	}
	public static DbJdbc getInstance(String dbName) {
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
		DbJdbc db;
		try {
			db = DbJdbc.getInstance(DbBase.MYSQL);
			System.out.println(db.select("select * from t1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
