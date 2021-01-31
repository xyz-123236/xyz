package cn.xyz.test.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class T021 {
    public static void main(String[] args) {
        double[] arr = new double[]{25.3,17.2, 15.6,100,100,100};
        Arrays.sort(arr);
        //print(arr);

        double amount = 217.2;
        double sum = 0;
        List<Integer> list = new ArrayList<>();
        for (int i = arr.length-1; i >= 0; i--) {
            double v = arr[i];
            if(v + sum == amount){
                list.add(i);
                sum += v;
                break;
            }
            if(v + sum < amount){
                sum += v;
                list.add(i);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            Integer index =  list.get(i);
            System.out.println(arr[index]);
        }
    }

    public static void print(double[] arr){
        for (int i = arr.length-1; i >= 0; i--) {
            double v = arr[i];
            System.out.println(arr[i] + ", ");
        }
    }
}
