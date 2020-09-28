package cn.xyz.main.service.sys;

import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.annotation.Service;
import cn.xyz.common.orm.DbBase;
import cn.xyz.common.orm.DbTool;
@Service
public class SysTableImpl implements SysTable {

	@Override
	public boolean createTable(JSONObject obj) throws Exception {
		//创建表
		DbBase db = DbBase.getDruid();
		DbTool.getInstance().createInsertSql(db, table, row, create_by)
		//添加到sys_fields
		return false;
	}

}
