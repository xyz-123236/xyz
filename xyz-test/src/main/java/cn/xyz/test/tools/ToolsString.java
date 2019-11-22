package cn.xyz.test.tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class ToolsString {
	public static JSONObject fileds = new JSONObject();
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
	public static boolean isEmpty(Object obj) throws Exception {
		if (obj == null) {
            return true;
        }else if (obj instanceof String) {
			if(((String)obj).trim().length() == 0) {
				return true;
			}
		}else if(obj instanceof List){
			if(((List<?>)obj).size() == 0 || ((List<?>)obj).isEmpty()) {
				return true;
			}
		}else if(obj instanceof Map){
			if(((Map<?, ?>)obj).isEmpty() || ((Map<?, ?>)obj).size() == 0) {
				return true;
			}
		}else if(obj.getClass().isArray()){
			Object[] o = (Object[])obj;
			for (int i = 0; i < o.length; i++) {
				if(!isEmpty(o[i])) {
					return false;
				}
			}
			return true;
		}else {
			Class<?> object = (Class<?>) obj.getClass();// 得到类对象
	        Field[] fs = object.getDeclaredFields();//得到属性集合
				for (Field f : fs) {//遍历属性
				    f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
				    if(!isEmpty(f.get(obj))) {// 得到此属性的值//只要有1个属性不为空,那么就不是所有的属性值都为空
				        return false;
				    }
				}
	        return true;
		}
		return false;
	}
}
