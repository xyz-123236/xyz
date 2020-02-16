package cn.xyz.common.tool;

import java.util.ArrayList;
import java.util.List;

public class ToolsList {
	
	public static void main(String[] args) {
		List<String> list1 =new ArrayList<String>();
		list1.add("A");
		list1.add("B");

		List<String> list2 =new ArrayList<String>();
		list2.add("B");
		list2.add("C");
		
		//并集
		list1.addAll(list2);//A, B, B, C

		//无重复并集
		list2.removeAll(list1);
		list1.addAll(list2);//A, B, C

		//交集
		list1.retainAll(list2);//B

		//差集
		list1.removeAll(list2);//A
	}
}
