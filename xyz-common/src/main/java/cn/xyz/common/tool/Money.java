package cn.xyz.common.tool;

public class Money {
	static double sum = 100;
	static int index = 10;
	public static void main(String[] args) {
		for (int i = 0; i < 9; i++) {
			index--;
			double aa = 0;
			do {
				aa = Math.random()*10 + 5;
			}while(!check(aa));
			sum -= aa;
			System.out.println(aa);
		}
		System.out.println(sum);
	}
	public static boolean check(double aa) {
		if((sum - aa) > 5*index && (sum - aa) < 15*index) {
			return true;
		}
		return false;
	}
}
