package cn.xyz.mp;

import cn.xyz.mapper.UserMapper;
import cn.xyz.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
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
public class T002Select {
    @Resource
    private UserMapper userMapper;

    @Test
    public void test1() {
        System.out.println("test");
    }

    @Test
    public void test2() {
        User user = userMapper.selectById(1369109870727602177L);
        System.out.println(user);
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1369607454579703809L, 1369607454646812673L));
        users.forEach(System.out::println);
        Map<String, Object> map = new HashMap<>();
        map.put("name","姓名5");
        map.put("age",5);
        List<User> users1 = userMapper.selectByMap(map);
        users1.forEach(System.out::println);
    }

    @Test
    public void test3() {
        QueryWrapper<User> qw = new QueryWrapper<>();
        QueryWrapper<User> qw2 = Wrappers.query();
        qw.select("u_id","name","create_time")
                .select(User.class, info->!info.getColumn().equals("create_time")
                    && !info.getColumn().equals("manager_id") )
                .like( 1 < 2,"name", "5")
                .lt("age", 10)
                .between("age", 2, 9)
                .isNotNull("email")
                .in("age", Arrays.asList(3,4,5,6))
                .orderByDesc("name")
                .orderByAsc("age")
                .last("limit 1");
        List<User> users = userMapper.selectList(qw);
        users.forEach(System.out::println);
    }

    @Test
    public void test4() {
        //SELECT u_id,name,age,email,manager_id,create_time FROM user
        // WHERE date_format(create_time,'%Y-%m-%d')=? AND manager_id IN (select u_id from user where age < 3)
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.apply("date_format(create_time,'%Y-%m-%d')={0}", "2021-03-10")//{0}占位符，防注入
                .inSql("manager_id","select u_id from user where age < 3")
                ;
        List<User> users = userMapper.selectList(qw);
        users.forEach(System.out::println);
    }

    @Test
    public void test5() {
        //SELECT u_id,name,age,email,manager_id,create_time FROM user
        // WHERE name LIKE ? AND (age < ? OR email IS NULL)
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.likeRight("name","姓")
                .and(wq->wq.lt("age",5).or().isNull("email"))//and
        ;
        List<User> users = userMapper.selectList(qw);
        users.forEach(System.out::println);
    }

    @Test
    public void test6() {
        //SELECT u_id,name,age,email,manager_id,create_time FROM user
        // WHERE name = ? OR (age > ? AND email IS NOT NULL)
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("name","姓名3")
                .or(wq->wq.gt("age",5).isNotNull("email"))//or
        ;
        List<User> users = userMapper.selectList(qw);
        users.forEach(System.out::println);
    }

    @Test
    public void test7() {
        //SELECT u_id,name,age,email,manager_id,create_time FROM user
        // WHERE (age > ? AND email IS NOT NULL) OR name = ?
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.nested(wq->wq.gt("age",5).isNotNull("email"))//nested
                .or().eq("name","姓名3")
        ;
        List<User> users = userMapper.selectList(qw);
        users.forEach(System.out::println);
    }

    @Test
    public void test8() {
        //SELECT u_id,name,age,email,manager_id,create_time FROM user
        // WHERE name LIKE CONCAT('%',?,'%') AND age<? AND (age > ?)
        User user = new User();
        user.setName("姓名");
        user.setAge(6);
        QueryWrapper<User> qw = new QueryWrapper<>(user);//user
        qw.gt("age", 3);
        List<User> users = userMapper.selectList(qw);
        users.forEach(System.out::println);
    }

    @Test
    public void test9() {
        Map<String, Object> map = new HashMap<>();
        map.put("name","姓名5");
        map.put("age",5);
        map.put("email",null);
        QueryWrapper<User> qw = new QueryWrapper<>();
        //qw.allEq(true, (k,v)-> k.equals("name"), map, true);//第一个条件是false不加入条件，第二个通过map的key/value过滤条件
        qw.allEq(map);
        List<User> users = userMapper.selectList(qw);
        users.forEach(System.out::println);
    }

    @Test
    public void test10() {
        //SELECT avg(age) avg_age,min(age) min_age,max(age) max_age FROM user
        // GROUP BY manager_id HAVING sum(age) > ?
        //selectMaps可以避免实体返回null字段,或实体类没有的字段
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.select("avg(age) avg_age","min(age) min_age","max(age) max_age")
                .groupBy("manager_id")
                .having("sum(age) > {0}", 25);
        List<Map<String, Object>> maps = userMapper.selectMaps(qw);//selectMaps
        maps.forEach(System.out::println);
    }

    @Test
    public void test11() {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.gt("age", 3);
        List<Object> objects = userMapper.selectObjs(qw);//selectObjs只返回第一列
        objects.forEach(System.out::println);
    }
    @Test
    public void test12() {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.gt("age", 3);
        Integer integer = userMapper.selectCount(qw);//selectCount
        System.out.println(integer);;
    }
    @Test
    public void test13() {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("age", 3);
        //qw.gt("age", 3);//报错
        User user = userMapper.selectOne(qw);//selectOne只能返回预期一条的查询
        System.out.println(user);;
    }

    @Test
    public void test14() {
        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();
        LambdaQueryWrapper<User> lambda2 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> lambda3 = Wrappers.lambdaQuery();

        lambda.eq(User::getAge, 3);
        User user = userMapper.selectOne(lambda);//selectOne只能返回预期一条的查询
        System.out.println(user);;
    }
    @Test
    public void test15() {
        List<User> list = new LambdaQueryChainWrapper<>(userMapper)//LambdaQueryChainWrapper
                .eq(User::getAge, 3).list();
        list.forEach(System.out::println);
    }

    @Test
    public void test16() {
        //SELECT u_id,name,age,email,manager_id,create_time FROM user
        // WHERE (age > ? AND email IS NOT NULL) OR name = ?
        MyQueryWrapper<User> qw = new MyQueryWrapper<>();
        qw.eq("age",null).ne("name","姓名3").eq("age", 7).le("age", 7)
        ;
        List<User> users = userMapper.selectList(qw);
        users.forEach(System.out::println);
    }
}
