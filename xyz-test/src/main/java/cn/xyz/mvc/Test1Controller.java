package cn.xyz.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

@MyController()
@MyRequestMapping("test1")
public class Test1Controller {

    @MyAutowired
    private TestService testService;

    @MyRequestMapping("test")
    public void myTest(HttpServletRequest request, HttpServletResponse response,
                      JSONObject obj){
        try {
            response.getWriter().write( "Test1Controller:the param you send is :"+obj);
            this.testService.printParam(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

