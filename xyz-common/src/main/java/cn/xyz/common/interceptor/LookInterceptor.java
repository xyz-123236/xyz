package cn.xyz.common.interceptor;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.xyz.common.pojo.SysUser;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LookInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//String url=request.getServletPath();
		//String str=url.substring(url.lastIndexOf('/'));
		SysUser user=(SysUser)request.getSession().getAttribute("userSession");
		if(user!=null){
			/*if(user.getRid()==11){
				if(!vilidate(str)){
					return false;
				}
			}*/
		}
		return true;
	}
	
	public boolean vilidate(String url){
		if(Pattern.matches(".*add.*", url)||Pattern.matches(".*update.*", url)||Pattern.matches(".*delete.*", url)) return false;
		return true;
	}
	
}
