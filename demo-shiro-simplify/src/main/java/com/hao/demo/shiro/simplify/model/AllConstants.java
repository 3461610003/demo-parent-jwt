package com.hao.demo.shiro.simplify.model;

import java.util.UUID;

/**
 * <p>
 * AllConstants
 * </p>
 *
 * @author zhenghao
 * @date 2020/10/14 16:21
 */
public class AllConstants {

    // 获取jwt key/ token key，存储session信息(uuid+ip)
    public static String getJwtKey(String userType, Long userId) {
        return "login:jwt:" + userType + ":" + userId;
    }

    // sessionId key，用于存储用户信息
    public static String getSessionId(String userType) {
        return "login:shiro:session:" + userType + ":" + UUID.randomUUID().toString();
    }

    // 异地登录标记key
    public static String getRepeatKey(String tokenKey) {
        return tokenKey + ":repeat";
    }

}
