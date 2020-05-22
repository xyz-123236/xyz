package cn.xyz.common.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Tools {
	private final static Comparator<Object> CHINA_COMPARE = Collator.getInstance(java.util.Locale.CHINA);

    
    public static boolean isEmpty(Object obj) throws Exception {
		if (obj == null) {
            return true;
        }else if (obj instanceof String) {
			if(((String)obj).trim().length() == 0) {
				return true;
			}
		}else if(obj instanceof List){
			if(((List<?>)obj).isEmpty() || ((List<?>)obj).size() == 0) {
				return true;
			}
		}else if(obj instanceof Map){
			if(((Map<?, ?>)obj).isEmpty() || ((Map<?, ?>)obj).size() == 0) {
				return true;
			}
		}else if(obj.getClass().isArray()){
			if(((Object[])obj).length == 0) {
				return true;
			}
			/*Object[] o = (Object[])obj;
			for (int i = 0; i < o.length; i++) {
				if(!isEmpty(o[i])) {
					return false;
				}
			}
			return true;*/
		}
		return false;
	}
	public static boolean isEmptyValue(Object obj) throws Exception {
		if(isEmpty(obj)) {
			return true;
		}else if(obj instanceof List){
			List<?> list = (List<?>)obj;
			for (Object object : list) {
				if(!isEmpty(object)) {
					return false;
				}
			}
			return true;
		}else if(obj instanceof Map){
			for(Object key: ((Map<?, ?>)obj).keySet()) {
				if(!isEmpty(((Map<?, ?>)obj).get(key))) {
					return false;
				}
			}
			return true;
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
	}
	public static void test(String obj) throws ClassNotFoundException {
		//获取字节码对象.class .getClass() Class.forName(全类名)
		Class<?> clazz = (Class<?>) Class.forName(obj);
		//1.获取方法
		//  1.1 获取取clazz对应类中的所有方法--方法数组（一）
		Method[] methods = clazz.getMethods();//不能获取private方法,且获取从父类继承来的所有方法
		methods = clazz.getDeclaredMethods();//所有方法
		//调用invoke方法来调用
	}
	public static void sort(Object[][] object, int[] sorts) {
		String[] orders = new String[sorts.length];
		for (int i = 0; i < orders.length; i++) {
			orders[i] = "asc";
		}
		sort(object, sorts, orders);
	}
	public static void sort(Object[][] object, int[] sort, String[] order) {    
        Arrays.sort(object, new Comparator<Object>() {    
            public int compare(Object o1, Object o2) {    
            	Object[] one = (Object[]) o1;    
            	Object[] two = (Object[]) o2;    
                for (int i = 0; i < order.length; i++) {
                    String valA = ToolsString.toString(one[sort[i]]);
	                String valB = ToolsString.toString(two[sort[i]]);
                    if (valA.compareToIgnoreCase(valB) > 0) { 
                        return "asc".equals(order[i])? 1 : -1;    
                	} else if (valA.compareToIgnoreCase(valB) < 0) {   
                        return "asc".equals(order[i])? -1 : 1;    
                    } else {    
                        continue;  //如果按一条件比较结果相等，就使用第二个条件进行比较。  
                    }
                }
                return 0;
            }
        });
    }  
	public static void sortAsc(JSONArray data, String sort) {
		data.sort(Comparator.comparing(obj -> ((JSONObject) obj).getString(sort)));
		
	}
	public static void sortDesc(JSONArray data, String sort) {
		data.sort(Comparator.comparing(obj -> ((JSONObject) obj).getString(sort)).reversed());
	}
	public static JSONArray sort(JSONArray data, String sort) throws Exception {
		String[] sorts = sort.split(",");
		return sort(data, sorts);
	}
	public static JSONArray sort(JSONArray data, String sort, String order) throws Exception {
		String[] sorts = sort.split(",");
    	String[] orders = order.split(",");
    	return sort(data, sorts, orders);
	}
	public static JSONArray sort(JSONArray data, String[] sorts) throws Exception {
		String[] orders = new String[sorts.length];
		for (int i = 0; i < orders.length; i++) {
			orders[i] = "asc";
		}
		return sort(data, sorts, orders);
	}
	public static JSONArray sort(JSONArray data, String[] sorts, String[] orders) throws Exception {
		if(!Tools.isEmpty(data)) {
			JSONArray sortData = new JSONArray();
	        List<JSONObject> list = new ArrayList<JSONObject>();
	        for (int i = 0; i < data.size(); i++) {
	        	list.add(data.getJSONObject(i));
	        }
	        Collections.sort(list, (JSONObject a, JSONObject b)-> {
	        	for (int i = 0; i < sorts.length; i++) {    
	        		String valA = ToolsString.toString(a.getString(sorts[i]));
	                String valB = ToolsString.toString(b.getString(sorts[i]));
	                if (valA.compareToIgnoreCase(valB) > 0) { 
                        return "asc".equals(orders[i])? 1 : -1;    
                	} else if (valA.compareToIgnoreCase(valB) < 0) {   
                        return "asc".equals(orders[i])? -1 : 1;    
                    } else {    
                        continue;  //如果按一条件比较结果相等，就使用第二个条件进行比较。  
                    }
                }
                return 0;
	        });
	        for (int i = 0; i < list.size(); i++) {
	        	sortData.add(list.get(i));
	        }
	        return sortData;
		}
		return data;
	}
	public static JSONArray limit(JSONArray data, Integer page, Integer rows) throws Exception {
		if(!Tools.isEmpty(data)){
			int begin = (page-1)*rows;
			int end = (begin+rows) > data.size()?data.size():(begin+rows);
			JSONArray t = new JSONArray();
			for (int i = begin; i < end; i++) {
				t.add(data.getJSONObject(i));
			}
			return t;
			//return JSON.parseArray(JSON.toJSONString(data.subList(begin, end)));
		}
		return data;
	}
	public static void main(String[] args) {
		Object array[][] = new Object[][] {     
            { 12, 55, 68, 32, 9, 12, 545 },     
            { 34, 72, 82, 57, 56, 0, 213 },     
            { 12, 34, 68, 32, 21, 945, 23 },     
            { 91, 10, 3, 2354, 73, 34, 18 },    
            { 12, 83, 189, 26, 27, 98, 33 },     
            { "zz", 23, 889, 24, 899, 23, 657 },     
            { null, 34, 68, 343, 878, 235, 768 },     
            { 12, 34, 98, 56, 78, 12, 546 },     
            { 26, 78, 2365, 78, 34, 256, 873 } }; 

            
	    sort(array, new int[] {0,1});   //先根据第一列比较，若相同则再比较第二列
	    System.out.println(JSON.toJSONString(array));
    	//可能是ftp/httpclient多文件上传
    	//MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    	//中文排序
        /*List<String> cityList = new ArrayList<String>();
        cityList.add("上海");
        cityList.add("北京");
        cityList.add("广州");
        cityList.add("深圳");

        Collections.sort(cityList, CHINA_COMPARE);
        System.out.println(cityList);*/
		JSONObject employee = new JSONObject();
		employee.put("name", null);
		employee.put("pwd", null);
		try {
			System.out.println(isEmpty(employee));
			System.out.println(isEmptyValue(employee));
			
			Integer[] arr = {};
			System.out.println(isEmpty(arr));
			System.out.println(isEmptyValue(arr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
