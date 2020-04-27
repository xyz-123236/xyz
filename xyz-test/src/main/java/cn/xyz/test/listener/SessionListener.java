package cn.xyz.test.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession ses = se.getSession();
		String id = ses.getId() + ses.getCreationTime();
		System.out.println("sessionCreated:"+id); // 添加用户
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession ses = se.getSession();
		String id = ses.getId() + ses.getCreationTime();
		synchronized (this) {
			System.out.println("sessionDestroyed:"+id);
		}
	}
}
