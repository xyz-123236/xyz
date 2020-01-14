package cn.xyz.test.tools;

import com.alibaba.fastjson.JSONObject;

import cn.xyz.main.dao.DbBase;

public class ToolsSql {
	private String sql = "";
	private JSONObject obj;
	
	public ToolsSql(JSONObject obj) {
		this.setObj(obj);
	}
	
	public String find() throws Exception {
		return find(new DbBase("mysql"));
	}
	public String find(DbBase db) throws Exception {
		if(!sql.toLowerCase().contains("limit") && obj.getIntValue("rows") > 0)
		{
			sql += " limit " + obj.getIntValue("rows");// rows 页面容量
			sql += " offset "+ ((obj.getIntValue("page")-1)*obj.getIntValue("rows"));// page 开始页
		}
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +": "+ sql.replaceAll(" +"," "));
		return Result.successEasyui(db.executeQueryJson(sql), count(db));
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
		sql = "";
		sql += "select " + fields + " from " + table;
		return this;
	}
	public ToolsSql left(String table, String on) {
		sql += " left join " + table + " on " + on;
		return this;
	}
	public ToolsSql right(String table, String on) {
		sql += " right join " + table + " on " + on;
		return this;
	}
	public ToolsSql inner(String table, String on) {
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
	public ToolsSql where() throws Exception {
		return where("");
	}
	public ToolsSql where(String condition) throws Exception {
		if(Tools.isEmpty(condition)) {
			sql += " where 1 = 1 ";
		}else {
			sql += " where " + condition;
		}
		return this;
	}
	public ToolsSql and(String sql) {
		sql += " and " + sql;
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
				sql += " and "+key+" like '%"+value+"%' ";
			}else {
				sql += " and "+key+" "+op+" '"+value+"' ";
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
	public ToolsSql in(String sql) {
		sql += " in ("+sql+") ";
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
		sql += field + " in ("+sb.toString()+") ";
		return this;
	}
	public ToolsSql empty(String field) {
		sql += " and ("+field+" is null or "+field+" = '') ";
		return this;
	}
	public ToolsSql notEmpty(String field) {
		sql += " and ("+field+" is not null and "+field+" != '') ";
		return this;
	}
	public ToolsSql date(String key, JSONObject row) throws Exception {
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
	public ToolsSql sort(String sortDafault) throws Exception {
		String order = obj.getString("order");//升序还是降序
		String sort = obj.getString("sort");//排序的列
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
	private JSONObject deleteKey(JSONObject row, String...deleteKey) throws Exception {
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
		return sql;
	}

	public ToolsSql setSql(String sql) {
		this.sql = sql;
		return this;
	}

	public JSONObject getObj() {
		return obj;
	}

	public void setObj(JSONObject obj) {
		this.obj = obj;
	}
	
}
