package cn.xyz.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DbDruid {
	private static Map<String, DataSource> map_ds = new HashMap<>();
	
	public static DataSource getDataSource(String dbName) {
		DataSource ds = map_ds.get(dbName);
		if(ds == null) {
			try(InputStream is = DbDruid.class.getClassLoader().getResourceAsStream(dbName + "_druid.properties")) {
				Properties properties = new Properties();
				properties.load(is);
				ds = DruidDataSourceFactory.createDataSource(properties);
				map_ds.put(dbName, ds);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ds;
	}
	
    public static Connection getConnection(String dbName) throws SQLException {
        return getDataSource(dbName).getConnection();
 
    }
    
}
