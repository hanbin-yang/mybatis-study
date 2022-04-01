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
        InputStream resourceAsStream = ExecutorTest.class.getResourceAsStream("/mybatis-config.xml");
        factory = factoryBuilder.build(resourceAsStream);
        configuration = factory.getConfiguration();
        connection = DriverManager.getConnection(JdbcTest.URL, JdbcTest.USERNAME, JdbcTest.PASSWORD);
        jdbcTransaction = new JdbcTransaction(connection);
        // 获取SQL映射
        ms = configuration.getMappedStatement("org.coderead.mybatis.UserMapper.selectByid");
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
        SqlSession sqlSession1 = factory.openSession(true);
        UserMapper mapper1 = sqlSession1.getMapper(UserMapper.class);
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
