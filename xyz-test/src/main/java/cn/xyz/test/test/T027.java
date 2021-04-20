package cn.xyz.test.test;

public class T027 {
    public static void main(String[] args) {
        String a = "R107-108";
        String b = "RS124-RS126";
        System.out.println(a.replaceAll("[A-Z]", ""));
        System.out.println(b.replaceAll("[A-Z]", ""));

        System.out.println(a.replaceAll("\\d", ""));
        System.out.println(b.replaceAll("\\d", ""));

        String ss = a.replaceAll("\\d", "");
        System.out.println(ss.split("-").length);
    }
}
