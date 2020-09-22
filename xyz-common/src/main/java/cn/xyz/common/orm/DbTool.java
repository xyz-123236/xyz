package cn.xyz.common.orm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.alibaba.druid.sql.visitor.functions.Insert;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.pojo.Basic;
import cn.xyz.common.tools.Tools;
import cn.xyz.common.tools.ToolsDate;
import cn.xyz.common.tools.ToolsJson;
import cn.xyz.common.tools.ToolsString;

public class DbTool extends Basic {
	public static String[] DEFAULT_REMOVE_KEYS = {"page","rows","sort","order","jsp_name"};
	public static final String DEFAULT_JUDGE = "=";
	private StringBuffer sql = new StringBuffer();
	protected JSONObject columns = new JSONObject();
	protected JSONObject tables = new JSONObject();
	
	protected String table;
	protected String alias;
	protected String where;
	protected String limit;
	protected String on;
	protected String join_type;
	
	static {
		//初始化数据字典，把表字段/类型添加到缓存
	}
	protected DbTool() {}
	public DbTool(JSONObject obj) {
		if(obj != null) {
			this.rows = obj.getInteger("rows");
			this.page = obj.getInteger("page");
			this.sort = obj.getString("sort");
			this.order = obj.getString("order");
		}
	}
	public static DbTool getInstance() {
		return getInstance(null);
	}
	public static DbTool getInstance(JSONObject obj) {
		return new DbTool(obj);
	}
	
