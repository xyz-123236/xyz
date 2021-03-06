package cn.xyz.test.function;

import com.alibaba.fastjson.JSONObject;

public interface JSONFilter {
    boolean filter(JSONObject obj);
}
