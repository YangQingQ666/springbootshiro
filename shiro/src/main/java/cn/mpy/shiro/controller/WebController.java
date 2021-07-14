package cn.mpy.shiro.controller;

import cn.mpy.shiro.entity.UserBean;
import cn.mpy.shiro.exception.UnauthorizedException;
import cn.mpy.shiro.service.UserService;
import cn.mpy.shiro.util.JwtUtils;
import cn.mpy.shiro.util.Md5Utils;
import cn.mpy.shiro.util.RsaUtils;
import cn.mpy.shiro.bean.ResponseBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.PrivateKey;
import java.util.UUID;

/**
 * web控制类
 * @author mpy
 */
@RestController
public class WebController {

    @Autowired
    private UserService userService;
    /**保存公钥的位置*/
    private static String pubkeyPath = "E:\\rsa_key.pub";

    /**保存私钥的位置*/
    private static String prikeyPath = "E:\\rsa_key";

    @PostMapping("/findUser")
    public ResponseBean login(@RequestParam("username") String username) {
        UserBean userBean = this.userService.findUserWhere(username);
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(userBean);
        return responseBean;
    }

    @PostMapping("/login")
    public ResponseBean login(@RequestParam("username") String username,
                              @RequestParam("password") String password) {
        UserBean userBean = this.userService.findUserWhere(username);
        String result = Md5Utils.getMD5Password(password, userBean.getSalt());
        try {
            RsaUtils.generateKey(pubkeyPath, prikeyPath, String.valueOf(UUID.randomUUID()), 2048);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrivateKey privateKey = null;
        try {
            privateKey = RsaUtils.getPrivateKey(prikeyPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //生成token
        String token = JwtUtils.generateTokenExpireInMinutes(userBean, privateKey);
        System.out.println("token = " + token);
        if (userBean.getPassword().equals(result)) {
            return new ResponseBean(200, "Login success", token);
        } else {
            throw new UnauthorizedException();
        }
    }

    @GetMapping("/article")
    @RequiresPermissions("edit")
    public ResponseBean article() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return new ResponseBean(200, "You are already logged in", null);
        } else {
            return new ResponseBean(200, "You are guest", null);
        }
    }

    /**
     * 验证是否登陆
     */
    @GetMapping("/require_auth")
    @RequiresAuthentication
    public ResponseBean requireAuth() {
        return new ResponseBean(200, "You are authenticated", null);
    }

    /**
     * 验证是否是user角色
     */
    @GetMapping("/require_role")
    @RequiresRoles("user")
    public ResponseBean requireRole() {
        return new ResponseBean(200, "You are visiting require_role", null);
    }

    /**
     * 验证是否有 view edit 权限
     */
    @GetMapping("/require_permission")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public ResponseBean requirePermission() {
        return new ResponseBean(200, "You are visiting permission require edit,view", null);
    }

    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseBean unauthorized() {
        return new ResponseBean(401, "登陆过期，token失效", null);
    }
}
