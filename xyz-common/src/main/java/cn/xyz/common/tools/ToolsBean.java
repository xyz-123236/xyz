package cn.xyz.common.tools;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import cn.xyz.common.pojo.Basic;

public class ToolsBean {

	public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) throws InstantiationException, IllegalAccessException {    
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
  
        Map<String, Object> map = new HashMap<>();
  
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

    private static <T> String getValue(String f, T t) throws Exception {
        /*Field[] declaredFields = t.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            String name = field.getName();
            if (name.equals(f)) {
                String getField = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                try {
                    return t.getClass().getMethod(getField).invoke(t).toString();
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    logger.error("根据字段名获取属性值失败" + ExceptionMessage.mess(e));
                }
            }
        }
        return null;*/
        BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            if (descriptor.getName().equals(f)) {
                return descriptor.getReadMethod().invoke(t).toString();
            }
        }
        return null;
    }
    private static <T> String getValue2(String k, T t) throws Exception {
        Class<?> object = t.getClass();// 得到类对象
        Field[] fs = object.getDeclaredFields();//得到属性集合
        for (Field f : fs) {//遍历属性
            f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
            if(f.getName().equals(k)){
                return f.get(t).toString();
            }
        }
        return null;
    }
    public static void main(String[] args) {
    	try {

    		Map<String, Object> map = new HashMap<>();
    		map.put("page", 10);
    		map.put("rows", "");
    		Basic b = mapToBean(map, Basic.class );
    		System.out.println(b.getPage());
    		System.out.println(b.getRows());
    		Map<?, ?> map2 = objectToMap2(b);
    		System.out.println(map2.get("page"));
    		System.out.println(map2.get("rows"));
            System.out.println(beanToMap(b));
            System.out.println(mapToObject2(map,Basic.class));
			//JSON.parseObject("", Basic.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
