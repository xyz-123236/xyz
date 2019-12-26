package cn.xyz.test.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import freemarker.template.Configuration;
import freemarker.template.Template;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.main.dao.DbBase;

public class ToolsExcel {
	// excel默认宽度；
	private static int width = 256 * 14;
	// 默认字体
	private static String excelfont = "微软雅黑";
	
	
	/**
	 * export：导出excel
	 * @param obj
	  			data: 数据
				file_name：文件名
				sheet_name：表名
				title：标题
				prohibits：禁用列（前后需紧跟逗号","）
	 * @param formats
				第一行：表头（中文）
				第二行：字段名（英文）
				第三行：数据类型
				第四行：表格宽度
	 * @throws Exception
	 */
	public static void export(JSONObject obj, String[][] cells, Integer[][] formats) throws Exception {
		JSONArray data = obj.getJSONArray("data");
		String file_name = obj.getString("file_name");
		String sheet_name = obj.getString("sheet_name");
		String title = obj.getString("title");
		String prohibits = obj.getString("prohibits");
		
		String[] heads = (String[]) cells[0];
		String[] fileds = (String[]) cells[1];
		Integer[] types = null;
		Integer[] widths = null;
		if(formats.length > 0 && !Tools.isEmpty(formats[0])) {
			types = (Integer[]) formats[0];
		} 
		if(formats.length > 1 && !Tools.isEmpty(formats[1])) {
			widths = (Integer[]) formats[1];
		}
		
		// 创建一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个sheet
		HSSFSheet sheet = wb.createSheet((!Tools.isEmpty(sheet_name)) ? sheet_name : "sheet1");
		
		HSSFCellStyle lock_style = wb.createCellStyle();
		lock_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		lock_style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		lock_style.setLocked(true);
        HSSFCellStyle un_lock_style = wb.createCellStyle();
        un_lock_style.setLocked(false);
        
		int index = 0;//表行号
		if (title != null) {// 创建表头，如果没有跳过
			HSSFRow row = sheet.createRow(index);
			// 表头样式
			HSSFCellStyle style = wb.createCellStyle();
			HSSFFont font = wb.createFont();
			font.setBold(true);
			font.setFontName(excelfont);
			font.setColor(IndexedColors.BLACK.index);
			font.setFontHeightInPoints((short) 20);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);

			HSSFCell cell = row.createCell(0);
			cell.setCellValue(title);
			cell.setCellStyle(style);
			CellRangeAddress region = new CellRangeAddress(0, 0, 0, heads.length-1);
	        sheet.addMergedRegion(region);
			index++;

		}
		if (heads != null) {
			HSSFRow row = sheet.createRow(index);
			// 表头样式
			HSSFCellStyle style = wb.createCellStyle();
			HSSFFont font = wb.createFont();
			font.setBold(true);
			font.setFontName(excelfont);
			font.setFontHeightInPoints((short) 14);
			style.setFont(font);
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			for (int i = 0; i < heads.length; i++) {
				if(Tools.isEmpty(widths))sheet.setColumnWidth(i, widths[i]);
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(heads[i]);
				cell.setCellStyle(style);
			}
			index++;
		}
		// 表格主体 解析list
		if (data != null) {
			List<HSSFCellStyle> styleList = new ArrayList<HSSFCellStyle>();

			for (int i = 0; i < heads.length; i++) { // 列数
				HSSFCellStyle style = wb.createCellStyle();
				HSSFFont font = wb.createFont();
				font.setFontName(excelfont);
				font.setFontHeightInPoints((short) 10);
				style.setFont(font);
				style.setBorderBottom(BorderStyle.THIN);
				style.setBorderLeft(BorderStyle.THIN);
				style.setBorderRight(BorderStyle.THIN);
				style.setBorderTop(BorderStyle.THIN);
				if(Tools.isEmpty(types)) {
					style.setAlignment(HorizontalAlignment.LEFT);
				}else {
					if (types[i] == 1) {
						style.setAlignment(HorizontalAlignment.LEFT);
					} else if (types[i] == 2) {
						style.setAlignment(HorizontalAlignment.CENTER);
					} else if (types[i] == 3) {
						style.setAlignment(HorizontalAlignment.RIGHT);
						// int类型
					} else if (types[i] == 4) {
						style.setAlignment(HorizontalAlignment.RIGHT);
						// int类型
						style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
					} else if (types[i] == 5) {
						// float类型
						style.setAlignment(HorizontalAlignment.RIGHT);
						style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
					} else if (types[i] == 6) {
						// 百分比类型
						style.setAlignment(HorizontalAlignment.RIGHT);
						style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
					}
				}
				styleList.add(style);
			}
			
			for (int i = 0; i < data.size(); i++) { // 行数
				HSSFRow row = sheet.createRow(index);
				JSONObject map = data.getJSONObject(i);
				for (int j = 0; j < fileds.length; j++) { // 列数
					HSSFCell cell = row.createCell(j);
					Object o = map.get(fileds[j]);
					if(Tools.isEmpty(types)) {
						cell.setCellValue(map.get(fileds[j]) + "");
					}else {
						if (o == null || "".equals(o)) {
							cell.setCellValue("");
						} else if (types[j] == 4) {
							// int
							cell.setCellValue((Long.valueOf((map.get(fileds[j])) + "")).longValue());
						} else if (types[j] == 5 || types[j] == 6) {
							// float
							cell.setCellValue((Double.valueOf((map.get(fileds[j])) + "")).doubleValue());
						} else {
							cell.setCellValue(map.get(fileds[j]) + "");
						}
					}
					cell.setCellStyle(styleList.get(j));
					if(j == 2) {
						cell.getCellStyle().cloneStyleFrom(lock_style);
					}else {
						cell.getCellStyle().cloneStyleFrom(un_lock_style);
					}
				}
				index++;
			}
			//添加一行空行：用于解除锁定行
			HSSFRow row = sheet.createRow(index);
			for (int j = 0; j < fileds.length; j++) { // 列数
				HSSFCell cell = row.createCell(j);
				cell.setCellValue("");
				cell.getCellStyle().cloneStyleFrom(un_lock_style);
			}
			sheet.protectSheet("123456");
		}
		
