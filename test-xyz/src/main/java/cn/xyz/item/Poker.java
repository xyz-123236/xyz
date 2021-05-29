package cn.xyz.item;

import java.util.Scanner;

public class Poker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        for (int j = 0; j < 100; j++) {
            String list2 = Test.xx;
            StringBuilder list_a = new StringBuilder();
            StringBuilder list_b = new StringBuilder();
            StringBuilder list_c = new StringBuilder();
            System.out.print("z:");
            String z = sc.nextLine();
            for (int i = 0; i < z.length(); i++) {
                list2 = list2.replaceFirst(z.charAt(i)+"","");
            }
            System.out.println("list:"+list2);
            while (true){
                System.out.print("1:");
                String a = sc.nextLine();
                if("0".equals(a)) break;
                if(!"*".equals(a)){
                    list_a.append(a);
                }
                System.out.println("list:"+list2);
                System.out.println("a:"+list_a);
                System.out.println("b:"+list_b);
                System.out.println("c:"+list_c);
                System.out.print("2:");
                String b = sc.nextLine();
                if("0".equals(b)) break;
                if(!"*".equals(b)){
                    list_b.append(b);
                    for (int i = 0; i < b.length(); i++) {
                        list2 = list2.replaceFirst(b.charAt(i)+"","");
                    }
                }
                System.out.println("list:"+list2);
                System.out.println("a:"+list_a);
                System.out.println("b:"+list_b);
                System.out.println("c:"+list_c);
                System.out.print("3:");
                String c = sc.nextLine();
                if("0".equals(c)) break;
                if(!"*".equals(c)){
                    list_c.append(c);
                    for (int i = 0; i < c.length(); i++) {
                        list2 = list2.replaceFirst(c.charAt(i)+"","");
                    }
                }
                System.out.println("list:"+list2);
                System.out.println("a:"+list_a);
                System.out.println("b:"+list_b);
                System.out.println("c:"+list_c);
            }
        }
    }
}
