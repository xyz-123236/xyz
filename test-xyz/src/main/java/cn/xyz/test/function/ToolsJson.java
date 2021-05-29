package cn.xyz.test.function;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ToolsJson {
    public static JSONArray filter(JSONArray data, JSONFilter filter){
        JSONArray result = new JSONArray();
        for (int i = 0; i < data.size(); i++) {
            JSONObject item = data.getJSONObject(i);
            if(filter.filter(item)){
                result.add(item);
            }
        }
        return result;
    }
}