		file_name = "E:\\file\\temp\\"+file_name + ".xls";
		// 创建文件输出流，准备输出电子表格  
        OutputStream out = new FileOutputStream(file_name);  
        wb.write(out);
        out.close();
        wb.close();
		/*String filename = "";
		try {
			filename = encodeChineseDownloadFileName(request, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setHeader("Content-disposition", filename);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + filename);
		response.setHeader("Pragma", "No-cache");
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
		session.setAttribute("state", "open");*/

	}
	/**
	 * 对文件流输出下载的中文文件名进行编码 屏蔽各种浏览器版本的差异性
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName) throws Exception {

		String filename = null;
		String agent = request.getHeader("USER-AGENT");
		if (null != agent) {
			if (-1 != agent.indexOf("Firefox")) {// Firefox
				filename = "=?UTF-8?B?"
						+ (new String(org.apache.commons.codec.binary.Base64.encodeBase64(pFileName.getBytes("UTF-8"))))
						+ "?=";
			} else if (-1 != agent.indexOf("Chrome")) {// Chrome
				filename = new String(pFileName.getBytes(), "ISO8859-1");
			} else {// IE7+
				filename = java.net.URLEncoder.encode(pFileName, "UTF-8");
				filename = filename.replace("+", "%20");
			}
		} else {
			filename = pFileName;
		}
		return filename;
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
	
	public static void main(String[] args) {
		try {
			DbBase db = new DbBase("mysql");
			JSONArray data = db.executeQueryJson("select * from sn_detail");
			
			JSONObject obj = new JSONObject();
			obj.put("data", data);
			obj.put("file_name", "test1");
			obj.put("sheet_name", "sheet1");
			obj.put("title", "title");
			obj.put("prohibits", ",2,1,");
			String[][] cells = {{"用户名","姓名","密码"}
			,{"id","batch_id","sn_detail"}};
			Integer[][] formats = {
					{}
					,{4000,4000,4000}	
			};
			export(obj, cells, formats);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*JSONObject data = new JSONObject();
		String classpath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
		export(classpath+"template", "3.xml", data, "E:\\file\\temp\\9.xls");*/
		/*
		 * 	版本问题：
			目前个人测试结果是，在 MAC 系统上仅支持生成 03 版本 Excel, 07 版本存在打不开的情况；
			
			无法写入大批量数据：
			视图引擎生成文件无法往 Excel 里面追加数据，所以仅仅适用于数据量不大的个性化 Excel 生成，否则写入大批量数据时，存在内存溢出(OOM)的情况发生；
			
			MAC 系统存在生成的 Excel 文件无法编辑保存的情况：
			测试中发现，生成 excel 在 MAC 系统上存在编辑后，无法保存的情况；而 Windows 系统 Microsoft Excel 和 WPS 均能够正常编辑保存；
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
