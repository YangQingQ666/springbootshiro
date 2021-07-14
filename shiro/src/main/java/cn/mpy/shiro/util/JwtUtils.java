package cn.mpy.shiro.util;

import cn.mpy.shiro.entity.UserBean;
import cn.mpy.shiro.exception.UnauthorizedException;
import com.alibaba.druid.support.json.JSONUtils;
import cn.mpy.shiro.entity.Payload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.UUID;

/**
 * Jwt 工具类
 * @author mpy
 */
public class JwtUtils {

    /**jwt超时时间5分钟*/
    private static int OUT_TIME = 5;
    private static final String JWT_PAYLOAD_USER_KEY = "user";

    /**保存公钥的位置*/
    private static String pubkeyPath = "E:\\rsa_key.pub";

    /**保存私钥的位置*/
    private static String prikeyPath = "E:\\rsa_key";

    /**
     * 私钥加密token
     * @param userInfo   载荷中的数据
     * @param privateKey 私钥
     * @return JWT
     */
    public static String generateTokenExpireInMinutes(Object userInfo, PrivateKey privateKey) {
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JsonUtils.toString(userInfo))
                .setId(createJTI())
                .setExpiration(DateTime.now().plusMinutes(OUT_TIME).toDate())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 私钥加密token
     * @param userInfo   载荷中的数据
     * @param privateKey 私钥
     * @param expire     过期时间，单位秒
     * @return JWT
     */
    public static String generateTokenExpireInSeconds(Object userInfo, PrivateKey privateKey, int expire) {
        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JSONUtils.toJSONString(userInfo))
                .setId(createJTI())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public static boolean verify(String token, String username, String secret) {
        //校验token
        Payload<UserBean> payload = null;
        UserBean userInfo = null;
        try {
            PublicKey publicKey = RsaUtils.getPublicKey(pubkeyPath);
            payload = JwtUtils.getInfoFromToken(token, publicKey, UserBean.class);
            //得到载荷对象中的用户信息
            userInfo = payload.getUserInfo();
            if (userInfo!=null&&userInfo.getUsername()!=null&&userInfo.getPassword()!=null){
                if (userInfo.getUsername().equals(username)&&userInfo.getPassword().equals(secret)){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static UserBean getUserBean(String token) {
        //公钥
        PublicKey publicKey = null;
        Payload<UserBean> infoFromToken = null;
        UserBean userInfo = null;
        try {
            publicKey = RsaUtils.getPublicKey(pubkeyPath);
            //得到载荷对象
            infoFromToken = JwtUtils.getInfoFromToken(token, publicKey, UserBean.class);
            //得到载荷对象中的用户信息
            userInfo = infoFromToken.getUserInfo();
        } catch (Exception e) {
            throw new UnauthorizedException("token 过期咯111");
        }
        return userInfo;
    }

    /**
     * 公钥解析token
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return Jws<Claims>
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    private static String createJTI() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }

    /**
     * 获取token中的用户信息
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey, Class<T> userType) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setUserInfo(JsonUtils.toBean(body.get(JWT_PAYLOAD_USER_KEY).toString(), userType));
        claims.setExpiration(body.getExpiration());
        return claims;
    }

    /**
     * 获取token中的载荷信息
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setExpiration(body.getExpiration());
        return claims;
    }
}
