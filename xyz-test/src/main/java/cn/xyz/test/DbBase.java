package cn.xyz.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class DbBase {
	protected String db_name;
	protected Connection conn;
	protected PreparedStatement pstm;
	public static final String ORACLE = "oracle";
	public static final String MYSQL = "mysql";
	public static final String SQLSERVER = "sqlserver";
	public static final String POSTGRESQL = "postgresql";
	public static final String MONGODB = "mongodb";
	public static final String SYBASE = "sybase";
	public static final String HANA = "hana";

	private static final String SQL = "SELECT * FROM ";// 数据库操作

	public abstract Connection getConnection() throws Exception;

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
			return data.getInteger("count");
		}
		return null;
	}

	// 填补？
	public PreparedStatement fillPstm(String sql, Object[] params) throws Exception {
		this.pstm = this.getConnection().prepareStatement(sql);
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				this.pstm.setObject(i + 1, params[i]);
			}
		}
		return this.pstm;
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
