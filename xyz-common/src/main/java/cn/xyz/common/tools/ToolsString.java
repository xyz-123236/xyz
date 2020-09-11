package cn.xyz.common.tools;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
	public static boolean same(String str1, String str2) throws Exception {
		if(Tools.isEmpty(str1) && Tools.isEmpty(str2)) return true;
		if(Tools.isEmpty(str1)) return false;
		if(Tools.isEmpty(str2)) return false;
		if(str1.equals(str2)) {
			return true;
		}
		return false;
	}
	private static int num = 0;
	public static synchronized String getId() {
		num = ++num % 1000;
		return ToolsDate.getLong()+String.format("%03d", num);
	}
	public static synchronized String getId(HttpServletRequest request) throws Exception {
		num = ++num % 1000;
		String id = ToolsDate.getString("yyyyMMddHHmmssSSS")+String.format("%03d", num);
		//+new Random().nextInt(1000);
		String ip = ToolsSys.getRemoteIP(request);
		String[] ips = ip.split("\\.");
		for (int i = 0; i < ips.length; i++) {
			id += String.format("%03d", Integer.parseInt(ips[i]));
		}
		return id;
	}
	public static void main(String[] args) {
		try {
			long a = new Date().getTime();
			String id = "";
			System.out.println(new Date().getTime()-a);
			for (int i = 0; i < 100; i++) {
				id = UUID.randomUUID().toString().replaceAll("-", "");
			}
			System.out.println(new Date().getTime()-a);
			for (int i = 0; i < 100; i++) {
				id = UUID.randomUUID().toString();
			}
			System.out.println(new Date().getTime()-a);
			for (int i = 0; i < 10000; i++) {
				id = getId();
			}
			System.out.println(new Date().getTime()-a);
			for (int i = 0; i < 10000; i++) {
				id = getId();
			}
			
			System.out.println(new Date().getTime()-a);
			/*new Thread(()->{
				for (int i = 0; i < 100; i++) {
					System.out.println("t1:"+getId());
				}
			},"t1").start();
			new Thread(()->{
				for (int i = 0; i < 100; i++) {
					System.out.println("t2:"+getId());
				}
			},"t2").start();
			String b = "123";
			System.out.println(Double.valueOf(b));
			String a=" 　　　   afdsasdf      　　　　";
			System.out.println(trim(a));
			String[] c = {"","B","c"};
			System.out.println(String.join(",", c));
			System.out.println(big5ToChinese(ChineseTobig5("好好学习")));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
