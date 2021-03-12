package cn.xyz.mp;

import cn.xyz.mapper.UserMapper;
import cn.xyz.pojo.User;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class T002Update {
    @Resource
    private UserMapper userMapper;

    @Test
    public void test1() {
        System.out.println("test");
    }

    @Test
    public void test2() {
        User user = new  User();
        user.setU_id(1369607453568876545L);
        user.setAge(20);
        int update = userMapper.updateById(user);
        System.out.println(update);
    }

    @Test
    public void test3() {
        UpdateWrapper<User> uw = new UpdateWrapper<>();
        uw.eq("age", 20);
        User user = new  User();
        user.setEmail("test20@test.cn");
        int update = userMapper.update(user, uw);
        System.out.println(update);
    }

    @Test
    public void test4() {
        User user2 = new User();
        user2.setAge(20);
        UpdateWrapper<User> uw = new UpdateWrapper<>(user2);
        uw.gt("age", 2);
        User user = new User();
        user.setEmail("test30@test.cn");
        int update = userMapper.update(user, uw);
        System.out.println(update);
    }

    @Test
    public void test5() {//set
        UpdateWrapper<User> uw = new UpdateWrapper<>();
        uw.eq("age", 3).set("email","test3@test.cn");

        int update = userMapper.update(null, uw);
        System.out.println(update);
    }

    @Test
    public void test6() {//lambda
        LambdaUpdateWrapper<User> lambda = new UpdateWrapper<User>().lambda();
        lambda.eq(User::getAge, 5).set(User::getEmail, "test5@test.cn");
        int update = userMapper.update(null, lambda);
        System.out.println(update);
    }

    @Test
    public void test7() {//链式调用 传userMapper
        boolean update = new LambdaUpdateChainWrapper<User>(userMapper) //userMapper
                .eq(User::getAge, 5).set(User::getEmail, "test5@test.cn").update();
        System.out.println(update);
    }
}
