package cn.xyz.common.tools;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import com.alibaba.fastjson.JSON;

import cn.xyz.common.pojo.Basic;

public class ToolsBean {

	public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) throws Exception {    
        if (map == null)  
            return null;    
  
        T obj = beanClass.newInstance();  
  
        Field[] fields = obj.getClass().getDeclaredFields();   
        for (Field field : fields) {    
            int mod = field.getModifiers();    
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){    
                continue;    
            }  
            field.setAccessible(true); 
            if(!Tools.isEmpty(map.get(field.getName()))) {
            	field.set(obj, map.get(field.getName())); 
            }  
        }   
  
        return obj;    
    }    
  
    public static Map<String, Object> beanToMap(Object obj) throws Exception {    
        if(obj == null){    
            return null;    
        }   
  
        Map<String, Object> map = new HashMap<String, Object>();    
  
        Field[] declaredFields = obj.getClass().getDeclaredFields();    
        for (Field field : declaredFields) {    
            field.setAccessible(true);  
            map.put(field.getName(), field.get(obj));  
        }    
  
        return map;  
    }
    public static <T> T mapToObject2(Map<String, Object> map, Class<T> beanClass) throws Exception {    
        if (map == null)  
            return null;  
  
        T obj = beanClass.newInstance();  
        BeanUtils.populate(obj, map);  
  
        return obj;  
    }    
      
    public static Map<?, ?> objectToMap2(Object obj) {  
        if(obj == null)  
            return null;   
  
        return new BeanMap(obj);  
    }    
    public static void main(String[] args) {
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("page", 10);
    		map.put("rows", "");
    		Basic b = mapToBean(map, Basic.class );
    		System.out.println(b.getPage());
    		System.out.println(b.getRows());
    		Map<?, ?> map2 = objectToMap2(b);
    		System.out.println(map2.get("page"));
    		System.out.println(map2.get("rows"));
			//JSON.parseObject("", Basic.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
