package cn.xyz.mp;

import cn.xyz.mapper.UserMapper;
import cn.xyz.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class T002Delete {
    @Resource
    private UserMapper userMapper;

    @Test
    public void test1() {
        System.out.println("test");
    }

    @Test
    public void test2() {
        int i = userMapper.deleteById(1369612626273968129L);
        System.out.println(i);
    }

    @Test
    public void test3() {
        Map<String, Object> map = new HashMap<>();
        map.put("age", 20);
        int i = userMapper.deleteByMap(map);
        System.out.println(i);
    }

    @Test
    public void test4() {
        int i = userMapper.deleteBatchIds(Arrays.asList(12,23,45));
        System.out.println(i);
    }

    @Test
    public void test5() {
        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();
        lambda.eq(User::getAge, 20);
        int i = userMapper.delete(lambda);
        System.out.println(i);
    }
}
