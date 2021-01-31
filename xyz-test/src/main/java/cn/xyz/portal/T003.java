package cn.xyz.portal;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class T003 {
    public static void main(String[] args) {
        double d = 5.03056;
        BigDecimal b = new BigDecimal(Double.toString(d),new MathContext(4, RoundingMode.HALF_UP));
        System.out.println(b);

        double a=0.23456D;
        System.out.printf("%1.2e\r\n", a);

        a=12.3456D;
        System.out.printf("%1.3e\r\n", a);

        a=123456D;
        System.out.printf("%1.4e\r\n", a);


    }
}
