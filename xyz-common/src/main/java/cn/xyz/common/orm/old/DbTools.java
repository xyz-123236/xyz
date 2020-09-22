package cn.xyz.common.orm.old;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.pojo.Basic;
import cn.xyz.common.tools.Tools;
import cn.xyz.common.tools.ToolsDate;
import cn.xyz.common.tools.ToolsJson;
import cn.xyz.common.tools.ToolsString;

public class DbTools extends Basic {
	public static String[] DEFAULT_REMOVE_KEYS = {"page","rows","sort","order","jsp_name"};
	public static final String DEFAULT_JUDGE = "=";
	private StringBuffer sql = new StringBuffer();
	protected JSONObject columns = new JSONObject();
	
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
	
	public String insert(DbBase db, String table, JSONObject row, String create_by, boolean remove, Object...keys) throws Exception{
		this.sql = new StringBuffer("");
		JSONObject obj = db.getFiledType(table);
		JSONArray pk = db.getPrimaryKey(table);
		String id_name = "";
		if(Tools.isEmpty(pk)) {
			id_name = pk.getJSONObject(0).getString("COLUMN_NAME");
		}
		String string = ",VARCHAR,CHAR,";
		String number = ",INTEGER,BIGINT,FLOAT,INT,DOUBLE,";
		//String date = ",DATE,DATETIME,TIME,TIMESTAMP,";
		if(!Tools.isEmpty(row)) {
			String key = "";
			String value = "";
			if(remove) {
				if(!Tools.isEmpty(create_by)) {
					row.put("create_by", create_by);
					row.put("create_date", ToolsDate.getLongString());
				}
				ToolsJson.removeKey(obj, id_name, "update_by,update_date");
				for(String str: obj.keySet()){
					key += str + ",";
					Object v = row.get(str);
					if(string.indexOf(","+obj.getString(str)+",") >= 0) {
						value += " '"+(Tools.isEmpty(v)?"":v)+"',";
					}else if(number.indexOf(","+obj.getString(str)+",") >= 0) {
						value += " "+(Tools.isEmpty(v)?"0":v)+",";
					}else if(!Tools.isEmpty(v)) {
						value += "'"+row.get(str) + "',";
					}
				}
			}else {
				for (int i = 0; i < keys.length; i++) {
					key += keys[i] + ",";
					Object v = row.get(keys[i]);
					if(string.indexOf(","+obj.get(keys[i])+",") >= 0) {
						value += " '"+(Tools.isEmpty(v)?"":v)+"',";
					}else if(number.indexOf(","+obj.get(keys[i])+",") >= 0) {
						value += " "+(Tools.isEmpty(v)?"0":v)+",";
					}else if(!Tools.isEmpty(v)) {
						value += "'"+row.get(keys[i]) + "',";
					}
				}
				if(!Tools.isEmpty(create_by)) {
					key += "create_by" + ",";
					value += "'"+create_by + "',";
					key += "create_date" + ",";
					value += "'"+ToolsDate.getLongString() + "',";
				}
			}
			this.sql.append( "insert into "+table+" ("+key.substring(0,key.lastIndexOf(","))+ ") values ("+value.substring(0,value.lastIndexOf(","))+ ")");
		}else {
			throw new Exception("没有要插入的数据");
		}
		return this.sql.toString();
	}
	/**
	 * excel应限制上传格式：第一行中文名，第二行英文名
	 * @param db
	 * @param table
	 * @param rows
	 * @param create_by
	 * @param remove
	 * @param keys
	 * @throws Exception
	 */
	public void insertBatch(DbBase db, String table,JSONArray rows, String create_by, boolean remove, Object...keys) throws Exception {
		this.sql = new StringBuffer("");
		int n = (int)Math.ceil((double)(rows.size())/128);
		for (int i = 0; i < n; i++) {
			int begin=i*128;
			int end=((i+1)*128) > rows.size() ? rows.size() : (i+1)*128 ;
			String sqls[] = new String[end - begin + 1];
			for (int j = begin; i < end; j++) {
				sqls[j] = insert(db, table, rows.getJSONObject(j), create_by, remove, keys);
			}
		}
	}
	
	
	/**
	 * select("test") === select * from test
	 * select("test", "code,name,pwd") === select code,name,pwd from test
	 * @param table 表名 ("test"),("test t")
	 * @param fields 返回字段名  ("code,name,pwd"),("code","name","pwd"),("code","name,pwd")
	 * @return
	 * @throws Exception
	 */
	public DbTools select(String table, String...fields) throws Exception {
		this.sql = new StringBuffer("");
		if(Tools.isEmpty(fields)) {
			this.sql.append( "select * from "+ table);
		}else {
			this.sql.append( "select "+ String.join(",",fields)+" from "+ table);
			for (int i = 0; i < fields.length; i++) {
				String[] fs = fields[i].split(",");
				for (int j = 0; j < fs.length; j++) {
					String key = fs[j].trim().split(" ")[0];
					String[] arr = key.split("\\.");
					if(arr.length == 2) {
						this.columns.put(arr[1], key);
					}
				}
			}
		}
		return this;
	}
	public DbTools left(String table, String on) {
		this.sql.append( " left join " + table + " on " + on);
		return this;
	}
	public DbTools right(String table, String on) {
		this.sql.append( " right join " + table + " on " + on);
		return this;
	}
	public DbTools inner(String table, String on) {
		this.sql.append( " inner join " + table + " on " + on);
		return this;
	}
	public DbTools full(String table, String on) {
		this.sql.append( " full join " + table + " on " + on);
		return this;
	}
	public DbTools where() throws Exception {
		return where("");
	}
	public DbTools where(String condition) throws Exception {
		if(!Tools.isEmpty(condition)) {
			this.sql.append( " where 1 = 1 ");
		}else {
			this.sql.append( " where " + condition);
		}
		return this;
	}
	/**
	 * :字段名不能以_from,_to结尾，用于范围查询
	 * @param row 
	 * @param removeKey
	 * @return
	 * @throws Exception
	 */
	public DbTools where(JSONObject row, String...removeKey) throws Exception {
		//public DbTools where(JSONObject row, String... numberKey, String...removeKey) throws Exception {
		this.sql.append( " where 1 = 1 ");
		if(!Tools.isEmpty(row)) {
			JSONObject obj = ToolsJson.removeKey(row, DEFAULT_REMOVE_KEYS, removeKey);
			for(String key:obj.keySet()){
				String value = obj.getString(key);
				if(key.indexOf("_date_from") > 0) {
					this.setDate(key.substring(0, key.indexOf("_from")), row);
				}else if(key.indexOf("_from") > 0) {
					String from = row.getString(key);
					if(!Tools.isEmpty(from)) {
						String to = row.getString(key.substring(0, key.indexOf("_from"))+"_to");
						String filed = getTableKey(key.substring(0, key.indexOf("_from")));
						if(Tools.isEmpty(to)) {
							this.sql.append( " AND "+filed+" like '%" + from + "%' ");
						}else {
							this.sql.append( " AND "+filed+" >= '" + from + "' and "+filed+" <= '" + to + "' ");
						}
					}
				}else {
					if(!Tools.isEmpty(value) && key.indexOf("_to") < 0) {
						String filed = getTableKey(key);
						this.sql.append( " and "+filed+" like '%"+value.trim()+"%' ");
					}
				}
			}
		}
		return this;
	}
	
