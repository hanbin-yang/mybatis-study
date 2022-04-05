package org.coderead.mybatis;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.coderead.mybatis.bean.Blog;
import org.coderead.mybatis.bean.Comment;
import org.coderead.mybatis.bean.User;

import java.util.List;

@CacheNamespace
public interface UserMapper {

    @Select({" select * from users where id=#{1}"})
    //@Options(flushCache = Options.FlushCachePolicy.TRUE)
    User selectByid(Integer id);

    @Select({" select * from users where id=#{id} and name=#{name}"})
    User selectByIdAndName(@Param("id") Integer id, @Param("name") String name);

    @Select({" select * from users where id=#{1}"})
    User selectByid3(Integer id);

    @Select({" select * from users where name='${name}'"})
    @Options(statementType = StatementType.PREPARED)
    List<User> selectByName(User user);

    List<User> selectByUser(User user);

    @Insert("INSERT INTO `users`( `name`, `age`, `sex`, `email`, `phone_number`) VALUES ( #{name}, #{age}, #{sex}, #{email}, #{phoneNumber})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addUser(User user);

    int editUser(User user);

    @Update("update  users set name=#{arg1} where id=#{arg0}")
    int setName(Integer id, String name);

    @Delete("delete from users where id=#{id}")
    int deleteUser(Integer id);

    Blog selectBlogById(Integer id);

    User selectUserByUserId(Integer id);
    Comment selectCommentByBlogId(Integer id);

    Blog selectBlogByIdLazy(Integer id);
    Comment selectCommentsByBlog(Integer id);

    Blog selectByBlogIdCollection(Integer id);

    Blog selectByBlogIdCollectionCircu(Integer id);
}
