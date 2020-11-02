package cn.xyz.common.tools;

import java.math.BigDecimal;

/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。
 */
public class ToolsDouble {
	// 默认除法运算精度
	private static final int DEF_SCALE = 2;
	//定义加减乘除
	enum DoubleOpr{
        ADD,SUB,MUL,DIV
    }
	
	//DoubleOpr:运算方式
    public static Double opr(DoubleOpr opr, Object v1, Object v2, int scale) {
        BigDecimal num1 = new BigDecimal(String.valueOf(v1));
        BigDecimal num2 = new BigDecimal(String.valueOf(v2));
        switch (opr){
            case ADD: return num1.add(num2).setScale(scale).doubleValue();
            case SUB: return num1.subtract(num2).setScale(scale).doubleValue();
            case MUL: return num1.multiply(num2).setScale(scale).doubleValue();
            case DIV: return num1.divide(num2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return null;
    }
    
	public static Double add(Double v1, Double v2) {//加
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}
	public static Double sub(Double v1, Double v2) {//减
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	public static Double mul(Double v1, Double v2) {//乘
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}
	public static Double div(Double v1, Double v2) {//除
		return div(v1, v2, DEF_SCALE);
	}
	public static Double div(Double v1, Double v2, int scale) {//除
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, handleScale(scale), BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static Double round(Double v) {//四舍五入
		return round(v, DEF_SCALE);
	}
	public static Double round(Double v, int scale) {//四舍五入
		return new BigDecimal(String.valueOf(v)).setScale(handleScale(scale), BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static Double up(Double v, int scale) {//向上舍入
		return new BigDecimal(String.valueOf(v)).setScale(handleScale(scale), BigDecimal.ROUND_UP).doubleValue();
	}
	public static Double down(Double v, int scale) {//向下
		return new BigDecimal(String.valueOf(v)).setScale(handleScale(scale), BigDecimal.ROUND_DOWN).doubleValue();
	}
	public static int handleScale(int scale) {//四舍五入
		return scale < 0 ? DEF_SCALE : scale;
	}
	public static String getString(Double v) {//转为字符串
		return new BigDecimal(String.valueOf(v)).toPlainString();
	}

	public static void main(String[] args) {
		/*Double a = 38.45666666666666;
		System.out.println(round(a, 2));
		int b = 5;
		try {
			
			BigDecimal aa = new BigDecimal(1.30);
			System.out.println(Tools.isEmpty(b));
			System.out.println(mul(1.21,1.11));
			System.out.println(new BigDecimal("123.4", new MathContext(4,RoundingMode.HALF_UP)));
			System.out.println(new BigDecimal("123.4", new MathContext(2,
					RoundingMode.HALF_UP)));
			System.out.println(new BigDecimal("123.4", new MathContext(2,
					RoundingMode.CEILING)));
			System.out.println(new BigDecimal("123.4", new MathContext(1,
					RoundingMode.CEILING)));
			System.out.println(aa.setScale(2, BigDecimal.ROUND_HALF_UP));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		/*Double a = 301353.5;
		System.out.println(Math.round(a));
		 System.out.println(DoubleToBigDecimal(301353.05));
		 System.out.println(DoubleToBigDecimal(-301353.05));
		 System.out.println(DoubleToBigDecimal(new Double(-301353.05)));
		 System.out.println(DoubleToBigDecimal(301353));
		 System.out.println(DoubleToBigDecimal(new Double(-301353)));
		 System.out.println(a);*/
		try {
			System.out.println(opr(DoubleOpr.DIV,1.0,3.0,3));
			System.out.println(div(1.0,3.0,3));
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*Double d = 1270000009.1;// 5898895455898954895989;
		System.out.println(d);
		System.out.println(new Double(d).toString());
		System.out.println(new BigDecimal(new Double(d).toString()));
		System.out.println(new BigDecimal(d));
		System.out.println(new BigDecimal(String.valueOf(d)));
		System.out.println(new BigDecimal(String.valueOf(d)).toPlainString());*/
	}

}
