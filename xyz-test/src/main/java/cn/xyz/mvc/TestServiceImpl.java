package cn.xyz.mvc;

import com.alibaba.fastjson.JSONObject;

import cn.xyz.mvc.annotation.MyService;

@MyService
public class TestServiceImpl implements TestService {

    @Override
    public void printParam(JSONObject param) {
        System.out.println("接收到的参数为： "+param);
    }
}

