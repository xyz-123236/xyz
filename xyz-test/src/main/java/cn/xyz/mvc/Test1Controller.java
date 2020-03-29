package cn.xyz.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MyController()
@MyRequestMapping("test1")
public class Test1Controller {

    @MyAutowired
    private TestService testService;

    @MyRequestMapping("test")
    public void myTest(HttpServletRequest request, HttpServletResponse response,
                      @MyRequestParam("param") String param){
        try {
            response.getWriter().write( "Test1Controller:the param you send is :"+param);
            this.testService.printParam(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

