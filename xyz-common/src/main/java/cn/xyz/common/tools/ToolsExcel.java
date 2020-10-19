package cn.xyz.common.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import freemarker.template.Configuration;
import freemarker.template.Template;

import org.apache.commons.net.util.Base64;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.pojo.Excel;


public class ToolsExcel {
	// excel默认宽度；
	public static final int WIDTH = 256 * 14;
	// 默认字体
	private static String excelfont = "微软雅黑";
	
	/**
	 * 先下载模板，再上传，下载模板里应有字段序号
     * @param file 文件
     * @param fileName 文件名
     * @return 3级JSONArray[表][行][列]
     * @throws Exception 
     */
	public static JSONArray readExcel(File file, String fileName) throws Exception {
		String ext = fileName.substring(fileName.lastIndexOf(".")+1);
		try (FileInputStream fis = new FileInputStream(file);){
			if ("xls".equals(ext)) {
				return xssf(fis, fileName);
	        }else if("xlsx".equals(ext)) {
	        	return hssf(fis, fileName);
	        }else {
	        	return null;
	        }
		}catch (Exception e) {
			throw e;
		}
		
	}
	public static JSONArray hssf(FileInputStream fis, String fileName) throws Exception {
		try (Workbook wb = new XSSFWorkbook(fis);){
			return readExcel(wb);
		} catch (IOException e) {
			throw e;
		}
	}
	public static JSONArray xssf(FileInputStream fis, String fileName) throws Exception {
		try (Workbook wb = new HSSFWorkbook(new POIFSFileSystem(fis));){
			return readExcel(wb);
		} catch (IOException e) {
			throw e;
		}
	}
	public static JSONArray readExcel(Workbook wb) throws Exception {
        Cell cell = null;
        JSONArray data = new JSONArray();
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {//多表
			Sheet st = wb.getSheetAt(i);
			
			Row rowHead = st.getRow(0);
			Row rowCode = st.getRow(1);
			if (rowHead == null || rowCode == null) {
				continue;
			}
			JSONArray sheet = new JSONArray();
			boolean hasRow = false;
			boolean hasSheet = false;
			int cellNum = rowHead.getLastCellNum();
			int rowNum = st.getLastRowNum();
			for (int j = 2; j <= rowNum; j++) {//行
				Row row = st.getRow(j);
				if (row == null) {
					continue;
				}
				JSONArray values = new JSONArray();
				//Arrays.fill(values, "");//填充数组Arrays.fill(arrayname ,starting index ,ending index ,value)
				for (int k = 0; k <= cellNum; k++) {//列
					String value = "";
					cell = row.getCell(k);
					if (cell != null) {
						switch (cell.getCellTypeEnum()) {
				            case NUMERIC://分为纯数字和日期型数字
				            	if (DateUtil.isCellDateFormatted(cell)) { // date类型
				            		value = ToolsDate.getString(DateUtil.getJavaDate(cell.getNumericCellValue()),"yyyy-MM-dd HH:mm:ss");  
				                } else { // 纯数字   
				                    value = new BigDecimal(String.valueOf(cell.getNumericCellValue())).toPlainString();
				                    //value = new DecimalFormat("0").format(cell.getNumericCellValue());//0：没有补0，#：没有为空
				                }
				            	hasRow = true;
				                break;
				            case STRING://字符
				            	value = cell.getStringCellValue();
				            	hasRow = true;
				                break;
				            case FORMULA://公式
								//value = cell.getCellFormula();// 导入时为公式
				            	try {
				            		value = String.valueOf(cell.getNumericCellValue());
				            	} catch (Exception e) {
				            		value = String.valueOf(cell.getStringCellValue());
				            	}
				            	hasRow = true;
								break;
				            case BLANK: //空值
				            	value = "";
				                break;
				            case BOOLEAN://boolean
				            	value = cell.getBooleanCellValue() == true ? "Y" : "N";
				            	hasRow = true;
				                break;
				            case ERROR://故障
				            	value = "";
				                break;
				            default:
				            	value = "";
				            	break;
				        }
						values.add(value.trim());
					}else {
						values.add("");
					}
				}
				if (hasRow) {
					sheet.add(values);
					hasSheet = true;
					hasRow = false;
				}
			}
			if (hasSheet) {
				data.add(sheet);
				hasSheet = false;
			}
		}
		return data;
    }
	
