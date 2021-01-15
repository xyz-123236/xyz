package cn.xyz.test.test;

public class T020 {
    public static void main(String[] args) {
        if("dgad-4556_ADF奋斗故事".matches("[\\w\\u4e00-\\u9fa5-]+")){
            System.out.println("ok");
        }else {
            System.out.println("false");
        }
    }
}
