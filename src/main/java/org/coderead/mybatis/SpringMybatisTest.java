package org.coderead.mybatis;

import org.coderead.mybatis.bean.User;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author HanBin_Yang
 * @since 2022/4/5 21:54
 */
public class SpringMybatisTest {
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
