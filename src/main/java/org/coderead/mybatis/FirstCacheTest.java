package org.coderead.mybatis;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import org.coderead.mybatis.bean.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


import java.util.List;

/**
 * @author YangHanBin
 * @date 2021-09-12 10:04
 */
public class FirstCacheTest {
    private SqlSessionFactory factory;
    private SqlSession sqlSession;

    @Before
    public void init() {
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        factory = builder.build(FirstCacheTest.class.getResourceAsStream("/mybatis-config.xml"));
        sqlSession = factory.openSession();
    }

    /**
     * 一级缓存命中规则：
     * 1 sq和参数相同
     * 2 必须是相同的·statementid
     * 3 sqlSession 必须一样 （会话级别缓存）
     * RowBounds 返回行范围相同
     * 未手动清空
     * 未调用 sqlSession.clearCache() @Options(flushCache = Options.FlushCachePolicy.TRUE)
     * 未执行update
     * 缓存作用域不是statement-> 嵌套查询
     */
    @Test
    public void test1() {
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.selectByid(10);
        mapper.selectByid(10);
        RowBounds rowBounds = new RowBounds(0, 10);
        List<Object> list = sqlSession.selectList("org.coderead.mybatis.UserMapper.selectByid", 10);
    }


    @Test
    public void test2() {
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.selectByid(10);
        sqlSession.clearCache();
        mapper.selectByid(10);

    }

    @Test
    public void testBySpring() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        UserMapper mapper = context.getBean(UserMapper.class);
        //  动态代理                                  动态代理               mybatis
        //    |                                         |
        //   \|/                                       \|/
        // mapper   -> SqlSessionTemplate -> SqlSessionInterceptor -> SqlSessionFactory

        DataSourceTransactionManager txManager = (DataSourceTransactionManager) context.getBean("transactionManager");
        // 手动开启事务
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());

        User u1 = mapper.selectByid(10);
        User u2 = mapper.selectByid(10);
        System.out.println("u1 == u2 " + (u1 == u2));
    }
}
