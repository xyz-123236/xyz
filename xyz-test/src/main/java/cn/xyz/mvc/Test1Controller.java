package cn.xyz.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.mvc.annotation.*;

@MyController()
@MyRequestMapping("test1")
public class Test1Controller {

    @MyAutowired
    private TestService testService;

    @MyRequestMapping("test")
    @MyResponseBoby
    public String myTest(HttpServletRequest request, HttpServletResponse response,
                      JSONObject obj){
        try {
            this.testService.printParam(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(obj);
    }
}

