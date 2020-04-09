package cn.xyz.test;

import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import javax.servlet.http.HttpServlet;

public class TestServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static FileLock lock = null;
    private static FileChannel channel = null;
	public void init(){
		try {
			channel = new FileOutputStream("test.txt").getChannel();
			lock = channel.tryLock();
			if(lock != null) {
				System.out.println("test2==========");
			}
			System.out.println("test2==========结束");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("test2==========异常");
		}
	}
	@Override
	public void destroy() {
		super.destroy();
		System.out.println("test2==========destroy");
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
                //lock.close();
                lock = null;
                System.out.println("destroy成功");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("destroy失败");
            }
        }
	}
}
