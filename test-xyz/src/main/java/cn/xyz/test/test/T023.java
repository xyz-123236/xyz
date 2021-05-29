package cn.xyz.test.test;

import java.util.Calendar;

public class T023 {
    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2021);
        cal.set(Calendar.MONTH, 9 + 3);
        System.out.println(cal.get(Calendar.YEAR) + "年" + cal.get(Calendar.MONTH) + "月对帐单");
        cal.set(Calendar.DATE, 0);
        System.out.println(cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月对帐单");
    }
}
