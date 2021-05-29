package cn.xyz.test.test;

import java.time.LocalDate;

public class T032 {
    public static void main(String[] args) {
        String str = "试模组";
        if(str.contains("（试模组）")){
            System.out.println("ok");
        }else {
            System.out.println("false");
        }

        String a = "　JSGYC@FTJT.NET";
        System.out.println("1"+a.trim());

        System.out.println(LocalDate.parse("2000/01/01").plusDays(2).toString());
    }
}
