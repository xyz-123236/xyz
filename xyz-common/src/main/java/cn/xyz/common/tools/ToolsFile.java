package cn.xyz.common.tools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.xyz.common.exception.CustomException;
import org.apache.commons.fileupload.FileItem;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public class ToolsFile {
	//保存文件的方法单独处来
	public static String[] unitNames = {"B", "KB", "MB","GB", "TB", "PB"};
	public static final int MAX_SIZE = 100*1024*1024;
	public static int radix = 1024;
	public static HashMap<String, String> extMap = new HashMap<>();
	static {
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
	}
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
		return ToolsDate.getString("yyyyMMddHHmmssSSS") + (split == null?"":split) + (new Random().nextInt(900) + 100);
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
				InputStream in = item.getInputStream()) {
			int length;
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
	public static JSONObject upload(String url, String savePath, MultipartFile uploadFile, String dir) throws Exception {
		JSONObject obj = new JSONObject();
		if (uploadFile != null && !uploadFile.isEmpty()) {
			String new_name = ToolsString.getId();
			String old_name = uploadFile.getOriginalFilename();
			String ext = getExt(old_name);// 获取文件后缀
			// String ext = FilenameUtils.getExtension(old_name);
			long size = uploadFile.getSize();
			// 判断文件的大小,格式
			if (size > MAX_SIZE) {
				throw new CustomException("上传文件大小超过限制" + formatSize(MAX_SIZE));
			}
			// 检查扩展名
			if (!Tools.isEmpty(dir) && !Arrays.asList(extMap.get(dir).split(",")).contains(ext)) {
				throw new CustomException("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dir) + "格式。");
			}
			// 保存文件
			File newFile = new File(savePath + File.separator + new_name + "." + ext);
			if (!newFile.exists()) {
				if(newFile.mkdirs()){
					throw new CustomException("创建文件目录失败");
				}
			}
			uploadFile.transferTo(newFile);

			// 附件模型对象
			obj.put("old_name", old_name);
			obj.put("new_name", new_name);
			obj.put("ext", ext);
			obj.put("size", formatSize(size));
			obj.put("path", savePath + File.separator + new_name + "." + ext);
			obj.put("url", url + "/" + new_name + "." + ext);
		}
		return obj;
	}

	/**
	 * 删除文件，可以是文件或文件夹
	 *
	 * @param fileName 要删除的文件名
	 * @return 删除成功返回true，否则返回false
	 */
	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return false;
		} else {
			if (file.isFile())
				return deleteFile(fileName);
			else
				return deleteDirectory(fileName);
		}
	}

	/**
	 * 删除单个文件
	 *
	 * @param fileName 要删除的文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			return file.delete();
		} else {
			return false;
		}
	}

	/**
	 * 删除目录及目录下的文件
	 *
	 * @param dir 要删除的目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dir) {
		String _dir = "";
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator))
			_dir = dir + File.separator;
		File dirFile = new File(_dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹中的所有文件包括子目录
		File[] files = dirFile.listFiles();
		if(Tools.isEmpty(files)) return true;
		for (File file : files) {
			// 删除子文件
			if (file.isFile()) {
				flag = deleteFile(file.getAbsolutePath());
				if (!flag)
					break;
			}
			// 删除子目录
			else if (file.isDirectory()) {
				flag = deleteDirectory(file.getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			return false;
		}
		// 删除当前目录
		return dirFile.delete();
	}
	public static void download(String savePath, String fileName, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		//String downLoadPath =filePath.replaceAll("/", "\\\\\\\\");   //replace replaceAll区别
		
		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(savePath));
				BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
			long fileLength = new File(savePath).length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		System.out.println(formatSize((1024 * 25 + 25) * 1024 * 1024L));//测试参数要加l：long
		System.out.println(getExt("123.456.789.jpg"));
	}
}
