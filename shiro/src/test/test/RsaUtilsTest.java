import cn.mpy.shiro.entity.Payload;
import cn.mpy.shiro.entity.UserBean;
import cn.mpy.shiro.util.JwtUtils;
import cn.mpy.shiro.util.RsaUtils;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;


public class RsaUtilsTest {

    /**保存公钥的位置*/
    private String pubkeyPath = "E:\\rsa_key.pub";

    /**保存私钥的位置*/
    private String prikeyPath = "E:\\rsa_key";

    /**生成私钥和公钥*/
    @Test
    public void testGenerateKey() throws Exception {
        //参数一：给公钥指定路径和名称，参数二：给私钥指定路径和名称，参数三：盐，参数四：密钥大小
        RsaUtils.generateKey(pubkeyPath, prikeyPath, String.valueOf(UUID.randomUUID()), 2048);
    }

    /**
     * 获取公钥和私钥
     */
    @Test
    public void getKey() throws Exception {
        //获取公钥
        PublicKey publicKey = RsaUtils.getPublicKey(pubkeyPath);
        System.out.println("publicKey = " + publicKey);
        //获取私钥
        PrivateKey privateKey = RsaUtils.getPrivateKey(prikeyPath);
        //只能打印该对象的地址，不会输出私钥内容
        System.out.println("privateKey = " + privateKey);
    }

    @Test
    public void generateToken() throws Exception {
        UserBean userInfo = new UserBean("smith", "admin", "user");
        PrivateKey privateKey = RsaUtils.getPrivateKey(prikeyPath);
        //生成token
        String token = JwtUtils.generateTokenExpireInMinutes(userInfo, privateKey);
        System.out.println("token = " + token);
    }

    @Test
    public void parseToken() throws Exception {
        //上面生成的
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJ1c2VyIjoie1widXNlcm5hbWVcIjpcInNtaXRoXCIsXCJwYXNzd29yZFwiOlwiYWRtaW5cIixcInNhbHRcIjpudWxsLFwicm9sZVwiOlwidXNlclwiLFwicGVybWlzc2lvblwiOm51bGwsXCJyb2xlc1wiOm51bGwsXCJwZXJtaXNzaW9uc1wiOm51bGx9IiwianRpIjoiT0RVNU16RXlaRFl0WWpCbE5TMDBORFEwTFdJeVpEVXRZelk0TmpJNE9XSTBPV0U0IiwiZXhwIjoxNjI2MTQ4MzgzfQ.Wrwl4LZnjqzEuBX9vEsVU-IPM33d-Ga6DkCBUhxjNXLLhFoGPtfW-bsQ6FZGoLDpEubRIXYL4S94YW9gGsg6p7p_gx_tL8ZFSwUpBfyz2UQSHAm8fIeUfyIolbJFqGrhuphE0JcfgMDWYlNu4w6XQlkJCdWNOH3pc8PJLyuGLVGQrwZkMRsFM-ukpoSk-eBzbWDcjQoIYoEHW5_ENHL5sOgRDFAztPP1zUBHj5pULX_DMmxhsDE82hZf47jVzzIwuAeURKM-AQPmp09jyQOaKlT67bpzBRWbDaEN3zffb3ot23bKMtmeYNSDsUfdzhsZmNBKDNc16kpE_r_DuxThag";
        //公钥
        PublicKey publicKey = RsaUtils.getPublicKey(pubkeyPath);
        //得到载荷对象
        Payload<UserBean> infoFromToken = JwtUtils.getInfoFromToken(token, publicKey, UserBean.class);
        //得到载荷对象中的用户信息
        UserBean userInfo = infoFromToken.getUserInfo();
        System.out.println(userInfo.getUsername());
        System.out.println(userInfo.getPassword());
        System.out.println(userInfo.getRole());
        System.out.println(userInfo.getPermission());
        System.out.println(infoFromToken.getId());
        System.out.println(infoFromToken.getExpiration());
    }
}
