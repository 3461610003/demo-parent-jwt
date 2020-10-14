package com.hao.demo.shiro.util;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
  * 用户工具类 - 存储session
*/
@Component
public class UserUtils {

	private static RedisUtil redisUtil;

	private static final Integer EXPIRE_TIME = 30*60;

	private static final String TOKEN = "Token";
	
	public static String getToken(HttpServletRequest request){
		return request.getHeader(TOKEN);
	}

    public static Long getCurrentUserId(HttpServletRequest request){
        return JWTUtil.getUid(getToken(request));
    }

	public static Object getCurrentUser(String token){
    	return redisUtil.get(JWTUtil.getSessionId(token));
	}

	public static Object getCurrentUser(HttpServletRequest request){
		return redisUtil.get(JWTUtil.getSessionId(getToken(request)));
	}
	
	public static void setCurrentUser(String sessionId, Object currentUser){
		redisUtil.set(sessionId, currentUser, EXPIRE_TIME);
	}

	public static void setCurrentUser(String sessionId, Object currentUser, Integer expireTime){
		redisUtil.set(sessionId, currentUser, expireTime);
	}
	
	public static void clearCachedAuthorizationInfo(HttpServletRequest request) {
		String sessionId = JWTUtil.getSessionId(getToken(request));
		redisUtil.del(sessionId);
	}

	@Resource
	public  void setRedisUtil(RedisUtil redisUtil) {
		UserUtils.redisUtil = redisUtil;
	}
}
