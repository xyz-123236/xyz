package cn.xyz.common.orm;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import cn.xyz.common.tools.ToolsProperties;

public class DbDruid extends DbBase{
	private static final Map<String, DataSource> map_ds = new HashMap<>();
	//public static final String[] DB_NAMES = {"oracle", "mysql", "sqlserver", "postgresql", "mongodb", "sybase", "hana"};
	private DbDruid(String db_name) {
		this.db_name = db_name;
	}
	//加载配置文件
	/*static {
		for (String dbName : DB_NAMES) {
			try {
				Properties properties = ToolsProperties.load("druid/druid_" + dbName + ".properties");
				DataSource ds = DruidDataSourceFactory.createDataSource(properties);
				map_ds.put(dbName, ds);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/
	//必须通过此方法创建对象
	public static DbDruid getInstance() {
		return new DbDruid(DbBase.DEFAULT_DB);
	}
	public static DbDruid getInstance(String dbName) {
		return new DbDruid(dbName);
	}
	public DataSource getDataSource() {
		DataSource ds = map_ds.get(this.db_name);
		if(ds == null) {
			try {
				Properties properties = ToolsProperties.load("druid/druid_"+ this.db_name +".properties");
				ds = DruidDataSourceFactory.createDataSource(properties);
				map_ds.put(this.db_name, ds);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ds;
	}
	
    public Connection getConnection() throws Exception {
		if(this.conn == null || this.conn.isClosed()) {
			this.conn = this.getDataSource().getConnection();
		}
		return this.conn;
	}
    
    public static void main(String[] args) {
    	DbDruid db;
		try {
			System.out.println(DbTool.tables_info);
			db = DbDruid.getInstance(DbBase.MYSQL);
			System.out.println(db.select("select * from t1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
