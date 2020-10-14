package com.hao.demo.shiro.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hao.demo.shiro.model.UserType;

import java.util.Date;
import java.util.UUID;

/**
 * jwt工具类
 */
public class JWTUtil {
    // 过期时间30分钟
    private static final long EXPIRE_TIME = 30 * 60 * 1000;
    // 密码
    private static final String SECRET = "alicms+2018";

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token, String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
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
            return jwt.getClaim("userId").asLong();
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
            return jwt.getClaim("loginType").asString();
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
            return jwt.getClaim("salt").asString();
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
            return jwt.getClaim("sessionId").asString();
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
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        String sessionKey;
        String loginType;
        if (type.equals(UserType.CLIENT.getValue())) {
            sessionKey = "shiro:session:pc:";
            loginType = UserType.CLIENT.getValue();
        } else if (type.equals(UserType.APP.getValue())) {
            sessionKey = "shiro:session:app:";
            loginType = UserType.APP.getValue();
        } else if (type.equals(UserType.BROKER.getValue())) {
            sessionKey = "shiro:session:broker:";
            loginType = UserType.BROKER.getValue();
        } else {
            sessionKey = "shiro:session:manager:";
            loginType = UserType.ADMIN.getValue();
        }
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withClaim("loginType", loginType)
                .withClaim("sessionId", sessionKey + UUID.randomUUID().toString())
                .withClaim("userId", userId)
                .withClaim("salt", salt)
                .withExpiresAt(date)
                .sign(algorithm);
    }

//
//    public static String delay(String token, String salt) {
//        Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
//        Algorithm algorithm = Algorithm.HMAC256(SECRET);
//        // 附带username信息
//        return JWT.create()
//                .withClaim("username", getUsername(token))
//                .withClaim("loginType", getLoginType(token))
//                .withClaim("sessionId", getSessionId(token))
//                .withClaim("userId",getUid(token))
//                .withClaim("salt",salt)
//                .withExpiresAt(date)
//                .sign(algorithm);
//    }

    public static String getTokenKey(String token) {
        return "jwt:" + getLoginType(token) + ":" + getUid(token);
    }
}
