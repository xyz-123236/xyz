package cn.xyz.common.tool;

import java.math.BigDecimal;

/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。
 */
public class ToolsDouble {
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	// 这个类不能实例化
	private ToolsDouble() {
	}

	
	//定义加减乘除
	enum BigDecimalOprations{
        add,subtract,multiply,divide
    }
	//numOne：数1	numTwo：数2	bigDecimalOpration:运算方式（BigDecimalOprations.add）	scale:精确位数	 roundingMode:BigDecimal.ROUND_DOWN
    public static BigDecimal OperationASMD(Object numOne,Object numTwo,BigDecimalOprations bigDecimalOpration,int scale,int roundingMode) throws Exception{
        BigDecimal num1 = new BigDecimal(String.valueOf(numOne)).setScale(scale,roundingMode);
        BigDecimal num2 = new BigDecimal(String.valueOf(numTwo)).setScale(scale,roundingMode);
        switch (bigDecimalOpration){
            case add: return num1.add(num2).setScale(scale,roundingMode);
            case subtract: return num1.subtract(num2).setScale(scale,roundingMode);
            case multiply: return num1.multiply(num2).setScale(scale,roundingMode);
            case divide: return num1.divide(num2, scale, roundingMode);
        }
        return null;
    }
    
    
    
	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static BigDecimal doubleToBigDecimal(double d) {
		String doubleStr = String.valueOf(d);
		if (doubleStr.indexOf(".") != -1) {
			int pointLen = doubleStr.replaceAll("\\d+\\.", "").length(); // 取得小数点后的数字的位数
			pointLen = pointLen > 16 ? 16 : pointLen; // double最大有效小数点后的位数为16
			double pow = Math.pow(10, pointLen);
			long tmp = (long) (d * pow);
			return new BigDecimal(tmp).divide(new BigDecimal(pow));
		}
		return new BigDecimal(d);
	}

	public static void main(String[] args) {
		double a = 301353.5;
		System.out.println(Math.round(a));
		 System.out.println(doubleToBigDecimal(301353.05));
		 System.out.println(doubleToBigDecimal(-301353.05));
		 System.out.println(doubleToBigDecimal(new Double(-301353.05)));
		 System.out.println(doubleToBigDecimal(301353));
		 System.out.println(doubleToBigDecimal(new Double(-301353)));
		 System.out.println(a);
		double d = 1270000009.1;// 5898895455898954895989;
		System.out.println(doubleToBigDecimal(d));
		System.out.println(d);
		System.out.println(new Double(d).toString());
		System.out.println(new BigDecimal(new Double(d).toString()));
		System.out.println(new BigDecimal(d));

		System.out.println(new BigDecimal(String.valueOf(d)).toPlainString());
	}

}
