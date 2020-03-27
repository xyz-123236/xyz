package cn.xyz.orm.db;

import java.util.List;

public class DbTools extends And {
	private String table;
	private String alias;
	private String fields;
	private String where;
	private List<String> order;
	private List<String> group;
	private String limit;
	private List<DbTools> join;//left;right;inner;
	private String on;
	private String join_type;
	protected DbTools() {}
	public static DbTools getInstance() {
		return new DbTools();
	}
	public String getTable() {
		return this.table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getAlias() {
		return this.alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getFields() {
		return this.fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getWhere() {
		return this.where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	
	public List<String> getOrder() {
		return this.order;
	}

	public void setOrder(List<String> order) {
		this.order = order;
	}

	public List<String> getGroup() {
		return this.group;
	}

	public void setGroup(List<String> group) {
		this.group = group;
	}

	public String getLimit() {
		return this.limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}

	public List<DbTools> getJoin() {
		return this.join;
	}

	public void setJoin(List<DbTools> join) {
		this.join = join;
	}

	public String getJoin_type() {
		return this.join_type;
	}

	public void setJoin_type(String join_type) {
		this.join_type = join_type;
	}

	public String getOn() {
		return this.on;
	}

	public void setOn(String on) {
		this.on = on;
	}
	
	
	
}
