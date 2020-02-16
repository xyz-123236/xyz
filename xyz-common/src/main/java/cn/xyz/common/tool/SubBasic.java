package cn.xyz.common.tool;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

public class SubBasic {
	/**
	 * 添加
	 * @param db
	 * @param row 添加的数据
	 * @param deleteKey 需要清除的key
	 * @return
	 * @throws Exception
	 */
	/*public long add(DBTool db, JSONObject row, String...deleteKey) throws Exception {
		return db.set(handleAdd(row, deleteKey)).insert();
	}*/
	/**
	 * 修改
	 * @param db
	 * @param id 修改的id的字段名，不是值
	 * @param row 修改的数据
	 * @param deleteKey 需要清除的key
	 * @return
	 * @throws Exception
	 */
	/*public long update(DBTool db, String id, JSONObject row, String...deleteKey) throws Exception {
		return db.eq(id, row.getString(id)).set(handleUpdate(row, deleteKey)).update();
	}
	public JSONArray find(DBTool db, JSONObject row, String sort) {
		return getEasyData(initFind(db, row, sort, null));
	}
	public JSONArray find(DBTool db, JSONObject row, String sort, String dateKey) {
		return getEasyData(initFind(db, row, sort, dateKey));
	}
	public JSONArray find(String sql, JSONObject row, String sort) {
		return getEasyData(initFind(sql, row, sort, null));
	}
	public JSONArray find(String sql, JSONObject row, String sort, String dateKey) {
		return getEasyData(initFind(sql, row, sort, dateKey));
	}
	
	public DBTool initFind(DBTool db, JSONObject row, String sort) {
		return initFind(db, row, sort, null);
	}*/
	//处理查询参数
	/*public DBTool initFind(DBTool db, JSONObject row, String sort, String dateKey) {
		if(db == null) db = DBTool.getInstance();
		if(Tools.isValidJSON(row)) {
			row.remove("timestamp");
			row.remove("recordTime");
			for(String key:row.keySet()){
				String value = row.getString(key);
				if("dateFrom".equals(key)) {
					if(!Tools.isEmpty(value)) {
						String dateTo = row.getString("dateTo");
						if(Tools.isEmpty(dateTo)) {
							db.eq(dateKey, value);
						}else {
							db.gte(dateKey, value)
							.lte(dateKey, dateTo);
						}
					}
				}else if("dateTo".equals(key)) {
					
				}else {
					if(!Tools.isEmpty(value)) {
						db.like(key, "%"+value.trim()+"%");
					}
				}
			}
		}
		return addSort(db, sort);
	}*/
	public String initFind(String sql, JSONObject row, String sort) throws Exception {
		return initFind(sql, row, sort, null);
	}
	public String initFind(String sql, JSONObject row, String sort, String dateKey) throws Exception {
		if(Tools.isEmpty(row)) {
			row.remove("timestamp");
			row.remove("recordTime");
			for(String key:row.keySet()){
				String value = row.getString(key);
				if("dateFrom".equals(key)) {
					if(!Tools.isEmpty(value)) {
						String dateTo = row.getString("dateTo");
						if(Tools.isEmpty(dateTo)) {
							sql += " AND "+dateKey+" = '" + value + "' ";
						}else {
							sql += " AND "+dateKey+" >= '" + value + "' and "+dateKey+" <= '" + dateTo + "' ";
						}
					}
				}else if("dateTo".equals(key)) {
					
				}else {
					if(!Tools.isEmpty(value)) {
						sql += " and "+key+" like '%"+value.trim()+"%' ";
					}
				}
			}
		}
		return sql + addSort(sort);
	}
	//默认排序靠后
	/*public DBTool addSort(DBTool db, String str) {
		String order = getAction().getOrder();
		String sort2 = getAction().getSort();
		if(!Tools.isEmpty(sort2)) {
			if("asc".equals(order)) {
				db.ascending(sort2);
			}else {
				db.descending(sort2);
			}
		}
		String[] sorts = str.trim().split(",");
		for (int i = 0; i < sorts.length; i++) {
			sorts[i] = sorts[i].trim();
		}
		if(sorts != null) db.ascending(sorts);
		return db;
	}*/
	/**
	 * 添加排序，优先添加前端排序
	 * @param sorts 默认排序
	 * @return
	 * @throws Exception 
	 */
	public String addSort(String sorts) throws Exception {
		String order = "";
		String sort = "";
		if(Tools.isEmpty(sort) && Tools.isEmpty(sorts)) {
			return "";
		}else {
			String sql = " order by ";
			if(!Tools.isEmpty(sort)) {
				if("asc".equals(order)) {
					sql += sort + " asc, ";
				}else {
					sql += sort + " desc, ";
				}
			}
			if(!Tools.isEmpty(sorts)) sql += sorts;
			return sql;
		}
	}
	
	
	//清除空格：用于表单提交前后有空格
	public JSONObject clearSpace(JSONObject row) {
		for(String key:row.keySet()){
			String value = row.getString(key);
			if(value != null) {
				row.put(key, value.trim());
			}
		}
		return row;
	}
	//清除空key
	public JSONObject clearNull(JSONObject row) throws Exception {
		for(String key:row.keySet()){
			String value = row.getString(key);
			if(Tools.isEmpty(value)) {
				row.remove(key);
			}
		}
		return row;
	}
	//把空字符串转为null：用于数字类型，空字符串不能添加
	public JSONObject toNull(JSONObject row) {
		for(String key:row.keySet()){
			String value = row.getString(key);
			if(value != null && value.trim() == "") {
				row.put(key, null);
			}
		}
		return row;
	}
	//insert时添加默认字段；deleteKey清除一些不用添加的字段
	public JSONObject handleAdd(JSONObject row, String...deleteKey) {
		row.remove("timestamp");
		row.remove("recordTime");
		row.put("entby", "");
		row.put("entdate", new Date());
		if(deleteKey != null && deleteKey.length > 0) {
			for (int i = 0; i < deleteKey.length; i++) {
				row.remove(deleteKey[i]);
			}
		}
		return row;
	}
	//update时添加默认字段；deleteKey清除一些不用添加的字段
	public JSONObject handleUpdate(JSONObject row, String...deleteKey) {
		row.remove("timestamp");
		row.remove("recordTime");
		row.put("modifyby", "");
		row.put("modifydate", new Date());
		if(deleteKey != null && deleteKey.length > 0) {
			for (int i = 0; i < deleteKey.length; i++) {
				row.remove(deleteKey[i]);
			}
		}
		return row;
	}

