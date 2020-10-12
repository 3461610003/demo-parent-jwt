package com.hao.demojjwt;

import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: JJWT测试
 * @Author zhenghao
 * @Date 2019/12/18 14:56
 */
public class JJWTTest {
    public static void main(String[] args) {
        String token = getJwtToken("456465", 132L);
        System.out.println("token:" + token);

        Jws<Claims> claimsJws = verifyJwt(token);
        System.out.println("claims:" + claimsJws);
        System.out.println("signature:" + claimsJws.getSignature());
        System.out.println("header:" + claimsJws.getHeader());
        System.out.println("Body:" + claimsJws.getBody());
    }


    // token 过期时间, 单位: 秒. 这个值表示 30 天
    private static final long TOKEN_EXPIRED_TIME = 30 * 24 * 60 * 60;
    // jwt 加密解密密钥
    private static final String JWT_SECRET = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";

    /**
     * 加密
     * 根据userId和openid生成token
     */
    public static String getJwtToken(String openId, Long userId) {
        /*Claims claims = Jwts.claims();
        claims.put("name","cy");
        claims.put("userId", userId);
        claims.setAudience("cy");
        claims.setIssuer("cy");*/

        Map<String, Object> map = new HashMap<>();
        map.put("openId", openId);
        map.put("userId", userId);

        Date now = new Date();
        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(map)             // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setId("tokenId")           // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setAudience("PC")          // aud: 接收jwt的一方
                .setIssuer("user_id")       // iss: jwt签发者
//                .setSubject("")           // sub: jwt所面向的用户
//                .setNotBefore(now)          // nbf: 定义在什么时间之前，该jwt都是不可用的.
                .setIssuedAt(now)           // iat: jwt的签发时间
                .setExpiration(new Date(now.getTime() + TOKEN_EXPIRED_TIME))    //设置过期时间
                .signWith(SignatureAlgorithm.HS256, new SecretKeySpec(Base64.decodeBase64(JWT_SECRET), "AES"));     //设置签名使用的签名算法和签名使用的秘钥，此处是header那部分，jjwt已经将这部分内容封装好了。
        return builder.compact();
    }


    /**
     * 解析jwt
     */
    public static Jws<Claims> verifyJwt(String token) {
        return Jwts.parser()        //得到DefaultJwtParser
                .setSigningKey(new SecretKeySpec(Base64.decodeBase64(JWT_SECRET), "AES")) //设置签名的秘钥
                .parseClaimsJws(token);

    }

}
