package cn.xyz.test.test;

public class T002 {

	public static void main(String[] args) {
		//String command = "cmd /k start C:/MyStart.bat";
		try {
			Runtime run = Runtime.getRuntime();
			System.out.println("准备打开cmd");
			//启动cmd窗口
			run.exec("cmd /k  start cmd.exe");
		} catch (Exception e) {
			System.out.println("ERROR!");
			e.printStackTrace();
		}

	}

}
