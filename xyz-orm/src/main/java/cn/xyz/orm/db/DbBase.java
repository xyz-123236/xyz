package cn.xyz.orm.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.tools.ToolsDate;

public abstract class DbBase {
	protected String db_name;
	protected Connection conn;
	protected PreparedStatement pstm;
	public static final String DEFAULT_DB = "mysql";
	public static final String ORACLE = "oracle";
	public static final String MYSQL = "mysql";
	public static final String SQLSERVER = "sqlserver";
	public static final String POSTGRESQL = "postgresql";
	public static final String MONGODB = "mongodb";
	public static final String SYBASE = "sybase";
	public static final String HANA = "hana";
	
	private static final String SQL = "SELECT * FROM ";// 数据库操作

	public abstract Connection getConnection() throws Exception;
	
	public static DbBase getJdbc() throws Exception {
		return getJdbc(DbBase.DEFAULT_DB);
	}
	public static DbBase getJdbc(String dbName) throws Exception {
		return DbJdbc.getInstance(dbName);
	}
	public static DbBase getDruid() throws Exception {
		return getDruid(DbBase.DEFAULT_DB);
	}
	public static DbBase getDruid(String dbName) throws Exception {
		return DbDruid.getInstance(dbName);
	}
	// 开启事务
	public DbBase startTransaction() throws Exception {
		this.getConnection().setAutoCommit(false);
		return this;
	}

	// 回滚事务
	public void rollback() {
		try {
			this.conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}
	}

	// 提交事务
	public void commit() throws Exception {
		try {
			this.conn.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			this.closeConnection();
		}
	}

	// 原生
	public boolean execute(String sql, Object... params) throws Exception {
		try {
			this.fillPstm(sql, params).execute();
			return true;
		} catch (Exception e) {
			throw e;
		} finally {
			this.close();
		}
	}

	// 原生查询
	/*
	 * private ResultSet executeQuery(String sql, Object... params) throws
	 * Exception{ return this.fillPstm(sql, params).executeQuery(); }
	 */
	// 原生修改
	public Integer executeUpdate(String sql, Object... params) throws Exception {
		try {
			return this.fillPstm(sql, params).executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			this.close();
		}
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
	// 查询返回json
	public JSONArray find(String sql, Object... params) throws Exception {
		ResultSet rs = null;
		try {
			rs = this.fillPstm(sql, params).executeQuery();
			return rsToJson(rs);
		} catch (Exception e) {
			throw e;
		} finally {
			this.close(rs);
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
	public JSONObject get(String sql, Object... params) throws Exception {
		JSONArray data = find(sql, params);
		if (data != null && !data.isEmpty()) {
			return data.getJSONObject(0);
		}
		return null;
	}

	public Integer count(String sql, Object... params) throws Exception {
		JSONObject data = get(sql, params);
		if (data != null && !data.isEmpty()) {
			if(data.size() == 1) {
				return data.getInteger("count");
			}else {
				return data.size();
			}
		}
		return null;
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

	// 释放资源
	private void closeResource(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (this.pstm != null) {
				this.pstm.close();
				this.pstm = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 关闭连接
	private void closeConnection() {
		try {
			if (this.conn != null) {
				this.conn.close();
				this.conn = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 关闭连接和资源
	private void close() {
		close(null);
	}

	private void close(ResultSet rs) {
		this.closeResource(rs);
		try {
			if (this.conn.getAutoCommit()) {
				this.closeConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection();
		}
	}

	/**
	 * 获取数据库下的所有表名
	 * @param catalog 表目录（可能为空）conn.getCatalog()
	 * @param schemaPattern 表架构（可能为空）conn.getCatalog()
	 * @param tableNamePattern 表名 "%"
	 * @param types :new String[] { "TABLE" }:"TABLE","VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY","LOCAL TEMPORARY", "ALIAS","SYNONYM"
	 * @return
	 * @throws Exception
	 */
	public JSONArray getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws Exception {
		ResultSet rs = null;
		try {
			// 获取数据库的元数据
			DatabaseMetaData db = this.getConnection().getMetaData();
			// 从元数据中获取到所有的表名
			rs = db.getTables(catalog, schemaPattern, tableNamePattern, types);
			return rsToJson(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			this.close(rs);
		}
	}

	public JSONObject getFiledType(String tableName) throws Exception {
		try (PreparedStatement pStemt = this.getConnection().prepareStatement(SQL + tableName);) {
			// 结果集元数据
			ResultSetMetaData rsmd = pStemt.getMetaData();
			JSONObject obj = new JSONObject();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				obj.put(rsmd.getColumnName(i + 1), rsmd.getColumnTypeName(i + 1));
			}
			return obj;
		} catch (SQLException e) {
			throw e;
		} finally {
			close();
		}
	}

	public JSONArray getTableFiled(String tableName) throws Exception {
		ResultSet rs = null;
		try{
			PreparedStatement pStemt = this.getConnection().prepareStatement(SQL + tableName);
			rs = pStemt.executeQuery("show full columns from " + tableName);
			return rsToJson(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			close(rs);
		}
	}
}
