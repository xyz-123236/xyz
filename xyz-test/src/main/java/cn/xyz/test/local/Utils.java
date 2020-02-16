package cn.xyz.test.local;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Utils {
	public static String getLanguage(HttpServletRequest request) {
        String language = "";
        if (request == null) return language;

        // priority:  url?l=en-us > Cookie:language=zh-cn
        language = request.getParameter("l");
        if (language == null || Config.LANGUAGECONFIG.get(language.toLowerCase()) == null) {
            language = Utils.getLanguageByCookie(request);
        }
        
        if (language == null || Config.LANGUAGECONFIG.get(language.toLowerCase()) == null) {
            language = "en-us"; // default "en-us"
        }

        return language;
    }
    
    public static String getLanguageByCookie(HttpServletRequest request) {
        String language = "";
        if (request == null) return language;

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return language;
        }

        for (Cookie cookie : cookies) {
            if ("language".equalsIgnoreCase(cookie.getName())) {
                language = cookie.getValue();
            }
        }

        return language;
    }
    
    public static ResourceBundle getResourceBundle(HttpServletRequest request) {
        String language = Utils.getLanguage(request);
        String[] languages = language.split("-");
        Locale locale = null;
        if (languages.length >= 2) {
            locale  = new Locale(language.split("-")[0], language.split("-")[1]);
        } else if (languages.length == 1) {
            locale  = new Locale(language.split("-")[0], "ES");
        }
        return ResourceBundle.getBundle("i18n/MessgesBundle", locale);
    }
}
