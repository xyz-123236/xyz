package cn.xyz.test.tools;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import freemarker.template.Configuration;
import freemarker.template.Template;

import org.apache.commons.lang3.time.DateUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ToolsExcel {
	public static void main(String[] args) {
		JSONObject data = new JSONObject();
		String classpath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
		export(classpath+"template", "3.xml", data, "E:\\file\\temp\\9.xls");
		/*
		 * 	版本问题：
			目前个人测试结果是，在 MAC 系统上仅支持生成 03 版本 Excel, 07 版本存在打不开的情况；
			
			无法写入大批量数据：
			视图引擎生成文件无法往 Excel 里面追加数据，所以仅仅适用于数据量不大的个性化 Excel 生成，否则写入大批量数据时，存在内存溢出(OOM)的情况发生；
			
			MAC 系统存在生成的 Excel 文件无法编辑保存的情况：
			小哈在测试中发现，生成 excel 在 MAC 系统上存在编辑后，无法保存的情况；而 Windows 系统 Microsoft Excel 和 WPS 均能够正常编辑保存；
		JSONObject data = new JSONObject();
		data.put("count", data_count);
		data.put("detail", data_detail);
		
		String date_to = "01" + "-" + t.convertMonthToEnglish(Integer.parseInt(t.getDatePart("月"))-2) + "-" + t.getDatePart("年");
		String date_from = t.getDatePart("年") + "-" + t.getDatePart("月") + "-" + "01";
		Calendar cal = Calendar.getInstance();
		cal.setTime(t.getDate(date_from, "yyyy-MM-dd"));
		cal.add(Calendar.DATE, -1);
		Date date = cal.getTime();
		date_from = t.getDatePart("日",date) + "-" + t.convertMonthToEnglish(Integer.parseInt(t.getDatePart("月",date))-1) + "-" + t.getDatePart("年",date);
		String date_now = t.getDatePart("日") + "-" + t.convertMonthToEnglish(Integer.parseInt(t.getDatePart("月"))-1) + "-" + t.getDatePart("年") + " " + t.getDatePart("时") + ":" + t.getDatePart("分") + ":" + t.getDatePart("秒");
		
		data.put("date_to", date_to);
		data.put("date_from", date_from);
		data.put("date_now", date_now);
	 	
	 	String classpath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
		export(classpath+"template", "tcodelssues.xml", data, t.getBaseTmpPath()+exportFileName);*/
	}
	
	public static void export(String tPath, String tName, Object data, String filePath) {
		try {
			Configuration c = new Configuration(Configuration.VERSION_2_3_22);
			c.setDirectoryForTemplateLoading(new File(tPath));//模板目录
			Template t = c.getTemplate(tName, "UTF-8");//模板文件名
			t.process(data, new FileWriter(filePath));//生成文件路径（+文件名）
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	<Table ss:ExpandedColumnCount="4" ss:ExpandedRowCount="${detail?size+count?size+2}"
			x:FullColumns="1" x:FullRows="1" ss:DefaultColumnWidth="48"
			ss:DefaultRowHeight="12.75">
	<#list count as c>
				<Row>
					<Cell>
						<Data ss:Type="String">${c.logi9lpn}</Data>
					</Cell>
					<Cell>
						<Data ss:Type="String">${c.logi64lpn}</Data>
					</Cell>
					<Cell>
						<Data ss:Type="String">${c.reason}</Data>
					</Cell>
					<Cell ss:StyleID="s50" />
				</Row>
			</#list>
	*/
}
