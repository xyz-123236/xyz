package cn.xyz.test.tools;

import java.lang.reflect.Field;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.mime.MultipartEntityBuilder;

public class Tools {
	private final static Comparator<Object> CHINA_COMPARE = Collator.getInstance(java.util.Locale.CHINA);

    public static void main(String[] args) {
    	//可能是ftp/httpclient多文件上传
    	MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    	//中文排序
        List<String> cityList = new ArrayList<String>();
        cityList.add("上海");
        cityList.add("北京");
        cityList.add("广州");
        cityList.add("深圳");

        Collections.sort(cityList, CHINA_COMPARE);
        System.out.println(cityList);
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
		}
		return false;
	}
	public static boolean isEmptyObject(Object obj) throws Exception {
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
}
