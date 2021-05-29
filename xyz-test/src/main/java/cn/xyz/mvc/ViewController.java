package cn.xyz.mvc;


import cn.xyz.common.annotation.Controller;
import cn.xyz.common.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/view")
public class ViewController {

	@RequestMapping(value = "/**")
	public String returnView(HttpServletRequest request) {
		String path = request.getServletPath();
		return path.substring(6);
	}
}
