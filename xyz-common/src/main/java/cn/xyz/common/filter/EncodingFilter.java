package cn.xyz.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter implements Filter {  
    private String encoding = "utf-8";  
  
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {  
        //设置request编码  
        request.setCharacterEncoding(encoding);  
        //设置response编码  
        response.setContentType("text/html;charset=" + encoding);  
        response.setCharacterEncoding(encoding);  
        filterChain.doFilter(request, response);         
    }  
  
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {  
        String encodingParam = filterConfig.getInitParameter("encoding");  
        if (encodingParam != null) {  
            encoding = encodingParam;  
        }  
    }  
  
    public void destroy() {  
          
    }
    
    /**
     <filter>  
                <filter-name>EncodingFilter</filter-name>  
                <filter-class>  
                                cn.xyz.common.filter.EncodingFilter  
                </filter-class>  
                <init-param>  
                                <param-name>encoding</param-name>  
                                <param-value>UTF-8</param-value>  
                </init-param>  
	</filter>  
	   
	<filter-mapping>  
	                <filter-name>EncodingFilter</filter-name>  
	                <url-pattern>/*</url-pattern>  
	</filter-mapping>
     */

}
