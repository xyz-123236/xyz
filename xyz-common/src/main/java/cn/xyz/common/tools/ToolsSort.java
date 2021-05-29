package cn.xyz.common.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.Collator;
import java.util.*;

public class ToolsSort<T> {
    public static void sortAsc(JSONArray data, String sort) {
        data.sort(Comparator.comparing(obj -> ((JSONObject) obj).getString(sort)));
    }

    public static void sortDesc(JSONArray data, String sort) {
        data.sort(Comparator.comparing(obj -> ((JSONObject) obj).getString(sort)).reversed());
    }

    public static void sortZH(String[] names) {//中文排序
        Arrays.sort(names, Collator.getInstance(java.util.Locale.CHINA));//升序;
    }

    /* 以下为优先数字排序，及字段是可分割成数组
    *   {"aa":"A5-1","bb":"bdsg"}
        {"aa":"C2-2,A5-2","bb":"fdsg"}
        {"aa":"A5-12","bb":"fdsg"}
        {"aa":"A12-3","bb":"bdsg"}
    */

    public static void sort(Object[] data) {
        sort(data, "asc");
    }
    public static void sort(Object[] data, String order) {
        Arrays.parallelSort(data, (o1, o2) -> compare(o1, o2, order));
    }


    public static <T> T sort(T data, String[] sorts) {
        return sort(data, sorts, "");
    }
    public static <T> T sort(T data, String[] sorts, String spilt) {
        String[] orders = new String[sorts.length];
        Arrays.fill(orders, "asc");
        return sort(data, sorts, orders, spilt);
    }
    public static <T> T sort(T data, String sort, String order) {
        return sort(data, sort, order, "");
    }
    public static <T> T sort(T data, String sort, String order, String spilt) {
        String[] sorts = sort.split(",");
        String[] orders = order.split(",");
        return sort(data, sorts, orders, spilt);
    }
    public static <T> T sort(T data, String[] sorts, String[] orders) {
        return sort(data, sorts, orders, "");
    }
    public static <T> T sort(T data, String[] sorts, String[] orders, String spilt) {
        if (!Tools.isEmpty(data)) {
            if(data instanceof JSONArray){
                ((JSONArray) data).sort((a, b) -> {
                    for (int i = 0; i < sorts.length; i++) {
                        int t = compare(((JSONObject) a).getString(sorts[i]), ((JSONObject) b).getString(sorts[i]), orders[i], spilt);
                        if(t != 0){
                            return t;
                        }
                    }
                    return 0;
                });
            }else if(data instanceof List){
                ((List<JSONObject>) data).sort((a, b) -> {
                    for (int i = 0; i < sorts.length; i++) {
                        int t = compare(a.getString(sorts[i]), b.getString(sorts[i]), orders[i], spilt);
                        if(t != 0){
                            return t;
                        }
                    }
                    return 0;
                });
            }else if(data instanceof Object[][]){
                //Arrays.sort单线程 Arrays.parallelSort并线，超过10000用后者
                Arrays.parallelSort((Object[][])data, (o1, o2) -> {
                    for (int i = 0; i < orders.length; i++) {
                        int t = compare(o1[Integer.parseInt(sorts[i])], o2[Integer.parseInt(sorts[i])], orders[i], spilt);
                        if(t != 0){
                            return t;
                        }
                    }
                    return 0;
                });
            }else if(data instanceof Object[]){
                //Arrays.sort单线程 Arrays.parallelSort并线，超过10000用后者
                Arrays.parallelSort((Object[])data, (o1, o2) -> compare(o1, o2, orders[0], spilt));
            }
        }
        return data;
    }
    public static int compare(Object a, Object b, String order, String spilt) {
        String s1 = ToolsString.toString(a);
        String s2 = ToolsString.toString(b);
        if(Tools.isEmpty(spilt)){
            return compare(s1, s2, order);
        }else{
            String[] arr1 = s1.split(spilt);
            String[] arr2 = s2.split(spilt);
            sort(arr1);
            sort(arr2);
            for (int j = 0; j < arr1.length; j++) {
                if(arr2.length < j + 1) return 1;
                int t = compare(arr1[j], arr2[j], order);
                if(t != 0){
                    return t;
                }
            }
            if(arr1.length < arr2.length){
                return -1;
            }
            return 0;
        }
    }
    public static int compare(Object o1, Object o2, String order) {
        String valA = ToolsString.toString(o1);
        String valB = ToolsString.toString(o2);
        if (compare(valA, valB) > 0) {
            return "asc".equals(order) ? 1 : -1;
        } else if (compare(valA, valB) < 0) {
            return "asc".equals(order) ? -1 : 1;
        }
        return 0;
    }
    public static int compare(String a, String b) {
        if (Tools.isEmpty(a) && Tools.isEmpty(b)) return 0;
        if (Tools.isEmpty(a)) return -1;
        if (Tools.isEmpty(b)) return 1;
        if (a.equals(b)) return 0;
        List<String[]> al = spilt(a);
        List<String[]> bl = spilt(b);
        for (int i = 0; i < al.size(); i++) {
            if (bl.size() < i + 1) return 1;
            String[] ar = al.get(i);
            String[] br = bl.get(i);
            if (ar[1].equals(br[1])) {
                continue;
            }
            if ("N".equals(ar[0]) && "N".equals(br[0])) {
                return Integer.parseInt(ar[1]) > Integer.parseInt(br[1]) ? 1 : -1;
            } else {
                return ar[1].compareToIgnoreCase(br[1]);
            }
        }
        if(al.size() == bl.size()){
            return 0;
        }
        return -1;
    }

