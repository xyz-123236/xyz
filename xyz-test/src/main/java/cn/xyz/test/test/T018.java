package cn.xyz.test.test;

import cn.xyz.common.tools.ToolsDate;

import java.util.Calendar;
import java.util.Date;

public class T018 {
    public static void main(String[] args) {
       /* Calendar cal = Calendar.getInstance();
        System.out.println(cal.get(Calendar.MONTH));
        cal.set(Calendar.MONTH,12);
        System.out.println(cal.get(Calendar.YEAR));*/

        Calendar cal = Calendar.getInstance();
        cal.set(2020, 8+3, 0);
        Calendar cal2 = Calendar.getInstance();
        if(cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)){
            System.out.println(ToolsDate.getString("yyyyMMdd"));
        }else{
            System.out.println(ToolsDate.getString(cal.getTime(), "yyyyMMdd"));
        }
    }
}
