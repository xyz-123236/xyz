package cn.xyz.mvc;

import com.alibaba.fastjson.JSONObject;

import cn.xyz.mvc.annotation.Service;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public void printParam(JSONObject param) {
        System.out.println("接收到的参数为： "+param);
    }
}

