package cn.xyz.common.pojo;


public class Email {
	private String email_to;//发给谁，可多个，逗号分隔
	private String email_cc;//抄送给谁，可多个，逗号分隔
	private String subject;//主题
	private String file_urls[];//附件
	private String content;//发送内容
	private boolean html = false;//判断是否为html

	public String getEmail_to() {
		return this.email_to;
	}

	public void setEmail_to(String email_to) {
		this.email_to = email_to;
	}

	public String getEmail_cc() {
		return this.email_cc;
	}

	public void setEmail_cc(String email_cc) {
		this.email_cc = email_cc;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String[] getFile_urls() {
		return this.file_urls;
	}

	public void setFile_urls(String[] file_urls) {
		this.file_urls = file_urls;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isHtml() {
		return this.html;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}
}
