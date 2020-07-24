package cn.xyz.test.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import cn.xyz.orm.db.DbJdbc;

public class testUpdateFile {
	public static void main(String[] args) {
		String path = testUpdateFile.class.getResource("/").getPath() + "test.properties";
		System.out.println(path);
		String fileUrl = testUpdateFile.class.getClassLoader().getResource("test.properties").getPath();
		System.out.println(fileUrl);
		fileUrl = fileUrl.replaceAll("%20", " ");
		Properties prop = new Properties();
		InputStream fis = null;
		OutputStream fos = null;
		try {
			/*
			 * File file = new File(path); if (!file.exists()) file.createNewFile();
			 */
			 fis = new FileInputStream(fileUrl);
			//fis = testUpdateFile.class.getClassLoader().getResourceAsStream("test.properties");
			prop.load(fis);
			fis.close();// 一定要在修改值之前关闭fis
			fos = new FileOutputStream(fileUrl);
			prop.setProperty("test2", "fff"); // 设值-保存
			prop.store(fos, "");
		} catch (IOException e) {
		} finally {
			try {
				fos.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
