package cn.xyz.test.test;

import java.util.Arrays;

public class T029 {
    public static void main(String[] args) {

        System.out.println(Integer.parseInt("02"));
        String docno = "10000453,10000452";
        String[] ss = docno.split(",");
        Arrays.sort(ss);
        for (int i = 0; i < ss.length; i++) {
            System.out.println(ss[i]);
        }
    }
}
