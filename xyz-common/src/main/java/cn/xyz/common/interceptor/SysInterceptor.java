package cn.xyz.common.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.xyz.common.pojo.SysUser;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class SysInterceptor extends HandlerInterceptorAdapter {


	/*
	 * @Resource private FunctionService functionService;
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// String ip= request.getRemoteAddr();//获取ip
		// Integer port= request.getRemotePort();//获取客服端端口号
		// String urlString = request.getRequestURI();
		// String urlString2 = request.getContextPath();
		// //Integer port2= request.getLocalPort();//：获取的是应用服务器的端口，即该应用的实际端口。
		// Integer port3=
		// request.getServerPort();//：获取的是URL请求的端口，比如你的请求时127.0.0.1:8080，获取到的是8080。
		String houzhui = request.getQueryString();// ?后缀内容
		String urlString3 = request.getServletPath();// 获取请求地址
		HttpSession session = request.getSession();
		if ("".equals(houzhui) || houzhui == null) {
			session.setAttribute("url", urlString3);
		} else {
			session.setAttribute("url", urlString3/* +"?"+houzhui */);
		}

		SysUser loginUser = (SysUser) session.getAttribute("userSession");
		if (null != loginUser) {
			return true;
		} else {
			//String loginCookieUserName = "";
			//String loginCookiePassword = "";

			Cookie[] cookies = request.getCookies();
			if (null != cookies) {
				for (Cookie cookie : cookies) {
					// if("/".equals(cookie.getPath())){ //getPath为null
					if ("loginUserName".equals(cookie.getName())) {
						//loginCookieUserName = cookie.getValue();
					} else if ("loginPassword".equals(cookie.getName())) {
						//loginCookiePassword = cookie.getValue();
					}
					// }
				}
				/*
				 * if(!"".equals(loginCookieUserName) &&
				 * !"".equals(loginCookiePassword)){ User user
				 * =userService.loginUser(loginCookieUserName,
				 * loginCookiePassword);
				 * //userService.getUserByName(loginCookieUserName);
				 * //if(loginCookiePassword.equals(loginUser.getPassword())){
				 * if(user!=null){ List<Function>
				 * onelist=functionService.getList(user.getRid());
				 * session.setAttribute("oneList", onelist);
				 * request.getSession().setAttribute("userSession", user);
				 * return true; } //} }
				 */
			}
		}
		// return true;
		response.sendRedirect(request.getContextPath() + "/login");
		return false;
	}

}
