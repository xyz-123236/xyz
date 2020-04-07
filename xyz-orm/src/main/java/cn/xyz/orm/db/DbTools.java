package cn.xyz.orm.db;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.pojo.Basic;
import cn.xyz.common.tools.Tools;

public class DbTools extends Basic {
	private StringBuffer sql;
	protected List<String> fields = new ArrayList<>();
	
	protected String table;
	protected String alias;
	protected String where;
	protected String limit;
	protected String on;
	protected String join_type;
	
	protected DbTools() {}
	public DbTools(JSONObject obj) {
		if(obj != null) {
			this.rows = obj.getInteger("rows");
			this.page = obj.getInteger("page");
			this.sort = obj.getString("sort");
			this.order = obj.getString("order");
		}
	}
	public static DbTools getInstance() {
		return getInstance(null);
	}
	public static DbTools getInstance(JSONObject obj) {
		return new DbTools(obj);
	}
	
	
	public DbTools select(String table, String...fields) throws Exception {
		if(Tools.isEmpty(fields)) {
			this.sql.append( "select * from " + table);
			this.fields.add("*");
		}else {
			
		}
		return this;
	}

	public String getSql() {
		return sql.toString();
	}
	public DbTools setSql(String sql) {
		this.sql = new StringBuffer(sql);
		return this;
	}

	
}
