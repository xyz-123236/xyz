package cn.xyz.test.lock;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class Task implements Runnable  {
	@Override
	public void run() {
		FileLock lock = null;
        try (FileChannel channel = new FileOutputStream("test.txt").getChannel();){
        	lock = channel.tryLock();
        	if(lock != null) {
        		System.out.println(Thread.currentThread().getName()+"执行操作");
        		Thread.sleep(5000);
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
	}
}
