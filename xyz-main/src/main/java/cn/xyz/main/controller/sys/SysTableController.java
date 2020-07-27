package cn.xyz.main.controller.sys;

import cn.xyz.mvc.annotation.Controller;
import cn.xyz.mvc.annotation.RequestMapping;
import cn.xyz.mvc.annotation.ResponseBoby;

@Controller
public class SysTableController {
	@RequestMapping("/")
	@ResponseBoby
	public String find() {
		
		return "";
	}
}
