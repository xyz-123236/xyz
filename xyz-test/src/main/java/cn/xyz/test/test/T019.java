package cn.xyz.test.test;

import cn.xyz.common.pojo.NotNull;
import cn.xyz.common.tools.ToolsDate;

import com.alibaba.fastjson.JSONArray;


import java.util.Calendar;

public class T019 {
    public static void main(String[] args) {
        //print("dga122");
        JSONArray data = new JSONArray();
        if(data == null){
            System.out.println("null");
        }

        int day = 23;
        Calendar cal = Calendar.getInstance();
        int day2 = cal.get(Calendar.DATE);
        if(day2 < 25){
            cal.add(Calendar.MONTH, -1);
        }
        if (day == 20 || day == 25) {
            cal.set(Calendar.DATE, day);
        }else {
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DATE, 0);
        }
        System.out.println(ToolsDate.getString(cal.getTime()));
    }

    public static void print(NotNull<String> str){
        System.out.println(str);
    }
}
