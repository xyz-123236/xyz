package cn.xyz.common.orm;

import java.util.*;

import cn.xyz.common.config.Config;
import cn.xyz.common.pojo.Result;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.exception.CustomException;
import cn.xyz.common.tools.Tools;
import cn.xyz.common.tools.ToolsDate;
import cn.xyz.common.tools.ToolsJson;

public class DbTool extends Condition<DbTool> {
	public static final String DEFAULT_JUDGE = "=";
	//private StringBuffer sql = new StringBuffer();
	private List<String> sqls;
	protected JSONObject columns = new JSONObject();
	protected JSONObject tables = new JSONObject();
	protected DbBase db = null;
	public static final String DATE_FROM = "_date_from";
	public static final String DATE_TO = "_date_to";
	public static final String FROM = "_from";
	public static final String TO = "_to";
	/*protected String table;
	protected String alias;
	protected String where;
	protected String limit;
	protected String on;
	protected String join_type;*/
	public static JSONObject tables_info = new JSONObject();//缓存表信息
	//public static JSONObject config = new JSONObject();//缓存配置信息
	static {
		init();
	}
	public static void init(){
		DbBase db = DbBase.getDruid();
		try {
			//初始化表信息
			JSONArray tableNames = db.getTables(null,"fic_portal","%",null);
			for(int i = 0; i < tableNames.size(); i++){
				String table = tableNames.getJSONObject(i).getString("TABLE_NAME");
				JSONObject obj = new JSONObject();
				JSONArray data = db.getPrimaryKey("fic_portal", table);
				if(Tools.isEmpty(data)){
					System.out.println("没有主键"+table);
				}else{
					if(data.size() == 1){
						obj.put("primaryKey", db.getPrimaryKey("fic_portal", table).getJSONObject(0).getString("COLUMN_NAME"));
					}else{
						System.out.println("主键不是一列"+table);
					}
				}
				obj.put("filedType", db.getFiledType(table));
				tables_info.put(table, obj);
			}
			//初始化配置信息
			/*JSONArray data = db.select("select * from sys_config");
			for (int i = 0; i < data.size(); i++) {
				config.put(data.getJSONObject(i).getString("key"), data.getJSONObject(i).get("value"));
			}*/
		} catch (Exception e) {
			Result.error(e);
		}
	}
	protected DbTool() {}
	public DbTool(String db_name, JSONObject obj) {
		try {
			this.db = DbBase.getJdbc(db_name);
			if(obj != null) {
				this.rows = obj.getInteger("rows");
				this.page = obj.getInteger("page");
				this.sort = obj.getString("sort");
				this.order = obj.getString("order");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static DbTool getInstance() {
		return getInstance(DbBase.DEFAULT_DB, null);
	}
	public static DbTool getInstance(String db_name) {
		return getInstance(db_name, null);
	}
	public static DbTool getInstance(JSONObject obj) {
		return getInstance(DbBase.DEFAULT_DB, obj);
	}
	public static DbTool getInstance(String db_name, JSONObject obj) {
		return new DbTool(db_name, obj);
	}
	// 开启事务
	public DbTool startTransaction() throws Exception {
		this.db.startTransaction();
		return this;
	}
	// 回滚事务
	public DbTool rollback() {
		this.db.rollback();
		return this;
	}
	// 提交事务
	public DbTool commit() throws Exception {
		this.db.commit();
		return this;
	}

	public boolean create() throws Exception {
		return this.db.execute(this.sql.toString());
	}
	public Integer insert() throws Exception {
		return this.db.insert(this.sql.toString());
	}
	public boolean insertBatch() throws Exception {
		return this.db.insertBatch(this.sqls);
	}
	public int delete() throws Exception {
		if(this.sql.toString().toLowerCase().contains(" where ")){
			String sql = this.sql.toString().substring(this.sql.toString().toLowerCase().indexOf(" where ")+6).replace(" 1 = 1 ","").trim();
			if(Tools.isEmpty(sql)){
				throw new CustomException("删除不能没有条件1");
			}
		}else{
			throw new CustomException("删除不能没有条件2");
		}

		return this.db.executeUpdate(this.sql.toString());
	}
	public int update() throws Exception {
		return this.db.executeUpdate(this.sql.toString());
	}
	public JSONArray select() throws Exception {
		return this.db.select(this.sql.toString());
	}
	public JSONObject selectOne() throws Exception {
		String sql = this.sql.toString();
		if(sql.toLowerCase().contains(" limit ")){
			sql = sql.substring(0, sql.toLowerCase().indexOf(" limit ")) + " limit 1";
		}else{
			sql = sql + " limit 1";
		}
		return this.db.selectOne(sql);
	}
	public DbTool createTableSql(JSONObject obj) throws Exception {
		this.sql = new StringBuffer();
		JSONArray rows = obj.getJSONArray("rows");
		String table = obj.getString("table");
		String database = obj.getString("database");
		if(Tools.isEmpty(table)) throw new CustomException("表名不能为空");
		this.sql.append("CREATE TABLE ").append(database).append(".").append(table).append(" (\r\n\t");
		StringBuilder primary = new StringBuilder();
		StringBuilder unique = new StringBuilder();
		if(Tools.isEmpty(rows)) throw new CustomException("表字段不能为空");
		for (int i = 0; i < rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			String field = row.getString("field");
			String type = row.getString("type");
			String length = row.getString("length");
			String default_value = row.getString("default_value");
			String comment = row.getString("comment");
			if(Tools.isEmpty(field)) {
				throw new CustomException("列名不能为空");
			}else {
				this.sql.append(field).append(" ");
			}
			if(field.endsWith("_from") || field.endsWith("_to")){
				throw new CustomException("任何字段不能以_from,_to结尾，可使用begin,end");
			}
			if(Tools.isEmpty(type)) {
				throw new CustomException("类型为空");
			}
			if(Tools.isEmpty(length)) {
				this.sql.append(type).append(" ");
			}else {
				this.sql.append(type).append("(").append(length).append(") ");
			}
			if(row.getBooleanValue("not_null")) {
				this.sql.append("NOT NULL ");
			}
			if("0".equals(default_value)) {
				this.sql.append("DEFAULT 0 ");
			}else if("_null".equals(default_value)) {
				this.sql.append("DEFAULT null ");
			}else if("empty".equals(default_value)) {
				this.sql.append("DEFAULT '' ");
			}else if(!Tools.isEmpty(default_value)) {
				this.sql.append("DEFAULT '").append(default_value).append("' ");
			}
			if(row.getBooleanValue("auto_increment")) {
				this.sql.append("AUTO_INCREMENT ");
			}
			if(!Tools.isEmpty(comment)) {
				this.sql.append("COMMENT '").append(comment).append("' ");
			}
			if(row.getBooleanValue("unique")) {
				unique.append(field).append(",");
			}
			if(row.getBooleanValue("primary")) {
				primary.append(field).append(",");
			}
			this.sql.append(",\r\n\t");
		}
		if(!Tools.isEmpty(unique.toString())) {
			this.sql.append("UNIQUE KEY ").append(table).append("_index1 (").append(unique.replace(unique.lastIndexOf(","),unique.lastIndexOf(",")+1,"")).append("),\r\n\t");
		}
		if(Tools.isEmpty(primary.toString())) {
			throw new CustomException("不能没有主键");
		}
		this.sql.append("PRIMARY KEY (").append(primary.substring(0, primary.lastIndexOf(","))).append(")\r\n");
		this.sql.append(") ENGINE=").append(obj.getString("engine")).append(" DEFAULT CHARSET=").append(obj.getString("charset")).append(" COLLATE=").append(obj.getString("collate")).append("\r\n");
		return this;
	}

	//excel应限制上传格式：第一行中文名，第二行英文名
	public DbTool createInsertBatchSql(String table,JSONArray rows, String create_by) throws Exception {
		int n = (int)Math.ceil((double)(rows.size())/128);
		JSONObject filedType = this.db.getFiledType(table);
		String primary_name = getPrimaryName(this.db, table);
		String sql = createInsertKeySql(table, filedType);
		for (int i = 0; i < n; i++) {
			int begin=i*128;
			int end= Math.min(((i + 1) * 128), rows.size());
			for (int j = begin; j < end; j++) {
				//sqls.add(createInsertSql(table, rows.getJSONObject(j), create_by));
				this.sqls.add(sql + createInsertValueSql(rows.getJSONObject(j), create_by, filedType, primary_name));
			}
		}
		return this;
	}
	private static String createInsertKeySql(String table, JSONObject filedType) {
		StringBuilder sql = new StringBuilder("insert into ").append(table).append(" (");
		for(String key: filedType.keySet()){
			sql.append(key).append(",");
		}
		return sql.substring(0, sql.lastIndexOf(",")) + ") values ";
	}
	private static String createInsertValueSql(JSONObject row, String create_by,JSONObject filedType,String primary_name) {
		StringBuilder sql = new StringBuilder(" (");
		if(!Tools.isEmpty(create_by)) {
			row.put("create_by", create_by);
			row.put("create_date", ToolsDate.getLongString());
		}
		for(String key: filedType.keySet()){
			Object v = row.get(key);
			String type = filedType.getString(key);
			if(!Tools.isEmpty(v) && !Tools.isEmpty(type) && !key.equals(primary_name)) {
				if(Config.NUMBER_TYPES.contains("," + type + ",")) {
					sql.append(" ").append(v).append(",");
				}else {
					sql.append(" '").append(v).append("',");
				}
			}
		}
		return sql.substring(0, sql.lastIndexOf(",")) + ")";
	}
	//插入和修改可以是null，不用赋初值，但类型还是需要（sybase不支持数字类型插入字符串）
	public DbTool createInsertSql(String table, JSONObject row, String create_by) throws Exception{
		this.sql = new StringBuffer();
		if(!Tools.isEmpty(row)) {
			JSONObject filedType = this.db.getFiledType(table);
			String primary_name = getPrimaryName(this.db, table);
			StringBuilder fileds = new StringBuilder();
			StringBuilder values = new StringBuilder();
			if(!Tools.isEmpty(create_by)) {
				row.put("create_by", create_by);
				row.put("create_date", ToolsDate.getLongString());
			}
			for(String key: filedType.keySet()){

				Object v = row.get(key);
				String type = filedType.getString(key);
				//if(!Tools.isEmpty(v) && !Tools.isEmpty(type) && !key.equals(primary_name)) {
				if(!Tools.isEmpty(v) && !Tools.isEmpty(type)) {
					fileds.append(key).append(",");
					if(Config.NUMBER_TYPES.contains("," + type + ",")) {
						values.append(" ").append(v).append(",");
					}else {
						values.append(" '").append(v).append("',");
					}
				}
			}
			//this.sql.append(createInsertKeySql(table, filedType)).append(createInsertValueSql(row, create_by, filedType, primary_name));
			this.sql.append("insert into ").append(table).append(" (").append(fileds.substring(0, fileds.lastIndexOf(","))).append(") values (").append(values.toString(), 0, values.lastIndexOf(",")).append(")");
		}else {
			throw new Exception("没有要插入的数据");
		}
		return this;
	}


	public DbTool createUpdateSqlById(String table, JSONObject row, String update_by) throws Exception{
		createUpdateSql(table, row, update_by).where().andPrimaryId(table, row);
		return this;
	}

	public DbTool createUpdateSql(String table, JSONObject row, String update_by) throws Exception{
		this.createUpdateSql(table);
		JSONObject filedType = this.db.getFiledType(table);
		String number = ",INTEGER,BIGINT,FLOAT,INT,DOUBLE,";
		String string = ",VARCHAR,CHAR,";
		//String date = ",DATE,DATETIME,TIME,TIMESTAMP,";
		if(!Tools.isEmpty(row)) {
			StringBuilder set = new StringBuilder();
			if(!Tools.isEmpty(update_by)) {
				row.put("update_by", update_by);
				row.put("update_date", ToolsDate.getLongString());
			}
			String primary_name = getPrimaryName(this.db, table);
			for(String key: row.keySet()){
				Object value = row.get(key);
				String type = filedType.getString(key);
				if(!Tools.isEmpty(type) && !key.equals(primary_name)) {
					if(string.contains("," + type + ",")) {
						set.append(key).append("='").append(Tools.isEmpty(value) ? "" : value).append("',");
					}else if(number.contains("," + type + ",")) {
						set.append(key).append(" = ").append(Tools.isEmpty(value) ? "0" : value).append(",");
					}else{
						set.append(key).append("=").append(Tools.isEmpty(value) ? null : "'" + value + "'").append(",");
					}
				}
			}
			this.sql.append(set.substring(0, set.lastIndexOf(",")));
		}else {
			throw new Exception("没有要修改的数据");
		}
		return this;
	}
	public DbTool createUpdateNotNullSql(String table, JSONObject row, String update_by) throws Exception{
		this.createUpdateSql(table);
		JSONObject filedType = this.db.getFiledType(table);
		String number = ",INTEGER,BIGINT,FLOAT,INT,DOUBLE,";
		String string = ",VARCHAR,CHAR,";
		//String date = ",DATE,DATETIME,TIME,TIMESTAMP,";
		if(!Tools.isEmpty(row)) {
			StringBuilder set = new StringBuilder();
			if(!Tools.isEmpty(update_by)) {
				row.put("update_by", update_by);
				row.put("update_date", ToolsDate.getLongString());
			}
			String primary_name = getPrimaryName(this.db, table);
			for(String key: row.keySet()){
				Object value = row.get(key);
				String type = filedType.getString(key);
				if(!Tools.isEmpty(type) && !key.equals(primary_name) && !Tools.isEmpty(value)) {
					set.append(key).append("='").append(value).append("',");
				}
			}
			this.sql.append(set.substring(0, set.lastIndexOf(",")));
		}else {
			throw new Exception("没有要修改的数据");
		}
		return this;
	}
	public DbTool createUpdateNoNullSql(String table, JSONObject row, String update_by) throws Exception{
		this.createUpdateSql(table);
		JSONObject filedType = this.db.getFiledType(table);
		String number = ",INTEGER,BIGINT,FLOAT,INT,DOUBLE,";
		String string = ",VARCHAR,CHAR,";
		//String date = ",DATE,DATETIME,TIME,TIMESTAMP,";
		if(!Tools.isEmpty(row)) {
			StringBuilder set = new StringBuilder();
			if(!Tools.isEmpty(update_by)) {
				row.put("update_by", update_by);
				row.put("update_date", ToolsDate.getLongString());
			}
			String primary_name = getPrimaryName(this.db, table);
			for(String key: row.keySet()){
				Object value = row.get(key);
				String type = filedType.getString(key);
				if(!Tools.isEmpty(type) && !key.equals(primary_name) && !Tools.isEmpty(value)) {//值为空不修改
					if(string.contains("," + type + ",")) {
						set.append(key).append("='").append(Tools.isEmpty(value) ? "" : value).append("',");
					}else if(number.contains("," + type + ",")) {
						set.append(key).append(" = ").append(Tools.isEmpty(value) ? "0" : value).append(",");
					}else{
						set.append(key).append("=").append(Tools.isEmpty(value) ? null : "'" + value + "'").append(",");
					}
				}
			}
			this.sql.append(set.substring(0, set.lastIndexOf(",")));
		}else {
			throw new Exception("没有要修改的数据");
		}
		return this;
	}
	public DbTool createUpdateSql(String table) {
		this.sql = new StringBuffer().append("update ").append(table).append(" set ");
		return this;
	}
	public DbTool setString(String field, Object obj) {
		return setObject(field, obj, false);
	}
	public DbTool setNumber(String field, Object obj) {
		return setObject(field, obj, true);
	}
	public DbTool setObject(String field, Object obj, boolean isNumber) {
		String value = getValue(field, obj);
		if(!Tools.isEmpty(this.sql.substring(this.sql.toString().indexOf(" set ")+5))) {
			this.sql.append(",");
		}
		if(!Tools.isEmpty(value)) {
			if(isNumber) {
				this.sql.append(" and ").append(field).append(" = ").append(value).append(" ");
			}else {
				this.sql.append(" and ").append(field).append(" = '").append(value).append("' ");
			}

		}
		return this;
	}
	public DbTool createDeleteSql(String table) {
		this.sql = new StringBuffer().append("delete from ").append(table);
		return this;
	}

	public DbTool createDeleteSqlById(DbBase db, String table, JSONObject row) throws Exception {
		createDeleteSql(table).where().andPrimaryId(table, row);
		return this;
	}

	public DbTool createDeleteSqlByIdLogic(DbBase db, String table, JSONObject row, String update_by) throws Exception {
		this.createUpdateSql(table).setString("version", "D").where().andPrimaryId(table, row);
		return this;
	}
	public DbTool createSelectDistinctSql(String table, String...fields) {
		createSelectSql(table, fields);
		this.sql.replace(0, 6, "select distinct");
		return this;
	}
	/**
	 * select("test") <=> select * from test
	 * select("test", "code,name,pwd") <=> select code,name,pwd from test
	 * @param table 表名 ("test"),("test t")
	 * @param fields 返回字段名  ("code,name,pwd"),("code","name","pwd"),("code","name,pwd")
	 */
	public DbTool createSelectSql(String table, String...fields) {
		this.sql = new StringBuffer();
		if(Tools.isEmpty(fields)) {
			this.sql.append("select * from ").append(table);
		}else {
			this.sql.append("select ").append(String.join(",", fields)).append(" from ").append(table);
			for (String field : fields) {
				int begin = 0;
				Stack<Integer> stack = new Stack<>();
				boolean flag = false;
				for (int j = 0; j < field.length(); j++) {
					if (field.charAt(j) == '(') {
						flag = true;
						stack.push(j + 1);
					}
					if (field.charAt(j) == ')') {
						String str = field.substring(stack.pop(), j);
						if (!str.contains("(")) {
							addColumns(str.split(",")[0]);
						}
					}
					if (field.charAt(j) == ',' || j == field.length() - 1) {
						if (flag) {
							if (Tools.isEmpty(stack)) {
								flag = false;
							}
						} else {
							addColumns(field.substring(begin, (j == field.length() - 1) ? j + 1 : j));
						}
						begin = j + 1;
					}
				}
			}
		}
		return this;
	}
	private void addColumns(String value) {
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
		this.sql.append(" left join ").append(table).append(" on ").append(on);
		return this;
	}
	public DbTool left(DbTool dbTool, String on) {
		this.sql.append(" left join ").append(dbTool.getSql()).append(" on ").append(on);
		return this;
	}
	public DbTool right(String table, String on) {
		this.sql.append(" right join ").append(table).append(" on ").append(on);
		return this;
	}
	public DbTool right(DbTool dbTool, String on) {
		this.sql.append(" right join ").append(dbTool.getSql()).append(" on ").append(on);
		return this;
	}
	public DbTool inner(String table, String on) {
		this.sql.append(" inner join ").append(table).append(" on ").append(on);
		return this;
	}
	public DbTool inner(DbTool dbTool, String on) {
		this.sql.append(" inner join ").append(dbTool.getSql()).append(" on ").append(on);
		return this;
	}
	public DbTool full(String table, String on) {
		this.sql.append(" full join ").append(table).append(" on ").append(on);
		return this;
	}
	public DbTool full(DbTool dbTool, String on) {
		this.sql.append(" full join ").append(dbTool.getSql()).append(" on ").append(on);
		return this;
	}
	public DbTool union(DbTool dbTool, String on) {
		this.sql.append(" union ").append(dbTool.getSql());
		return this;
	}
	public DbTool unionAll(DbTool dbTool, String on) {
		this.sql.append(" union all ").append(dbTool.getSql());
		return this;
	}
	public DbTool where() {
		return where("");
	}
	public DbTool where(String condition) {
		if(Tools.isEmpty(condition)) {
			this.sql.append( " where 1 = 1 ");
		}else {
			this.sql.append(" where ").append(condition);
		}
		return this;
	}
	//字段名不能以_from,_to结尾，用于范围查询(如果字段是数组，要用in)
	public DbTool where(JSONObject row, String...removeKey) {
		//public DbTools where(JSONObject row, String... numberKey, String...removeKey) throws Exception {
		this.sql.append( " where 1 = 1 ");
		if(Tools.isEmpty(row)) return this;
		JSONObject obj = ToolsJson.removeKey(row, Config.DEFAULT_REMOVE_KEYS, removeKey);
		if(Tools.isEmpty(obj)) return this;
		for(String key:obj.keySet()){
			String value = obj.getString(key);
			if(key.endsWith("_date_from")) {
				this.andDate(key.substring(0, key.indexOf("_from")), row);
			}else if(key.endsWith("_from")) {
				String from = row.getString(key);
				if(!Tools.isEmpty(from)) {
					String to = row.getString(key.substring(0, key.indexOf("_from"))+"_to");
					String filed = getTableKey(key.substring(0, key.indexOf("_from")));
					if(Tools.isEmpty(to)) {
						this.sql.append(" AND ").append(filed).append(" like '%").append(from).append("%' ");
					}else {
						this.sql.append(" AND ").append(filed).append(" >= '").append(from).append("' and ").append(filed).append(" <= '").append(to).append("' ");
					}
				}
			}else {
				if(!Tools.isEmpty(value) && !key.contains("_to")) {
					String filed = getTableKey(key);
					this.sql.append(" and ").append(filed).append(" like '%").append(value.trim()).append("%' ");
				}
			}
		}
		return this;
	}
	public DbTool where(And and) {
		this.sql.append(WHERE).append(and.getSql());
		return this;
	}
	public DbTool where(Or or) {
		this.sql.append(WHERE).append(or.getSql());
		return this;
	}
	public void andPrimaryId(String table, JSONObject row) throws Exception {
		this.andNumber(getPrimaryName(this.db, table), row);
	}
	public void andPrimaryId(String table, JSONArray rows) throws Exception {
		this.in(getPrimaryName(this.db, table), rows);
	}
	private static String getPrimaryName(DbBase db, String table) throws Exception {
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
	public DbTool in(String field, String[] values){
		this.sql.append(" and ").append(field).append(" in (");
		for (int i = 0; i < values.length; i++) {
			if(i > 0) {
				this.sql.append(",");
			}
			this.sql.append(values[i]);
		}
		this.sql.append(")");
		return this;
	}
	public DbTool andString(String field, Object obj, String... judge) {
		return this.andObject(field, obj, judge == null ? DEFAULT_JUDGE : judge[0], false);
	}
	public void andNumber(String field, Object obj, String... judge) {
		this.andObject(field, obj, judge == null ? DEFAULT_JUDGE : judge[0], true);
	}
	public DbTool andObject(String field, Object obj, String judge, boolean isNumber) {
		String[] arr = field.trim().split("\\.");
		String key = arr.length == 1? field: arr[1];
		String column = arr.length == 1? getTableKey(key): field;
		String value = getValue(key, obj);
		if(!Tools.isEmpty(value)) {
			if(isNumber) {
				this.sql.append(" and ").append(column).append(" ").append(judge).append(" ").append(value).append(" ");
			}else {
				if("%".equals(judge)) {
					this.sql.append(" and ").append(column).append(" like '%").append(value).append("%' ");
				}else {
					this.sql.append(" and ").append(column).append(" ").append(judge).append(" '").append(value).append("' ");
				}
			}

		}
		return this;
	}
	//setDate用于时间范围（from、to）查找，单个时间查询用setString
	public void andDate(String field, JSONObject row) {
		String[] arr = field.trim().split("\\.");
		String key = arr.length == 1? field: arr[1];
		String column = arr.length == 1? getTableKey(key): field;
		String dateFrom = row.getString(key+"_from");
		if(!Tools.isEmpty(dateFrom)) {
			String dateTo = row.getString(key+"_to");
			if(Tools.isEmpty(dateTo)) {
				this.sql.append(" AND ").append(column).append(" >= '").append(dateFrom).append(" 00:00:00' and ").append(column).append(" <= '").append(dateFrom).append(" 23:59:59' ");
			}else {
				this.sql.append(" AND ").append(column).append(" >= '").append(dateFrom).append(" 00:00:00' and ").append(column).append(" <= '").append(dateTo).append(" 23:59:59' ");
			}
		}
	}
	public DbTool andSn(String field, JSONObject row) {
		String[] arr = field.trim().split("\\.");
		String key = arr.length == 1? field: arr[1];
		String column = arr.length == 1? getTableKey(key): field;
		String from = row.getString(key+"_from");
		if(!Tools.isEmpty(from)) {
			String to = row.getString(key+"_to");
			if(Tools.isEmpty(to)) {
				this.sql.append(" AND ").append(column).append(" like '%").append(from).append("%' ");
			}else {
				this.sql.append(" AND ").append(column).append(" >= '").append(from).append("' and ").append(column).append(" <= '").append(to).append("' ");
			}
		}
		return this;
	}
	public DbTool order(String sortDafault) {
		return order(sortDafault, false);
	}

	/**
	 *
	 * @param sortDafault 默认排序字段
	 * @param removeDafault sybase不支持同一个字段进行2次排序，mysql，hana出现2次则以前一个为准
	 * @return DbTool
	 */
	public DbTool order(String sortDafault, boolean removeDafault) {
		if(!Tools.isEmpty(this.sort) || !Tools.isEmpty(sortDafault)) {
			this.sql.append( " order by ");
			if(!Tools.isEmpty(this.sort)) {
				String[] orders = this.order.split(",");
				String[] sorts = this.sort.split(",");
				for (int i = 0; i < sorts.length; i++) {
					this.sql.append(getTableKey(sorts[i])).append(" ").append(orders[i].trim()).append(", ");
				}
			}
			if(!Tools.isEmpty(sortDafault) && (Tools.isEmpty(this.sort) || !removeDafault)) {
				this.sql.append(sortDafault);
			}else {
				sql = new StringBuffer(sql.substring(0, sql.lastIndexOf(",")));
			}
		}
		return this;
	}
	public DbTool group(String group) {
		if(!Tools.isEmpty(group)) {
			this.sql.append(" group by ").append(group);
		}
		return this;
	}
	public DbTool limit() {
		if(!sql.toString().toLowerCase().contains(" limit ") && this.rows > 0)
		{
			this.sql.append(" limit ").append(this.rows);
			this.sql.append(" offset ").append((this.page - 1) * this.rows);
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
		return db.select(countSql);
	}
	public JSONArray sum(DbBase db, String projection, boolean deleteLimit) throws Exception {
		String _sql = this.sql.toString();
		int beginIndex = _sql.toLowerCase().indexOf(" from ");
		int endIndex1 = _sql.toLowerCase().indexOf(" order ") > 1 ? _sql.toLowerCase().indexOf(" order ") : this.sql.length();
		int endIndex2 = _sql.toLowerCase().indexOf(" limit ") > 1 ? _sql.toLowerCase().indexOf(" limit ") : this.sql.length();
		if(deleteLimit) {
			return db.select("select " + projection +_sql.substring(beginIndex, Math.min(endIndex1, endIndex2)));
		}else {
			return db.select("select " + projection +_sql.substring(beginIndex));
		}
	}




	public JSONArray sortData(JSONArray data) {
		return Tools.sort(data,this.sort,this.order);
	}
	public JSONArray limitData(JSONArray data) {
		return Tools.limit(data,this.page,this.rows);
	}




	public String getKey(String key) {
		String[] arr = key.trim().split("\\.");
		return arr.length == 1? key: arr[1];
	}
	public String getTableKey(String key) {
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
	public String getValue(String key, Object obj) {
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

	public String getSql() {
		System.out.println(ToolsDate.getString("yyyy-MM-dd HH:mm:ss.SSS") +" DbTool: "+ this.sql.toString().replaceAll("\t", " ").replaceAll(" +"," "));
		return this.sql.toString();
	}
	public DbTool setSql(String sql) {
		this.sql = new StringBuffer(sql);
		return this;
	}
	public void clearNull(JSONObject row) {
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
	}

	public static void main(String[] args) {
		try {
			//Basic bean = new Basic();
			//DbTool.getInstance().b(bean);
			//SysUser bean2 = new SysUser();
			//DbTool.getInstance().clearNull(new JSONObject());
			//System.out.println(DbTool.config);
			JSONObject obj = new JSONObject();
			obj.put("table", "t6");
			obj.put("database", "xyz");
			obj.put("engine", "InnoDB");
			obj.put("charset", "utf8");
			obj.put("collate", "utf8_general_ci");
			JSONArray rows = new JSONArray();
			for (int i = 0; i < 10; i++) {
				JSONObject row = new JSONObject();
				row.put("field", "f"+i);
				if(i==0) {
					row.put("type", "bigint");
					row.put("auto_increment", true);
					row.put("not_null", true);
					row.put("primary", true);
				}else {
					row.put("type", "varchar");
				}
				if(i==2 || i== 3) {
					row.put("unique", true);
				}
				row.put("length", 10+i);
				row.put("default_value", 0);
				row.put("comment", "备注"+i);
				rows.add(row);
			}
			obj.put("rows", rows);
			System.out.println(DbTool.getInstance().createTableSql(obj).getSql());
			//DbTool.getInstance().createTable(DbBase.getJdbc(), obj);
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
			/*JSONObject row = new JSONObject();
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
			System.out.println(d.getSql());*/
			/*Stack<Integer> stack =new Stack<>();
			System.out.println(stack);
			System.out.println(Tools.isEmpty(stack));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
