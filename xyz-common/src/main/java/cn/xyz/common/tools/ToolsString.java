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
	public static void main(String[] args) {
		String a = "123";
		System.out.println(Double.valueOf(a));
	}
}
