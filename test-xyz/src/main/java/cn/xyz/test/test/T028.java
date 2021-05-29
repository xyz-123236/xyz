package cn.xyz.test.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class T028 {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6,1, 2, 3, 4, 5, 6,1, 2, 3, 4, 5, 6,1, 2, 3,4,5,});
        List<List<Integer>> all = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<Integer> v = new ArrayList<>();
            test(v, i, list, all);
        }

        /*for (List<Integer> str : all) {
            System.out.println(str.toString());
        }*/
        System.out.println("size: " + all.size());
    }

    public static void test(List<Integer> v, int index, List<Integer> list2, List<List<Integer>> all) {
        v.add(list2.get(index));
        all.add(v);
        for (int i = index + 1; i < list2.size(); i++) {
            List<Integer> v2 = new ArrayList<>();
            v2.addAll(v);
            test(v2, i, list2, all);
        }
    }
}
