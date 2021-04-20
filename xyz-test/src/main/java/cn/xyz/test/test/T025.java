package cn.xyz.test.test;

public class T025 {
    public static void main(String[] args) {
        String b = "4373540.0";
       /* System.out.println(b.replace(".0",""));
        System.out.println(b.replace("\\.0",""));
        System.out.println(b.replaceAll("\\.0",""));*/
        String a = "                                      1";
        if(a.contains(" ")){
            System.out.println("a"+a.replaceAll(" ",""));
        }
        if(a.contains(" ")){
            System.out.println("a"+a.replaceAll(" ",""));
        }
        if(a.contains("　")){
            System.out.println("c"+a.replaceAll("　",""));
        }
        if(a.contains("　")){
            System.out.println("d"+a.replaceAll("　",""));
        }
        /*System.out.println("1"+a.trim());
        System.out.println("2"+a.replaceAll(" ",""));
        System.out.println("2"+a.replaceAll(" ",""));
        System.out.println("2"+a.replaceAll("　",""));
        System.out.println("2"+a.replaceAll("　",""));

        System.out.println(((int)(' '))+"");
        System.out.println(((int)('　'))+"");*/

        System.out.println("半角空格"+((int)(' ')));
        System.out.println("半角空格"+((int)(' ')));
        System.out.println("全角空格"+((int)('　')));
        System.out.println("全角空格"+((int)('　')));
        System.out.println("特殊字符"+((int)(' ')));
        System.out.println("特殊字符"+((int)(' ')));
    }
}
