package cn.xyz.common.filter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class CharacterEncodingFilter implements Filter{
	
	String encoding = null;
	@SuppressWarnings("unused")
	private FilterConfig fg = null;
		
	public void destroy() {
		this.encoding = null;
		this.fg = null;

	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain fc) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse)res;
		response.setContentType("text/html;charset="+this.encoding);
		HttpServletRequest request = (HttpServletRequest)req;
		HttpRequestWrapper hrw  = new HttpRequestWrapper(request);
		fc.doFilter(hrw, res);
		
	}

	public void init(FilterConfig fg) throws ServletException {			
		this.fg = fg;
		this.encoding = fg.getInitParameter("encoding");
		if(this.encoding == null || "".equals(this.encoding)) this.encoding = "UTF-8";
	}
	
	public class HttpRequestWrapper extends HttpServletRequestWrapper{
		public 	HttpRequestWrapper(HttpServletRequest request){
			super(request);
		}
		public String getParameter(String name){
			String value = null;
			value = this.encoding(((HttpServletRequest)this.getRequest()).getParameter(name));
			
			return value;
		}
		public String[] getParameterValues(String name){
			String[] values = ((HttpServletRequest)this.getRequest()).getParameterValues(name);
			
			if(values != null){
				int length = values.length;
				for(int i = 0; i < length; i++){
					values[i] = this.encoding(values[i]);
				}
			}
			return values;
		}
		public String encoding(String value){
			if(value != null){
				try {
					value = new String(value.getBytes("ISO-8859-1"), CharacterEncodingFilter.this.encoding);
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
				}
			}
			return value;
		}
	}
}
