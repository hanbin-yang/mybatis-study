package org.coderead.mybatis;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.coderead.mybatis.bean.Blog;
import org.coderead.mybatis.bean.User;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author YangHanBin
 * @date 2021-09-15 11:38
 */
public class MetaObjectTest {

    @Test
    public void mOTest() {
        Blog blog = new Blog();
        Configuration configuration = new Configuration();
        MetaObject metaObject = configuration.newMetaObject(blog);
        metaObject.setValue("body", "dddd");

        // 对象直接设置，可以自动创建
        metaObject.setValue("author.name", "LuBan");

        System.out.println(metaObject.getValue("author.name"));
        String property = metaObject.findProperty("author.phoneNumber", true);
        System.out.println("property = " + property);

        ArrayList<User> list = new ArrayList<>();
        list.add(new User());

        // 数组不能自动创建
        metaObject.setValue("comments", list);
        metaObject.setValue("comments[0].name", "yhb");
        Object value = metaObject.getValue("comments[0].name");

        System.out.println(value);
    }
}
