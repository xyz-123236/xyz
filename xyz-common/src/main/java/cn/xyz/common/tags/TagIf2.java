package cn.xyz.common.tags;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

public class TagIf2 extends TagSupport{

	private static final long serialVersionUID = 1L;
	private String role;
	private String permission;
	
	@Override
	public int doStartTag() {
		System.out.println(this.role);
		System.out.println(this.permission);
        JspWriter out=super.pageContext.getOut();
        //HttpSession sessiion = super.pageContext.getSession();
        try {
			out.println("<h3>"+this.role+"--"+this.permission+"</h3>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 1、EVAL_BODY_INCLUDE：把Body读入存在的输出流中，doStartTag()函数可用
        // 2、EVAL_PAGE：继续处理页面，doEndTag()函数可用
        // 3、SKIP_BODY：忽略对Body的处理，doStartTag()和doAfterBody()函数可用
        // 4、SKIP_PAGE：忽略对余下页面的处理，doEndTag()函数可用

		//通过SSO获取权限：需要系统标识，2种方式：在session/appcontion中存放系统名称（权限数据库要有系统标识字段）；或用系统权限命名（系统+权限名）

		if("admin".equalsIgnoreCase(this.role) || "find".equalsIgnoreCase(this.permission)) {
			//TagSupport.EVAL_BODY_INCLUDE返回则表示需要显示标签体内的内容
			return Tag.EVAL_BODY_INCLUDE;
		}
		//TagSupport.SKIP_BODY返回则表示不显示标签体内的内容
		return Tag.SKIP_BODY;
	}

	/*@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		String str = "<h"+this.role+">"+"hello world"+"</h"+this.role+">";
		try {
			out.write(str);
			if("admin".equalsIgnoreCase(this.role) || "find".equalsIgnoreCase(this.permission)) {
				getJspBody().invoke(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPermission() {
		return this.permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
}
