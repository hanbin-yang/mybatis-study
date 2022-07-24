package org.coderead.mybatis;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
import org.coderead.mybatis.bean.Blog;
import org.coderead.mybatis.bean.User;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;

/**
 * @author YangHanBin
 * @date 2021-09-12 22:50
 */
public class SecondCacheTest {

    private Configuration configuration;

    private Connection connection;

    private JdbcTransaction jdbcTransaction;

    private MappedStatement ms;

    private SqlSessionFactory factory;

    @Before
    public void init() throws SQLException {
        // 获取构建器
        SqlSessionFactoryBuilder factoryBuilder = new SqlSessionFactoryBuilder();
        // 解析XML 并构造会话工厂
        InputStream configSteam = ExecutorTest.class.getResourceAsStream("/mybatis-config.xml");
        Properties properties = new Properties();
        properties.setProperty("jdbc.driver", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("jdbc.url", "jdbc:mysql:///blog?serverTimezone=GMT%2B8");
        properties.setProperty("jdbc.username", "root");
        properties.setProperty("jdbc.password", "123456");

        factory = factoryBuilder.build(configSteam, properties);
        this.configuration = factory.getConfiguration();
        this.configuration.setLazyLoadTriggerMethods(new HashSet<>());
        connection = DriverManager.getConnection(JdbcTest.URL, JdbcTest.USERNAME, JdbcTest.PASSWORD);
        jdbcTransaction = new JdbcTransaction(connection);
        // 获取SQL映射
        ms = this.configuration.getMappedStatement("org.coderead.mybatis.UserMapper.selectByid");
    }

    @Test
    public void cacheTest1() {
        Cache cache = configuration.getCache("org.coderead.mybatis.UserMapper");
        User user = new User();
        user.setName("鲁班");
        cache.putObject("luban", user);
        cache.getObject("luban");
    }

    @Test
    public void cacheTest2() {
        SqlSession sqlSession = factory.openSession(true);
        UserMapper mapper1 = sqlSession.getMapper(UserMapper.class);
        mapper1.selectByid3(10);
    }

    @Test
    public void lazyLoadTest() {
        SqlSession sqlSession = factory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Blog blog = mapper.selectBlogByIdLazy(10);
        System.out.println("blog = " + blog.toString());
    }
}
