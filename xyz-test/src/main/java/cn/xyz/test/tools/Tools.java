package cn.xyz.test.tools;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.entity.mime.MultipartEntityBuilder;

public class Tools {
	private final static Comparator<Object> CHINA_COMPARE = Collator.getInstance(java.util.Locale.CHINA);

    public static void main(String[] args) {
    	//可能是ftp/httpclient多文件上传
    	MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    	//中文排序
        List<String> cityList = new ArrayList<String>();
        cityList.add("上海");
        cityList.add("北京");
        cityList.add("广州");
        cityList.add("深圳");

        Collections.sort(cityList, CHINA_COMPARE);
        System.out.println(cityList);
    }
}
