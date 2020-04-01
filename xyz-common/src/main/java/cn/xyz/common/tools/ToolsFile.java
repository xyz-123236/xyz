package cn.xyz.common.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.alibaba.fastjson.JSONObject;

public class ToolsFile {
	public static String[] unitNames = {"B", "KB", "MB","GB", "TB", "PB"};
	public static int radix = 1024;
	public static String formatSize(long size) {
		if(size <= 0) return "0";
		int i = (int)(Math.floor(Math.log(size) / Math.log(radix)));
		DecimalFormat df = new DecimalFormat("#.## " + unitNames[i]);
		return df.format(size / Math.pow(radix, i));
	}
	public static String createFileName() {
		return createFileName("");
	}
	public static String createFileName(String split) {
		split = split == null?"":split;
		return ToolsDate.getString("yyyyMMddHHmmssSSS") + split + (new Random().nextInt(900) + 100);
	}
	private static String getExt(String fileName) {
		if (fileName == null)
			return "";
		String ext = "";
		int lastIndex = fileName.lastIndexOf(".");
		if (lastIndex >= 0) {
			ext = fileName.substring(lastIndex + 1).toLowerCase();
		}
		return ext;
	}
	public static JSONObject upload(FileItem item, String path, String url, HttpServletResponse response) {
		
		/** 
         * 以下三步，主要获取 上传文件的名字 
         */  
        //获取路径名  
        String value = item.getName() ; 
        //截取 上传文件的 字符串名字，加1是 去掉反斜杠，  
        String old_name = value.substring(value.lastIndexOf(File.separator) + 1);
        String ext = getExt(old_name);
        String new_name = createFileName() + "." + ext;
        
        JSONObject temp = new JSONObject();
    	temp.put("old_name", old_name);
    	temp.put("new_name", new_name);
    	temp.put("ext", ext);
    	temp.put("size", item.getSize());
    	temp.put("path", path + new_name);
    	temp.put("url", url + new_name);
        //真正写到磁盘上  
        //它抛出的异常 用exception 捕捉  
        //item.write( new File(path,filename) );//第三方提供的  
        //手动写的  
		try(	OutputStream out = new FileOutputStream(new File(path,new_name));
				InputStream in = item.getInputStream(); ) {
			int length = 0 ;  
			byte [] buf = new byte[1024] ;  
			// in.read(buf) 每次读到的数据存放在   buf 数组中  
			while( (length = in.read(buf) ) != -1) {  
			    //在   buf 数组中 取出数据 写到 （输出流）磁盘上  
			    out.write(buf, 0, length);  
			}
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void download(String savePath, String fileName, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		java.io.BufferedInputStream bis = null;
		java.io.BufferedOutputStream bos = null;
	
		//String downLoadPath =filePath.replaceAll("/", "\\\\\\\\");   //replace replaceAll区别
		
		try {
			long fileLength = new File(savePath).length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			bis = new BufferedInputStream(new FileInputStream(savePath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null)
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	public static void main(String[] args) {
		System.out.println(formatSize((1024*25+25)*1024*1024l));//测试参数要加l：long
		System.out.println(getExt("123.456.789.jpg"));
	}
}
