package cn.xyz.common.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

public class ToolsProperties {
	
	public static Properties load(String location) throws IOException {
		try(InputStream is = ToolsProperties.class.getClassLoader().getResourceAsStream(location)) {
			Properties pro = new Properties();
			pro.load(is);
			return pro;
		}
    }
	public static Properties load(String location, String charset) throws IOException {
		try(InputStream is = ToolsProperties.class.getClassLoader().getResourceAsStream(location);
				InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(is), charset)) {
			Properties pro = new Properties();
			pro.load(isr);
			return pro;
		}
	}
}
