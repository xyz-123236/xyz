package cn.xyz.main.service.sys;

import com.alibaba.fastjson.JSONArray;

public class SysUserServiceImpl implements SysUserService{
	public boolean isAdmin(JSONArray data) throws Exception {
		for (int i = 0; i < data.size(); i++) {
			if(data.getJSONObject(i).getIntValue("ruleid") == 1) {
				return true;
			}
		}
		return false;
	}
}
