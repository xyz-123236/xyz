package cn.xyz.main.dao.sys;

import com.alibaba.fastjson.JSONArray;

import cn.xyz.orm.db.DbBase;
import cn.xyz.orm.db.DbTool;

public class SysConfigDaoImpl implements SysConfigDao{

	@Override
	public JSONArray find() throws Exception {
		DbBase db = DbBase.getDruid();
		String sql = DbTool.getInstance().select("sys_config").getSql();
		return db.find(sql);
	}

}
