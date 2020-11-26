package cn.xyz.common.pojo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Email {
	private String email_to;//发给谁，可多个，逗号分隔
	private String email_cc;//抄送给谁，可多个，逗号分隔
	private String subject;//主题
	private String file_urls[];//附件
	private String content;//发送内容
	private boolean html = false;//判断是否为html

	public String getEmail_to() {
		return email_to;
	}

	public void setEmail_to(String email_to) {
		this.email_to = email_to;
	}

	public String getEmail_cc() {
		return email_cc;
	}

	public void setEmail_cc(String email_cc) {
		this.email_cc = email_cc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String[] getFile_urls() {
		return file_urls;
	}

	public void setFile_urls(String[] file_urls) {
		this.file_urls = file_urls;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isHtml() {
		return html;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}
}
