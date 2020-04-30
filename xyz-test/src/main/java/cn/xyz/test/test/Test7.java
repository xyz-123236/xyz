package cn.xyz.test.test;

import java.util.Arrays;

public class Test7 {

	public static void main(String[] args) {
		int[] a = {1,3,4,5,99};
		group(a,3);
	}
	public static int[] group(int[] a, int num) {
		int[] g = new int[num];
		Arrays.sort(a);
		print(a);
		//int m = a.length%num==0?a.length/num:a.length/num+1;
		int avg = (int)Math.ceil((double)sum(a, a.length)/num);
		System.out.println(avg);
		if(a[a.length-1] >= avg) {
			g[num-1] = a[a.length-1];
			int[] t = group(Arrays.copyOfRange(a, 0, a.length-1), num-1);
			for (int i = 0; i < t.length; i++) {
				g[i] = t[i];
			}
		}else {
			//
		}
		return g;
		/*int[][] g = new int[num][a.length];
		
		System.out.println(m);
		int x = 0, y = 0;
		for (int i = 0; i < a.length; i++) {
			if(i != 0 && i % m == 0) {
				x++;
				y = 0;
			}
			g[x][y++] = a[i];
		}
		print(g);*/
	}
	public static int sum(int arr[], int n) {
        if(n == 1) {
            return arr[0];
        }else {
            return arr[n-1] + sum(arr, n-1);
        }
    }
	public static void print(int[] g) {
		for (int i = 0; i < g.length; i++) {
			System.out.print(g[i] + ",");
		}
		System.out.println();
	}
	public static void print(int[][] g) {
		for (int i = 0; i < g.length; i++) {
			for (int j = 0; j < g.length; j++) {
				System.out.print(g[i][j] + ",");
			}
			System.out.println();
		}
	}
}
