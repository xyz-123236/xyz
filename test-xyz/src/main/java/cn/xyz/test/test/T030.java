package cn.xyz.test.test;

import com.alibaba.fastjson.JSONObject;

import java.text.Collator;
import java.util.Arrays;

public class T030 {
    public static void main(String[] args) {
        double[] d = new double[10];
        System.out.println(d[5]);
        System.out.println(Double.valueOf("0"));
        String[] ss = {"B夜班","A夜班","B白班","A白班"};
        Arrays.sort(ss, Collator.getInstance(java.util.Locale.CHINA));
        for (String s: ss) {
            System.out.println(s);
        }

        JSONObject obj = new JSONObject();
        System.out.println(obj.getDoubleValue("aa"));
    }
}
