package cn.xyz.test.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cn.xyz.test.thread.T2.T;

public class T4 {

	public static void main(String[] args) {
		char[] ai = "1234567".toCharArray();
		char[] ac = "ABCDEFG".toCharArray();
		
		Lock lock = new ReentrantLock();
		Condition condition1 = lock.newCondition();
		Condition condition2 = lock.newCondition(); 
		new Thread(() -> {
			try {
				lock.lock();
				for(char c: ai) {
					System.out.println(c);
					condition2.signal();
					condition1.await();
				}
				condition2.signal();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				lock.unlock();
			}
		},"t1").start();
		
		new Thread(() -> {
			try {
				lock.lock();
				for(char c: ac) {
					System.out.println(c);
					condition1.signal();
					condition2.await();
				}
				condition1.signal();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				lock.unlock();
			}
		},"t2").start();
	}

}
