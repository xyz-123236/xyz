package cn.xyz.test.test;

import cn.xyz.common.tools.ToolsDate;
import com.alibaba.fastjson.JSONObject;

import java.util.Calendar;

public class T020 {
    public static void main(String[] args) {
        if("dgad-4556_ADF奋斗故事".matches("[\\w\\u4e00-\\u9fa5-]+")){
            System.out.println("ok");
        }else {
            System.out.println("false");
        }
        if(" ".equals("　")){
            System.out.println("ok");
        }else {
            System.out.println("false");
        }
        JSONObject obj = new JSONObject();
        obj.put("ss", "2021-02-22");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        System.out.println(ToolsDate.getString(obj.getDate("ss")));
        System.out.println(ToolsDate.getString(cal.getTime()));
        if(obj.getDate("ss").after(cal.getTime())){
            System.out.println("ok");
        }else {
            System.out.println("false");
        }
    }
}
