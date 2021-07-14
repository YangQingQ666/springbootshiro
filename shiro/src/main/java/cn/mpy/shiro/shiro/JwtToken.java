package cn.mpy.shiro.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * jwt生成token
 * @author mpy
 */
public class JwtToken implements AuthenticationToken {

    /**密钥*/
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
