package cn.xyz.test.test;

import java.util.Calendar;

public class T011 {

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 0);
        cal.add(Calendar.MONTH,-10);
        System.out.println(cal.get(Calendar.MONTH));
        System.out.println(cal.get(Calendar.DATE));
        StringBuffer sql = new StringBuffer();
        sql.append("select * from t1");
        System.out.println(sql);
        System.out.println(sql.replace(0,6,"select distinct"));
    }
}
