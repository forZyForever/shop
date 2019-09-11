package com.Nintendo.user.dao;
import com.Nintendo.user.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/****
 * @Author:admin
 * @Description:User的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface UserMapper extends Mapper<User> {
    @Update("update tb_user set points=points+#{points} where username=#{username} ")
    int addPoints(@Param(value = "points") Integer points,@Param(value = "username") String username);
}
