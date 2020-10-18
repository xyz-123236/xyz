package cn.xyz.test.test;

import java.util.Comparator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.pojo.Sn;
import cn.xyz.common.tools.ToolsSn;

public class T005 {

	public static void main(String[] args) {
		try {
			//JSONArray data2 = create();
			JSONArray data = new JSONArray();
			JSONArray item1 = new JSONArray();
			item1.add("0186FK-0160572GK");
			item1.add("0186");
			JSONArray item2 = new JSONArray();
			item2.add("0186FK-0160573GK");
			item2.add("0186");
			data.add(item1);
			data.add(item2);
			data.sort(Comparator.comparing(obj -> ((JSONArray) obj).getString(0)));
			System.out.println("完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
