package cn.xyz.test.local;

import java.util.HashMap;
import java.util.Map;

public interface Config {
	public static final Map<String, String> LANGUAGECONFIG = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
	    {
	        put("en-us", "en-US");
	        put("zh-cn", "zh-CN");
	        put("es", "es-ES");
	        put("pt-br", "pt-BR");
	    }
	};
}
