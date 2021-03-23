package cn.xyz.tool;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.Map;
import java.util.Set;

public class ToolQuery {

    public static <T> MyQuery<T>  buildMyQuery(Map<String, String> param){
        MyQuery<T> myQuery = new MyQuery<T>();
        Long page = 1L;
        Long limit = 10L;

        QueryWrapper<T> wrapper = new QueryWrapper<>();

        Set<Map.Entry<String, String>> entries = param.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String value = entry.getValue();
            if(StringUtils.isNotBlank(value)){
                String key = entry.getKey();
                if("page".equals(key)){
                    page = Long.parseLong(value);
                }else if("limit".equals(key)){
                    limit = Long.parseLong(value);
                }else {
                    String[] splits = key.split("\\$");
                    switch (splits[0]){
                        case "like":
                            wrapper.like(splits[1], value);
                        case "le":
                            wrapper.le(splits[1], value);
                        case "ge":
                            wrapper.ge(splits[1], value);
                        case "eq":
                        default:
                            wrapper.eq(splits[1], value);
                    }
                }
            }
        }

        return myQuery;
    }
}
