package cn.xyz.common.tools;

import java.io.InputStream;
import java.util.Properties;

public class ToolsProperties {
	
	public static Properties load(String location) throws Exception{
		try(InputStream is = ToolsProperties.class.getClassLoader().getResourceAsStream(location)) {
			Properties pro = new Properties();
			pro.load(is);
			return pro;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
	
}
