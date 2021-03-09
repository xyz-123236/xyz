package cn.xyz.mp;

import cn.xyz.mapper.UserMapper;
import cn.xyz.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest//测试的包名要与启动MainApplication一致，否则要加(classes = MainApplication.class)
public class T001 {
	@Resource
	private UserMapper userMapper;

	@Test
	public void test1() {
		System.out.println("test");
	}

	@Test
	public void test2() {
		List<User> users = userMapper.selectList(null);
		users.forEach(System.out::println);
	}
	@Test
	public void test3() {
		User user = new  User();
		user.setName("姓名1");
		user.setAge(1);
		user.setManager_id(0L);
		user.setEmail("test1@test.cn");
		user.setCreate_time(LocalDateTime.now());
		int rows = userMapper.insert(user);
		System.out.println("=========="+rows);

	}
}
