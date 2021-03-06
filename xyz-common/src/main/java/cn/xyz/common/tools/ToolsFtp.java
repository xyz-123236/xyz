package cn.xyz.common.tools;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

public class ToolsFtp {
    public static JSONObject uploadFileFtp(String host, String username, String password, String filename, String filePath, String basePath) {
        FTPClient ftp = new FTPClient();
        FileInputStream input = null;
        JSONObject obj = new JSONObject();
        try {
            ftp.enterLocalPassiveMode();
            input=new FileInputStream(new File(filePath));
            ftp.connect(host);// 连接FTP服务器
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.out.println("uploadFileFtp-------isPositiveCompletion------false" + ftp.getReplyCode());
                obj.put("status", 0);
                obj.put("msg", "isPositiveCompletion" + ftp.getReplyCode());
                return obj;
            }
            //切换到上传目录
            ftp.changeWorkingDirectory(basePath);
            //设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //上传文件
            if (!ftp.storeFile(filename, input)) {
                System.out.println("uploadFileFtp-------storeFile------false" + ftp.getReplyCode());
                obj.put("status", 0);
                obj.put("msg", "storeFile--false" + ftp.getReplyCode());
                return obj;
            }
            System.out.println("uploadFileFtp-------storeFile------true" + ftp.getReplyCode());
            obj.put("status", 1);
            obj.put("msg", "storeFile--true" + ftp.getReplyCode());
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("uploadFileFtp-------exception------false" + ftp.getReplyCode());
            obj.put("status", 0);
            obj.put("msg", "exception" + ftp.getReplyCode());
            return obj;
        } finally {
            try {
                ftp.logout();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static boolean downloadFile(String host, String username, String password, String remotePath, String localPath) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return false;
            }
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                //if (ff.getName().equals(fileName)) {
                File localFile = new File(localPath + "/" + ff.getName());

                OutputStream is = new FileOutputStream(localFile);
                ftp.retrieveFile(ff.getName(), is);
                is.close();
                //}
            }

            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ignored) {
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String fileName = "";
        String filePath = "" + fileName;
        String savePath = "/in";
        //JSONObject obj = ToolsFtp.uploadFileFtp("ftpas.flextronics.com", "ftpfujik", "u7j6cZ#W", fileName, filePath, savePath);
        //FileUtilities.downloadFile("ftpas.flextronics.com","ftpfjtsf","w7y6mX%G","/test_out","E:\\temp");

    }
}
