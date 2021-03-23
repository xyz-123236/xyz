package cn.xyz.common.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.Map;

public class ToolsCsv {
    public static void createCSV(String fileName, String filePath, JSONObject head, JSONArray data, String[] order) {
        createCSV(fileName, filePath, head, data, order, ",");
    }
    public static void createCSV(String fileName, String filePath, JSONObject head, JSONArray data, String[] order, String separator) {
    	try {
	    	File csvFile = new File(filePath + fileName);
	        File parent = csvFile.getParentFile();
	        if (parent != null && !parent.exists()) {
	            parent.mkdirs();
	        }
			csvFile.createNewFile();
			try(BufferedWriter csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "gbk"), 1024)) {
	            // 写入文件头部
	            if(!Tools.isEmpty(head)) {
	                writeRow(head, csvWriter, order, separator);
	            }

	            // 写入文件内容
	            if(!Tools.isEmpty(data)) {
	                for (int i = 0; i < data.size(); i++) {
	                    writeRow(data.getJSONObject(i), csvWriter, order, separator);
	                }
	            }
	            csvWriter.flush();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
    }

    private static void writeRow(JSONObject row, BufferedWriter csvWriter, String[] order, String separator) throws IOException {
        StringBuffer sb = new StringBuffer();
        if(order != null) {
            for (int j = 0; j < order.length; j++) {
                sb.append(row.getString(order[j])).append(separator);
            }
        }else {
            for (Map.Entry<String,Object> entry : row.entrySet()) {
                sb.append(entry.getValue()).append(separator);
            }
        }
        csvWriter.write(sb.toString());
        csvWriter.newLine();
    }
}
