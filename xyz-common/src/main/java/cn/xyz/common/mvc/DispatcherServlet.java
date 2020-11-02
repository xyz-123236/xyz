package cn.xyz.common.mvc;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.xyz.common.exception.CustomException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.annotation.*;
import cn.xyz.common.tools.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;


public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

    private final List<String> classNames = new ArrayList<>();
    private final Map<String, Object> ioc = new HashMap<>();
    //handlerMapping的类型可以自定义为Handler
    private final Map<String, Method> handlerMapping = new  HashMap<>();
    private final Map<String, Object> controllerMap  =new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        try {
            Properties properties = ToolsProperties.load(config.getInitParameter("contextConfigLocation"));
			//2.根据properties，初始化所有相关联的类,扫描用户设定的包下面所有的类
	        doScanner(properties.getProperty("scanPackage"));
	        //3.拿到扫描到的类,通过反射机制,实例类,并且放到ioc容器中(k-v  beanName-bean) beanName默认是首字母小写
	        doInstance();
	        // 4.自动化注入依入?
	        doAutowired();
	        //5.初始化HandlerMapping(将url和method对应)
	        initHandlerMapping();
	        doAutowired2();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 注释掉父类实现，不然会报错：405 HTTP method GET is not supported by this URL
        //super.doPost(req, resp);
        System.out.println("执行MyDispatcherServlet的doPost()");
        try {
            //处理请求
            doDispatch(req,resp);
        } catch (Exception e) {
            resp.getWriter().write("500!! Server Exception");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 注释掉父类实现，不然会报错：405 HTTP method GET is not supported by this URL
        //super.doGet(req, resp);
        System.out.println("执行MyDispatcherServlet的doGet()");
        try {
            //处理请求
            doDispatch(req,resp);
        } catch (Exception e) {
            resp.getWriter().write("500!! Server Exception");
        }
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(this.handlerMapping.isEmpty()){
            return;
        }
        String url =request.getRequestURI();
        if(url.contains("statics")) {
        	//request.getRequestDispatcher("logo.png").forward(request, response); 
        	return;
        }
        String contextPath = request.getContextPath();
        url=url.replace(contextPath, "").replaceAll("/+", "/");
        // 去掉url前面的斜�?"/"，所有的@MyRequestMapping可以不用写斜�?"/"
        if(url.lastIndexOf('/')!=0){
            url=url.substring(1);
        }
        //System.out.println( request.getContentType());
         //System.out.println( request.getAttribute("Content-Type"));
        JSONObject obj = new JSONObject();
        
        if(request.getContentType() != null && request.getContentType().contains("multipart/form-data")) {
        	request.setCharacterEncoding("utf-8");  //设置编码
            //获得磁盘文件条目工厂  
            DiskFileItemFactory factory = new DiskFileItemFactory();  
            //获取文件�?要上传到的路�?  
            //String path2 = request.getSession().getServletContext().getRealPath(File.separator)+"upload"+File.separator+ToolsDate.getString("yyyyMMdd")+File.separator;  
            //System.out.println(path2);
            String path = "E:"+File.separator+"file"+File.separator+"upload"+File.separator+ToolsDate.getString("yyyyMMdd") + File.separator; 
            String url2 = "/file/upload/"+ToolsDate.getString() + "/";
              
            //如果没以下两行设置的话，上传大的 文件 会占�? 很多内存�?  
            //设置暂时存放�? 存储�? , 这个存储室，可以�? �?终存储文�? 的目录不�?  

             // 原理 它是先存�? 暂时存储室，然后在真正写�? 对应目录的硬盘上�?
             // 按理来说 当上传一个文件时，其实是上传了两份，第一个是�? .tem 格式�?
             // 然后再将其真正写�? 对应目录的硬盘上

    		factory.setRepository(new File(path));
            //设置 缓存的大小，当上传文件的容量超过该缓存时，直接放�? 暂时存储�?  
            factory.setSizeThreshold(1024*1024) ;  
         
            //高水平的API文件上传处理  
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 设置上传内容的大小限制（单位：字节）
            upload.setSizeMax(100*1024*1024L);
            try {  
                //可以上传多个文件  
                List<FileItem> list = upload.parseRequest(request);
                for(FileItem item : list) {  
                    //获取表单的属性名�?  
                    String name = item.getFieldName();
                    //如果获取�? 表单信息是普通的 文本 信息  
                    if(item.isFormField()) {
                    	if(obj.containsKey(name)) {
                    		if(obj.get(name) instanceof String) {
                    			JSONArray arr = new JSONArray();
                    			arr.add(obj.get(name));
                    			arr.add(item.getString());
                    			obj.put(name, arr);
                    		}else {
                    			obj.getJSONArray(name).add(item.getString());
                    		}
                    	}else {
                    		obj.put(name, item.getString());
                    	}
                    } else {  //对传入的�? �?单的字符串进行处�? ，比如说二进制的 图片，电影这�? 
                    	//System.out.println(item.getSize());
                    	if(item.getSize() > 0) {
                    		if(obj.containsKey(name)) {
                        		if(obj.get(name).getClass().isArray()) {
                        			obj.getJSONArray(name).add(ToolsFile.upload(item, path, url2, response));
                        		}else {
                        			JSONArray arr = new JSONArray();
                        			arr.add(obj.get(name));
                        			arr.add(ToolsFile.upload(item, path, url2, response));
                        			obj.put(name, arr);
                        		}
                        	}else {
                        		obj.put(name, ToolsFile.upload(item, path, url2, response));
                        	}
                    	}
                    }  
                }
            }
            catch (Exception e) {  
                e.printStackTrace();  
            } 
        }else {
        	/*Enumeration enu=request.getParameterNames();  
        	while(enu.hasMoreElements()){  
	        	String paraName=(String)enu.nextElement();  
	        	System.out.println(paraName+": "+request.getParameter(paraName));  
        	}*/
        	
        	@SuppressWarnings("unchecked")
			Map<String, String[]> parameterMap = request.getParameterMap();
        	//System.out.println(parameterMap.entrySet());
        	for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
                String value =Arrays.toString(param.getValue()).replaceAll("[\\[\\]]", "").replaceAll(",\\s", ",");
                obj.put(param.getKey(), value);
            }
        }
        
          
          
        //request.getRequestDispatcher("filedemo.jsp").forward(request, response);  
        
        
        
        //要做/*,/**的匹配
        if(!this.handlerMapping.containsKey(url)){//�?要处�?/*,view/{url}(@PathVariable)
        	//System.out.println(url);
        	//response.getWriter().write("404 NOT FOUND!");
            System.out.println("404 NOT FOUND:"+url);
            return;
        }
        Method method =this.handlerMapping.get(url);
        //获取方法的参数列�?
        Class<?>[] parameterTypes = method.getParameterTypes();

        //获取请求的参�?
        //Map<String, String[]> parameterMap = request.getParameterMap();
        //保存参数�?
        Object [] paramValues= new Object[parameterTypes.length];
        //方法的参数列�?
        for (int i = 0; i<parameterTypes.length; i++){
            //根据参数名称，做某些处理
            String requestParam = parameterTypes[i].getSimpleName();
            if (requestParam.equals("HttpServletRequest")){
                //参数类型已明确，这边强转类型
                paramValues[i]=request;
                continue;
            }
            if (requestParam.equals("HttpServletResponse")){
                paramValues[i]=response;
                continue;
            }
            if (requestParam.equals("JSONObject")){
                paramValues[i] = obj;
            }
            //文件怎么接收
            /*if(requestParam.equals("JSONObject")){//不合�?
                for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
                    String value =Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
                    paramValues[i]=value;
                }
            }*/
        }
        //利用反射机制来调�?
        try {
            //第一个参数是method�?对应的实�? 在ioc容器�?
            //method.invoke(this.controllerMap.get(url), paramValues);
            String obj2 = method.invoke(this.controllerMap.get(url), paramValues).toString();
            System.out.println(obj2);
            if(method.isAnnotationPresent(ResponseBoby.class)){
            	response.getWriter().write(obj2);
            }else if(obj2.contains("redirect:")) {
            	//重定向带数据：使用model，再拼接url
            	response.sendRedirect(obj2.substring(9));
            }else {
            	if(request.getHeader("x-requested-with") == null) {
                	//response.getWriter().write(obj2.toString());
                	request.getRequestDispatcher("/WEB-INF/jsp/"+obj2).forward(request,response); 
                }else {
                	response.getWriter().write(obj2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Des: 将指定包下扫描得到的类，添加到classNames字段中；
     * @param packageName 要扫描的包名
     */
    private void doScanner(String packageName) throws CustomException {
        URL url  =this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        if(url == null) throw new CustomException("初始扫描包异常");
        File dir = new File(url.getFile());
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if(file.isDirectory()){
                //递归读取
                doScanner(packageName+"."+file.getName());
            }else{
                String className =packageName +"." +file.getName().replace(".class", "");
                this.classNames.add(className);
            }
        }
    }

    /**
     * Description:  将classNames中的类实例化，经key-value：类名（小写-类对象放入ioc字段
     */
    private void doInstance() {//要判断名称是否重复
        if (this.classNames.isEmpty()) {
            return;
        }
        for (String className : this.classNames) {
            try {
                //根据注解实例化类
                Class<?> clazz =Class.forName(className);
                if(clazz.isAnnotationPresent(Controller.class)){
                	this.ioc.put(ToolsString.toLowerFirstWord(clazz.getSimpleName()),clazz.newInstance());
                }else if(clazz.isAnnotationPresent(Service.class)){
                    Service myService=clazz.getAnnotation(Service.class);
                    String beanName=myService.value();
                    if ("".equals(beanName.trim())){
                        beanName=ToolsString.toLowerFirstWord(clazz.getSimpleName());
                    }

                    Object instance= clazz.newInstance();
                    this.ioc.put(beanName,instance);
                    Class<?>[] interfaces=clazz.getInterfaces();
                    for (Class<?> i:interfaces){
                    	this.ioc.put(i.getName(),instance);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Description:自动化的依赖注入
     */
    private void doAutowired(){

        if (this.ioc.isEmpty()){
            return;
        }
        for (Map.Entry<String,Object> entry:this.ioc.entrySet()){
            //包括私有的方法，在spring中没有隐私，@MyAutowired可以注入public、private字段
            Field[] fields=entry.getValue().getClass().getDeclaredFields();
            for (Field field:fields){
                if (!field.isAnnotationPresent(Autowired.class)){
                    continue;
                }
                Autowired autowired= field.getAnnotation(Autowired.class);
                String beanName=autowired.value().trim();
                if ("".equals(beanName)){
                    beanName=field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(),this.ioc.get(beanName));//字段.set(对象，实例)
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void doAutowired2(){

        if (this.controllerMap.isEmpty()){
            return;
        }
        for (Map.Entry<String,Object> entry:this.controllerMap.entrySet()){
            //包括私有的方法，在spring中没有隐私，@MyAutowired可以注入public、private字段
            Field[] fields=entry.getValue().getClass().getDeclaredFields();
            for (Field field:fields){
                if (!field.isAnnotationPresent(Autowired.class)){
                    continue;
                }
                Autowired autowired= field.getAnnotation(Autowired.class);
                String beanName=autowired.value().trim();
                if ("".equals(beanName)){
                    beanName=field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(),this.ioc.get(beanName));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Description:  初始化HandlerMapping(将url和method对应?)
     */
    private void initHandlerMapping(){//?要判断路径是否重复?
    	//要处理/*,/**的匹配
        if(this.ioc.isEmpty()){
            return;
        }
        try {
            for (Map.Entry<String, Object> entry: this.ioc.entrySet()) {
                Class<?> clazz = entry.getValue().getClass();
                if(!clazz.isAnnotationPresent(Controller.class)){
                    continue;
                }

                //拼url,是controller头的url拼上方法上的url
                String baseUrl ="";
                if(clazz.isAnnotationPresent(RequestMapping.class)){
                    RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
                    baseUrl=annotation.value();
                }
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if(!method.isAnnotationPresent(RequestMapping.class)){
                        continue;
                    }
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    String url = annotation.value();

                    url =(baseUrl+"/"+url).replaceAll("/+", "/");
                    this.handlerMapping.put(url,method);
                    this.controllerMap.put(url,clazz.newInstance());
                    System.out.println(url+","+method);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
