package cn.xyz.test.thread;

public class T2 {
	enum  T  {T1, T2};
	static volatile T r = T.T1;
	public static void main(String[] args) {
		//自旋锁
		char[] ai = "1234567".toCharArray();
		char[] ac = "ABCDEFG".toCharArray();
		new Thread(() -> {
			for(char c: ai) {
				while(r != T.T1) {
					//
				}
				System.out.println(c);
				r=T.T2;
			}
		},"t1").start();
		
		new Thread(() -> {
			for(char c: ac) {
				while(r != T.T2) {
					//
				}
				System.out.println(c);
				r=T.T1;
			}
		},"t2").start();
	}
}
