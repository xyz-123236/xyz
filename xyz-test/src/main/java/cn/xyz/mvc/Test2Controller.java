package cn.xyz.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MyController
@MyRequestMapping("test2")
public class Test2Controller {

    @MyRequestMapping("test")
    public static void myTest(HttpServletRequest request, HttpServletResponse response,
                      @MyRequestParam("param") String param){
        try {
            response.getWriter().write( "Test2Controller:the param you send is :"+param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

