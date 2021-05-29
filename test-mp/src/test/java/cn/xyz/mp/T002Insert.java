package cn.xyz.mp;

import cn.xyz.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class T002Insert {
    @Resource
    private UserMapper userMapper;

    @Test
    public void test1() {
        System.out.println("test");
    }
}
