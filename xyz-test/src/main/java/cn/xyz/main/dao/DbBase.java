package cn.xyz.main.dao;

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

public class DbBase {
	Connection conn;
	PreparedStatement pstm;
	private static Properties properties = null;
	//加载配置文件
	static {
		InputStream is = null;
	    try {
	    	properties = new Properties();
		    is = DbBase.class.getClassLoader().getResourceAsStream("db.properties");
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
                if(is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
	}
	
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
		conn.setAutoCommit(false);
	}
	//回滚事务
	public void rollback() {
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//提交事务
	public void commit() throws Exception {
		conn.commit();
	}
	//原生查询
	public ResultSet executeQuery(String sql, Object... params) throws Exception{
		pstm = conn.prepareStatement(sql);
		this.fillPstm(pstm,params);
		return pstm.executeQuery();
	}
	//查询返回json
	public JSONArray executeQueryJson(String sql, Object... params) throws Exception{
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
			close(rs);
		}
	}
	
	//修改
	public Integer executeUpdate(String sql, Object... params) throws Exception{
		pstm = conn.prepareStatement(sql);
		this.fillPstm(pstm,params);
		return pstm.executeUpdate();
	}
	//插入返回主键
	public Integer insert(String sql, JSONObject params) throws Exception{
		ResultSet rs = null;
		try {
			sql = formatSql(sql,params);
			pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			this.fillPstm(pstm, params, sql);
			pstm.executeUpdate();
			rs = pstm.getGeneratedKeys();
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
			close(rs);
		}
	}
	//批量插入:用于excel
	public boolean insertBatch(String sql, Object[][] params) throws Exception{
		ResultSet rs = null;
		try {
			sql = formatSql(sql, new JSONObject());
			pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < params.length; i++) {
                this.fillPstm(pstm, params[i], sql);
                pstm.addBatch();
            }
			int[] result = pstm.executeBatch();
			for (int i = 0; i < result.length; i++) {
				if(result[i] != 1) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			throw e;
		}finally {
			close(rs);
		}
	}
	//批量插入
	public boolean insertBatch(String sql, JSONArray params) throws Exception{
		ResultSet rs = null;
		try {
			sql = formatSql(sql,params);
			pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < params.size(); i++) {
                this.fillPstm(pstm, params.getJSONObject(i), sql);
                pstm.addBatch();
            }
			int[] result = pstm.executeBatch();
			for (int i = 0; i < result.length; i++) {
				if(result[i] != 1) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			throw e;
		}finally {
			close(rs);
		}
	}
	//填补？
	public void fillPstm(PreparedStatement pstm, Object... params) throws SQLException {
		if(params != null) {
			for(int i = 0; i < params.length; i++){
				pstm.setObject(i+1, params[i]);
			}
		}
		printSql(pstm);
	}
	//用json填补？
	public void fillPstm(PreparedStatement pstm, JSONObject params, String sql) throws SQLException {
		if(params != null) {
			//字段数组
			String[] arr = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")")).split(",");
			//？数组：不全是？的情况
			String[] arr2 = sql.substring(sql.lastIndexOf("(") + 1, sql.lastIndexOf(")")).split(",");
			int jump = 0;
			for(int i = 0; i < arr.length; i++){
				if(!"?".equals(arr2[i].trim())) jump++;
				if(i+jump < arr.length) pstm.setObject(i+1, params.getString(arr[i+jump].trim()));
			}
		}
		printSql(pstm);
	}
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
		String sql = pstm.toString();
		System.out.println(sql.substring(sql.lastIndexOf(":")+1).trim());
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
			if(pstm != null){
				pstm.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//关闭连接
	public void closeConnection(){
		if (conn != null) {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.close();
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
			//System.out.println(db.insert("insert into sn_detail (batch_id,sn_detail) values (20,'ccc')"));
			//System.out.println(db.executeQueryJson("select * from sn_detail"));
			JSONArray data = new JSONArray();
			JSONObject a = new JSONObject();
			a.put("batch_id", 23);
			a.put("sn_detail", "xx");
			data.add(a);
			JSONObject b = new JSONObject();
			b.put("batch_id", 22);
			b.put("sn_detail", "yy");
			data.add(b);
			//String str = "insert into sn_detail (batch_id,sn_detail) values";
			//System.out.println(str.replaceAll("values", ""));
			//System.out.println(db.insert("insert into sn_detail (batch_id,sn_detail) values (24,?)",a));
			//System.out.println(db.insert("sn_detail",a));
			//System.out.println(db.insert("insert into sn_detail (batch_id,sn_detail) values",a));
			//System.out.println(db.insertBatch("insert into sn_detail (batch_id,sn_detail) values",data));
			//System.out.println(db.insertBatch("sn_detail",data));
			//System.out.println(db.insertBatch("insert into sn_detail (batch_id,sn_detail) values",data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