	public JSONArray insert(DbBase db, String table, JSONObject row, String create_by) throws Exception{
		return db.insert(createInsertSql(db, table, row, create_by));
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
	public boolean insertBatch(DbBase db, String table,JSONArray rows, String create_by) throws Exception {
		int n = (int)Math.ceil((double)(rows.size())/128);
		for (int i = 0; i < n; i++) {
			int begin=i*128;
			int end=((i+1)*128) > rows.size() ? rows.size() : (i+1)*128 ;
			String sqls[] = new String[end - begin + 1];
			for (int j = begin; j < end; j++) {
				sqls[j] = createInsertSql(db, table, rows.getJSONObject(j), create_by);
			}
			if(!db.insertBatch(sqls)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 插入和修改可以是null，不用赋初值，但类型还是需要（sybase不支持数字类型插入字符串）
	 * @param db
	 * @param table
	 * @param row
	 * @param create_by
	 * @return
	 * @throws Exception
	 */
	public String createInsertSql(DbBase db, String table, JSONObject row, String create_by) throws Exception{
		this.sql = new StringBuffer("");
		JSONObject obj = db.getFiledType(table);
		String number = ",INTEGER,BIGINT,FLOAT,INT,DOUBLE,";
		if(!Tools.isEmpty(row)) {
			String fileds = "";
			String values = "";
			if(!Tools.isEmpty(create_by)) {
				row.put("create_by", create_by);
				row.put("create_date", ToolsDate.getLongString());
			}   
			String primary_name = getPrimaryName(db, table);
			for(String key: row.keySet()){
				fileds += key + ",";
				Object v = row.get(key);
				String type = obj.getString(key);
				if(!Tools.isEmpty(v) && !Tools.isEmpty(type) && !key.equals(primary_name)) {
					if(number.indexOf(","+type+",") >= 0) {
						values += " "+v+",";
					}else {
						values += " '"+v+"',";
					}
				}
			}
			this.sql.append( "insert into "+table+" ("+fileds.substring(0,fileds.lastIndexOf(","))+ ") values ("+values.substring(0,values.lastIndexOf(","))+ ")");
		}else {
			throw new Exception("没有要插入的数据");
		}
		return this.sql.toString();
	}
	public String getPrimaryName(DbBase db, String table) throws Exception {
		/*JSONArray pk = db.getPrimaryKey(table);
		if(Tools.isEmpty(pk)) return null;
		String[] pks = new String[pk.size()];
		for (int i = 0; i < pk.size(); i++) {
			pks[i] = pk.getJSONObject(0).getString("COLUMN_NAME");
		}
		return pks;*/
		JSONArray pk = db.getPrimaryKey(table);
		if(!Tools.isEmpty(pk) && pk.size() == 1) {
			return pk.getJSONObject(0).getString("COLUMN_NAME");
		}
		return "";
	}
	
	public DbTool update(DbBase db, String table, JSONObject row, String update_by) throws Exception{
		createUpdateSql(db, table, row, update_by);
		return this;
	}
	
	public boolean updateById(DbBase db, String table, JSONObject row, String update_by) throws Exception{
		createUpdateSql(db, table, row, update_by);
		this.where().andPrimaryId(db, table, row);
		if(db.executeUpdate(getSql()) > 0) {
			return true;
		}
		return false;
	}
	
	public String createUpdateSql(DbBase db, String table, JSONObject row, String update_by) throws Exception{
		this.sql = new StringBuffer("");
		JSONObject obj = db.getFiledType(table);
		String number = ",INTEGER,BIGINT,FLOAT,INT,DOUBLE,";
		String string = ",VARCHAR,CHAR,";
		//String date = ",DATE,DATETIME,TIME,TIMESTAMP,";
		if(!Tools.isEmpty(row)) {
			String set = "";
			if(!Tools.isEmpty(update_by)) {
				row.put("update_by", update_by);
				row.put("update_date", ToolsDate.getLongString());
			}
			String primary_name = getPrimaryName(db, table);
			for(String key: row.keySet()){
				Object value = row.get(key);
				String type = obj.getString(key);
				if(!Tools.isEmpty(type) && !key.equals(primary_name)) {
					if(string.indexOf(","+type+",") >= 0) {
						set += key + "='"+(Tools.isEmpty(value)?"":value)+"',";
					}else if(number.indexOf(","+type+",") >= 0) {
						set += key + " = "+(Tools.isEmpty(value)?"0":value)+",";
					}else{
						set += key + "="+(Tools.isEmpty(value)?null:"'"+value+"'")+",";
					}
				}
			}
			this.sql.append( "update "+table+" set "+set.substring(0,set.lastIndexOf(",")));
		}else {
			throw new Exception("没有要修改的数据");
		}
		return this.sql.toString();
	}
	
	public DbTool update(String table) {
		this.sql.append("update ").append(table).append(" set ");
		return this;
	}
	public DbTool setString(String field, Object obj) throws Exception {
		return setObject(field, obj, false);
	}
	public DbTool setNumber(String field, Object obj) throws Exception {
		return setObject(field, obj, true);
	}
	public DbTool setObject(String field, Object obj, boolean isNumber) throws Exception {
		String value = getValue(field, obj);
		if(!Tools.isEmpty(this.sql.substring(this.sql.toString().indexOf(" set ")+5))) {
			this.sql.append(",");
		}
		if(!Tools.isEmpty(value)) {
			if(isNumber) {
				this.sql.append( " and "+field+" = "+value+" ");
			}else {
				this.sql.append( " and "+field+" = '"+value+"' ");
			}
			
		}
		return this;
	}
	public DbTool delete(String table) throws Exception {
		this.sql = new StringBuffer();
		this.sql.append("delete from ").append(table);
		return this;
	}
	
	public boolean deleteById(DbBase db, String table, JSONObject row) throws Exception {
		this.sql = new StringBuffer();
		String primary_name = getPrimaryName(db, table);
		this.sql.append("delete from ").append(table).append(" where ").append(primary_name).append(" = ").append(row.getInteger(primary_name));
		if(db.executeUpdate(this.sql.toString()) > 0) {
			return true;
		}
		return false;
	}
	
	public boolean deleteById(DbBase db, String table, JSONArray rows) throws Exception {
		this.sql = new StringBuffer();
		this.delete(table).where().andPrimaryId(db, table, rows);
		if(db.executeUpdate(this.sql.toString()) > 0) {
			return true;
		}
		return false;
	}
	
	public boolean deleteByIdLogic(DbBase db, String table, JSONObject row, String update_by) throws Exception {
		this.sql = new StringBuffer();
		this.update(table).setString("version", "D").where().andPrimaryId(db, table, row);
		if(db.executeUpdate(getSql()) > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * select("test") <=> select * from test
	 * select("test", "code,name,pwd") <=> select code,name,pwd from test
	 * @param table 表名 ("test"),("test t")
	 * @param fields 返回字段名  ("code,name,pwd"),("code","name","pwd"),("code","name,pwd")
	 * @return
	 * @throws Exception
	 */
	public DbTool select(String table, String...fields) throws Exception {
		this.sql = new StringBuffer("");
		if(Tools.isEmpty(fields)) {
			this.sql.append( "select * from "+ table);
		}else {
			this.sql.append( "select "+ String.join(",", fields)+" from "+ table);
			for (int i = 0; i < fields.length; i++) {
				int begin = 0;
				Stack<Integer> stack =new Stack<>();
				boolean flag = false;
				for (int j = 0; j < fields[i].length(); j++) {
					if(fields[i].charAt(j)=='('){
						flag = true;
						stack.push(j+1);
		        	}
					if(fields[i].charAt(j)==')'){
						String str = fields[i].substring(stack.pop(), j);
						if(str.indexOf("(") < 0) {
							addColumns(str.split(",")[0]);
						}
					}
					if(fields[i].charAt(j)==',' || j == fields[i].length()-1){
						if(flag) {
							if(Tools.isEmpty(stack)) {
								flag = false;
							}
						}else {
							addColumns(fields[i].substring(begin, (j == fields[i].length()-1)?j+1:j));
						}
						begin = j + 1;
					}
				}
			}
		}
		return this;
	}
	public void addColumns(String value) {
		String key = value.trim().split(" ")[0];
		String[] arr = key.split("\\.");
		if(arr.length == 2) {
			if("*".equals(arr[1])) {
				this.columns.put(arr[1], arr[0]+".");
			}else {
				this.columns.put(arr[1], key);
			}
		}else {
			if(!"*".equals(value)) {
				this.columns.put(key, key);
			}
		}
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
	public DbTool full(String table, String on) {
		this.sql.append( " full join " + table + " on " + on);
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
	/**
	 * :字段名不能以_from,_to结尾，用于范围查询(如果字段是数组，要用in)
	 * @param row 
	 * @param removeKey
	 * @return
	 * @throws Exception
	 */
	public DbTool where(JSONObject row, String...removeKey) throws Exception {
		//public DbTools where(JSONObject row, String... numberKey, String...removeKey) throws Exception {
		this.sql.append( " where 1 = 1 ");
		if(!Tools.isEmpty(row)) {
			JSONObject obj = ToolsJson.removeKey(row, DEFAULT_REMOVE_KEYS, removeKey);
			for(String key:obj.keySet()){
				String value = obj.getString(key);
				if(key.indexOf("_date_from") > 0) {
					this.andDate(key.substring(0, key.indexOf("_from")), row);
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
	public DbTool andPrimaryId(DbBase db, String table, JSONObject row) throws Exception {
		return this.andNumber(getPrimaryName(db, table), row);
	}
	public DbTool andPrimaryId(DbBase db, String table, JSONArray rows) throws Exception {
		return this.in(getPrimaryName(db, table), rows);
	}
	public DbTool in(String field, JSONArray rows){
		this.sql.append(" and ").append(field).append(" in (");
		for (int i = 0; i < rows.size(); i++) {
			if(i > 0) {
				this.sql.append(",");
			}
			this.sql.append(rows.getJSONObject(i).getInteger(field));
		}
		this.sql.append(")");
		return this;
	}
	public DbTool andString(String field, Object obj, String... judge) throws Exception {
		return this.andObject(field, obj, judge == null ? DEFAULT_JUDGE : judge[0], false);
	}
	public DbTool andNumber(String field, Object obj, String... judge) throws Exception {
		return this.andObject(field, obj, judge == null ? DEFAULT_JUDGE : judge[0], true);
	}
	public DbTool andObject(String field, Object obj, String judge, boolean isNumber) throws Exception {
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
	 * setDate用于时间范围（from、to）查找，单个时间查询用setString
	 * @param key
	 * @param row
	 * @return
	 * @throws Exception
	 */
	public DbTool andDate(String field, JSONObject row) throws Exception {
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
	public DbTool andSn(String field, JSONObject row) throws Exception {
		String[] arr = field.trim().split("\\.");
		String key = arr.length == 1? field: arr[1];
		String column = arr.length == 1? getTableKey(key): field;
		String from = row.getString(key+"_from");
		if(!Tools.isEmpty(from)) {
			String to = row.getString(key+"_to");
			if(Tools.isEmpty(to)) {
				this.sql.append( " AND "+column+" like '%" + from + "%' ");
			}else {
				this.sql.append( " AND "+column+" >= '" + from + "' and "+column+" <= '" + to + "' ");
			}
		}
		return this;
	}
	
	public JSONArray count(DbBase db) throws Exception {//hana不支持对order进行count，mysql支持
		String countSql = "select count(*) as count "+ this.sql.substring(this.sql.toString().toLowerCase().indexOf(" from "));
		if(countSql.toLowerCase().contains(" order ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" order "));
		}else if(countSql.toLowerCase().contains(" limit ")) {
			countSql=countSql.substring(0,countSql.toLowerCase().indexOf(" limit "));
		}
		return db.find(countSql);
	}
	public JSONArray sum(DbBase db, String projection, boolean deleteLimit) throws Exception {
		String _sql = this.sql.toString();
		int beginIndex = _sql.toLowerCase().indexOf(" from ");
		int endIndex1 = _sql.toLowerCase().indexOf(" order ") > 1 ? _sql.toLowerCase().indexOf(" order ") : this.sql.length();
		int endIndex2 = _sql.toLowerCase().indexOf(" limit ") > 1 ? _sql.toLowerCase().indexOf(" limit ") : this.sql.length();
		if(deleteLimit) {
			return db.find("select " + projection +_sql.substring(beginIndex, endIndex1 < endIndex2 ? endIndex1 : endIndex2));
		}else {
			return db.find("select " + projection +_sql.substring(beginIndex));
		}
	}
	
	public JSONArray sortData(JSONArray data) throws Exception {
		return Tools.sort(data,this.sort,this.order);
	}
	public JSONArray limitData(JSONArray data) throws Exception {
		return Tools.limit(data,this.page,this.rows);
	}
	
	
	
	
	public String getKey(String key) {
		String[] arr = key.trim().split("\\.");
		return arr.length == 1? key: arr[1];
	}
	public String getTableKey(String key) throws Exception {
		if(!Tools.isEmpty(this.columns)) {
			String column = this.columns.getString(key);
			if(Tools.isEmpty(column)) {
				String _column = this.columns.getString("*");
				if(!Tools.isEmpty(_column)) {
					return _column + key;
				}
			}else {
				return column;
			}
		}
		return key;
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
	public static String escape(String str) {
		return str.replaceAll("\"", "&quot;").replaceAll("'", "&apos;").replaceAll(" ", "&nbsp;")
				.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>");//.replaceAll("&", "&amp;")
	}
	/*private DbBase getDefaultDb() throws Exception {
		return DbBase.getDruid();
	}*/
	public String getSql() {
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +" DbTool: "+ this.sql.toString().replaceAll("\t", " ").replaceAll(" +"," "));
		return this.sql.toString();
	}
	public DbTool setSql(String sql) {
		this.sql = new StringBuffer(sql);
		return this;
	}
	public JSONObject clearNull(JSONObject row) throws Exception {
		Set<String> set = new HashSet<>();
		for(String key: row.keySet()){
			String value = row.getString(key);
			if(Tools.isEmpty(value)) {
				set.add(key);
			}
		}
		for(String key: set){
			row.remove(key);
		}
		return row;
	}
	public JSONArray find() throws Exception {
		return find(DbBase.DEFAULT_DB);
	}
	public JSONArray find(String db_name) throws Exception {
		return find(DbBase.getDruid(db_name));
	}
	public JSONArray find(DbBase db) throws Exception {
		return db.find(this.getSql());
	}
	public JSONObject get() throws Exception {
		return get(DbBase.DEFAULT_DB);
	}
	public JSONObject get(String db_name) throws Exception {
		return get(DbBase.getDruid(db_name));
	}
	public JSONObject get(DbBase db) throws Exception {
		return db.get(this.getSql());
	}
	
	public static void main(String[] args) {
		try {
			/*String a = " name as  n ";
			String[] arr = a.trim().split(" ");
			for (int i = 0; i < arr.length; i++) {
				System.out.println(arr[i]);
			}*/
			/*System.out.println(DbTool.getInstance().select("test").getSql());
			System.out.println(DbTool.getInstance().select("test", "code,name,pwd").getSql());
			System.out.println(DbTool.getInstance().select("test", "code","name","pwd").getSql());
			System.out.println(DbTool.getInstance().select("test", "code","name,pwd").getSql());*/
			//System.out.println(this.filds);
			/*DbTool dt = DbTool.getInstance().select("test t","t.*,a.str1 as cc,c.str1 as dd,a.str2,b.num1,b.num2,sum(CAST(c.ENTBY AS INTEGER)),ifnull(d.NUM5, 0)+ifnull(d.NUM6, 0)");
			System.out.println(dt.columns);
			System.out.println(dt.getSql());*/
			JSONObject row = new JSONObject();
			row.put("dat", "9'h");
			row.put("num", "9");
			row.put("ok", "9\'9");
			row.put("ng", "9\"99");
			row.put("pid", "9");
			row.put("ng", escape(row.getString("ng")));
			DbTool d = DbTool.getInstance();
			try {
				System.out.println(d.insert(DbBase.getJdbc(DbBase.DEFAULT_DB), "test3", row, null));
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(d.getSql());
			/*Stack<Integer> stack =new Stack<>();
			System.out.println(stack);
			System.out.println(Tools.isEmpty(stack));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
