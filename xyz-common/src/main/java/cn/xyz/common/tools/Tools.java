package cn.xyz.common.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Tools {

    public static boolean isEmpty(Object obj) {
		if (obj == null) {
            return true;
        }else if (obj instanceof String) {
			return ((String) obj).trim().length() == 0;
		}else if(obj instanceof List){
			return ((List<?>) obj).isEmpty() || ((List<?>) obj).size() == 0;
		}else if(obj instanceof Map){
			return ((Map<?, ?>) obj).isEmpty() || ((Map<?, ?>) obj).size() == 0;
		}else if(obj.getClass().isArray()){
			return ((Object[]) obj).length == 0;
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
			for (Object value : o) {
				if (!isEmpty(value)) {
					return false;
				}
			}
			return true;
		}else {
			Class<?> object = obj.getClass();// 得到类对象
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
		Class<?> clazz = Class.forName(obj);
		//1.获取方法
		//  1.1 获取取clazz对应类中的所有方法--方法数组（一）
		Method[] methods = clazz.getMethods();//不能获取private方法,且获取从父类继承来的所有方法
		Method[] methods2 = clazz.getDeclaredMethods();//所有方法
		//调用invoke方法来调用
		System.out.println(Arrays.toString(methods));
		System.out.println(Arrays.toString(methods2));
	}
	public static void sort(Object[][] object, int[] sorts) {
		String[] orders = new String[sorts.length];
		Arrays.fill(orders, "asc");
		sort(object, sorts, orders);
	}
	public static void sort(Object[][] object, int[] sort, String[] order) {
    	//Arrays.sort单线程 Arrays.parallelSort并线，超过10000用后者
		Arrays.parallelSort(object, (o1, o2) -> {
			for (int i = 0; i < order.length; i++) {
				String valA = ToolsString.toString(o1[sort[i]]);
				String valB = ToolsString.toString(o2[sort[i]]);
				try {
					if (sort(valA,valB) > 0) {
						return "asc".equals(order[i])? 1 : -1;
					} else if (sort(valA,valB) < 0) {
						return "asc".equals(order[i])? -1 : 1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return 0;
		});
    }  
	public static void sortAsc(JSONArray data, String sort) {
		data.sort(Comparator.comparing(obj -> ((JSONObject) obj).getString(sort)));
	}
	public static void sortDesc(JSONArray data, String sort) {
		data.sort(Comparator.comparing(obj -> ((JSONObject) obj).getString(sort)).reversed());
	}
	public static void sort(String[] names){//中文排序
		Arrays.sort(names, Collator.getInstance(java.util.Locale.CHINA));//升序;
	}
	public static JSONArray sort(JSONArray data, String sort) {
		String[] sorts = sort.split(",");
		return sort(data, sorts);
	}
	public static JSONArray sort(JSONArray data, String sort, String order) {
		String[] sorts = sort.split(",");
    	String[] orders = order.split(",");
    	return sort(data, sorts, orders);
	}
	public static JSONArray sort(JSONArray data, String[] sorts) {
		String[] orders = new String[sorts.length];
		Arrays.fill(orders, "asc");
		return sort(data, sorts, orders);
	}
	public static JSONArray sort(JSONArray data, String[] sorts, String[] orders) {
		if(!Tools.isEmpty(data)) {
			data.sort((a, b) -> {
				for (int i = 0; i < sorts.length; i++) {
					String valA = ToolsString.toString(((JSONObject)a).getString(sorts[i]));
					String valB = ToolsString.toString(((JSONObject)b).getString(sorts[i]));
					try {
						if (sort(valA, valB) > 0) {
							return "asc".equals(orders[i]) ? 1 : -1;
						} else if (sort(valA, valB) < 0) {
							return "asc".equals(orders[i]) ? -1 : 1;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return 0;
			});
		}
		return data;
	}
	public static int sort(String a, String b) {
		if(isEmpty(a)) return -1;
		if(isEmpty(b)) return 1;
		if(a.equals(b)) return 0;
		List<String[]> al = spilt(a);
		List<String[]> bl = spilt(b);
		for (int i = 0; i < al.size(); i++) {
			if(bl.size() < i+1) return 1;
			String[] ar = al.get(i);
			String[] br = bl.get(i);
			if(ar[1].equals(br[1])) {
				continue;
			}
			if("N".equals(ar[0]) && "N".equals(br[0])) {
				return Integer.parseInt(ar[1]) > Integer.parseInt(br[1]) ? 1 : -1;
			}else {
				return ar[1].compareToIgnoreCase(br[1]);
			}
		}
		return -1;
	}
	public static List<String[]> spilt(String str){
		List<String[]> l = new ArrayList<>();
		if(!isEmpty(str)) {
			StringBuilder temp = new StringBuilder();
			String type = "";
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (Character.isDigit(c)) {
					if("N".equals(type) || "".equals(type)) {
						temp.append(c);
					}else {
						String[] arr = new String[2];
						arr[0] = "S";
						arr[1] = temp.toString();
						l.add(arr);
						temp = new StringBuilder(String.valueOf(c));
					}
					type = "N";
				}else {
					if("S".equals(type) || "".equals(type)) {
						temp.append(c);
					}else {
						String[] arr = new String[2];
						arr[0] = "N";
						arr[1] = temp.toString();
						l.add(arr);
						temp = new StringBuilder(String.valueOf(c));
					}
					type = "S";
				}
			}
			String[] arr = new String[2];
			arr[0] = type;
			arr[1] = temp.toString();
			l.add(arr);
		}
		return l;
	}
	public static JSONArray limit(JSONArray data, Integer page, Integer rows) {
		if(!Tools.isEmpty(data)){
			int begin = (page-1)*rows;
			int end = Math.min((begin + rows), data.size());
			JSONArray t = new JSONArray();
			for (int i = begin; i < end; i++) {
				t.add(data.getJSONObject(i));
			}
			return t;
			//return JSON.parseArray(JSON.toJSONString(data.subList(begin, end)));
		}
		return data;
	}
	public static JSONObject error(String msg) {
		JSONObject obj = new JSONObject();
		obj.put("status", false);
		obj.put("msg", msg);
		return obj;
	}
	public static JSONObject success(Object data) {
		JSONObject obj = new JSONObject();
		obj.put("status", true);
		obj.put("data", data);
		return obj;
	}
	
	public static void main(String[] args) {
		try {
			JSONArray arr = new JSONArray();
			JSONObject obj1 = new JSONObject();
			obj1.put("aa","gdf");
			obj1.put("bb","fdsg");
			JSONObject obj2 = new JSONObject();
			obj2.put("aa","adf");
			obj2.put("bb","fdsg");
			JSONObject obj3 = new JSONObject();
			obj3.put("aa","adf");
			obj3.put("bb","bdsg");
			JSONObject obj4 = new JSONObject();
			obj4.put("aa","edf");
			obj4.put("bb","bdsg");
			arr.add(obj1);
			arr.add(obj2);
			arr.add(obj3);
			arr.add(obj4);
			Tools.sort(arr,"aa,bb");
			arr.forEach(System.out::println);
			Tools.sortAsc(new JSONArray(), "");
			Tools.sortDesc(new JSONArray(), "");
			System.out.println(sort("5A","11A23"));
			System.out.println(sort("11A","11A23"));
			System.out.println(sort("11A23","11A"));
			ToolsSys.isWindowsOS();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		Object[][] array = new Object[][] {
            { 12, 55, 68, 32, 9, 12, 545 },     
            { 34, 72, 82, 57, 56, 0, 213 },     
            { "11A", 34, 68, 32, 21, 945, 23 },     
            { "11A23", 10, 3, 2354, 73, 34, 18 },    
            { "11A156a", 83, 189, 26, 27, 98, 33 },     
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
