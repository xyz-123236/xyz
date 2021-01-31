package cn.xyz.portal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class T013 {
    public static int a = 0;

    private static boolean pro(Vector<Double> v, int index, Double[] array, Vector<Vector<Double>> all, double amount) {
        a++;
        if(a > 10000){
            return false;
        }
        if(all.size() > 0){
            return false;
        }
        v.add(array[index]);

        int sum = sum(v);

        if (sum == amount) {
            System.out.println(a);
            all.add((Vector<Double>) v.clone());
            v.remove(v.size() - 1);
            return true;
        } else if (sum < amount) {
            for (int i = index + 1; i < array.length; i++) {
                if (!pro(v, i, array, all, amount)) {
                    break;
                }
            }
            v.remove(v.size() - 1);
            return true;
        } else {
            v.remove(v.size() - 1);
            for (int i = index + 1; i < array.length; i++) {
                if (!pro(v, i, array, all, amount)) {
                    break;
                }
            }
            return false;
        }
    }

    private static int sum(Vector<Double> v) {
        int sum = 0;
        for (Double aDouble : v) {
            sum += aDouble;
        }
        return sum;
    }

    public static void main(String[] args) {
        Vector<Vector<Double>> all = new Vector<>();
        Double[] array = new Double[]{460.0, 720.0, 1250.0, 1800.0, 2200.0, 3080.0, 4100.0, 4750.0,
                300.0, 500.0, 600.0, 1200.0, 1500.0, 630.0, 1752.0, 1562.0, 466.0, 1532.0, 5661.0, 5618.0, 2451.0, 5465.0, 4652.0, 5652.0, 4561.0, 5201.0,
                6510.0, 6900.0, 22749.1, 22749.1, 22749.1};
        Arrays.sort(array);
        double amount = 22750;
        List<Double> list = new ArrayList<>();
        double b = 0;
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == amount) {
                b = array[i];
                break;
            }
            if (array[i] < amount) {
                list.add(array[i]);
            }
        }
        if(b > 0){
            System.out.println(b);
            return;
        }
        Double[] arr = list.toArray(new Double[0]);
        for (int i = 0; i < arr.length; i++) {
            Vector<Double> v = new Vector<>();
            if(all.size() > 0) break;
            pro(v, i, arr, all, amount);
        }
        for (Vector<Double> doubles : all) {
            System.out.println(doubles.toString());
        }
        System.out.println(a);
    }
}
