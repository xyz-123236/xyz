package cn.xyz.pattern.责任链模式;

import cn.xyz.common.exception.CustomException;

public class T001 {
    public static int INFO = 1;
    public static int DEBUG = 2;
    public static int ERROR = 3;
    public static void main(String[] args) throws CustomException {
        print(INFO, "This is an information.");
        print(DEBUG,"This is a debug level information.");
        print(ERROR,"This is an error information.");
    }
    public static void print(int type, String msg) throws CustomException {
        switch (type){
            case 3:
                System.out.println("Error Console::Logger: " + msg);
            case 2:
                System.out.println("File::Logger: " + msg);
            case 1:
                System.out.println("Standard Console::Logger: " + msg);
                break;
            default:
                throw new CustomException("not find type");
        }
    }
}
