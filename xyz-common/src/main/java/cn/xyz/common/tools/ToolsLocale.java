package cn.xyz.common.tools;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class ToolsLocale {
	public static void main(String[] args) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/i18n", new Locale("en", "US"));
		// 打印从资源文件获取的值
		System.out.println(resourceBundle.getString("hello"));

		resourceBundle = ResourceBundle.getBundle("i18n/i18n", new Locale("zh", "CN"));
		System.out.println(resourceBundle.getString("hello")); // 资源文件是UTF-8编码。这里打印结果乱码

		// 处理乱码
		String msg = resourceBundle.getString("hello");
		try {
		    msg = new String(msg.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		} catch (Exception e) {
		    e.printStackTrace();
		}

		System.out.println(msg);
		
		
		Locale defaultLocale = Locale.getDefault();
		System.out.print("country=" + defaultLocale.getCountry());// country=CN
		System.out.println(", language=" + defaultLocale.getLanguage());// language=zh

		Locale locale1 = Locale.CHINA;
		System.out.print("country=" + locale1.getCountry());// country=CN
		System.out.println(", language=" + locale1.getLanguage());// language=zh

		Locale locale2 = Locale.CHINESE;
		System.out.print("country=" + locale2.getCountry());// country=
		System.out.println(", language=" + locale2.getLanguage());// language=zh

		Locale locale3 = Locale.TRADITIONAL_CHINESE;
		System.out.print("country=" + locale3.getCountry());// country=TW
		System.out.println(", language=" + locale3.getLanguage());// language=zh

		Locale locale4 = Locale.SIMPLIFIED_CHINESE;
		System.out.print("country=" + locale4.getCountry());// country=CN
		System.out.println(", language=" + locale4.getLanguage());// language=zh
		
		Locale locale5 = Locale.ENGLISH;
		System.out.print("country=" + locale5.getCountry());// country=CN
		System.out.println(", language=" + locale5.getLanguage());// language=zh
	}
	public static String getMessage(ResourceBundle resourceBundle, String key, Object... args) {
	    String msg = "";
	    
	    if (resourceBundle == null || key == null || key.isEmpty()) return msg;
	    
	    if (args == null || args.length == 0) {
	        msg = resourceBundle.getString(key);
	        try {
	            msg = new String(msg.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
	            return msg;
	        } catch (Exception e) {
	            //UtilFunctions.log.error("UtilFunctions getMessage error, msg:{}, exception:{}", e.toString(), e);
	            //UtilFunctions.reportError("UtilFunctions getMessage: " + e.toString(), e);
	            return msg;
	        }
	    }
	    //处理中英文替换，公用部分也需要中英文，拼接不支持国家化
	    msg = MessageFormat.format(resourceBundle.getString(key), args);// format(" {0} {1} {2} {3}", "a", "bb","","ccc"))
	    try {
	        msg = new String(msg.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
	    } catch (Exception e) {
	        //UtilFunctions.log.error("UtilFunctions getMessage error, msg:{}, exception:{}", e.toString(), e);
	        //UtilFunctions.reportError("UtilFunctions getMessage: " + e.toString(), e);
	        return msg;
	    }
	    
	    return msg;
	}
}
