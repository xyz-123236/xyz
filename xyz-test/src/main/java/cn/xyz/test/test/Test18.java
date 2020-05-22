package cn.xyz.test.test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class Test18 {
	public static void main(String[] args) {
		FileLock lock = null;File file = new File("E:\\file\\temp\\test.txt"); 
        try (FileChannel channel = new FileOutputStream("test.txt").getChannel();){
        	lock = channel.tryLock(0,Long.MAX_VALUE, false);
        	System.out.println(lock);
        	if(lock != null) {
        		System.out.println(Thread.currentThread().getName()+"执行操作");
        		//Thread.sleep(5000);
            }
        	System.out.println(Thread.currentThread().getName()+"结束");
        } catch (Exception e) {
        	System.out.println(Thread.currentThread().getName()+"异常1");
            e.printStackTrace();
        } finally {
            if (lock != null) {
                try {
                    //lock.close();
                    lock = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(Thread.currentThread().getName()+"异常2");
                }
            }
        }
        System.out.println("test2");
	}
}
