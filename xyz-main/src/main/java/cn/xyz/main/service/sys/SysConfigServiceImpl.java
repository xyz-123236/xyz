package cn.xyz.main.service.sys;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.tools.Tools;
import cn.xyz.main.dao.sys.SysConfigDao;
import cn.xyz.mvc.annotation.Autowired;
import cn.xyz.mvc.annotation.Service;

@Service
public class SysConfigServiceImpl implements SysConfigService {

	@Autowired
	public SysConfigDao scd;
	
	public static JSONObject config;
	
	@Override
	public String get(String key) throws Exception {
		if(Tools.isEmpty(config)) {
			JSONArray data = this.scd.find();
			for (int i = 0; i < data.size(); i++) {
				config.put(data.getJSONObject(i).getString("key"), data.getJSONObject(i).getString("value"));
			}
		}
		return config.getString(key);
	}

}
