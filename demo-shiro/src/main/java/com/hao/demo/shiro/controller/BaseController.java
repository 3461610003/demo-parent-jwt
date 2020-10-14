package com.hao.demo.shiro.controller;

import com.hao.demo.shiro.util.RedisUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 项目基础Controller
 */
public class BaseController {
    /**
     * <p>
     * 防止重复提交表单
     * </p>
     * @return 返回null表示锁被占用，返回repeatKey表示通过
     *
     */
    protected String repeatForm(HttpServletRequest request, Long userId, RedisUtil redisUtil) {
        String repeatKey = "repeat:" + userId + request.getRequestURI();
        return redisUtil.setIfAbsent(repeatKey, "1", 15) ? repeatKey : null;
    }

    /**
     * 防止重复提交表单
     * @return 返回null表示锁被占用，返回repeatKey表示通过
     */
    protected String repeatForm(HttpServletRequest request, String userName, RedisUtil redisUtil) {
        String repeatKey = "repeat:" + userName + request.getRequestURI();
        return redisUtil.setIfAbsent(repeatKey, "1", 15) ? repeatKey : null;
    }
}
