package cn.xyz.test.thread;

import java.util.concurrent.CountDownLatch;

import cn.xyz.test.thread.T2.T;

public class T3 {
	private static volatile boolean t2Start = false;
	private static CountDownLatch latch = new CountDownLatch(1);
	public static void main(String[] args) {
		final Object o = new Object();
		char[] ai = "1234567".toCharArray();
		char[] ac = "ABCDEFG".toCharArray();
		new Thread(() -> {
			//latch.await();
			synchronized(o) {
				while(!t2Start) {
					try {
						o.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				for(char c: ai) {
					System.out.println(c);
					try {
						o.notify();
						o.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				o.notify();
			}
		},"t1").start();
		
		new Thread(() -> {
			synchronized(o) {
				for(char c: ac) {
					System.out.println(c);
					t2Start = true;
					//latch.countDown();
					try {
						o.notify();
						o.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				o.notify();
			}
		},"t2").start();
	}

}
