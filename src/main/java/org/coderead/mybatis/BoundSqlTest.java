package org.coderead.mybatis;

import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.ExpressionEvaluator;
import org.coderead.mybatis.bean.Blog;
import org.coderead.mybatis.bean.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author YangHanBin
 * @date 2021-09-16 11:31
 */
public class BoundSqlTest {
    @Test
    public void ognlTest() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Blog blog = new Blog();
        blog.setTitle("ddf");
        boolean b = evaluator.evaluateBoolean("title != null", blog);
        System.out.println("b = " + b);
    }

}
