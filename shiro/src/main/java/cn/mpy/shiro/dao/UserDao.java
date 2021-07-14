package cn.mpy.shiro.dao;

import cn.mpy.shiro.entity.UserBean;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserDao {
    UserBean findUser(String username);

    UserBean findUserWhere(String username);

}
