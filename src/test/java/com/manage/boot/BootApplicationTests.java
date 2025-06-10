package com.manage.boot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
class BootApplicationTests {
    @Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() {
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull("数据库连接不应为null", connection);
            assertTrue("连接应有效", connection.isValid(5));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
