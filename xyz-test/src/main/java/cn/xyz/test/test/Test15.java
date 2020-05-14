package cn.xyz.test.test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Test15 {
	public static void main(String[] args) {
		//Unirest.setTimeouts(0, 0);
		try {
			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.post("http://jira.fujikon.com/rest/api/2/issue")
			  .header("Authorization", "Basic dGFuZy53dTpUdyowMTMzMzYz")
			  .header("Content-Type", "application/json")
			  .body("{\"fields\": {\"project\": {\"id\": \"19000\"},\"summary\": \"创建测试\",\"issuetype\": {\"id\": \"18107\"},\"description\": \"description\"}}")
			  .asString();
			
			
			/*HttpResponse<String> response = Unirest.post("http://jira.fujikon.com/rest/api/2/issue")
			  .header("Content-Type", "application/json")
			  .header("Cookie", "JSESSIONID=C01C280B531701DC1F984C9293CE9C8C; atlassian.xsrf.token=BSB1-S2TT-9NH0-ULD3_f96341737d86c04b415730d9a354bd3f94f3e810_lin")
			  .body("{\"fields\": {\"project\": {\"id\": \"11800\"},\"summary\": \"something wrong\",\"issuetype\": {\"id\": \"12107\"},\"description\": \"description\"}}")
			  .asString();*/
			System.out.println(response.getBody());
			System.out.println(response.getStatus());
			System.out.println(response.getRawBody());
		} catch (UnirestException e) {
			e.printStackTrace();
		}           
	}
	
}
