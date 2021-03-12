package cn.xyz.test.test;

import cn.xyz.test.pojo.Basic;
import com.alibaba.fastjson.JSONObject;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class T024 {
    public static void main(String[] args) {

        List<String> items = Arrays.asList(
                "apple", "apple",
                "orange", "orange", "orange",
                "blueberry",
                "peach", "peach", "peach", "peach"
        );

        // 分组，计数
        Map<String, Long> result = items.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println(result);

        Basic user1 = new Basic();//"zhangsan", "beijing", 10
        user1.setOrder("zhangsan");
        user1.setSort("beijing");
        user1.setPage(10);
        Basic user2 = new Basic();//"zhangsan", "beijing", 10
        user2.setOrder("zhangsan");
        user2.setSort("beijing");
        user2.setPage(20);
        Basic user3 = new Basic();//"zhangsan", "beijing", 1
        user3.setOrder("lisi");
        user3.setSort("shanghai");
        user3.setPage(30);
        List<Basic> list = new ArrayList<Basic>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        Map<String, Map<String, List<Basic>>> collect
                = list.stream().collect(
                Collectors.groupingBy(
                        Basic::getSort, Collectors.groupingBy(Basic::getOrder)
                )
        );
        System.out.println(collect);

    }
}
