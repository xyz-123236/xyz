package cn.xyz.main.dao;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.main.dao.DbBase;
import cn.xyz.test.pojo.Basic;
import cn.xyz.test.tools.Tools;
import cn.xyz.test.tools.ToolsDate;

public class DbTool extends Basic{
	private String sql;
	private JSONObject obj;
	private String user_code;
	private Integer id;
	
	public DbTool(JSONObject obj) {
		new DbTool(obj, null);
	}
	public DbTool(JSONObject obj, Integer id) {
		this.rows = obj.getInteger("rows");
		this.page = obj.getInteger("page");
		this.sort = obj.getString("sort");
		this.order = obj.getString("order");
		this.obj = obj;
		this.id = id;
	}
	
	/*public JSONArray find() throws Exception {
		return find(new DbBase("mysql"));
	}
	public JSONArray find(DbBase db) throws Exception {
		if(!sql.toLowerCase().contains("limit") && obj.getIntValue("rows") > 0)
		{
			sql += " limit " + rows;// rows 页面容量
			sql += " offset "+ ((page-1)*rows);// page 开始页
		}
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +": "+ sql.replaceAll(" +"," "));
		//return Result.successEasyui(db.executeQueryJson(sql), count(db));
		return db.executeQueryJson(sql);
	}
	public Integer count() throws Exception {
		return count(new DbBase("mysql"));
	}
	public Integer count(DbBase db) throws Exception {
		String countSql = "select count(*) as count "+ sql.substring(sql.toLowerCase().indexOf(" from "));
		if(countSql.toLowerCase().contains(" order ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" order "));
		}else if(countSql.toLowerCase().contains(" limit ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" limit "));
		}
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +": "+ countSql.replaceAll(" +"," "));
		return db.executeQueryJson(countSql).getJSONObject(0).getInteger("count");
	}*/
	/**
	 * 删除一部分key不添加
	 * @param table
	 * @param row
	 * @param deleteKey 这些key要移除
	 * @return
	 * @throws Exception 
	 */
	public DbTool insert(String table, JSONObject row, String usercode, String[] deleteKey) throws Exception{
		sql = "";
		if(Tools.isEmpty(row)) {
			row.put("entby", usercode);
			row.put("entdate", ToolsDate.getString());
			deleteKey(row, deleteKey);
			String key = "";
			String value = "";
			for(String str: row.keySet()){
				key += str + ",";
				value += row.get(key) + ",";
			}
			sql += "insert into "+sql+" ("+key.substring(0,key.lastIndexOf(","))+ ") values ("+value.substring(0,value.lastIndexOf(","))+ ")";
		}else {
			throw new Exception("SQL不正确");
		}
		return this;
	}
	/**
	 * 只添加一些key
	 * @param table
	 * @param retain 只添加这些key
	 * @param row
	 * @return
	 */
	public DbTool insert(String table, String retain, String usercode, JSONObject row){
		sql = "";
		return this;
	}
	
