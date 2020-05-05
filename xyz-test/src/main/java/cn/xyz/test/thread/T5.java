package cn.xyz.test.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.LockSupport;

public class T5 {

	static BlockingQueue<String> q1 = new ArrayBlockingQueue<>(1);
	static BlockingQueue<String> q2 = new ArrayBlockingQueue<>(1);
	public static void main(String[] args) {
		char[] ai = "1234567".toCharArray();
		char[] ac = "ABCDEFG".toCharArray();
		new Thread(()->{
			for (char c : ai) {
				System.out.println(c);
				try {
					q1.put("o");
					q2.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		},"t1").start();
		
		new Thread(()->{
			for (char c : ac) {
				try {
					q1.take();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println(c);
				try {
					q2.put("o");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		},"t2").start();
	}

}
