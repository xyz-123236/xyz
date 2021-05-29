package cn.xyz.item;

import cn.xyz.common.tools.ToolsDouble;

public class Money {
	static double sum = 100;
	static int index = 10;
	static int min = 5;
	static int max = 15;
	public static void main(String[] args) {
		for (int i = 0; i < 9; i++) {
			index--;
			double aa = 0;
			do {
				aa = ToolsDouble.round(Math.random()*(max-min) + min,2);
			}while(!check(aa));
			sum = ToolsDouble.round(sum - aa,2);
			System.out.println(aa);
		}
		System.out.println(sum);
	}
	public static boolean check(double aa) {
		if((sum - aa) > min*index && (sum - aa) < max*index) {
			return true;
		}
		return false;
	}
}
