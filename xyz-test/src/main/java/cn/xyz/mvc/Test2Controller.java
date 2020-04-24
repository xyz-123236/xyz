package cn.xyz.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.xyz.mvc.annotation.*;

@Controller
@RequestMapping("t2")
public class Test2Controller {

    @RequestMapping("t2")
    public static String myTest(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam("param") String param){
        try {
        	//https://blog.csdn.net/yiluoak_47/article/details/51012406
            //response.getWriter().write( "Test2Controller:the param you send is :"+param);
        	//response.sendRedirect("login.jsp");
        	
        	//request.getRequestDispatcher("login.jsp").forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "login.jsp";
    }
}

