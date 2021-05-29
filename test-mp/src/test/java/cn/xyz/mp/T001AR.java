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
public class T001AR {
	@Resource
	private UserMapper userMapper;

	@Test
	public void test1() {
		System.out.println("test");
	}


	@Test
	public void test2() {
			User user = new  User();
			user.setName("姓名12");
			user.setAge(12);
			user.setManager_id(2L);
			user.setEmail("test"+12+"@test.cn");
			user.setCreate_time(LocalDateTime.now());
			user.setRemark("test");
		boolean insert = user.insert();
		System.out.println("=========="+user.getU_id());

	}
	@Test
	public void test3() {
		User user = new  User();
		User user1 = user.selectById(1369607454646812675L);
		System.out.println("=========="+user1);

	}
	@Test
	public void test4() {
		User user = new  User();
		user.setU_id(1369607454646812675L);
		User user1 = user.selectById();
		System.out.println("=========="+user1);
	}

	@Test
	public void test5() {
		User user = new  User();
		user.setU_id(1369607454646812675L);
		user.setAge(20);
		boolean b = user.updateById();
		System.out.println("=========="+b);
	}

	@Test
	public void test6() {
		User user = new  User();
		user.setU_id(1369607454646812675L);
		user.setAge(20);
		boolean b = user.deleteById(user);
		System.out.println("=========="+b);
	}

	@Test
	public void test7() {
		User user = new  User();
		user.setU_id(1369607454646812675L);
		user.setAge(20);
		boolean b = user.insertOrUpdate();
		System.out.println("=========="+b);
	}
}
