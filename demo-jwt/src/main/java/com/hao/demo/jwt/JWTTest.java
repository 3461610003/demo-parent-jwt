package com.hao.demo.jwt;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;

public class JWTTest {
    /**
     * APP登录Token的生成和解析
     */
    /** token秘钥，请勿泄露，请勿随便修改 backups:JKKLJOoasdlfj */
    public static final String SECRET = "JKKLJOoasdlfj";
    /** token 过期时间: 10天 */
    public static final int calendarField = Calendar.DATE;
    public static final int calendarInterval = 10;

    /**
     * JWT生成Token.<br/>
     * JWT构成:头部（header), 载荷（payload), 签证（signature) 由三部分生成token<br/>
     * 如：token:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBUFAiLCJ1c2VyX2lkIjoiMjEzIiwiaXNzIjoiU2VydmljZSIsImV4cCI6MTU3NzUxNDg1MiwiaWF0IjoxNTc2NjUwODUyfQ.ZbhYK0FVdnWV9Akiq9RX0Ms23gVLOZlAUk-79_Xp2dk  <br/>
     * @param user_id 登录成功后用户user_id, 参数user_id不可传空
     */
    public static String createToken(Long user_id) throws Exception {
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new HashMap<>();
        // 声明加密的算法，通常直接使用 HMAC SHA256
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        // build token
        // param backups {iss:Service, aud:APP}
        String token = JWT.create().withHeader(map) // header
                .withClaim("iss", "Service") // payload
                .withClaim("aud", "APP")
                .withClaim("user_id", null == user_id ? null : user_id.toString())
                .withIssuedAt(iatDate) // sign time
                .withExpiresAt(expiresDate) // expire time
                // HMAC256算法签名
                .sign(Algorithm.HMAC256(SECRET)); // signature

        return token;
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
     * @param token
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            // e.printStackTrace();
            // token 校验失败, 抛出Token验证非法异常
        }
        return jwt.getClaims();
    }

    /**
     * 根据Token获取user_id
     * @param token
     * @return user_id
     */
    public static Long getAppUID(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim user_id_claim = claims.get("user_id");
        if (null == user_id_claim || StringUtils.isEmpty(user_id_claim.asString())) {
            // token 校验失败, 抛出Token验证非法异常
        }
        return Long.valueOf(user_id_claim.asString());
    }

    public static void main(String[] args) throws Exception {
        String token = createToken(213L);
        System.out.println("token:" + token);

        System.out.println("=========================================x");
        Map<String, Claim> stringClaimMap = verifyToken(token);
        System.out.println("解密token:" + stringClaimMap);
        System.out.println("keys:" + stringClaimMap.keySet());
        System.out.println("aud:" + stringClaimMap.get("aud").asString());
        System.out.println("user_id:" + stringClaimMap.get("user_id").asString());
        System.out.println("iss:" + stringClaimMap.get("iss").asString());
        System.out.println("exp:" + formatDate(stringClaimMap.get("exp").asDate()));
        System.out.println("iat:" + formatDate(stringClaimMap.get("iat").asDate()));

        System.out.println("=========================================");
        System.out.println("user_id:" + getAppUID(token));
    }

    public static String formatDate(Date date) {
        if (date == null) date = new Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(date);
    }

}