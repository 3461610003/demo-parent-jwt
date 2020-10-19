package com.hao.demo.shiro.simplify.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hao.demo.shiro.simplify.model.AllConstants;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * jwt工具类
 */
public class JWTUtil {
    // 过期时间30分钟
    private static final long EXPIRE_TIME = 30 * 60 * 1000;

    // 密码
    private static final String SECRET = "alicms+2020";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    private static final String USERNAME = "username";
    private static final String LOGIN_TYPE = "loginType";
    private static final String SESSION_ID = "sessionId";
    private static final String USER_ID = "userId";
    private static final String SALT = "salt";

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token, String username) {
        return verify(token, username, null, null, null);
    }

    public static boolean verify(String token, String username, String type, String salt, Long userId) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .withClaim(USERNAME, username)
                    .withClaim(LOGIN_TYPE, type)
                    .withClaim(USER_ID, userId)
                    .withClaim(SALT, salt)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static DecodedJWT getJwt(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            return verifier.verify(token);
        } catch (Exception exception) {
            return null;
        }
    }

    public static Date getExpiresAt(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static Map<String, String> get(String token) {
        DecodedJWT jwt = getJwt(token);
        if (jwt != null) {
            Map<String, Claim> claims = jwt.getClaims();
            if (claims != null && claims.size() > 0) {
                return claims.entrySet().stream().filter(e -> e.getValue() != null)
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().asString()));
            }
        }
        return null;
    }

    public static String get(String token, String key) {
        DecodedJWT jwt = getJwt(token);
        if (jwt != null) {
            Claim claim = jwt.getClaim(key);
            if (claim != null) {
                return claim.asString();
            }
        }
        return null;
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(USERNAME).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中登陆加盐
     */
    public static Long getUid(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(USER_ID).asLong();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中登陆用户类型
     */
    public static String getLoginType(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(LOGIN_TYPE).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中登陆加盐
     */
    public static String getSalt(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(SALT).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中登陆用户信息
     */
    public static String getSessionId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(SESSION_ID).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,30min后过期
     *
     * @param username 用户名
     * @return 加密的token
     */
    public static String sign(String username, String type, String salt, Long userId) {
        Date expiresDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        // 附带username信息
        return JWT.create()
                .withClaim(USERNAME, username)
                .withClaim(LOGIN_TYPE, type)
                .withClaim(SESSION_ID, AllConstants.getSessionId(type))
                .withClaim(USER_ID, userId)
                .withClaim(SALT, salt)
                .withExpiresAt(expiresDate)
                .sign(ALGORITHM);
    }

    public static String reSign(String token) {
        Map<String, String> map = get(token);
        if (map == null) {
            return null;
        }
        Date expiresDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        // 附带username信息
        return JWT.create()
                .withClaim(USERNAME, map.get(USERNAME))
                .withClaim(LOGIN_TYPE, map.get(LOGIN_TYPE))
                .withClaim(SESSION_ID, AllConstants.getSessionId(map.get(LOGIN_TYPE)))
                .withClaim(USER_ID, map.get(USER_ID))
                .withClaim(SALT, map.get(SALT))
                .withExpiresAt(expiresDate)
                .sign(ALGORITHM);
    }

    public static String getTokenKey(String token) {
        return AllConstants.getJwtKey(getLoginType(token), getUid(token));
    }

//    public static void main(String[] args) {
////        String sign = sign("zhangsan", "app", "abc123", 100L);
////        System.out.println(sign);
//        String sign = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzYWx0IjoiYWJjMTIzIiwibG9naW5UeXBlIjoiYXBwIiwic2Vzc2lvbklkIjoibG9naW46c2hpcm86c2Vzc2lvbjphcHA6YzQ3YmM4NzAtMGYxOS00OTMzLTk4YzEtMjQzYTAwZDgyMmFlIiwiZXhwIjoxNjAyNzUzMDY3LCJ1c2VySWQiOjEwMCwidXNlcm5hbWUiOiJ6aGFuZ3NhbiJ9._hHqBlnjaEj7yc43Syni4NzhvT99f9CFN9e8Lx0mL2s";
//        System.out.println(getUsername(sign));
//        System.out.println(verify(sign, null));
//        System.out.println(verify(sign, "zhangsan", "app", "abc123", 100L));
//    }
}
