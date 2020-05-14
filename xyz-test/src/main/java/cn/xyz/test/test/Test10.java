package cn.xyz.test.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test10 {
	public static void main(String[] args){
		//curl -u username:password -X GET -H "Content-Type: application/json" http://localhost:8080/rest/api/2/issue/createmeta
        String[] cmds= {"curl", "-u", "tang.wu:Tw*0133363", "-X", "GET","-H","Content-Type: application/json", "http://jira.fujikon.com/rest/browse/FICITS-5644"};//必须分开写，不能有空格
        //String[] cmds = {"curl", "-H", "tang.wu:Tw*0133363", "-H", "Cache-Control: max-age=0", "--compressed", "http://jira.fujikon.com/browse/FICITS-5644"};
       System.out.println(execCurl(cmds));
   }


   public static String execCurl(String[] cmds) {
       ProcessBuilder process = new ProcessBuilder(cmds);
       Process p;
       try {
           p = process.start();
           BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
           StringBuilder builder = new StringBuilder();
           String line = null;
           while ((line = reader.readLine()) != null) {
               builder.append(line);
               builder.append(System.getProperty("line.separator"));
           }
           return builder.toString();

       } catch (IOException e) {
           System.out.print("error");
           e.printStackTrace();
       }
       return null;

   }

}
