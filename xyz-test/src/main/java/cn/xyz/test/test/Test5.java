package cn.xyz.test.test;

import cn.xyz.test.lock.Task;

public class Test5 {
	public static void main(String[] args) {
		try {
			for (int i = 0; i < 2; i++) {
				Task at = new Task();
				Thread t = new Thread(at,"a"+i);
				t.start();
				//Thread.sleep(5000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