	public DbTools setString(String field, Object obj, String... judge) throws Exception {
		return setObject(field, obj, judge == null ? DEFAULT_JUDGE : judge[0], false);
	}
	public DbTools setNumber(String field, Object obj, String... judge) throws Exception {
		return setObject(field, obj, judge == null ? DEFAULT_JUDGE : judge[0], true);
	}
	public DbTools setObject(String field, Object obj) throws Exception {
		return setObject(field, obj, DEFAULT_JUDGE, false);
	}
	public DbTools setObject(String field, Object obj, String judge) throws Exception {
		return setObject(field, obj, judge, false);
	}
	public DbTools setObject(String field, Object obj, String judge, boolean isNumber) throws Exception {
		String[] arr = field.trim().split("\\.");
		String key = arr.length == 1? field: arr[1];
		String column = arr.length == 1? getTableKey(key): field;
		String value = getValue(key, obj);
		if(!Tools.isEmpty(value)) {
			if(isNumber) {
				this.sql.append( " and "+column+" "+judge+" "+value+" ");
			}else {
				if("%".equals(judge)) {
					this.sql.append( " and "+column+" like '%"+value+"%' ");
				}else {
					this.sql.append( " and "+column+" "+judge+" '"+value+"' ");
				}
			}
			
		}
		return this;
	}
	/**
	 * D用于时间范围（from、to）查找，单个时间查询用setString
	 * @param key
	 * @param row
	 * @return
	 * @throws Exception
	 */
	public DbTools setDate(String field, JSONObject row) throws Exception {
		String[] arr = field.trim().split("\\.");
		String key = arr.length == 1? field: arr[1];
		String column = arr.length == 1? getTableKey(key): field;
		String dateFrom = row.getString(key+"_from");
		if(!Tools.isEmpty(dateFrom)) {
			String dateTo = row.getString(key+"_to");
			if(Tools.isEmpty(dateTo)) {
				this.sql.append( " AND "+column+" >= '" + dateFrom + " 00:00:00' and "+column+" <= '" + dateFrom + " 23:59:59' ");
			}else {
				this.sql.append( " AND "+column+" >= '" + dateFrom + " 00:00:00' and "+column+" <= '" + dateTo + " 23:59:59' ");
			}
		}
		return this;
	}
	
	public String getKey(String key) {
		String[] arr = key.trim().split("\\.");
		return arr.length == 1? key: arr[1];
	}
	public String getTableKey(String key) {
		String column = this.columns.getString(key);
		return column == null? key: column;
	}
	public String getValue(String key, Object obj) throws Exception {
		if(Tools.isEmpty(obj)) {
			return null;
		}else if(obj instanceof JSONObject){
			return ((JSONObject)obj).getString(key).trim();
        }else {
        	return obj.toString().trim();
        }
	}
	public String getSql() {
		return this.sql.toString();
	}
	public DbTools setSql(String sql) {
		this.sql = new StringBuffer(sql);
		return this;
	}

	public static void main(String[] args) {
		try {
			/*String a = " name as  n ";
			String[] arr = a.trim().split(" ");
			for (int i = 0; i < arr.length; i++) {
				System.out.println(arr[i]);
			}*/
			System.out.println(DbTools.getInstance().select("test").getSql());
			System.out.println(DbTools.getInstance().select("test", "code,name,pwd").getSql());
			System.out.println(DbTools.getInstance().select("test", "code","name","pwd").getSql());
			System.out.println(DbTools.getInstance().select("test", "code","name,pwd").getSql());
			//System.out.println(this.filds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
