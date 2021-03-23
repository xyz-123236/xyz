package cn.xyz.mp;

import cn.xyz.mapper.UserMapper;
import cn.xyz.pojo.User;
import cn.xyz.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest//测试的包名要与启动MainApplication一致，否则要加(classes = MainApplication.class)
public class T009Service {
	@Resource
	private UserService userService;

	@Test
	public void test1() {
		System.out.println("test");
	}

	@Test
	public void test2() {
		User one = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getAge, 3));
		System.out.println(one);
	}

	@Test
	public void test3() {//saveOrUpdateBatch
		User user1 = new  User();
		user1.setName("姓名21");
		user1.setAge(21);
		user1.setManager_id(3L);
		user1.setEmail("test"+21+"@test.cn");
		user1.setCreate_time(LocalDateTime.now());
		user1.setRemark("test");
		User user2 = new  User();
		user2.setName("姓名22");
		user2.setAge(22);
		user2.setManager_id(3L);
		user2.setEmail("test"+22+"@test.cn");
		user2.setCreate_time(LocalDateTime.now());
		user2.setRemark("test");
		List<User> users = Arrays.asList(user1,user2);
		boolean b = userService.saveBatch(users);
		userService.saveOrUpdateBatch(users);
		System.out.println(b);
	}

	@Test
	public void test4() {//list
		List<User> list = userService.lambdaQuery().gt(User::getAge, 5).list();
		list.forEach(System.out::println);
	}

	@Test
	public void test5() {//update
		boolean xingm56 = userService.lambdaUpdate().eq(User::getAge, 5).set(User::getName, "xingm56").update();
		System.out.println(xingm56);
	}

	@Test
	public void test6() {//remove
		boolean xingm56 = userService.lambdaUpdate().eq(User::getAge, 5).remove();
		System.out.println(xingm56);
	}
}
