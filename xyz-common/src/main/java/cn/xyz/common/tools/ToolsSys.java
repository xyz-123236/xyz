package cn.xyz.common.tools;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class ToolsSys {
	/**
	 * *获取客户端ip
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public static String getIP(HttpServletRequest request) throws Exception {
		String ip = null;
		String ipAddresses = request.getHeader("X-Real-IP");// X-Real-IP：nginx服务代理
		if (Tools.isEmpty(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
			ipAddresses = request.getHeader("Proxy-Client-IP");// Proxy-Client-IP：apache 服务代理
		}
		if (Tools.isEmpty(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
			ipAddresses = request.getHeader("X-Forwarded-For");// X-Forwarded-For：Squid 服务代理
		}
		if (Tools.isEmpty(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
			ipAddresses = request.getHeader("WL-Proxy-Client-IP");// WL-Proxy-Client-IP：weblogic 服务代理
		}
		if (Tools.isEmpty(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
			ipAddresses = request.getHeader("HTTP_CLIENT_IP");// HTTP_CLIENT_IP：有些代理服务器
		}
		// 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
		if (!Tools.isEmpty(ipAddresses)) {
			ip = ipAddresses.split(",")[0];
		}
		// 还是不能获取到，最后再通过request.getRemoteAddr();获取
		if (Tools.isEmpty(ip) || "unknown".equalsIgnoreCase(ipAddresses)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}
	/**
	 * 获取主机名称
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getHostName() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName();
	}

	/**
	 * 获取系统首选IP
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getLocalIP() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}

	/**
	 * 获取所有网卡IP，排除回文地址、虚拟地址
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static String[] getLocalIPs() throws SocketException {
		List<String> list = new ArrayList<>();
		Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
		while (enumeration.hasMoreElements()) {
			NetworkInterface intf = enumeration.nextElement();
			if (intf.isLoopback() || intf.isVirtual()) { //
				continue;
			}
			Enumeration<InetAddress> inets = intf.getInetAddresses();
			while (inets.hasMoreElements()) {
				InetAddress addr = inets.nextElement();
				if (addr.isLoopbackAddress() || !addr.isSiteLocalAddress() || addr.isAnyLocalAddress()) {
					continue;
				}
				list.add(addr.getHostAddress());
			}
		}
		return list.toArray(new String[0]);
	}

	/**
	 * 判断操作系统是否是Windows
	 * 
	 * @return
	 */
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}

	public static void main(String[] args) {
		try {
			System.out.println("主机是否为Windows系统：" + isWindowsOS());
			System.out.println("主机名称：" + getHostName());
			System.out.println("系统首选IP：" + getLocalIP());
			System.out.println("系统所有IP：" + String.join(",", getLocalIPs()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
