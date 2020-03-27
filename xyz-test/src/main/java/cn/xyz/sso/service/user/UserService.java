package cn.xyz.sso.service.user;

import com.alibaba.fastjson.JSONArray;

public class UserService {
	
	
	private static boolean isAdmin(JSONArray data) {
		for (int i = 0; i < data.size(); i++) {
			if(data.getJSONObject(i).getIntValue("ruleid") == 1) {
				return true;
			}
		}
		return false;
	}
}
