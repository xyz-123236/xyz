package cn.xyz.test.test;

import java.util.Objects;

public class T001 {
    public static void main(String[] args) throws ClassNotFoundException {
        //T001 t001 = new T001();
        //t001.getClass() = T001.class
        //getClassLoader()相当于获取根目录
        System.out.println("01 " + T001.class);//class cn.xyz.test.test.T001
        System.out.println("01 " + Class.forName("cn.xyz.test.test.T001"));//class cn.xyz.test.test.T001

        //System.out.println("02 " + T001.class.getClass());//class java.lang.Class
        System.out.println("02 " + T001.class.getSuperclass());//class java.lang.Object

        System.out.println("03 " + T001.class.getClassLoader());//当前类应用类加载器sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println("03 " + T001.class.getClassLoader().getParent());//扩展类加载器sun.misc.Launcher$ExtClassLoader@776ec8df
        System.out.println("03 " + T001.class.getClassLoader().getParent().getParent());//根类加载器null
        System.out.println("03 " + Thread.currentThread().getContextClassLoader());//当前线程加载器null
        System.out.println("03 " + ClassLoader.getSystemClassLoader());//系统加载器，不是根类加载器，是AppClassLoader


        System.out.println("04 " + T001.class.getResource("/db.properties").getFile());// /E:/zm/xyz/xyz-test/target/classes/db.properties
        System.out.println("04 " + Objects.requireNonNull(T001.class.getClassLoader().getResource("db.properties")).getFile());// /E:/zm/xyz/xyz-test/target/classes/db.properties
        System.out.println("04 " + Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("db.properties")).getFile());// /E:/zm/xyz/xyz-test/target/classes/db.properties
        System.out.println("04 " + ClassLoader.getSystemResource("db.properties").getFile());// /E:/zm/xyz/xyz-test/target/classes/db.properties
        System.out.println("04 " + Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("db.properties")).getFile());// /E:/zm/xyz/xyz-test/target/classes/db.properties

        //getResourceAsStream 获取同级及子目录不用加/
        System.out.println("05 " + T001.class.getResourceAsStream("/db.properties"));//加/ java.io.BufferedInputStream@4eec7777
        System.out.println("05 " + T001.class.getClassLoader().getResourceAsStream("db.properties"));//不加/ java.io.BufferedInputStream@4eec7777

        //ServletActionContext.getServletContext().getRealPath("/"));
        //ServletContext.getResourceAsStream(String path);
    }
}
