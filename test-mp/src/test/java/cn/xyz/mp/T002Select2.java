package cn.xyz.mp;

import cn.xyz.mapper.UserMapper;
import cn.xyz.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class T002Select2 {
    @Resource
    private UserMapper userMapper;

    @Test
    public void test1() {
        System.out.println("test");
    }

    @Test
    public void test2() {//自定义@select
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("age", 3);
        List<User> users1 = userMapper.find(qw);
        users1.forEach(System.out::println);
    }

    @Test
    public void test3() {//自定义 mapper.xml
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lt("age", 3);
        List<User> users1 = userMapper.selectAll(qw);
        users1.forEach(System.out::println);
    }

    @Test
    public void test4() {//原mybatis写法
        User user = new User();
        user.setAge(5);
        List<User> users1 = userMapper.get(user);
        users1.forEach(System.out::println);
    }

    @Test
    public void test5() {//原mybatis写法
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.gt("age", 3);
        Page<User> page = new Page<>(1, 3);
        //Page<User> page = new Page<>(1, 3, false);//false只查分页记录，不查总记录数
        IPage<User> userPage = userMapper.selectPage(page, qw);
        System.out.println(userPage.getPages());
        System.out.println(userPage.getTotal());
        List<User> records = userPage.getRecords();
        records.forEach(System.out::println);
    }

    @Test
    public void test6() {//原mybatis写法
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.gt("age", 3);
        Page<User> page = new Page<>(1, 3);
        /*IPage<Map<String, Object>> ipage = userMapper.selectMapsPage(page, qw);

        System.out.println(ipage.getPages());
        System.out.println(ipage.getTotal());
        List<Map<String, Object>> records1 = ipage.getRecords();
        records1.forEach(System.out::println);*/
    }

    @Test
    public void test7() {//mapper.xml用page
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.gt("age", 3);
        Page<User> page = new Page<>(1, 3);
        //Page<User> page = new Page<>(1, 3, false);//false只查分页记录，不查总记录数
        IPage<User> userPage = userMapper.findPage(page, qw);
        System.out.println(userPage.getPages());
        System.out.println(userPage.getTotal());
        List<User> records = userPage.getRecords();
        records.forEach(System.out::println);
    }
}
