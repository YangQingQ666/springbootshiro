package cn.mpy.shiro.entity;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * 实体类
 * @author mpy
 */
@Component
public class UserBean implements Serializable {
    private String username;

    private String password;
    private String salt;

    private String role;

    private String permission;
    private List<String> roles;
    private List<String> permissions;

    public List<String> getRoles() {
        return roles;
    }

    public UserBean(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserBean() {
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
