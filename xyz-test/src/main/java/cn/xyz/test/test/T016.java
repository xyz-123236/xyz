package cn.xyz.test.test;

import cn.xyz.common.tools.ToolsDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class T016 {
    public static void main(String[] args) throws Exception {
        System.out.println(new SimpleDateFormat("FF/WW/yyyy hh:mm:ss aa", Locale.ENGLISH).format(new Date()));
        System.out.println(new SimpleDateFormat("ww/EE").format(new Date()));
        System.out.println(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa", Locale.ENGLISH).format(new Date()));
        Calendar cal = Calendar.getInstance();
        cal.setTime(ToolsDate.getDate("2000-12-30"));
        System.out.println(cal.get(Calendar.WEEK_OF_YEAR));
        cal.setTime(ToolsDate.getDate("2001-12-29"));
        System.out.println(cal.get(Calendar.WEEK_OF_YEAR));
        cal.setTime(ToolsDate.getDate("2016-12-31"));
        System.out.println(cal.get(Calendar.WEEK_OF_YEAR));
        System.out.println(ToolsDate.getDatePart("w","2000-12-30"));
        System.out.println(ToolsDate.getDatePart("w","2000-12-31"));
        for (int i = 1900; i < 3000; i++) {
            if(i%4 == 1){
                if("1".equals(ToolsDate.getDatePart("E",i+"-01-01"))) {
                    System.out.println(i);
                }
            }
        }
    }
}
