package com.hao.demo.jwt.test;

import java.text.SimpleDateFormat;
import java.util.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @description: JWTTest
 * @author: zhenghao
 * @date: 2019/12/18 14:58
 */
public class JWTTest {
    /**
     * APP登录Token的生成和解析
     */
    /**
     * token秘钥，请勿泄露，请勿随便修改 backups:JKKLJOoasdlfj
     */
    public static final String SECRET = "JKKLJOoasdlfj";
    /**
     * token 过期时间: 10天
     */
    public static final int EXPIRE_DAY = 10;

    /**
     * 加密： JWT生成Token.<br/>
     * JWT构成:头部（header), 载荷（payload), 签证（signature) 由三部分生成token<br/>
     * 如：token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBUFAiLCJ1c2VyX2lkIjoiMjEzIiwiaXNzIjoiU2VydmljZSIsImV4cCI6MTU3NzUxNDg1MiwiaWF0IjoxNTc2NjUwODUyfQ.ZbhYK0FVdnWV9Akiq9RX0Ms23gVLOZlAUk-79_Xp2dk  <br/>
     *
     * Jwt设置 Header 和 Payload
     * Header
     *      alg：jwt的算法值
     *      cty：内容类型
     *      typ：jwt类型
     *      kid：key的id值
     * Payload：
     *      iss：JWT的签发者，可选
     *      sub：JWT所面向的用户，可选
     *      exp：过期时间戳（必须大于iat），可选
     *      nbf：在此时间之前，该jwt都是不可用的，可选
     *      iat：签发时间，可选
     *      jti：唯一身份标识，主要用来作为一次性token,从而回避重放攻击，可选
     *      aud：接收该JWT的一方，可选
     *
     * @param user_id 登录成功后用户user_id, 参数user_id不可传空
     */
    public static String createToken(Long user_id) {
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.DATE, EXPIRE_DAY);
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new HashMap<>();
        // 声明加密的算法，通常直接使用 HMAC SHA256
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        // build token
        // param backups {iss:Service, aud:APP}

        return JWT.create().withHeader(map) // header
                .withClaim("iss", "Service") // payload 也可用withIssuer("Service")
                .withClaim("aud", "APP")    // withAudience
                .withClaim("user_id", null == user_id ? null : user_id.toString())
                .withIssuedAt(iatDate) // sign time
                .withExpiresAt(expiresDate) // expire time
                // base64UrlEncode(header) + "." + base64UrlEncode(payload) + your-256-bit-secret
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 解密Token <br/>
     * iss: jwt签发者
     * sub: jwt所面向的用户
     * aud: 接收jwt的一方
     * exp: jwt的过期时间，这个过期时间必须要大于签发时间
     * nbf: 定义在什么时间之前，该jwt都是不可用的.
     * iat: jwt的签发时间
     * jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
     *
     * @param token
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = getJwt(token);
        assert jwt != null;
        return jwt.getClaims();
    }

    public static DecodedJWT getJwt(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            // e.printStackTrace();
            // token 校验失败, 抛出Token验证非法异常
        }
        return jwt;
    }

    public static void main(String[] args) throws Exception {
        String token = createToken(213L);
        System.out.println("token:" + token);

        System.out.println("=========================================");
        Map<String, Claim> stringClaimMap = verifyToken(token);
        System.out.println("解密token:" + stringClaimMap);
        System.out.println("keys:" + stringClaimMap.keySet());
        System.out.println("aud:" + stringClaimMap.get("aud").asString());
        System.out.println("user_id:" + stringClaimMap.get("user_id").asString());
        System.out.println("iss:" + stringClaimMap.get("iss").asString());
        System.out.println("exp:" + formatDate(stringClaimMap.get("exp").asDate()));
        System.out.println("iat:" + formatDate(stringClaimMap.get("iat").asDate()));

        System.out.println("=========================================");
        DecodedJWT jwt = getJwt(token);
        System.out.println("Header:" + jwt.getHeader());
        System.out.println("Payload:" + jwt.getPayload());
        System.out.println("Alg:" + jwt.getAlgorithm());
        System.out.println("Signature:" + jwt.getSignature());
        System.out.println("token:" + jwt.getToken());
        stringClaimMap.forEach((k, v) -> System.out.println(k + ":" + (Arrays.asList("exp", "iat").contains(k) ? formatDate(v.asDate()) : v.asString())));


    }

    public static String formatDate(Date date) {
        if (date == null) date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(date);
    }

}