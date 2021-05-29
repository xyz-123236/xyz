package cn.xyz.mp;

import cn.xyz.mapper.UserMapper;
import cn.xyz.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest//测试的包名要与启动MainApplication一致，否则要加(classes = MainApplication.class)
public class T011delete {
	@Resource
	private UserMapper userMapper;

	@Test
	public void test1() {
		System.out.println("test");
	}

	@Test
	public void test2() {
		int i = userMapper.deleteById(1369607454579703810L);
		System.out.println(i);
	}

	@Test
	public void test3() {//自定义的方法不会过滤delete=1
		List<User> users = userMapper.selectList(null);
		users.forEach(System.out::println);
	}

	@Test
	public void test4() {
		User user = new  User();
		user.setName("姓名11");
		user.setAge(12);
		user.setU_id(1370345535456870401L);
		user.setManager_id(2L);
		user.setEmail("test"+11+"@test.cn");
		user.setCreate_time(LocalDateTime.now());
		user.setRemark("test");
		user.setVersion(1);
		int update = userMapper.updateById(user);
		System.out.println(update);
	}

	@Test
	public void test5() {//自定义的方法不会过滤delete=1
		QueryWrapper<User> qw = new QueryWrapper<>();
		qw.lt("age", 3);
		qw.eq("deleted", 0);
		List<User> users1 = userMapper.selectAll(qw);
		users1.forEach(System.out::println);
	}
}
