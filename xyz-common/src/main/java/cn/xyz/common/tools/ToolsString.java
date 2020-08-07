package cn.xyz.common.tools;

import java.io.UnsupportedEncodingException;

import org.apache.poi.hssf.record.DBCellRecord;

import com.alibaba.fastjson.JSONObject;

public class ToolsString {
	public static JSONObject fileds = new JSONObject();
	public static final String DEFAULT_REGEX = ",";
	static {
		fileds.put("emp_name", "displayname");
		fileds.put("empno", "userno");
		fileds.put("prod_ctr", "departmentno");
		fileds.put("arbpl", "workcenterNo");
		fileds.put("prod_des", "departmentname");
	}
	public static String filedConvert(String filed){
		if(fileds.containsKey(filed)) {
			return fileds.getString(filed);
		}
		return filed.toLowerCase().trim();
	}
	
	public static String toString(Object obj) {
		return (obj == null) ? "" : obj.toString();
	}
	public static String join(Object[] array, String... regex) throws Exception {
		StringBuffer sb = new StringBuffer();
		String _regex = Tools.isEmpty(regex) ? DEFAULT_REGEX : regex[0];
		for (int i = 0; i < array.length; i++) {
            if (i > 0) {
            	sb.append(_regex);
            }
            if (array[i] != null) {
            	sb.append(array[i]);
            }
        }
		return sb.toString();
	}
	
	/**
	 * 字符串去除前后空格　解决 String tirm()方法　对全角空格无效的问题
	 * 
	 * @param orin  需要进行处理的字符串
	 * @return String 处理完成的结果字符串
	 */
	public static String trim(String str) {
		String s = "";
		if (str != null) {
			s = str.trim();// 去除前后半角空格
			// 去除前后全角半角空格
			while (s.startsWith("　")) {// 循环
				s = s.substring(1, s.length()).trim();        //每截取一次全角空格，都trim清除一次半角空格，保证清除内部的半角
			}
			while (s.endsWith("　")) {// 循环
				s = s.substring(0, s.length() - 1).trim();
			}
			/*while (str.startsWith("　") || str.endsWith("　")) {// 循环
				str = str.replaceAll("　", "").trim();        //不能用，会清除中间的空格
			}*/
		}
		return s;
	}
	
	public static int length(String str) {
		if (str == null)
			return 0;
		try {
			return new String(str.getBytes("GBK"), "8859_1").length();
		} catch (Exception e) {
			return -1;
		}
	}
	public static String big5ToChinese(String s) throws UnsupportedEncodingException {
		return new String(toString(s).getBytes("big5"), "gb2312");//不能用utf-8
	}
	public static String ChineseTobig5(String s) throws UnsupportedEncodingException {
	    return new String(toString(s).getBytes("gb2312"), "big5");
	}
	public static String toLowerFirstWord(String name){
        char[] charArray = name.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }
	public static void main(String[] args) {
		try {
			String b = "123";
			System.out.println(Double.valueOf(b));
			String a=" 　　　   afdsasdf      　　　　";
			System.out.println(trim(a));
			String[] c = {"","B","c"};
			System.out.println(String.join(",", c));
			System.out.println(big5ToChinese(ChineseTobig5("好好学习")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
