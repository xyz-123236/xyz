package cn.xyz.jdbc.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.pojo.BasicPojo;
import cn.xyz.common.tools.Tools;
import cn.xyz.common.tools.ToolsDate;


public class ToolsSql extends BasicPojo{
	private String sql = "";
	private JSONObject obj;
	
	public ToolsSql(JSONObject obj) {
		this.rows = obj.getInteger("rows");
		this.page = obj.getInteger("page");
		this.sort = obj.getString("sort");
		this.order = obj.getString("order");
		this.obj = obj;
	}
	
	public JSONArray find() throws Exception {
		return find(new DbBase("mysql"));
	}
	public JSONArray find(DbBase db) throws Exception {
		if(!this.sql.toLowerCase().contains("limit") && this.obj.getIntValue("rows") > 0)
		{
			this.sql += " limit " + this.rows;// rows 页面容量
			this.sql += " offset "+ ((this.page-1)*this.rows);// page 开始页
		}
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +": "+ this.sql.replaceAll(" +"," "));
		//return Result.successEasyui(db.executeQueryJson(sql), count(db));
		return db.find(this.sql);
	}
	public Integer count() throws Exception {
		return count(new DbBase("mysql"));
	}
	public Integer count(DbBase db) throws Exception {
		String countSql = "select count(*) as count "+ this.sql.substring(this.sql.toLowerCase().indexOf(" from "));
		if(countSql.toLowerCase().contains(" order ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" order "));
		}else if(countSql.toLowerCase().contains(" limit ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" limit "));
		}
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +": "+ countSql.replaceAll(" +"," "));
		return db.find(countSql).getJSONObject(0).getInteger("count");
	}
	/*public ToolsSql insert(String table) {
		sql = "";
		sql += "insert into " + table;
		return this;
	}
	public ToolsSql insert(String table, String fields) {
		sql = "";
		sql += "insert into " + table + " (";
		return this;
	}
	public long add(DBTool db, JSONObject row, String...deleteKey) throws Exception {
		row.put("entby", getUserName());
		row.put("entdate", new Date());
		row = deleteKey(row, deleteKey);
		return db.set(row).insert();
	}
	public Integer getId(DBTool db) {
    	JSONArray data = db.select("select current_identity_value() as id FROM DUMMY");
    	if(t.isValidJSON(data)) {
    		return data.getJSONObject(0).getInteger("id");
    	}
    	return null;
    }
	public long update(DBTool db, String id, JSONObject row, String...deleteKey) throws Exception {
		row.put("modifyby", getUserName());
		row.put("modifydate", new Date());
		row = deleteKey(row, deleteKey);
		return db.eq(id, row.getString(id)).set(row).update();
	}*/
	
	
	public ToolsSql select(String table) {
		return select(table, "*");
	}
	public ToolsSql select(String table, String fields) {
		this.sql = "";
		this.sql += "select " + fields + " from " + table;
		return this;
	}
	public ToolsSql left(String table, String on) {
		this.sql += " left join " + table + " on " + on;
		return this;
	}
	public ToolsSql right(String table, String on) {
		this.sql += " right join " + table + " on " + on;
		return this;
	}
	public ToolsSql inner(String table, String on) {
		this.sql += " inner join " + table + " on " + on;
		return this;
	}
	/**
	 * 自动添加row中的条件
	 * @param table 表名
	 * @param row 条件
	 * @param sort 默认排序
	 * @return
	 * @throws Exception 
	 */
	public ToolsSql where(JSONObject obj, String...deleteKey) throws Exception {
		return where(null, obj, deleteKey);
	}
	/**
	 * 自动添加row中的条件
	 * @param table 表名
	 * @param row 条件
	 * @param sort 默认排序
	 * @param dateKey 时间范围的key
	 * @return
	 * @throws Exception 
	 */
	public ToolsSql where(String dateKey, JSONObject obj, String...deleteKey) throws Exception {
		JSONObject row = (JSONObject)obj.clone();
		this.sql += " where 1 = 1 ";
		if(Tools.isEmpty(row)) {
			row = deleteKey(row, deleteKey);
			for(String key:row.keySet()){
				String value = row.getString(key);
				if("datefrom".equals(key)) {
					if(!Tools.isEmpty(value)) {
						String dateTo = row.getString("dateto");
						if(Tools.isEmpty(dateTo)) {
							this.sql += " AND "+dateKey+" = '" + value + "' ";
						}else {
							this.sql += " AND "+dateKey+" >= '" + value + "' and "+dateKey+" <= '" + dateTo + "' ";
						}
					}
				}else if("dateto".equals(key)) {
					System.out.println("");
				}else {
					if(!Tools.isEmpty(value)) {
						this.sql += " and "+key+" like '%"+value.trim()+"%' ";
					}
				}
			}
		}
		return this;
	}
	public ToolsSql where() throws Exception {
		return where("");
	}
	public ToolsSql where(String condition) throws Exception {
		if(Tools.isEmpty(condition)) {
			this.sql += " where 1 = 1 ";
		}else {
			this.sql += " where " + condition;
		}
		return this;
	}
	public ToolsSql and(String sql) {
		this.sql += " and " + sql;
		return this;
	}
	public ToolsSql and(String key, Object obj) throws Exception {
		return and(key, obj, "%");
	}
	public ToolsSql and(String key, Object obj, String op) throws Exception {
		String[] arr = key.trim().split("\\.");
		String field = key;
		if(arr.length == 2) {
			field = arr[1];
		}
		String value = "";
		if(obj instanceof JSONObject)
        {
			value = ((JSONObject)obj).getString(field).trim();
        }else {
        	value = obj.toString().trim();
        }
		if(!Tools.isEmpty(value)) {
			if("%".equals(op)) {
				this.sql += " and "+key+" like '%"+value+"%' ";
			}else {
				this.sql += " and "+key+" "+op+" '"+value+"' ";
			}
		}
		return this;
	}
	public ToolsSql or(String key, JSONObject obj, String... fields) throws Exception {
		return or(key, "%", obj, fields);
	}
	public ToolsSql or(String key, String op, JSONObject obj, String... fields) throws Exception {
		return or(((JSONObject)obj).getString(key).trim(), op, fields);
	}
	public ToolsSql or(String value, String op, String... fields) throws Exception {
		if(!Tools.isEmpty(value)) {
			this.sql += " and (";
			for (int i = 0; i < fields.length; i++) {
				if(i != 0) {
					this.sql += " or ";
				}
				if("%".equals(op)) {
					this.sql += fields[i] + " like '%"+value+"%' ";
				}else {
					this.sql += fields[i] + " "+op+" '"+value+"' ";
				}
			}
			this.sql += ") ";
		}
		return this;
	}
	public ToolsSql in(String sql) {
		this.sql += " in ("+sql+") ";
		return this;
	}
	public ToolsSql in(String field, String str) {
		return in(field, str.split(","));
	}
	public ToolsSql in(String field, String... str)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length; i++) { 
			if(i != 0){
				sb.append(","); 
			}
			sb.append("'"+str[i]+"'");
		} 
		this.sql += field + " in ("+sb.toString()+") ";
		return this;
	}
	public ToolsSql empty(String field) {
		this.sql += " and ("+field+" is null or "+field+" = '') ";
		return this;
	}
	public ToolsSql notEmpty(String field) {
		this.sql += " and ("+field+" is not null and "+field+" != '') ";
		return this;
	}
	public ToolsSql date(String key, JSONObject row) throws Exception {
		String dateFrom = row.getString("dateFrom");
		if(Tools.isEmpty(dateFrom)) dateFrom = row.getString("datefrom");
		if(!Tools.isEmpty(dateFrom)) {
			String dateTo = row.getString("dateTo");
			if(Tools.isEmpty(dateTo)) dateTo = row.getString("dateto");
			if(Tools.isEmpty(dateTo)) {
				this.sql += " AND "+key+" = '" + dateFrom + "' ";
			}else {
				this.sql += " AND "+key+" >= '" + dateFrom + "' and "+key+" <= '" + dateTo + "' ";
			}
		}
		return this;
	}
	public ToolsSql sort(String sortDafault) throws Exception {
		if(!Tools.isEmpty(this.sort) || !Tools.isEmpty(sortDafault)) {
			this.sql += " order by ";
			if(!Tools.isEmpty(this.sort)) {
				String[] orders = this.order.split(",");
				String[] sorts = this.sort.split(",");
				for (int i = 0; i < sorts.length; i++) {
					if("asc".equals(orders[i].trim())) {
						this.sql += sorts[i] + " asc, ";
					}else {
						this.sql += sorts[i] + " desc, ";
					}
				}
			}
			if(!Tools.isEmpty(sortDafault)) this.sql += sortDafault;
		}
		return this;
	}
	public String easyuiData(JSONArray data) {
		return JSON.toJSONString(data.subList((this.page-1)*this.rows, this.page*this.rows>data.size()?data.size():this.page*this.rows));
	}
	private static JSONObject deleteKey(JSONObject row, String...deleteKey) throws Exception {
		if(Tools.isEmpty(row)) {
			if(deleteKey != null && deleteKey.length > 0) {
				for (int i = 0; i < deleteKey.length; i++) {
					row.remove(deleteKey[i]);
				}
			}
			row.remove("timestamp");
			row.remove("recordTime");
			for(String key:row.keySet()){
				if(Tools.isEmpty(row.getString(key))) {
					row.put(key, null);
				}
			}
		}
		return row;
	}
	
	
	
	public String getSql() {
		return this.sql;
	}

	public ToolsSql setSql(String sql) {
		this.sql = sql;
		return this;
	}

	public JSONObject getObj() {
		return this.obj;
	}

	public void setObj(JSONObject obj) {
		this.obj = obj;
	}
	
}
