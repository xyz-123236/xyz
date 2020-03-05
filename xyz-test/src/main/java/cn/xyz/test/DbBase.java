package cn.xyz.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DbBase {
	String dbName;
	Connection conn;
	PreparedStatement pstm;
	public static final String MYSQL = "mysql";
	public static final String SYBASE = "sybase";
	//private static Properties properties = null;
	//private static final String DB_FILE_NAME = "db.properties";
	
	//加载配置文件
	/*static {
	    try(InputStream is = DbBase.class.getClassLoader().getResourceAsStream(DB_FILE_NAME)) {
	    	properties = new Properties();
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	private DbBase(String dbName) {
		this.dbName = dbName;
	}
	//必须通过此方法创建对象
	public static DbBase getInstance() throws Exception {
		return new DbBase("mysql");
	}
	public static DbBase getInstance(String dbName) throws Exception {
		return new DbBase(dbName);
	}
	//获取连接
	private Connection getConnection() throws Exception {
		if(this.conn == null || this.conn.isClosed()) {
			/*Class.forName(properties.getProperty(dbName+"_driver"));
			this.conn = DriverManager.getConnection(properties.getProperty(dbName+"_url"), properties.getProperty(dbName+"_user"), properties.getProperty(dbName+"_password"));*/
			this.conn = DbDruid.getConnection(this.dbName);
		}
		return this.conn;
	}
	//开启事务
	public DbBase startTransaction() throws Exception {
		this.getConnection().setAutoCommit(false);
		return this;
	}
	//回滚事务
	public void rollback() {
		try {
			this.conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			this.closeConnection();
		}
	}
	//提交事务
	public void commit() throws Exception {
		try {
			this.conn.commit();
		} catch (Exception e) {
			throw e;
		}finally {
			this.closeConnection();
		}
	}
	//原生
	public boolean execute(String sql, Object... params) throws Exception{
		try {
			this.fillPstm(sql, params).execute();
			return true;
		} catch (Exception e) {
			throw e;
		}finally {
			this.close();
		}
	}
	//原生查询
	/*private ResultSet executeQuery(String sql, Object... params) throws Exception{
		return this.fillPstm(sql, params).executeQuery();
	}*/
	//原生修改
	public Integer executeUpdate(String sql, Object... params) throws Exception{
		try {
			return this.fillPstm(sql, params).executeUpdate();
		} catch (Exception e) {
			throw e;
		}finally {
			this.close();
		}
	}
	//查询返回json
	public JSONArray find(String sql, Object... params) throws Exception{
		ResultSet rs = null;
		try {
			rs = this.fillPstm(sql, params).executeQuery();
			JSONArray data = new JSONArray();
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				ResultSetMetaData md = rs.getMetaData();//获取键名
				int columnCount = md.getColumnCount();//获取行的数量
				for (int i = 1; i <= columnCount; i++) {
					obj.put(md.getColumnLabel(i), rs.getObject(i));//别名
					//obj.put(md.getColumnName(i), rs.getObject(i));//数据库原字段名
				}
				data.add(obj);
			}
			return data;
		} catch (Exception e) {
			throw e;
		}finally {
			this.close(rs);
		}
	}
	public JSONObject get(String sql, Object... params) throws Exception{
		JSONArray data = find(sql, params);
		if(data != null && !data.isEmpty()) {
			return data.getJSONObject(0);
		}
		return null;
	}
	public Integer count(String sql, Object... params) throws Exception {
		JSONObject data = get(sql, params);
		if(data != null && !data.isEmpty()) {
			return data.getInteger("count");
		}
		return null;
	}
	//填补？
	public PreparedStatement fillPstm(String sql, Object[] params) throws Exception {
		this.pstm = this.getConnection().prepareStatement(sql);
		if(params != null) {
			for(int i = 0; i < params.length; i++){
				this.pstm.setObject(i+1, params[i]);
			}
		}
		return this.pstm;
	}
	//释放资源
	private void closeResource(ResultSet rs){
		try {
			if(rs != null){
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(this.pstm != null){
				this.pstm.close();
				this.pstm = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//关闭连接
	private void closeConnection(){
		try {
			if (this.conn != null) {
				this.conn.close();
				this.conn = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//关闭连接和资源
	private void close(){
		close(null);
	}
	private void close(ResultSet rs){
		this.closeResource(rs);
		try {
			if(this.conn.getAutoCommit()) {
				this.closeConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection();
		}
	}
	public static void main(String[] args) {
		DbBase db = null;
		try {
			db = DbBase.getInstance(DbBase.MYSQL);
			System.out.println(db.find("select * from user"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
