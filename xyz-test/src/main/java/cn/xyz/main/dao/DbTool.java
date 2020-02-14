package cn.xyz.main.dao;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.test.pojo.Basic;
import cn.xyz.test.tools.Tools;
import cn.xyz.test.tools.ToolsDate;
import cn.xyz.test.tools.ToolsJson;

public class DbTool extends Basic{
	private String sql;
	public static String[] DEFAULT_REMOVE_KEYS = {"page","rows","sort","order","jsp_name"};
	
	public DbTool(JSONObject obj) {
		this.rows = obj.getInteger("rows");
		this.page = obj.getInteger("page");
		this.sort = obj.getString("sort");
		this.order = obj.getString("order");
	}
	/**
	 * 添加时清除一些字段
	 * @param table
	 * @param row
	 * @param entby
	 * @param removeKey
	 * @return
	 * @throws Exception
	 */
	public String insert(String table, JSONObject row, String entby, String...removeKey) throws Exception{
		return insert(table, row, entby, true, removeKey);
	}
	/**
	 * 添加时可以选择删除一些字段，还是只添加一些字段
	 * @param table
	 * @param row
	 * @param entby
	 * @param remove true表示移除keys，false只添加keys
	 * @param keys
	 * @return
	 * @throws Exception
	 */
	public String insert(String table, JSONObject row, String entby, boolean remove, String...keys) throws Exception{
		sql = "";
		if(Tools.isEmpty(row)) {
			row.put("entby", entby);
			row.put("entdate", ToolsDate.getString());
			String key = "";
			String value = "";
			if(remove) {
				ToolsJson.removeKey(row, DEFAULT_REMOVE_KEYS, keys);
				for(String str: row.keySet()){
					key += str + ",";
					value += row.get(key) + ",";
				}
			}else {
				for (int i = 0; i < keys.length; i++) {
					key += keys[i] + ",";
					value += row.get(keys[i]) + ",";
				}
			}
			sql += "insert into "+table+" ("+key.substring(0,key.lastIndexOf(","))+ ") values ("+value.substring(0,value.lastIndexOf(","))+ ")";
		}else {
			throw new Exception("没有要插入的数据");
		}
		return sql;
	}
	
	public DbTool insert(String table, JSONArray rows, String usercode){
		sql = "";
		return this;
	}
	public DbTool update(String table, JSONObject row, String usercode, String...removeKey) throws Exception{
		sql = "";
		row.put("modifyby", usercode);
		row.put("modifydate", ToolsDate.getString());
		JSONObject obj = ToolsJson.removeKey(row, DEFAULT_REMOVE_KEYS, removeKey);
		if(Tools.isEmpty(id)) {
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
	public long add(DBTool db, JSONObject row, String...removeKey) throws Exception {
		row.put("entby", getUserName());
		row.put("entdate", new Date());
		row = removeKey(row, removeKey);
		return db.set(row).insert();
	}
	public Integer getId(DBTool db) {
    	JSONArray data = db.select("select current_identity_value() as id FROM DUMMY");
    	if(t.isValidJSON(data)) {
    		return data.getJSONObject(0).getInteger("id");
    	}
    	return null;
    }
	public long update(DBTool db, String id, JSONObject row, String...removeKey) throws Exception {
		row.put("modifyby", getUserName());
		row.put("modifydate", new Date());
		row = removeKey(row, removeKey);
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
	public DbTool where(JSONObject obj, String...removeKey) throws Exception {
		return where(null, obj, removeKey);
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
	public DbTool where(String dateKey, JSONObject row, String...removeKey) throws Exception {
		sql += " where 1 = 1 ";
		if(Tools.isEmpty(row)) {
			JSONObject obj = ToolsJson.removeKey(row, DEFAULT_REMOVE_KEYS, removeKey);
			for(String key:obj.keySet()){
				String value = obj.getString(key);
				if("datefrom".equals(key)) {
					if(!Tools.isEmpty(value)) {
						String dateTo = obj.getString("dateto");
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
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +" DbTool: "+ asSql.replaceAll(" +"," "));
		return asSql;
	}

	public String countSql() throws Exception {
		String countSql = "select count(*) as count "+ sql.substring(sql.toLowerCase().indexOf(" from "));
		if(countSql.toLowerCase().contains(" order ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" order "));
		}else if(countSql.toLowerCase().contains(" limit ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" limit "));
		}
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +" DbTool: "+ countSql.replaceAll(" +"," "));
		return countSql;
	}
	public String limitData(JSONArray data) {
		return JSON.toJSONString(data.subList((page-1)*rows, page*rows>data.size()?data.size():page*rows));
	}

	
	
	
	public String getSql() {
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +" DbTool: "+ sql.replaceAll(" +"," "));
		return sql;
	}
	public DbTool setSql(String sql) {
		this.sql = sql;
		return this;
	}

	
	public static void main(String[] args) {
		String[] a = {"aa","bb"};
		String[] b = null;
		System.out.println(a[1]+b);
	}


}
