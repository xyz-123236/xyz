package cn.xyz.test.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import com.alibaba.fastjson.JSONArray;

public class Test17 {
	public static void main(String[] args) {
		FileLock lock = null;
		FileChannel channel = null;
        try{
        	channel = new FileOutputStream("test.txt").getChannel();
        	lock = channel.tryLock();
        	System.out.println(lock);
        	if(lock != null) {
        		System.out.println(Thread.currentThread().getName()+"执行操作");
        		Thread.sleep(5000);
            }
        	System.out.println(Thread.currentThread().getName()+"结束");
        } catch (Exception e) {
        	System.out.println(Thread.currentThread().getName()+"异常1");
            e.printStackTrace();
        }
        if(channel != null) {
            try {
            	channel.close();
            	channel = null;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(Thread.currentThread().getName()+"异常3");
            }
        }
        if (lock != null) {
            try {
                //lock.release();
                lock = null;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(Thread.currentThread().getName()+"异常2");
            }
        }
        System.out.println("test");
		try {
			/*File file = new File("db.properties");
			if(file != null) {
				System.out.println(file.getName());
				System.out.println(file.getPath());
			}*/
			/*DBTool db = DBTool.getInstance();
			db.startTransaction();
			db.execute("LOCK TABLE log IN INTENTIONAL EXCLUSIVE MODE;");
			JSONArray data = db.select("select * from log");
			System.out.println(data);
			Thread.sleep(30000);
			db.commit();
			db.endTransaction();
			System.out.println("结束");*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
