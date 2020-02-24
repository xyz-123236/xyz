package cn.xyz.jdbc.dao;

import java.io.IOException;
import java.io.InputStream;
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
	private static final String FILE_NAME = "db.properties";
	//加载配置文件
	static {
	    try(InputStream is = DbBase.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
	    	properties = new Properties();
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//必须通过此构造方法创建对象
	public DbBase(String databaseName) {
		this.conn = openConnection(databaseName);
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
	//原生查询
	public ResultSet executeQuery(String sql, Object... params) throws Exception{
		this.pstm = this.conn.prepareStatement(sql);
		this.fillPstm(this.pstm,params);
		return this.pstm.executeQuery();
	}
	//查询返回json
	public JSONArray find(String sql, Object... params) throws Exception{
		ResultSet rs = null;
		try {
			rs = executeQuery(sql, params);
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
			closeResource(rs);
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
		JSONArray data = find(sql, params);
		if(Tools.isEmpty(data)) {
			return null;
		}
		return data.getJSONObject(0).getInteger("count");
	}
	//修改
	public Integer executeUpdate(String sql, Object... params) throws Exception{
		this.pstm = this.conn.prepareStatement(sql);
		this.fillPstm(this.pstm,params);
		return this.pstm.executeUpdate();
	}
	//插入返回主键
	public Integer insert(String sql, JSONObject params) throws Exception{
		ResultSet rs = null;
		try {
			//sql = formatSql(sql,params);
			this.pstm = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			this.fillPstm(this.pstm, params, sql);
			this.pstm.executeUpdate();
			rs = this.pstm.getGeneratedKeys();
			Integer id = null;
			if (rs.next()) {  
				id = rs.getInt(1);  
		    }  else {  
		        throw new Exception("返回主键失败"); 
		    }
			return id;
		} catch (Exception e) {
			throw e;
		}finally {
			closeResource(rs);
		}
	}
	//批量插入:用于excel
	public boolean insertBatch(String sql, Object[][] params) throws Exception{
		ResultSet rs = null;
		try {
			sql = formatSql(sql, new JSONObject());
			this.pstm = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < params.length; i++) {
                this.fillPstm(this.pstm, params[i], sql);
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
	}
	//批量插入
	public boolean insertBatch(String sql, JSONArray params) throws Exception{
		ResultSet rs = null;
		try {
			sql = formatSql(sql,params);
			this.pstm = this.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < params.size(); i++) {
                this.fillPstm(this.pstm, params.getJSONObject(i), sql);
                this.pstm.addBatch();
            }
			System.out.println("===="+this.pstm.toString());
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
	}
	//填补？
	public void fillPstm(PreparedStatement pstm, Object... params) throws SQLException {
		if(params != null) {
			for(int i = 0; i < params.length; i++){
				this.pstm.setObject(i+1, params[i]);
			}
		}
		printSql(this.pstm);
	}
	//用json填补？
	/*public void fillPstm(PreparedStatement this.pstm, String sql, JSONObject params) throws SQLException {
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
		printSql(this.pstm);
	}*/
	//重载
	public String formatSql(String sql,JSONArray params) throws Exception {
		JSONObject obj = null; 
		if(params != null) obj = params.getJSONObject(0);
		return formatSql(sql, obj);
	}
	//处理sql
	public String formatSql(String sql,JSONObject params) throws Exception {
		if(sql.indexOf("(") < 0) {//没有括号
			if(params != null) {
				String key = "";
				String value = "";
				for(String str:params.keySet()){
					key += str + ",";
					value += "?" + ",";
				}
				return "insert into "+sql+" ("+key.substring(0,key.lastIndexOf(","))+ ") values ("+value.substring(0,value.lastIndexOf(","))+ ")";
			}else {
				throw new Exception("SQL不正确");
			}
		}else if(sql.indexOf("(") == sql.lastIndexOf("(")) {
			if(params != null) {
				String[] arr = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")")).split(",");
				String value = "";
				for (int i = 0; i < arr.length; i++) {
					value += "?" + ",";
				}
				sql = sql.replaceAll("values", "");
				return sql + " values ("+value.substring(0,value.lastIndexOf(","))+ ")";
			}else {
				throw new Exception("SQL不正确");
			}
		}
		return sql;
	}
	//输出sql
	public void printSql(PreparedStatement pstm) {
		String sql = this.pstm.toString();
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +": "+ sql.substring(sql.lastIndexOf(":")+1).trim().replaceAll(" +"," "));
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
	
	public static void main(String[] args) {
		DbBase db = new DbBase("mysql");
		try {
			System.out.println(db.find("select * from t1"));
			//System.out.println(db.insert("insert into sn_detail (batch_id,sn_detail) values (20,'ccc')"));
			//System.out.println(db.executeQueryJson("select * from sn_detail"));
			//System.out.println(db.executeUpdate("alter table t1 add code varchar(10) after id"));
			JSONArray data = new JSONArray();
			JSONObject a = new JSONObject();
			a.put("code", 23);
			a.put("name", "xx");
			data.add(a);
			JSONObject b = new JSONObject();
			b.put("code", 22);
			b.put("name", "yy");
			data.add(b);
			//String str = "insert into sn_detail (batch_id,sn_detail) values";
			//System.out.println(str.replaceAll("values", ""));
			//System.out.println(db.insert("insert into sn_detail (batch_id,sn_detail) values (24,?)",a));
			//System.out.println(db.insert("sn_detail",a));
			//System.out.println(db.insert("insert into sn_detail (batch_id,sn_detail) values",a));
			//System.out.println(db.insertBatch("insert into sn_detail (batch_id,sn_detail) values",data));
			System.out.println(db.insertBatch("t1",data));
			//System.out.println(db.insertBatch("insert into sn_detail (batch_id,sn_detail) values",data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
