package cn.xyz.test.test;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class T022 {
    public static void main(String[] args) {
        Double a = 2.5;
        Integer b = a.intValue();
        System.out.println(b);

        Map<String, JSONObject> map = new HashMap<>();
        //System.out.println(map.get("aa").getString("bb"));
        System.out.println(map.getOrDefault("aa", new JSONObject()).getOrDefault("bb","cc"));
        map.computeIfAbsent("java框架", key -> new JSONObject()).put("Spring","mvc");


        Map<String, Integer> countMap = new HashMap<>();

        Integer count = countMap.getOrDefault("java",0);
        countMap.put("java", count + 1);

        countMap.merge("java", 1, Integer::sum);

        countMap.merge("java", 1, Integer::sum);//=>
        countMap.merge("java", 1, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer oldValue, Integer newValue) {
                return Integer.sum(oldValue,newValue);
            }
        });
    }
}
