package cn.mpy.shiro.service;

import cn.mpy.shiro.entity.UserBean;

public interface UserService {
    UserBean findUser(String username);

    UserBean findUserWhere(String username);
}
