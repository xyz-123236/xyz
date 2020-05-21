package cn.xyz.common.tools;

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
	
	public static String format(Object obj) {
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
		if (str != null) {
			str = str.trim();// 去除前后半角空格
			// 去除前后全角半角空格
			while (str.startsWith("　")) {// 循环
				str = str.substring(1, str.length()).trim();        //每截取一次全角空格，都trim清除一次半角空格，保证清除内部的半角
			}
			while (str.endsWith("　")) {// 循环
				str = str.substring(0, str.length() - 1).trim();
			}
			/*while (str.startsWith("　") || str.endsWith("　")) {// 循环
				str = str.replaceAll("　", "").trim();        //不能用，会清除中间的空格
			}*/
		}
		return str;
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
	public static void main(String[] args) {
		String b = "123";
		System.out.println(Double.valueOf(b));
		String a=" 　　　   afdsasdf      　　　　";
		System.out.println(trim(a));
		String[] c = {"","B","c"};
		System.out.println(String.join(",", c));
	}
}
