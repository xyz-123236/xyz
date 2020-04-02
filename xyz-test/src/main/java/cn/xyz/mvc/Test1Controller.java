package cn.xyz.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.mvc.annotation.*;

@Controller()
@RequestMapping("test1")
public class Test1Controller {

    @Autowired
    private TestService testService;

    @RequestMapping("test")
    @ResponseBoby
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

