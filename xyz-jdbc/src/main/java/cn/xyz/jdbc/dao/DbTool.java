package cn.xyz.jdbc.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fujikon.lib.ToolsSql;
import com.fujikon.lib.t;

import cn.xyz.common.pojo.Basic;
import cn.xyz.common.tools.Tools;
import cn.xyz.common.tools.ToolsDate;
import cn.xyz.common.tools.ToolsJson;

public class DbTool extends Basic{
	private String sql;
	
	//存入数据字典，加载时放入Application或Redis缓存
	public static String[] DEFAULT_REMOVE_KEYS = {"page","rows","sort","order","jsp_name"};
	public static String CREATE_BY = "create_by";
	public static String CREATE_DATE = "create_date";
	public static String UPDATE_BY = "update_by";
	public static String UPDATE_DATE = "update_date";
	public static String DATE_FROM = "date_from";
	public static String DATE_TO = "date_to";
	
	public DbTool(JSONObject obj) {
		if(obj != null) {
			this.rows = obj.getInteger("rows");
			this.page = obj.getInteger("page");
			this.sort = obj.getString("sort");
			this.order = obj.getString("order");
		}
	}
	public static DbTool getInstance(JSONObject obj) {
		return new DbTool(obj);
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
	public DbTool insert(String table, JSONObject row, String entby, String...removeKey) throws Exception{
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
	public DbTool insert(String table, JSONObject row, String entby, boolean remove, String...keys) throws Exception{
		this.sql = "";
		if(!Tools.isEmpty(row)) {
			String key = "";
			String value = "";
			if(remove) {
				if(!Tools.isEmpty(CREATE_BY)) {
					row.put(CREATE_BY, entby);
					row.put(CREATE_DATE, ToolsDate.getString());
				}
				ToolsJson.removeKey(row, DEFAULT_REMOVE_KEYS, keys);
				for(String str: row.keySet()){
					key += str + ",";
					value += "'"+row.get(key) + "',";
				}
			}else {
				for (int i = 0; i < keys.length; i++) {
					key += keys[i] + ",";
					value += row.get(keys[i]) + ",";
				}
				if(!Tools.isEmpty(CREATE_BY)) {
					key += CREATE_BY + ",";
					value += "'"+entby + "',";
					key += CREATE_DATE + ",";
					value += "'"+ToolsDate.getString(pattern, date)() + "',";
				}
			}
			this.sql += "insert into "+table+" ("+key.substring(0,key.lastIndexOf(","))+ ") values ("+value.substring(0,value.lastIndexOf(","))+ ")";
		}else {
			throw new Exception("没有要插入的数据");
		}
		return this;
	}
	public DbTool insertBatch(String table, JSONArray row, String entby, String...removeKey) throws Exception{
		return insertBatch(table, row, entby, true, removeKey);
	}
	//处理sql
	public DbTool insertBatch(String table,JSONArray params, String entby, boolean remove, String...keys) throws Exception {
		this.sql = "";
		if(!Tools.isEmpty(params)) {
			Set<String> key_set = new HashSet<>();
			if(remove) {
				for (int i = 0; i < params.size(); i++) {
					JSONObject obj = params.getJSONObject(i);
					if(!Tools.isEmpty(CREATE_BY)) {
						obj.put(CREATE_BY, entby);
						obj.put(CREATE_DATE, ToolsDate.getString());
					}
					ToolsJson.removeKey(obj, DEFAULT_REMOVE_KEYS, keys);
					for(String key: obj.keySet()){
						key_set.add(key);
					}
				}
			}else {
				for (int j = 0; j < keys.length; j++) {
					key_set.add(keys[j]);
				}
				if(!Tools.isEmpty(CREATE_BY)) {
					key_set.add(CREATE_BY);
					key_set.add(CREATE_DATE);
				}
			}
			
			if(!Tools.isEmpty(key_set)) {
				String feilds = "";
				String values = "";
				for (String key : key_set) {
					feilds += key + ",";
					values += "?" + ",";
				}
				this.sql += "insert into "+table+" ("+feilds.substring(0,feilds.lastIndexOf(","))+ ") values ("+values.substring(0,values.lastIndexOf(","))+ ")";
			}
		}
		return this;
	}
	public DbTool insert(String table, JSONArray rows, String usercode){
		this.sql = "";
		return this;
	}
	public DbTool update(String table, JSONObject row, String usercode, Object...removeKey) throws Exception{
		return update(table, row, usercode, true, removeKey);
	}
	public DbTool update(String table, JSONObject row, String usercode, boolean remove, Object...keys) throws Exception{
		this.sql = "";
		if(!Tools.isEmpty(row)) {
			String set = "";
			if(remove) {
				if(!Tools.isEmpty(UPDATE_BY)) {
					row.put(UPDATE_BY, usercode);
					row.put(UPDATE_DATE, ToolsDate.getString());
				}
				JSONObject obj = ToolsJson.removeKey(row, DEFAULT_REMOVE_KEYS, keys);
				for(String str: obj.keySet()){
					set += str + "='"+obj.get(str)+"',";
				}
			}else {
				for (int i = 0; i < keys.length; i++) {
					set += keys[i] + "='"+row.get(keys[i])+"',";
				}
				if(!Tools.isEmpty(UPDATE_BY)) {
					set += UPDATE_BY + "='"+usercode+"',";
					set += UPDATE_DATE + "='"+ToolsDate.getString()+"',";
				}
			}
			this.sql += "update "+table+" set "+set.substring(0,set.lastIndexOf(","));
		}else {
			throw new Exception("没有要插入的数据");
		}
		return this;
	}

	
	
	public DbTool delete(String table, JSONObject row) {
		this.sql = "";
		return this;
	}
	public DbTool deleteLogic(String table, JSONObject row, String usercode) {
		this.sql = "";
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
		this.sql = "";
		this.sql += "select " + fields + " from " + table;
		return this;
	}
	public DbTool left(String table, String on) {
		this.sql += " left join " + table + " on " + on;
		return this;
	}
	public DbTool right(String table, String on) {
		this.sql += " right join " + table + " on " + on;
		return this;
	}
	public DbTool inner(String table, String on) {
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
		this.sql += " where 1 = 1 ";
		if(!Tools.isEmpty(row)) {
			JSONObject obj = ToolsJson.removeKey(row, DEFAULT_REMOVE_KEYS, removeKey);
			for(String key:obj.keySet()){
				String value = obj.getString(key);
				if(DATE_FROM.equals(key)) {
					this.date(dateKey, row);
				}else {
					if(!Tools.isEmpty(value) && !DATE_TO.equals(key)) {
						this.sql += " and "+key+" like '%"+value.trim()+"%' ";
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
		if(!Tools.isEmpty(condition)) {
			this.sql += " where 1 = 1 ";
		}else {
			this.sql += " where " + condition;
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
				this.sql += " and "+key+" like '%"+value+"%' ";
			}else {
				this.sql += " and "+key+" "+op+" '"+value+"' ";
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
		this.sql += field + " in ("+sb.toString()+") ";
		return this;
	}
	public DbTool empty(String field) {
		this.sql += " and ("+field+" is null or "+field+" = '') ";
		return this;
	}
	public DbTool notEmpty(String field) {
		this.sql += " and ("+field+" is not null and "+field+" != '') ";
		return this;
	}
	public DbTool date(String key, JSONObject row) throws Exception {
		String dateFrom = row.getString(DATE_FROM);
		if(!Tools.isEmpty(dateFrom)) {
			String dateTo = row.getString(DATE_TO);
			if(Tools.isEmpty(dateTo)) {
				this.sql += " AND "+key+" >= '" + dateFrom + " 00:00:00' and "+key+" <= '" + dateFrom + " 23:59:59' ";
			}else {
				this.sql += " AND "+key+" >= '" + dateFrom + " 00:00:00' and "+key+" <= '" + dateTo + " 23:59:59' ";
			}
		}
		return this;
	}
	public DbTool order(String sortDafault) throws Exception {
		return order(sortDafault, false);
	}
	/**
	 * 	排序
	 * @param sortDafault
	 * @param removeDafault: sybase不支持同一个字段进行2次排序，mysql，hana出现2次则以前一个为准
	 * @return
	 * @throws Exception
	 */
	public DbTool order(String sortDafault, boolean removeDafault) throws Exception {
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
			if(!Tools.isEmpty(sortDafault) && (Tools.isEmpty(this.sort) || !removeDafault)) {
				this.sql += sortDafault;
			}else {
				this.sql = this.sql.substring(0, this.sql.lastIndexOf(","));
			}
		}
		return this;
	}
	public DbTool group(String group) throws Exception {
		if(!Tools.isEmpty(group)) {
			this.sql += " group by " + group;
		}
		return this;
	}
	public DbTool limit() throws Exception {
		if(!this.sql.toLowerCase().contains("limit") && this.rows > 0)
		{
			this.sql += " limit " + this.rows;// rows 页面容量
			this.sql += " offset "+ ((this.page-1)*this.rows);// page 开始页
		}
		return this;
	}
	public String asSql(String as) throws Exception {
		String asSql = " ("+this.sql+") as ";
		if(Tools.isEmpty(as)) {
			asSql += "temp ";
		}else {
			asSql += as;
		}
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +" DbTool: "+ asSql.replaceAll(" +"," "));
		return asSql;
	}

	public String countSql() throws Exception {
		String countSql = "select count(*) as count "+ this.sql.substring(this.sql.toLowerCase().indexOf(" from "));
		if(countSql.toLowerCase().contains(" order ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" order "));
		}else if(countSql.toLowerCase().contains(" limit ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" limit "));
		}
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +" DbTool: "+ countSql.replaceAll(" +"," "));
		return countSql;
	}
	public String limitData(JSONArray data) {
		return JSON.toJSONString(data.subList((this.page-1)*this.rows, this.page*this.rows>data.size()?data.size():this.page*this.rows));
	}

	public static String replaceProjection(String sql, String projection) {
		return replaceProjection(sql, projection, true);
	}
	public static String replaceProjection(String sql, String projection, boolean deleteLimit) {
		int beginIndex = sql.toLowerCase().indexOf(" from ");
		int endIndex1 = sql.toLowerCase().indexOf(" order ") > 1 ? sql.toLowerCase().indexOf(" order ") : sql.length();
		int endIndex2 = sql.toLowerCase().indexOf(" limit ") > 1 ? sql.toLowerCase().indexOf(" limit ") : sql.length();
		if(deleteLimit) {
			return "select " + projection +sql.substring(beginIndex, endIndex1 < endIndex2 ? endIndex1 : endIndex2);
		}else {
			return "select " + projection +sql.substring(beginIndex);
		}
	} 
	
	
	public String getSql() {
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +" DbTool: "+ this.sql.replaceAll("\t", " ").replaceAll(" +"," "));
		return this.sql;
	}
	public DbTool setSql(String sql) {
		this.sql = sql;
		return this;
	}


}
