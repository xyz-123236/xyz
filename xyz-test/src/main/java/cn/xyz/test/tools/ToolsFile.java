package cn.xyz.test.tools;

import java.text.DecimalFormat;

public class ToolsFile {
	public static String[] unitNames = {"B", "KB", "MB","GB", "TB", "PB"};
	public static int radix = 1024;
	public static String formatSize(long size) {
		if(size <= 0) return "0";
		int i = (int)(Math.floor(Math.log(size) / Math.log(radix)));
		DecimalFormat df = new DecimalFormat("#.## " + unitNames[i]);
		return df.format(size / Math.pow(radix, i));
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
	public static void main(String[] args) {
		System.out.println(formatSize((1024*25+25)*1024*1024l));//测试参数要加l：long
		System.out.println(getExt("123.456.789.jpg"));
	}
}
