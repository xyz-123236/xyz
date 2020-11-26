package cn.xyz.common.orm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import cn.xyz.common.exception.CustomException;
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

	public static DbBase getJdbc() {
		return getJdbc(DbBase.DEFAULT_DB);
	}
	public static DbBase getJdbc(String dbName) {
		return DbJdbc.getInstance(dbName);
	}
	public static DbBase getDruid() {
		return getDruid(DbBase.DEFAULT_DB);
	}
	public static DbBase getDruid(String dbName) {
		return DbDruid.getInstance(dbName);
	}
	// 开启事务
	public DbBase startTransaction() throws Exception {
		this.getConnection().setAutoCommit(false);//false开启事务
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
		} finally {
			this.closeConnection();
		}
	}

	// 原生
	public boolean execute(String sql, Object... params) throws Exception {
		try {
			return this.fillPstm(sql, params).execute();
		} finally {
			this.close();
		}
	}

	// 原生查询
	
	public ResultSet executeQuery(String sql, Object... params) throws Exception{ 
		return this.fillPstm(sql, params).executeQuery(); 
	}
	 
	// 原生修改
	public int executeUpdate(String sql, Object... params) throws Exception {
		try {
			return this.fillPstm(sql, params).executeUpdate();
		} finally {
			this.close();
		}
	}
	//close是否关闭,params返回的序号
	public JSONArray call(String sql,boolean close,Integer... params) throws Exception {
		CallableStatement cstm = null;
		ResultSet rs = null;
		try {
			cstm = this.getConnection().prepareCall(sql);
			cstm.execute();
			System.out.println(cstm.execute());
			System.out.println(cstm.getResultSet());
			rs = cstm.getResultSet();
			//ResultSet rs = cstm.executeQuery();
			/*for (Integer param : params) {
				obj.put(param + "", cstm.getObject(param));
			}*/
			JSONArray data = rsToArray(rs);
			return data;
		} finally {
			close(rs);
			closeCall(cstm);
			closeConnection();
		}
		/*if(close) {
			closeCall(cstm);
			closeConnection();
			return null;
		}else {
			return cstm;
		}*/
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
	public JSONArray select(String sql, Object... params) throws Exception {
		ResultSet rs = null;
		try {
			rs = this.executeQuery(sql, params);
			return rsToArray(rs);
		} finally {
			this.close(rs);
		}
	}
	public JSONObject selectOne(String sql, Object... params) throws Exception {
		ResultSet rs = null;
		try {
			rs = this.executeQuery(sql, params);
			if (rs.next()) {
				return rsToJson(rs);
			}
			return null;
		} finally {
			this.close(rs);
		}
	}
	

	public Integer count(String sql, Object... params) throws Exception {
		JSONObject data = selectOne(sql, params);
		if (data != null && !data.isEmpty()) {
			if(data.size() == 1) {
				return data.getInteger("count");
			}else {
				return data.size();
			}
		}
		return null;
	}

	public Integer insert(String sql, Object... params) throws Exception{
		ResultSet rs = null;
		try {
			int row = this.fillPstm(sql, params).executeUpdate();
			if(row != 1){
				throw new CustomException("新增失败");
			}
			rs = this.pstm.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}
			return null;
		} finally {
			closeResource(rs);
		}
	}
	
	//批量插入
	public boolean insertBatch(String sql, JSONArray params) throws Exception{
		try {
			int[] result = this.fillPstm(sql, params).executeBatch();
			for (int value : result) {
				if (value != 1) {
					return false;
				}
			}
			return true;
		} finally {
			close();
		}
	}
	public boolean insertBatch(List<String> sqls) throws Exception{
		try {
			if(sqls != null) {
				this.pstm = this.getConnection().prepareStatement(sqls.get(0), Statement.RETURN_GENERATED_KEYS);
				for (String sql : sqls) {
					this.pstm.addBatch(sql);
				}
				int[] result = this.pstm.executeBatch();
				for (int value : result) {
					if (value != 1) {
						return false;
					}
				}
				return true;
			}
			return false;
		} finally {
			close();
		}
	}
	//批量插入:用于excel
	/*public boolean insertBatch(String sql, Object[][] params) throws Exception{
		ResultSet rs = null;
		try {
			sql = formatSql(sql, new JSONObject());
			this.pstm = this.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
	public PreparedStatement fillPstm(String sql, Object[] params) throws Exception {
		this.pstm = this.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		if(params != null) {
			for(int i = 0; i < params.length; i++){
				this.pstm.setObject(i+1, params[i]);
			}
		}
		printSql();
		return this.pstm;
	}
	//this.pstm.addBatch(sql)支持添加不同的SQL语句，所以，可以不用先拼接插入或修改的key集合
	public PreparedStatement fillPstm(String sql, JSONArray params) throws Exception {
		this.pstm = this.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		this.pstm.clearBatch();
		for (int i = 0; i < params.size(); i++) {
            this.fillPstm2(sql, params.getJSONObject(i));
            this.pstm.addBatch(sql);
        }
		//printSql();
		return this.pstm;
	}
	//用json填补？
	public void fillPstm2(String sql, JSONObject params) throws Exception {
		this.pstm = this.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		if(params != null) {
			//字段数组
			String[] arr = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")")).split(",");
			//？数组：不全是？的情况
			String[] arr2 = sql.substring(sql.lastIndexOf("(") + 1, sql.lastIndexOf(")")).split(",");
			int jump = 0;
			for(int i = 0; i < arr.length; i++){
				if(!"?".equals(arr2[i].trim())) jump++;
				System.out.println(params.getString(arr[i+jump].trim()));
				if(i+jump < arr.length) {
					this.pstm.setObject(i+1, params.getString(arr[i+jump].trim()));
				} 
			}
		}
		printSql();
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
	public JSONArray getCatalogs() throws Exception{
		ResultSet rs = null;
		try {
			// 获取数据库的元数据
			DatabaseMetaData db = this.getConnection().getMetaData();
			// 从元数据中获取到所有的表名
			rs = db.getCatalogs();
			return rsToArray(rs);
		} finally {
			this.close(rs);
		}
	}
	public JSONArray getSchemas() throws Exception{
		ResultSet rs = null;
		try {
			// 获取数据库的元数据
			DatabaseMetaData db = this.getConnection().getMetaData();
			// 从元数据中获取到所有的表名
			rs = db.getSchemas(null, null);
			return rsToArray(rs);
		} finally {
			this.close(rs);
		}
	}
	
	/**
	 * 获取数据库下的所有表名
	 * @param catalog 表目录（可能为空）conn.getCatalog()
	 * @param databaseName：schemaPattern 表架构（可能为空）conn.getCatalog()
	 * @param tableName：tableNamePattern 表名 "%"
	 * @param types :new String[] { "TABLE" }:"TABLE","VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY","LOCAL TEMPORARY", "ALIAS","SYNONYM"
	 */
	public JSONArray getTables(String catalog, String databaseName, String tableName, String[] types) throws Exception {
		ResultSet rs = null;
		try {
			// 获取数据库的元数据
			DatabaseMetaData db = this.getConnection().getMetaData();
			// 从元数据中获取到所有的表名
			rs = db.getTables(catalog, databaseName, tableName, types);
			return rsToArray(rs);
		} finally {
			this.close(rs);
		}
	}

	public JSONObject getFiledType(String tableName) throws Exception {
		try (PreparedStatement pStemt = this.getConnection().prepareStatement(SQL + tableName)) {
			// 结果集元数据
			ResultSetMetaData rsmd = pStemt.getMetaData();
			JSONObject obj = new JSONObject();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				obj.put(rsmd.getColumnName(i + 1), rsmd.getColumnTypeName(i + 1));
			}
			return obj;
		} finally {
			close();
		}
	}

	public JSONArray getTableFiled(String databaseName, String tableName) throws Exception {
		ResultSet rs = null;
		try{
			//PreparedStatement pStemt = this.getConnection().prepareStatement(SQL + tableName);
			//rs = pStemt.executeQuery("show full columns from " + tableName);
		
			DatabaseMetaData rsmd = this.getConnection().getMetaData();
			rs =rsmd.getColumns(null, databaseName, tableName, null);
			return rsToArray(rs);
		} finally {
			close(rs);
		}
	}
	public JSONArray getPrimaryKey(String tableName) throws Exception {
		return getPrimaryKey(this.getConnection().getCatalog(),tableName);
	}
	public JSONArray getPrimaryKey(String databaseName, String tableName) throws Exception {
        ResultSet rs = null;
        try{
            DatabaseMetaData dbmd = this.getConnection().getMetaData();
            rs = dbmd.getPrimaryKeys(null, databaseName, tableName);
            return rsToArray(rs);
        } finally{
        	close(rs);
        }
    }
	public JSONArray getUnique(String tableName) throws Exception {
		DatabaseMetaData dbmd = this.getConnection().getMetaData();
		ResultSet rs = dbmd.getIndexInfo(null, null, tableName, false, false);
		JSONArray data = new JSONArray();
        while (rs.next()) {
            //NON_UNIQUE:false,唯一
            if (!rs.getBoolean("NON_UNIQUE")) {
            	data.add(rsToJson(rs));
            }
        }
        return data;
	}
	public void getDataBaseInfo() throws Exception {
        ResultSet rs = null;
        try{
            DatabaseMetaData dbmd = this.getConnection().getMetaData();
            System.out.println("数据库已知的用户1: "+ dbmd.getCatalogSeparator());
            System.out.println("数据库已知的用户2: "+ dbmd.getDatabaseProductName());
            
            System.out.println("数据库已知的用户: "+ dbmd.getUserName());//IT_JIPC
            System.out.println("数据库的系统函数的逗号分隔列表: "+ dbmd.getSystemFunctions());//DATABASE,IFNULL,USER
            System.out.println("数据库的时间和日期函数的逗号分隔列表: "+ dbmd.getTimeDateFunctions());
            System.out.println("数据库的字符串函数的逗号分隔列表: "+ dbmd.getStringFunctions());
            System.out.println("数据库供应商用于 'catalog' 的首选术语: "+ dbmd.getCatalogTerm());//SCHEMA
            System.out.println("数据库供应商用于 'schema' 的首选术语: "+ dbmd.getSchemaTerm());//SCHEMA
            System.out.println("数据库URL: " + dbmd.getURL());//jdbc:sap://10.122.2.101:30515
            System.out.println("是否允许只读:" + dbmd.isReadOnly());//false
            System.out.println("数据库的产品名称:" + dbmd.getDatabaseProductName());//HDB
            System.out.println("数据库的版本:" + dbmd.getDatabaseProductVersion());//1.00.122.22.1543461992
            System.out.println("驱动程序的名称:" + dbmd.getDriverName());
            System.out.println("驱动程序的版本:" + dbmd.getDriverVersion());
 
            System.out.println("数据库中使用的表类型");
            rs = dbmd.getTableTypes();
            while (rs.next()) {
                System.out.println(rs.getString("TABLE_TYPE"));
            }
        } finally{
        	close(rs);
        }
    }
	
	
	
	private static JSONArray rsToArray(ResultSet rs) throws SQLException {
		JSONArray data = new JSONArray();
		while (rs.next()) {
			data.add(rsToJson(rs));
		}
		return data;
	}
	private static JSONObject rsToJson(ResultSet rs) throws SQLException {
		JSONObject obj = new JSONObject();
		ResultSetMetaData md = rs.getMetaData();// 获取键名
		int columnCount = md.getColumnCount();// 获取行的数量
		for (int i = 1; i <= columnCount; i++) {
			obj.put(md.getColumnLabel(i), rs.getObject(i));// 别名
			// obj.put(md.getColumnName(i), rs.getObject(i));//数据库原字段名
		}
		return obj;
	}
}
