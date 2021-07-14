package cn.mpy.shiro.service.impl;

import cn.mpy.shiro.dao.UserDao;
import cn.mpy.shiro.entity.UserBean;
import cn.mpy.shiro.service.UserService;
import cn.mpy.shiro.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserBean findUser(String username) {
        return this.userDao.findUser(username);
    }

    @Override
    public UserBean findUserWhere(String username) {
        UserBean userWhere = this.userDao.findUserWhere(username);
        userWhere.setPermission(StringUtil.Addall(userWhere.getPermissions()));
        userWhere.setRole(StringUtil.Addall(userWhere.getRoles()));
        return userWhere;
    }
}
