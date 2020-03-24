package cn.xyz.jdbc.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.tools.Tools;
import cn.xyz.common.tools.ToolsDate;

public class DbBase {
	Connection conn;
	PreparedStatement pstm;
	private static Properties properties = null;
	private static final String DB_FILE_NAME = "db.properties";
	//加载配置文件
	static {
	    try(InputStream is = DbBase.class.getClassLoader().getResourceAsStream(DB_FILE_NAME)) {
	    	properties = new Properties();
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private DbBase(String databaseName) {
		this.conn = openConnection(databaseName);
	}
	//必须通过此方法创建对象
	public static DbBase getInstance(String databaseName) {
		return new DbBase(databaseName);
	}
	//获取连接
	public static Connection openConnection(String databaseName) {
		Connection connection = null;
		try {
			Class.forName(properties.getProperty(databaseName+"_driver"));
			connection = DriverManager.getConnection(properties.getProperty(databaseName+"_url"), properties.getProperty(databaseName+"_user"), properties.getProperty(databaseName+"_password"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	//开启事务
	public void startTransaction() throws Exception {
		this.conn.setAutoCommit(false);
	}
	//回滚事务
	public void rollback() {
		try {
			this.conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//提交事务
	public void commit() throws Exception {
		this.conn.commit();
	}
	//原生
	public boolean execute(String sql, Object... params) throws Exception{
		this.fillPstm(sql, params).execute();
		return true;
	}
	//原生查询
	public ResultSet executeQuery(String sql, Object... params) throws Exception{
		return this.fillPstm(sql, params).executeQuery();
	}
	//原生修改
	public Integer executeUpdate(String sql, Object... params) throws Exception{
		return this.fillPstm(sql, params).executeUpdate();
	}
	//查询返回json
	public JSONArray find(String sql, Object... params) throws Exception{
		ResultSet rs = null;
		try {
			rs = this.executeQuery(sql, params);
			JSONArray data = new JSONArray();
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				ResultSetMetaData md = rs.getMetaData();//获取键名
				int columnCount = md.getColumnCount();//获取行的数量
				for (int i = 1; i <= columnCount; i++) {
					obj.put(md.getColumnName(i), rs.getObject(i));//获取键名及值
				}
				data.add(obj);
			}
			return data;
		} catch (Exception e) {
			throw e;
		}finally {
			closeResource(rs);//不能关闭连接，db事务可能还要其他操作
		}
	}
	public JSONObject get(String sql, Object... params) throws Exception{
		JSONArray data = find(sql, params);
		if(Tools.isEmpty(data)) {
			return null;
		}
		return data.getJSONObject(0);
	}
	public Integer count(String sql, Object... params) throws Exception {
		JSONObject data = get(sql, params);
		if(Tools.isEmpty(data)) {
			return null;
		}
		return data.getInteger("count");
	}
	
	/**
	 * 
	 * @param sql
	 * @param params
	 * @return 返回主键id
	 * @throws Exception
	 */
	public JSONArray insert(String sql, Object... params) throws Exception{
		ResultSet rs = null;
		try {
			this.fillPstm(sql, params).executeUpdate();
			rs = this.pstm.getGeneratedKeys();
			/*Integer id = null;
			if (rs.next()) {  
				id = rs.getInt(1);  
		    }  else {
		        throw new Exception("返回主键失败"); 
		    }
			return id;*/
			return rsToJson(rs);
		} catch (Exception e) {
			throw e;
		}finally {
			closeResource(rs);
		}
	}
	public static JSONArray rsToJson(ResultSet rs) throws SQLException {
		JSONArray data = new JSONArray();
		while (rs.next()) {
			JSONObject obj = new JSONObject();
			ResultSetMetaData md = rs.getMetaData();// 获取键名
			int columnCount = md.getColumnCount();// 获取行的数量
			for (int i = 1; i <= columnCount; i++) {
				obj.put(md.getColumnLabel(i), rs.getObject(i));// 别名
				// obj.put(md.getColumnName(i), rs.getObject(i));//数据库原字段名
			}
			data.add(obj);
		}
		return data;
	}
	//批量插入
	public boolean insertBatch(String sql, JSONArray params) throws Exception{
		ResultSet rs = null;
		try {
			int[] result = this.fillPstm(sql, params).executeBatch();
			for (int i = 0; i < result.length; i++) {
				if(result[i] != 1) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			throw e;
		}finally {
			closeResource(rs);
		}
	}
	//批量插入:用于excel
	/*public boolean insertBatch(String sql, Object[][] params) throws Exception{
		ResultSet rs = null;
		try {
			sql = formatSql(sql, new JSONObject());
			this.pstm = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < params.length; i++) {
                this.fillPstm(this.pstm, params[i]);
                this.pstm.addBatch();
            }
			int[] result = this.pstm.executeBatch();
			for (int i = 0; i < result.length; i++) {
				if(result[i] != 1) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			throw e;
		}finally {
			closeResource(rs);
		}
	}*/
	
	//填补？
	public PreparedStatement fillPstm(String sql, Object[] params) throws SQLException {
		this.pstm = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		if(params != null) {
			for(int i = 0; i < params.length; i++){
				this.pstm.setObject(i+1, params[i]);
			}
		}
		printSql();
		return this.pstm;
	}
	//this.pstm.addBatch(sql)支持添加不同的SQL语句，所以，可以不用先拼接插入或修改的key集合
	public PreparedStatement fillPstm(String sql, JSONArray params) throws SQLException {
		this.pstm = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		this.pstm.clearBatch();
		for (int i = 0; i < params.size(); i++) {
            this.fillPstm(sql, params.getJSONObject(i));
            this.pstm.addBatch();
        }
		//printSql();
		return this.pstm;
	}
	//用json填补？
	public PreparedStatement fillPstm(String sql, JSONObject params) throws SQLException {
		if(params != null) {
			//字段数组
			String[] arr = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")")).split(",");
			//？数组：不全是？的情况
			String[] arr2 = sql.substring(sql.lastIndexOf("(") + 1, sql.lastIndexOf(")")).split(",");
			int jump = 0;
			for(int i = 0; i < arr.length; i++){
				if(!"?".equals(arr2[i].trim())) jump++;
				if(i+jump < arr.length) this.pstm.setObject(i+1, params.getString(arr[i+jump].trim()));
			}
		}
		printSql();
		return this.pstm;
	}
	
	//输出sql
	public void printSql() {
		String sql = this.pstm.toString();
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +" DbBase: "+ sql.substring(sql.lastIndexOf(":")+1).trim().replaceAll(" +"," "));
	}
	//释放资源
	public void closeResource(ResultSet rs){
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
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//关闭连接
	public void closeConnection(){
		if (this.conn != null) {
			try {
				this.conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				this.conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	//关闭连接和资源
	public void close(ResultSet rs){
		closeResource(rs);
		closeConnection();
	}
	//close是否关闭,params返回的序号
	public CallableStatement call(String sql,boolean close,Integer... params) {
		CallableStatement cstm = null;
		try {
			cstm = this.conn.prepareCall(sql);
			cstm.execute();
			JSONObject obj = new JSONObject();
			for (int i = 0; i < params.length; i++) {
				
				obj.put(params[i]+"", cstm.getObject(params[i]));
			}
			//ResultSet rs = (ResultSet)cstm.getObject(2);
			//return obj;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(close) {
			closeCall(cstm);
			closeConnection();
			return null;
		}else {
			return cstm;
		}
	}
	public static void closeCall(CallableStatement cstm) {
		try {
			if(cstm != null){
				cstm.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
