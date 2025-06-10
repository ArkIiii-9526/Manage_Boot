package com.manage.boot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.manage.boot.mappers.UserMapper;
import com.manage.boot.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
class BootApplicationTests {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull("数据库连接不应为null", connection);
            assertTrue("连接应有效", connection.isValid(5));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void userList(){
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    void selectEmail(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email","332550@qq.com");
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }

}