	public static void download(Excel excel, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (OutputStream os = response.getOutputStream();
				HSSFWorkbook wb = createWB(excel);){
			String file_name = encodeChineseDownloadFileName(request, excel.getFile_name() + ".xls");
			response.setHeader("Content-disposition", file_name);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + file_name);
			response.setHeader("Pragma", "No-cache");
			wb.write(os);
			os.flush();
		} catch (Exception e) {
			throw e;
		}
	}
	public static void create(Excel excel) throws Exception {
		try (OutputStream out = new FileOutputStream(excel.getFile_path() + excel.getFile_name() + ".xls");
				HSSFWorkbook wb = createWB(excel);){
			File newFile = new File(excel.getFile_path());
			if (!newFile.exists()) {
				newFile.mkdirs();
			}
	        wb.write(out);  
		} catch (Exception e) {
			throw e;
		}
	}

	public static HSSFWorkbook createWB(Excel excel) throws Exception {
		try(HSSFWorkbook wb = new HSSFWorkbook();) {
			JSONArray data = excel.getData();
			String sheet_name = excel.getSheet_name();
			String title = excel.getTitle();
			LinkedHashMap<String,String> heads = excel.getHeads();
			String[] fileds = excel.getFileds();
			Integer[] types = excel.getFormats();
			Integer[] widths = excel.getWidths();
			
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
				CellRangeAddress region = new CellRangeAddress(0, 0, 0, fileds.length-1);
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
				for (int i = 0; i < fileds.length; i++) {
					if(Tools.isEmpty(widths))sheet.setColumnWidth(i, widths[i]);
					HSSFCell cell = row.createCell(i);
					cell.setCellValue(fileds[i]);
					cell.setCellStyle(style);
				}
				index++;
			}
			// 表格主体 解析list
			if (data != null) {
				List<HSSFCellStyle> styleList = new ArrayList<HSSFCellStyle>();

				for (int i = 0; i < fileds.length; i++) { // 列数
					HSSFCellStyle style = wb.createCellStyle();
					HSSFFont font = wb.createFont();
					HSSFDataFormat dataformat = wb.createDataFormat();
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
						if (types[i] == 11) {
							style.setAlignment(HorizontalAlignment.LEFT);
						} else if (types[i] == 12) {
							style.setAlignment(HorizontalAlignment.CENTER);
						} else if (types[i] == 13) {
							style.setAlignment(HorizontalAlignment.RIGHT);
							// int类型
						} else if (types[i] == 20) {
							style.setAlignment(HorizontalAlignment.RIGHT);
							// int类型
							style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
						} else if (types[i] > 30 && types[i] < 40) {
							// float类型
							style.setAlignment(HorizontalAlignment.RIGHT);
							style.setDataFormat(dataformat.getFormat("#,##0."+"000000000".substring(0, types[i]-30)));
						} else if (types[i] > 40 && types[i] < 50) {
							// 百分比类型
							style.setAlignment(HorizontalAlignment.RIGHT);
							style.setDataFormat(dataformat.getFormat("0."+"000000000".substring(0, types[i]-40)+"%"));
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
							} else if (types[j] == 20) {
								// int
								cell.setCellValue((Long.valueOf((map.get(fileds[j])) + "")).longValue());
							} else if (types[j] > 30 || types[j] < 50) {
								// float
								cell.setCellValue((Double.valueOf((map.get(fileds[j])) + "")).doubleValue());
								cell.setCellValue(new BigDecimal(String.valueOf(map.get(fileds[j]))).setScale(types[j]%10, BigDecimal.ROUND_HALF_UP).doubleValue());
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
			return wb; 
		} catch (Exception e) {
			throw e;
		}

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
						+ (new String(Base64.encodeBase64(pFileName.getBytes("UTF-8"))))
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
			//
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
