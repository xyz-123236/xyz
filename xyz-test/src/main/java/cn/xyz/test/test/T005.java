package cn.xyz.test.test;

import cn.xyz.common.tools.ToolsSn;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

public class T005 {
    static Map<String, Integer> map = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    map.put("a"+j, j);
                }
                System.out.println("===========================");
            }).start();
        }
        Thread.sleep(5000);
        int row = 0;
        for(String key: map.keySet()){
            System.out.println(key+":"+map.get(key));
            row++;
        }
        System.out.println("row:"+row);
    }
}
