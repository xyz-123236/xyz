package cn.xyz.test.test;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class T031 {
    public static void main(String[] args) {
        TreeMap<String, JSONObject> map = new TreeMap<>();
        map.computeIfAbsent("黎河洲", key -> new JSONObject());
        map.computeIfAbsent("许才", key -> new JSONObject());
        map.computeIfAbsent("刘国荣", key -> new JSONObject());
        map.computeIfAbsent("邓少波", key -> new JSONObject());
        for(String key: map.keySet()){
            System.out.println(key);
        }
        TreeMap<String, JSONObject> map2 = new TreeMap<>();
        map2.put("黎河洲", new JSONObject());
        map2.put("许才", new JSONObject());
        map2.put("刘国荣", new JSONObject());
        map2.put("邓少波", new JSONObject());
        for(String key: map2.keySet()){
            System.out.println(key);
        }
    }
}
