package cn.xyz.test.function;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test {
    public static void main(String[] args) {
        Double a = new Num<Double>().merge(2.0,3.0,Double::sum);
        System.out.println(a);

        JSONArray data = new JSONArray(){{
            add(new JSONObject(){{
                put("a",1);
                put("b",2);
            }});
            add(new JSONObject(){{
                put("c",3);
                put("d",4);
            }});
        }};
        JSONArray result = ToolsJson.filter(data, o-> o.getIntValue("a") == 1 && o.getIntValue("c") == 3);
        System.out.println(result);
        JSONArray result2 = ToolsJson.filter(data, o-> o.getIntValue("a") == 1 || o.getIntValue("c") == 3);
        System.out.println(result2);
        JSONArray result3 = ToolsJson.filter(data, new JSONFilter() {
            @Override
            public boolean filter(JSONObject obj) {
                return obj.getIntValue("a") == 1;
            }
        });
    }
}
