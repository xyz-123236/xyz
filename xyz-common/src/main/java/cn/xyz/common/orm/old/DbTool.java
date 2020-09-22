package cn.xyz.common.orm.old;

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
	

	public JSONArray find() throws Exception {
		return find(DbBase.getDruid());
	}
	public JSONArray find(DbBase db) throws Exception {
		if(!this.sql.toString().toLowerCase().contains("limit") && this.rows > 0){
			this.sql.append(" limit " + this.rows);// rows 页面容量
			this.sql.append(" offset "+ ((this.page-1)*this.rows));// page 开始页
		}
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +": "+ this.sql.toString().replaceAll(" +"," "));
		//return Result.successEasyui(db.executeQueryJson(sql), count(db));
		return db.find(this.sql.toString());
	}
	public Integer count() throws Exception {
		return count(DbBase.getDruid());
	}
	public Integer count(DbBase db) throws Exception {
		String countSql = "select count(*) as count "+ this.sql.substring(this.sql.toString().toLowerCase().indexOf(" from "));
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
	public DbTool in(String field, String... str){
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

	

}
