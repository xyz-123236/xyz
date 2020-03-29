package cn.xyz.orm.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.pojo.Basic;
import cn.xyz.common.tools.Tools;
import cn.xyz.common.tools.ToolsDate;
import cn.xyz.common.tools.ToolsJson;

public class DbTool extends Basic{
	private StringBuffer sql;
	
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
		this.sql = new StringBuffer();
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
					value += "'"+ToolsDate.getString() + "',";
				}
			}
			this.sql.append( "insert into "+table+" ("+key.substring(0,key.lastIndexOf(","))+ ") values ("+value.substring(0,value.lastIndexOf(","))+ ")");
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
		this.sql = new StringBuffer();
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
				this.sql.append("insert into "+table+" ("+feilds.substring(0,feilds.lastIndexOf(","))+ ") values ("+values.substring(0,values.lastIndexOf(","))+ ")");
			}
		}
		return this;
	}
	public DbTool insert(String table, JSONArray rows, String usercode){
		this.sql = new StringBuffer();
		return this;
	}
	public DbTool update(String table, JSONObject row, String usercode, Object...removeKey) throws Exception{
		return update(table, row, usercode, true, removeKey);
	}
	public DbTool update(String table, JSONObject row, String usercode, boolean remove, Object...keys) throws Exception{
		this.sql = new StringBuffer();
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
			this.sql.append( "update "+table+" set "+set.substring(0,set.lastIndexOf(",")));
		}else {
			throw new Exception("没有要插入的数据");
		}
		return this;
	}

	
	
	public DbTool delete(String table, JSONObject row) {
		this.sql = new StringBuffer();
		return this;
	}
	public DbTool deleteLogic(String table, JSONObject row, String usercode) {
		this.sql = new StringBuffer();
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
		this.sql = new StringBuffer();
		this.sql.append( "select " + fields + " from " + table);
		return this;
	}
	public DbTool left(String table, String on) {
		this.sql.append( " left join " + table + " on " + on);
		return this;
	}
	public DbTool right(String table, String on) {
		this.sql.append( " right join " + table + " on " + on);
		return this;
	}
	public DbTool inner(String table, String on) {
		this.sql.append( " inner join " + table + " on " + on);
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
		this.sql.append( " where 1 = 1 ");
		if(!Tools.isEmpty(row)) {
			JSONObject obj = ToolsJson.removeKey(row, DEFAULT_REMOVE_KEYS, removeKey);
			for(String key:obj.keySet()){
				String value = obj.getString(key);
				/*if(obj.get(key) instanceof Date) {
					this.date(key.substring(0, key.indexOf("_from")), row);
				}*/
				if(key.indexOf("_date_from") > 0) {
					this.date(key.substring(0, key.indexOf("_from")), row);
				}else if(key.indexOf("_from") > 0) {
					String dateFrom = row.getString(DATE_FROM);
					if(!Tools.isEmpty(dateFrom)) {
						String a = "锯锯锯锯锯锯锯锯";
						String dateTo = row.getString(DATE_TO);
						if(Tools.isEmpty(dateTo)) {
							this.sql.append( " AND "+key+" >= '" + dateFrom + " 00:00:00' and "+key+" <= '" + dateFrom + " 23:59:59' ");
						}else {
							this.sql.append( " AND "+key+" >= '" + dateFrom + " 00:00:00' and "+key+" <= '" + dateTo + " 23:59:59' ");
						}
					}
				}
				if(DATE_FROM.equals(key)) {
					this.date(dateKey, row);
				}else {
					if(!Tools.isEmpty(value) && !DATE_TO.equals(key)) {
						this.sql.append( " and "+key+" like '%"+value.trim()+"%' ");
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
			this.sql.append( " where 1 = 1 ");
		}else {
			this.sql.append( " where " + condition);
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
				this.sql.append( " and "+key+" like '%"+value+"%' ");
			}else {
				this.sql.append( " and "+key+" "+op+" '"+value+"' ");
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
			this.sql.append( " and (");
			for (int i = 0; i < fields.length; i++) {
				if(i != 0) {
					this.sql.append( " or ");
				}
				if("%".equals(op)) {
					this.sql.append( fields[i] + " like '%"+value+"%' ");
				}else {
					this.sql.append( fields[i] + " "+op+" '"+value+"' ");
				}
			}
			this.sql.append( ") ");
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
		this.sql.append( field + " in ("+sb.toString()+") ");
		return this;
	}
	public DbTool empty(String field) {
		this.sql.append( " and ("+field+" is null or "+field+" = '') ");
		return this;
	}
	public DbTool notEmpty(String field) {
		this.sql.append( " and ("+field+" is not null and "+field+" != '') ");
		return this;
	}
	public DbTool date(String key, JSONObject row) throws Exception {
		String dateFrom = row.getString(DATE_FROM);
		if(!Tools.isEmpty(dateFrom)) {
			String dateTo = row.getString(DATE_TO);
			if(Tools.isEmpty(dateTo)) {
				this.sql.append( " AND "+key+" >= '" + dateFrom + " 00:00:00' and "+key+" <= '" + dateFrom + " 23:59:59' ");
			}else {
				this.sql.append( " AND "+key+" >= '" + dateFrom + " 00:00:00' and "+key+" <= '" + dateTo + " 23:59:59' ");
			}
		}
		return this;
	}
	
	/**
	 * 	排序
	 * @param sortDafault
	 * @param removeDafault: sybase不支持同一个字段进行2次排序，mysql，hana出现2次则以前一个为准
	 * @return
	 * @throws Exception
	 */
	public DbTool order(String... sortDafault) throws Exception {
		if(!Tools.isEmpty(this.sort) || !Tools.isEmpty(sortDafault)) {
			this.sql.append( " order by ");
			StringBuffer order_sql = new StringBuffer();
			if(!Tools.isEmpty(this.sort)) {
				String[] orders = this.order.split(",");
				String[] sorts = this.sort.split(",");
				for (int i = 0; i < sorts.length; i++) {
					if("asc".equals(orders[i].trim())) {
						order_sql.append( sorts[i] + " asc, ");
					}else {
						order_sql.append( sorts[i] + " desc, ");
					}
				}
			}
			if(!Tools.isEmpty(sortDafault)){
				for (int i = 0; i < sortDafault.length; i++) {
					String[] sorts = sortDafault[i].split(",");
					for (int j = 0; j < sorts.length; j++) {
						if(order_sql.indexOf(" "+ sorts[j].trim() +" ") < 0) {
							order_sql.append(sorts[j] + " asc, ");
						}
					}
				}
			}
			this.sql = this.sql.append(order_sql);
			this.sql = new StringBuffer( this.sql.substring(0, this.sql.lastIndexOf(",")));
		}
		return this;
	}
	public DbTool group(String group) throws Exception {
		if(!Tools.isEmpty(group)) {
			this.sql.append( " group by " + group);
		}
		return this;
	}
	public DbTool limit() throws Exception {
		if(!this.sql.toString().toLowerCase().contains("limit") && this.rows > 0)
		{
			this.sql.append( " limit " + this.rows);// rows 页面容量
			this.sql.append( " offset "+ ((this.page-1)*this.rows));// page 开始页
		}
		return this;
	}
	public String asTable(String table) throws Exception {
		String asSql = " ("+this.sql+") as ";
		if(Tools.isEmpty(table)) {
			asSql += "temp ";
		}else {
			asSql += table;
		}
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +" DbTool: "+ asSql.replaceAll(" +"," "));
		return asSql;
	}

	public String countSql() throws Exception {//hana不支持对order进行count，mysql支持
		String countSql = "select count(*) as count "+ this.sql.substring(this.sql.toString().toLowerCase().indexOf(" from "));
		if(countSql.toLowerCase().contains(" order ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" order "));
		}else if(countSql.toLowerCase().contains(" limit ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" limit "));
		}
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +" DbTool: "+ countSql.replaceAll(" +"," "));
		return countSql;
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
	public JSONArray sortData(JSONArray data) throws Exception {
		if(!Tools.isEmpty(data)) {
			/*if("asc".equals(this.order)) {
				data.sort(Comparator.comparing(obj -> ((JSONObject) obj).getString(this.sort)));
			}else if("desc".equals(this.order)){
				data.sort(Comparator.comparing(obj -> ((JSONObject) obj).getString(this.sort)).reversed());
			}*/
			JSONArray sortData = new JSONArray();
	        List<JSONObject> list = new ArrayList<JSONObject>();
	        for (int i = 0; i < data.size(); i++) {
	        	list.add(data.getJSONObject(i));
	        }
	        Collections.sort(list, (JSONObject a, JSONObject b)-> {
                String valA = a.getString(this.sort) == null? "" :a.getString(this.sort);
                String valB = b.getString(this.sort) == null? "" :b.getString(this.sort);
                //是升序还是降序
                if("asc".equals(this.order)) {
                	return valA.compareTo(valB);
                }else if("desc".equals(this.order)){
                    return valB.compareTo(valA);
                }else {
                	return 0;
                }
	        });
	        for (int i = 0; i < list.size(); i++) {
	        	sortData.add(list.get(i));
	        }
	        return sortData;
		}
		return data;
	}
	public JSONArray limitData(JSONArray data) throws Exception {
		if(!Tools.isEmpty(data)){
			int begin = (this.page-1)*this.rows;
			int end = (begin+this.rows) > data.size()?data.size():(begin+this.rows);
			JSONArray t = new JSONArray();
			for (int i = begin; i < end; i++) {
				t.add(data.getJSONObject(i));
			}
			return t;
			//return JSON.parseArray(JSON.toJSONString(data.subList(begin, end)));
		}
		return data;
	}
	/**
	 * 分割数组，分批插入
	 * @param table
	 * @param data
	 * @return
	 */
	public String[] getBatchSql(String table, JSONArray data, String username) throws Exception {
		int n = (int)Math.ceil((double)(data.size())/128);
		String[] s = new String[n];
		for (int i = 0; i < n; i++) {
			int begin=i*128;
			int end=((i+1)*128) > data.size() ? data.size() : (i+1)*128 ;
			//s[i] = ToolsSql.getInstance().insertBatch(table, JSON.parseArray(JSON.toJSONString(data.subList(begin, end))), username).getSql();
		}
		return s;
	}
	public static boolean addBatch(String table, JSONArray data) {
		int x = (int)Math.ceil((double)(data.size())/128);
		for (int j = 0; j < x; j++) {
			int a=j*128;
			int b=((j+1)*128) > data.size() ? data.size() : (j+1)*128 ;
			//db.addBatch(table, data.subList(a, b));
		}
		/*for (int i = 1; i <= data.size(); i++) {
			if((i & 127) == 0 || i == data.size()) {
				
			}
		}*/
		return false;
	}
	
	public String getSql() {
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +" DbTool: "+ this.sql.toString().replaceAll("\t", " ").replaceAll(" +"," "));
		return this.sql.toString();
	}
	public DbTool setSql(String sql) {
		this.sql = new StringBuffer(sql);
		return this;
	}


}