	public DbTool insert(String table, JSONArray rows, String usercode){
		sql = "";
		return this;
	}
	public DbTool update(String table, JSONObject row, String usercode, String[] deleteKey) throws Exception{
		sql = "";
		row.put("modifyby", usercode);
		row.put("modifydate", ToolsDate.getString());
		row = deleteKey(row, deleteKey);
		if(Tools.isEmpty(this.id)) {
			throw new Exception("new ToolsSql需要参数id");
		}
		return this;
	}
	public DbTool delete(String table, JSONObject row) {
		sql = "";
		return this;
	}
	public DbTool deleteLogic(String table, JSONObject row, String usercode) {
		sql = "";
		return this;
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
	
	
	public DbTool select(String table) {
		return select(table, "*");
	}
	public DbTool select(String table, String fields) {
		sql = "";
		sql += "select " + fields + " from " + table;
		return this;
	}
	public DbTool left(String table, String on) {
		sql += " left join " + table + " on " + on;
		return this;
	}
	public DbTool right(String table, String on) {
		sql += " right join " + table + " on " + on;
		return this;
	}
	public DbTool inner(String table, String on) {
		sql += " inner join " + table + " on " + on;
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
	public DbTool where(JSONObject obj, String...deleteKey) throws Exception {
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
	public DbTool where(String dateKey, JSONObject obj, String...deleteKey) throws Exception {
		JSONObject row = (JSONObject)obj.clone();
		sql += " where 1 = 1 ";
		if(Tools.isEmpty(row)) {
			row = deleteKey(row, deleteKey);
			for(String key:row.keySet()){
				String value = row.getString(key);
				if("datefrom".equals(key)) {
					if(!Tools.isEmpty(value)) {
						String dateTo = row.getString("dateto");
						if(Tools.isEmpty(dateTo)) {
							sql += " AND "+dateKey+" = '" + value + "' ";
						}else {
							sql += " AND "+dateKey+" >= '" + value + "' and "+dateKey+" <= '" + dateTo + "' ";
						}
					}
				}else if("dateto".equals(key)) {
					
				}else {
					if(!Tools.isEmpty(value)) {
						sql += " and "+key+" like '%"+value.trim()+"%' ";
					}
				}
			}
		}
		return this;
	}
	public DbTool where() throws Exception {
		return where("");
	}
	public DbTool where(String condition) throws Exception {
		if(Tools.isEmpty(condition)) {
			sql += " where 1 = 1 ";
		}else {
			sql += " where " + condition;
		}
		return this;
	}
	public DbTool and(String sql) {
		sql += " and " + sql;
		return this;
	}
	public DbTool and(String key, Object obj) throws Exception {
		return and(key, obj, "%");
	}
	public DbTool and(String key, Object obj, String op) throws Exception {
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
				sql += " and "+key+" like '%"+value+"%' ";
			}else {
				sql += " and "+key+" "+op+" '"+value+"' ";
			}
		}
		return this;
	}
	public DbTool or(String key, JSONObject obj, String... fields) throws Exception {
		return or(key, "%", obj, fields);
	}
	public DbTool or(String key, String op, JSONObject obj, String... fields) throws Exception {
		return or(((JSONObject)obj).getString(key).trim(), op, fields);
	}
	public DbTool or(String value, String op, String... fields) throws Exception {
		if(!Tools.isEmpty(value)) {
			sql += " and (";
			for (int i = 0; i < fields.length; i++) {
				if(i != 0) {
					sql += " or ";
				}
				if("%".equals(op)) {
					sql += fields[i] + " like '%"+value+"%' ";
				}else {
					sql += fields[i] + " "+op+" '"+value+"' ";
				}
			}
			sql += ") ";
		}
		return this;
	}
	public DbTool in(String sql) {
		sql += " in ("+sql+") ";
		return this;
	}
	public DbTool in(String field, String str) {
		return in(field, str.split(","));
	}
	public DbTool in(String field, String... str)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length; i++) { 
			if(i != 0){
				sb.append(","); 
			}
			sb.append("'"+str[i]+"'");
		} 
		sql += field + " in ("+sb.toString()+") ";
		return this;
	}
	public DbTool empty(String field) {
		sql += " and ("+field+" is null or "+field+" = '') ";
		return this;
	}
	public DbTool notEmpty(String field) {
		sql += " and ("+field+" is not null and "+field+" != '') ";
		return this;
	}
	public DbTool date(String key, JSONObject row) throws Exception {
		String dateFrom = row.getString("dateFrom");
		if(Tools.isEmpty(dateFrom)) dateFrom = row.getString("datefrom");
		if(!Tools.isEmpty(dateFrom)) {
			String dateTo = row.getString("dateTo");
			if(Tools.isEmpty(dateTo)) dateTo = row.getString("dateto");
			if(Tools.isEmpty(dateTo)) {
				sql += " AND "+key+" = '" + dateFrom + "' ";
			}else {
				sql += " AND "+key+" >= '" + dateFrom + "' and "+key+" <= '" + dateTo + "' ";
			}
		}
		return this;
	}
	public DbTool sort(String sortDafault) throws Exception {
		if(!Tools.isEmpty(sort) || !Tools.isEmpty(sortDafault)) {
			sql += " order by ";
			if(!Tools.isEmpty(sort)) {
				String[] orders = order.split(",");
				String[] sorts = sort.split(",");
				for (int i = 0; i < sorts.length; i++) {
					if("asc".equals(orders[i].trim())) {
						sql += sorts[i] + " asc, ";
					}else {
						sql += sorts[i] + " desc, ";
					}
				}
			}
			if(!Tools.isEmpty(sortDafault)) sql += sortDafault;
		}
		return this;
	}
	public DbTool group(String group) throws Exception {
		if(!Tools.isEmpty(group)) {
			sql += " group by " + group;
		}
		return this;
	}
	public DbTool limit() throws Exception {
		if(!sql.toLowerCase().contains("limit") && rows > 0)
		{
			sql += " limit " + rows;// rows 页面容量
			sql += " offset "+ ((page-1)*rows);// page 开始页
		}
		return this;
	}
	public String asSql(String as) throws Exception {
		String asSql = " ("+sql+") as ";
		if(Tools.isEmpty(as)) {
			asSql += "temp ";
		}else {
			asSql += as;
		}
		return asSql;
	}

	public String countSql() throws Exception {
		String countSql = "select count(*) as count "+ sql.substring(sql.toLowerCase().indexOf(" from "));
		if(countSql.toLowerCase().contains(" order ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" order "));
		}else if(countSql.toLowerCase().contains(" limit ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" limit "));
		}
		return countSql;
	}
	public String limitData(JSONArray data) {
		return JSON.toJSONString(data.subList((page-1)*rows, page*rows>data.size()?data.size():page*rows));
	}
	private JSONObject deleteKey(JSONObject row, String[] deleteKey) throws Exception {
		if(Tools.isEmpty(row)) {
			if(deleteKey != null && deleteKey.length > 0) {
				for (int i = 0; i < deleteKey.length; i++) {
					row.remove(deleteKey[i]);
				}
			}
			row.remove("page");
			row.remove("rows");
			row.remove("sort");
			row.remove("order");
			row.remove("jsp_name");
			for(String key:row.keySet()){
				if(Tools.isEmpty(row.getString(key))) {
					row.put(key, null);
				}
			}
		}
		return row;
	}
	
	
	
	public String getSql() {
		return sql;
	}
	public DbTool setSql(String sql) {
		this.sql = sql;
		return this;
	}
	public JSONObject getObj() {
		return obj;
	}
	public void setObj(JSONObject obj) {
		this.obj = obj;
	}
	public String getUser_code() {
		return user_code;
	}
	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public static void main(String[] args) {
		String[] a = {"aa","bb"};
		String[] b = null;
		System.out.println(a[1]+b);
	}


}
