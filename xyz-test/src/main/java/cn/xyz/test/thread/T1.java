package cn.xyz.test.thread;

import java.util.concurrent.locks.LockSupport;

public class T1 {
	static Thread t1 = null, t2 = null;
	public static void main(String[] args) {
		char[] ai = "1234567".toCharArray();
		char[] ac = "ABCDEFG".toCharArray();
		t1 = new Thread(()->{
			for (char c : ai) {
				System.out.println(c);
				LockSupport.unpark(t2);
				LockSupport.park();
			}
		},"t1");
		
		t2 = new Thread(()->{
			for (char c : ac) {
				LockSupport.park();
				System.out.println(c);
				LockSupport.unpark(t1);
			}
		},"t2") ;
		t1.start();
		t2.start();
	}

}
