package cn.xyz.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.annotation.*;

@Controller()
@RequestMapping("t1")
public class Test1Controller {

    @Autowired
    private TestService testService;

    @RequestMapping("t1")
    @ResponseBoby
    public String myTest(HttpServletRequest request, HttpServletResponse response,
                      JSONObject obj){
        try {
        	
        	
        	Thread.sleep(3000);
        	System.out.println(obj);
            this.testService.printParam(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(obj);
    }
}