	/**
	 * @param key 字段、表别名.字段
	 * @param row 查询的表单数据
	 * @param op 条件符号
	 * @return
	 * @throws Exception 
	 */
	public String addCondition(JSONObject row, String key, String... ops) throws Exception {
		String op = "%";
		if(ops != null && ops.length > 0) op = ops[0];
		String filed = getFiled(key);
		if(!Tools.isEmpty(row.getString(filed))) {
			if("%".equals(op)) {
				return " and "+key+" like '%"+row.getString(filed).trim()+"%' ";
			}else {
				return " and "+key+" "+op+" '"+row.getString(filed).trim()+"' ";
			}
		}
		return "";
	}
	public String addConditionOr(JSONObject row, String key, String... fileds) throws Exception {
		if(!Tools.isEmpty(row.getString(key))) {
			String sql = " and (";
			for (int i = 0; i < fileds.length; i++) {
				if(i != 0) {
					sql += " or ";
				}
				sql += fileds[i] + " like '%"+row.getString(key)+"%' ";
			}
			sql += ") ";
			return sql;
		}
		return "";
	}
	public String addConditionDate(JSONObject row, String key) throws Exception {
		String dateFrom = row.getString("dateFrom");
		if(Tools.isEmpty(dateFrom)) dateFrom = row.getString("datefrom");
		if(!Tools.isEmpty(dateFrom)) {
			String dateTo = row.getString("dateTo");
			if(Tools.isEmpty(dateTo)) dateTo = row.getString("dateto");
			if(Tools.isEmpty(dateTo)) {
				return " AND "+key+" = '" + dateFrom + "' ";
			}else {
				return " AND "+key+" >= '" + dateFrom + "' and "+key+" <= '" + dateTo + "' ";
			}
		}
		return "";
	}
	public String getFiled(String key) {
		String[] arr = key.trim().split("\\.");
		String filed = key;
		if(arr.length == 2) {
			filed = arr[1];
		}
		return filed;
	}
	//
	/*public void result(JSONArray data) {
		result(data,null);
	}
	public void result(String msg) {
		putResultSuccess(msg);
	}
	public void result(JSONArray data, String msg) {
		if(Tools.isValidJSON(data) && Tools.isEmpty(msg)) {
			putResultSuccess(Tools.formatJSONDate(data, "yyyy-MM-dd HH:mm:ss"));
		}else if(Tools.isValidJSON(data) && !Tools.isEmpty(msg)) {
			putResultSuccess(Tools.formatJSONDate(data, "yyyy-MM-dd HH:mm:ss"), msg);
		}
	}
	public void result(JSONObject data) {
		result(data,null);
	}
	public void result(JSONObject data, String msg) {
		if(Tools.isValidJSON(data) && Tools.isEmpty(msg)) {
			putResultSuccess(Tools.formatJSONDate(data, "yyyy-MM-dd HH:mm:ss"));
		}else if(Tools.isValidJSON(data) && !Tools.isEmpty(msg)) {
			putResultSuccess(Tools.formatJSONDate(data, "yyyy-MM-dd HH:mm:ss"), msg);
		}
	}*/
}
