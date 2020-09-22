package cn.xyz.item.game.guess;

import java.util.Scanner;

public class Guess {
	
	public static void main(String[] args) {
		//Math.floor(向下).ceil(向上).round(4舍5入)
		int a = (int)(Math.random()*101);
		int max = 100;
		int min = 0;
		try (Scanner scanner = new Scanner(System.in);){
			while(true) {
				System.out.print("请输入");
				int i = scanner.nextInt();
				if(a > i){
					min = i;
					System.out.println("比竞猜数小，提示范围："+i+"-"+max);
				}else if(a < i){
					max = i;
					System.out.println("比竞猜数大，提示范围："+min+"-"+i);
				}else{
					System.out.println("成功"+a);
					break;
				} 
			}
			System.out.println("y游戏结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
