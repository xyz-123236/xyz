package cn.xyz.common.tools;

import javax.servlet.http.HttpSession;

import cn.xyz.common.pojo.Config;
import cn.xyz.common.pojo.SysUser;

public class ToolsData {
	public String getUsercode(HttpSession session) throws Exception {
		SysUser login_user = (SysUser) session.getAttribute(Config.LOGIN_USER);
		if(Tools.isEmpty(login_user)) {
			throw new Exception("没有登录");
		}
		return login_user.getUsercode();
	}
}
