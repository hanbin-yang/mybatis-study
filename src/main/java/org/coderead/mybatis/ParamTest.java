package org.coderead.mybatis;

import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.WhereSqlNode;
import org.apache.ibatis.session.*;
import org.coderead.mybatis.bean.Blog;
import org.coderead.mybatis.bean.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author YangHanBin
 * @date 2021-09-15 9:28
 */
public class ParamTest {
    private SqlSession sqlSession;
    private UserMapper userMapper;
    private SqlSessionFactory factory;

    @Before
    public void init() {
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        factory = builder.build(ParamTest.class.getResourceAsStream("/mybatis-config.xml"));
        factory.getConfiguration().setLazyLoadTriggerMethods(new HashSet<>());
        sqlSession = factory.openSession();
        userMapper = sqlSession.getMapper(UserMapper.class);
    }

    @After
    public void end() {
        sqlSession.close();
    }


    @Test
    public void singleTest() {
//        User user = userMapper.selectByid(10);
        User user = new User();
        user.setId(10);
        user.setName("鲁班大叔");
        List<User> users = userMapper.selectByUser(user);
        System.out.println("users = " + users);
    }

    @Test
    public void test2() {
        List<Object> list = new ArrayList<>();
        ResultHandler resultHandler = new ResultHandler() {
            @Override
            public void handleResult(ResultContext resultContext) {
                if (resultContext.getResultCount() == 1) {
                    resultContext.stop();
                }

                list.add(resultContext.getResultObject());
            }
        };
        sqlSession.select("", resultHandler);
    }

    @Test
    public void circuTest() {
        SqlSession sqlSession = factory.openSession();
        Object blog = sqlSession.selectOne("selectBlogByIdLazy", 10);
        System.out.println("blog = " + blog);
    }


    @Test
    public void collectionTest() {
        SqlSession sqlSession = factory.openSession();
        Blog blog = sqlSession.selectOne("selectByBlogIdCollection", 10);
        System.out.println("blog = " + blog);
    }

    /**
     * 循环引用 left join
     */
    @Test
    public void collectionTest2() {
        SqlSession sqlSession = factory.openSession();
        Blog blog = sqlSession.selectOne("selectByBlogIdCollectionCircu", 10);
        System.out.println("blog = " + blog);
    }

    @Test
    public void ifTest() {
        User user = new User();
        user.setId(10);
        Configuration configuration = factory.getConfiguration();
        DynamicContext context = new DynamicContext(configuration, user);
        new StaticTextSqlNode("select * from user").apply(context);

        IfSqlNode ifSqlNode = new IfSqlNode(new StaticTextSqlNode(" and id=#{id}"), "id != null");
        //ifSqlNode.apply(context);

        WhereSqlNode whereSqlNode = new WhereSqlNode(configuration, ifSqlNode);
        whereSqlNode.apply(context);
        System.out.println(context.getSql());
    }
}