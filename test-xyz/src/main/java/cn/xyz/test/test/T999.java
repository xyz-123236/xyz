package cn.xyz.test.test;

public class T999 {

	public static void main(String[] args) {
		String[] a = {"3","0","1","","17","", "2"};
		String p = "123456";
		for (int i = 0; i < p.length(); i++) {
			System.out.print(a[Integer.parseInt(p.substring(i, i+1))]);
		}
	}

}
