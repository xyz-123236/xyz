package cn.xyz.common.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.tools.Tools;

public class ToolsEmail {
	public static Properties properties = null;
    static {
	    try(InputStreamReader isr = new InputStreamReader(ToolsEmail.class.getClassLoader().getResourceAsStream("resource/email.properties"),"utf-8");) {
	    	properties = new Properties();
			properties.load(isr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void SendEmail(JSONObject obj) throws Exception {
        // 创建邮件配置
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", properties.getProperty("protocol")); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", properties.getProperty("host")); // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.port", properties.getProperty("port")); 
        props.setProperty("mail.smtp.socketFactory.class", properties.getProperty("fc"));
        props.setProperty("mail.smtp.auth", properties.getProperty("auth")); // 需要请求认证
        props.setProperty("mail.smtp.ssl.enable", properties.getProperty("ssl"));// 开启ssl

        // 根据邮件配置创建会话，注意session别导错包
        Session sion = Session.getDefaultInstance(props);
        //QQ邮箱需要下面这段代码，163邮箱不需要
        /*MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.ssl.socketFactory", sf);*/

		//获取默认session对象
		/*Session sion = Session.getDefaultInstance(properties, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(properties.getProperty("email"), properties.getProperty("password")); // 发件人邮箱账号、授权码
			}
		});*/
        
        // 开启debug模式，可以看到更多详细的输入日志
        //sion.setDebug(true);
        //创建邮件
        MimeMessage msg = new MimeMessage(sion);
        // address邮件地址, personal邮件昵称, charset编码方式
        InternetAddress fromAddress = new InternetAddress(properties.getProperty("email"), properties.getProperty("username"), "utf-8");
        // 设置发送邮件方
        msg.setFrom(fromAddress);
        // 设置邮件接收方
        msg.setRecipients(RecipientType.TO, InternetAddress.parse(obj.getString("email_to")));
        // 设置CC
        if(!Tools.isEmpty(obj.getString("email_cc"))) {
            msg.setRecipients(RecipientType.CC, InternetAddress.parse(obj.getString("email_cc")));
        }
        // 设置邮件标题
        msg.setSubject(obj.getString("subject"), "utf-8");
        //msg.setText("test内容");
        // 创建消息部分
        BodyPart messageBodyPart = new MimeBodyPart();
        // 消息
        if (obj.getBooleanValue("ishtml")){
            messageBodyPart.setContent(obj.getString("content"),"text/html;charset=UTF-8");
        }else {
            messageBodyPart.setText(obj.getString("content"));
        }
        // 创建多重消息
        Multipart multipart = new MimeMultipart();
        // 设置文本消息部分
        multipart.addBodyPart(messageBodyPart);
        // 附件部分
        JSONArray file_url = obj.getJSONArray("file_url");
        if(!Tools.isEmpty(file_url)) {
        	for (int i = 0; i < file_url.size(); i++) {
        		String url = file_url.getString(i);
        		File file = new File(url);
            	BodyPart messageAttachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                messageAttachmentPart.setDataHandler(new DataHandler(source));
                messageAttachmentPart.setFileName(url.substring(url.lastIndexOf(File.separator)+1, url.length()));
                multipart.addBodyPart(messageAttachmentPart);
			}
        }
        msg.setContent(multipart);
        // 设置显示的发件时间
        msg.setSentDate(new Date());
        // 保存设置
        msg.saveChanges();
        // 发送邮件
        //Transport.send(msg);
        //获取传输通道
        Transport transport = sion.getTransport();
        transport.connect(properties.getProperty("host"),properties.getProperty("email"), properties.getProperty("password"));
        //连接，并发送邮件
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
    }

	
	public static StringBuilder createTable(String head[],JSONArray data, String title){
		 
        StringBuilder table=new StringBuilder();
        table.append("    <html>");
        table.append("     <head>");
        table.append("      <title> New Document </title>");
        table.append("     </head>");
        table.append("    ");
        
        table.append("    <style type=\"text/css\">");
        table.append("    table {margin: 10px 0 30px 0;font-size: 13px;}");
        table.append("    table caption { text-align:left;}");
        table.append("    table tr th {background: #3B3B3B;color: #FFF;padding: 7px 4px;text-align: left;}");
        table.append("    table tr td {color: #FFF;padding: 7px 4px;text-align: left;}");
        table.append("    table tr.odd{background-color:#cef;}");
        table.append("    table tr.even{background-color:#ffc;}");
        table.append("    table tr td { color: #47433F;border-top: 1px solid #FFF;}");
        table.append("     </style>");
        table.append("    ");
        
        table.append("     <body>");
        table.append("<h2>"+title+"<h2/>");
        table.append("    <table style=\"width:500px; border-spacing:0;\">  ");
        table.append("       <tr>  ");
        for (int i=0;i<head.length;i++){
            table.append("          <th>"+head[i]+"</th>  ");
        }
        table.append("       </tr>  ");
        for (int i=0;i<data.size();i++){
			String tr = "<tr class=\"odd\">";
			if (i%2==1){
				tr = "<tr class=\"even\">";
			}
			table.append("     "+tr+"    ");
			table.append("         <td>"+data.getJSONObject(i).getString("name")+"</td>  ");
			table.append("         <td>"+data.getJSONObject(i).getString("pwd")+"</td>  ");
			table.append("       </tr>  ");
		}
        table.append("    </table> ");
        table.append("     </body>");
        
        table.append("    </html>");
        return table;
    }

    public static void main(String[] args) {
		try {
			String head[] = {"账号","密码"};
			JSONArray employees = new JSONArray();
			JSONObject employee = new JSONObject();
			employee.put("name", "小明");
			employee.put("pwd", "123");
			employees.add(employee);
			JSONObject employee2 = new JSONObject();
			employee2.put("name", "小张");
			employee2.put("pwd", "456");
			employees.add(employee2);
			JSONObject employee3 = new JSONObject();
			employee3.put("name", "小李");
			employee3.put("pwd", "789");
			employees.add(employee3);
			
			StringBuilder table = createTable(head,employees,"账号密码");
			
			JSONObject obj = new JSONObject();
			obj.put("email", "tang.wu@fujikon.com");
			obj.put("username", "tang.wu");
			obj.put("subject", "test主题");
			obj.put("content", table);
			obj.put("file_url", "E:\\file\\temp\\test.txt");
			obj.put("file_name", "test.txt");
			obj.put("ishtml", true);
			SendEmail(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