    public static List<String[]> spilt(String str) {
        List<String[]> l = new ArrayList<>();
        if (!Tools.isEmpty(str)) {
            StringBuilder temp = new StringBuilder();
            String type = "";
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isDigit(c)) {
                    if ("N".equals(type) || "".equals(type)) {
                        temp.append(c);
                    } else {
                        String[] arr = new String[2];
                        arr[0] = "S";
                        arr[1] = temp.toString();
                        l.add(arr);
                        temp = new StringBuilder(String.valueOf(c));
                    }
                    type = "N";
                } else {
                    if ("S".equals(type) || "".equals(type)) {
                        temp.append(c);
                    } else {
                        String[] arr = new String[2];
                        arr[0] = "N";
                        arr[1] = temp.toString();
                        l.add(arr);
                        temp = new StringBuilder(String.valueOf(c));
                    }
                    type = "S";
                }
            }
            String[] arr = new String[2];
            arr[0] = type;
            arr[1] = temp.toString();
            l.add(arr);
        }
        return l;
    }


    public static void main(String[] args) {
        try {
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("aa", "C2-2,A5-2");
            obj1.put("bb", "fdsg");
            JSONObject obj2 = new JSONObject();
            obj2.put("aa", "A5-12");
            obj2.put("bb", "fdsg");
            JSONObject obj3 = new JSONObject();
            obj3.put("aa", "A12-3");
            obj3.put("bb", "bdsg");
            JSONObject obj4 = new JSONObject();
            obj4.put("aa", "A5-1");
            obj4.put("bb", "bdsg");
            arr.add(obj1);
            arr.add(obj2);
            arr.add(obj3);
            arr.add(obj4);
            ToolsSort.sort(arr, new String[]{"aa","bb"}, ",");
            arr.forEach(System.out::println);
            ToolsSort.sortAsc(new JSONArray(), "");
            ToolsSort.sortDesc(new JSONArray(), "");
            System.out.println(compare("5A", "11A23"));
            System.out.println(compare("11A", "11A23"));
            System.out.println(compare("11A23", "11A"));
            ToolsSys.isWindowsOS();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        Object[][] array = new Object[][]{
                {12, 55, 68, 32, 9, 12, 545},
                {34, 72, 82, 57, 56, 0, 213},
                {"11A", 34, 68, 32, 21, 945, 23},
                {"11A23", 10, 3, 2354, 73, 34, 18},
                {"11A156a", 83, 189, 26, 27, 98, 33},
                {"zz", 23, 889, 24, 899, 23, 657},
                {null, 34, 68, 343, 878, 235, 768},
                {12, 34, 98, 56, 78, 12, 546},
                {26, 78, 2365, 78, 34, 256, 873}};


        sort(array, new String[]{"0", "1"});   //先根据第一列比较，若相同则再比较第二列
        System.out.println(JSON.toJSONString(array));

        Object[] array1 = new Object[]{"C10-2", "C2-32", "C2-5", 32, 9, "AB5-6", "C2-"};
        sort(array1);
        System.out.println(JSON.toJSONString(array1));
        //可能是ftp/httpclient多文件上传
        //MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        //中文排序
        /*List<String> cityList = new ArrayList<String>();
        cityList.add("上海");
        cityList.add("北京");
        cityList.add("广州");
        cityList.add("深圳");

        Collections.sort(cityList, CHINA_COMPARE);
        System.out.println(cityList);*/

    }
}
