package cn.mpy.shiro.util;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * Md5工具类
 * @author mpy
 */
public class Md5Utils {

    /**
     * 生成盐值
     * @return
     */
    public static String getSalt() {
        //生成盐需要存入数据库中的
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        return salt;
    }

    /**
     * 根据盐值和原始密码MD5加密n次 1024 次数
     * @param originalPassword 原始密码
     * @param salt             盐
     * @return
     */
    public static String getMD5Password(String originalPassword, String salt) {
        String md5Password = new Md5Hash(originalPassword, salt, 1024).toString();
        return md5Password;
    }

    public static void main(String[] args) {
        String pwd = getMD5Password("admin", "27d165e1f978ab3a670bf8123e25ca1b");
        System.out.println(pwd);
    }
}
