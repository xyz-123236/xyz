package cn.xyz.common.tools;


import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class ToolsWeb {

    public static String getBasePath(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("/");
    }

    //Context path (如:tomcat/webapps/mes/WEB-INF/classes/)
    public static String getClassPath() {
        return ToolsWeb.class.getResource("/").toString().replace("file:", "");
    }
}
