package junit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test7 {
	/*public static void main(String[] args) {
		JSONObject obj = new JSONObject();
		obj.put("userno", "123");
		try {
			DBTool.getInstance("test2").set(obj).insert();
			System.out.println("完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	@BeforeEach
	static void beforeEach() {
		System.out.println("BeforeAll");
	}
	@Test
	public void t1() {
		 String msg = "mSurface=Surface(name=com.bbk.launcher2/com.bbk).launcher2.Launcher)abc(123)";
		 Pattern p = Pattern.compile("(\\()([0-9a-zA-Z\\.\\/\\=])*(\\))");
		 Matcher m = p.matcher(msg);
		 while (m.find()) {
			 System.out.println(m.group(0).substring(1, m.group().length() - 1));
		 }
	}
	@Test
	public void t2() {
		String msg = "mSurface=Surface(name=com.bbk.launcher2/com.bbk.launcher2.Launcher)abc(123)";
		Pattern p = Pattern.compile("(\\([^\\]]*\\))");
		Matcher m = p.matcher(msg);
		while(m.find()){
			System.out.println(m.group().substring(1, m.group().length()-1));
		}

	}
	@Test
	public void t3() {//贪婪模式
		String msg = "mSurface=Surface(name=com.bbk.(launcher2/com.bbk).launcher2.Launcher)abc(123)";
		Pattern p = Pattern.compile("\\(.*\\)");
		Matcher m = p.matcher(msg);
		if(m.find()) {
			System.out.println(m.group());
			while(m.find()){
				System.out.println(m.group());
				//System.out.println(m.group().substring(1, m.group().length()-1));
			}
		}else {
			System.out.println(msg);
		}

	}
	@Test
	public void t4() {//非贪婪模式
		String msg = "mSurface=Surface(name=com.bbk.(launcher2/com.bbk).launcher2.Launcher)abc(123)";
		Pattern p = Pattern.compile("\\(.*?\\)");
		Matcher m = p.matcher(msg);
		if(m.find()) {
			System.out.println(m.group());
			while(m.find()){
				System.out.println(m.group());
				//System.out.println(m.group().substring(1, m.group().length()-1));
			}
		}else {
			System.out.println(msg);
		}

	}
	@Test
	public void t5() {
		String msg = "(a+b)=(c+d)>(d)";
		Pattern p = Pattern.compile("\\(([^}]*)\\)");
		Matcher m = p.matcher(msg);
		while(m.find()){
			System.out.println(m.group());
			//System.out.println(m.group().substring(1, m.group().length()-1));
		}

	}
	@Test
	public void t6() {
        String str="您好{options{abc}}评论了您的{options{def}}";
        ArrayList<String>  word=new ArrayList<String>();
        int m=0,n=0;
        int count=0;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)=='{'){
                if(count==0){
                    m=i;
                }
                count++;
            }
            if(str.charAt(i)=='}'){
                count--;
                if(count==0){
                    n=i;
                    word.add(str.substring(m,n+1));
                }
            }
                     }
        for(String a : word){
            System.out.println(a);
        }
    }
	
	@Test
	public void t7() {//括号成对匹配
        //List<Integer> list = new LinkedList<>();
		Stack<Integer> stack =new Stack<>();
        String str="您好(options(abc))评论了您的(options(def))";
        for(int i = 0; i < str.length(); i++){
        	 if(str.charAt(i)=='('){
        		 stack.push(i+1);
        		 //list.add(0, i+1);
        		 //System.out.println(list);
        		 System.out.println(stack);
        	 }
        	 if(str.charAt(i)==')'){
        		 //System.out.println(str.subSequence(list.get(0), i));
        		 //list.remove(0);
        		 System.out.println(str.subSequence(stack.pop(), i));
        	 }
        }
        //System.out.println(list);
        
    }
	
	@Test
	public void t8() {//非贪婪模式
		String msg = "t.*,a.str1,a.str2,b.num1,b.num2,sum(CAST(c.ENTBY AS INTEGER)),ifnull(d.NUM5, 0)+ifnull(d.NUM6, 0)";
		Pattern p = Pattern.compile("\\,.*?\\,");
		Matcher m = p.matcher(msg);
		if(m.find()) {
			System.out.println(m.group());
			while(m.find()){
				System.out.println(m.group());
				//System.out.println(m.group().substring(1, m.group().length()-1));
			}
		}else {
			System.out.println(msg);
		}

	}
}
