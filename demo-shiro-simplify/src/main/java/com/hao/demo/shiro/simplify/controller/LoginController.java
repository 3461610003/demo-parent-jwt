package com.hao.demo.shiro.simplify.controller;

import com.alibaba.fastjson.JSON;
import com.hao.demo.shiro.simplify.model.*;
import com.hao.demo.shiro.simplify.service.UserService;
import com.hao.demo.shiro.simplify.utils.CustomUUID;
import com.hao.demo.shiro.simplify.utils.IpUtil;
import com.hao.demo.shiro.simplify.utils.JWTUtil;
import com.hao.demo.shiro.simplify.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

/**
 * 登陆Controller
 */
@Slf4j
@RestController
@RequestMapping
public class LoginController {

    @Resource
    private UserService userService;

    @Resource
    private RedisUtil redisUtil;

    // 用户注册
    @PostMapping(value = "/register")
    public Result register(HttpServletRequest request, HttpServletResponse response, @RequestBody UserLoginDto userLoginDto) {
        if (StringUtils.isEmpty(userLoginDto.getUserName()) || StringUtils.isEmpty(userLoginDto.getPassword())) {
            return Result.result(null, 401, "参数有误");
        }
        //验证手机或邮箱是否已存在
        UserEntity user = userService.findByMobileOrEmail(userLoginDto.getUserName());
        if (user != null) {
            return Result.result(null, 500, "用户已存在");
        }
        userService.register(userLoginDto);
        String userType;
        if (userLoginDto.getClient() == null || userLoginDto.getClient() == 1){
            userType = UserType.CLIENT.getValue();
        }else {
            userType = UserType.APP.getValue();
        }
        return login(request, response, userLoginDto, userType);
    }

    @PostMapping(value = "/login")
    public Result pcLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody UserLoginDto userLoginDto) {
        return login(request, response, userLoginDto, UserType.CLIENT.getValue());
    }


    @PostMapping(value = "/app-login")
    public Result appLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody UserLoginDto userLoginDto) {
        return login(request, response, userLoginDto, UserType.APP.getValue());
    }

    private Result login(HttpServletRequest request, HttpServletResponse response, UserLoginDto userLoginDto, String userType) {
        String ip = IpUtil.getIpAdrress(request);
        String userName = userLoginDto.getUserName();
        UserEntity userEntity = userService.findByMobileOrEmail(userLoginDto.getUserName());
        if (userEntity == null || !userEntity.getPassword().equals(userLoginDto.getPassword())) {
            return Result.result(null, 500, "用户名或密码错误");
        }
        Long userId = userEntity.getId();
        String token;
        //登陆前先清除之前token信息
        String jwt = request.getHeader("token");
        if (!StringUtils.isEmpty(jwt)) {
            redisUtil.del(JWTUtil.getSessionId(jwt), JWTUtil.getTokenKey(jwt));
        }
        //token 唯一标识:唯一码+ip
        String salt = CustomUUID.getUUID() + "_" + ip;
        token = JWTUtil.sign(userName, userType, salt, userId);
        //设置jwt标记
        String tokenKey = AllConstants.getJwtKey(userType, userId);
        String tokenTemp = (String) redisUtil.get(tokenKey);
        if (StringUtils.isNotEmpty(tokenTemp)) {
            // 异地登录的情况
            if (!ip.equals(getTokenIp(tokenTemp))) {
                if (userType.equals(UserType.APP.getValue())) {
                    redisUtil.set(AllConstants.getRepeatKey(tokenKey), 1, 7 * 24 * 3600);
                } else {
                    redisUtil.set(AllConstants.getRepeatKey(tokenKey), 1, 14400);
                }
            }
        }
        if (userType.equals(UserType.APP.getValue())) {
            redisUtil.set(tokenKey, salt, 7 * 24 * 3600);
        } else {
            redisUtil.set(tokenKey, salt, 14400);
        }
        log.info("用户登录信息: {},ip: {}", userName, ip);

        response.setHeader("Token", token);
        response.setHeader("Access-Control-Expose-Headers","Token");
        return Result.result(null, 200, "登录成功");
    }


    /**
     * 重置登录密码
     */
    @PostMapping(value = "/forgot-password")
    public Result resetPassword(@RequestBody PasswordDto passwordDto) {
        String userName = passwordDto.getUserName();
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(passwordDto.getPassword())) {
            return Result.result(null, 401, "缺少必填项");
        }
        //校验密码正则
        if (!Pattern.matches("[a-zA-Z0-9]*", passwordDto.getRePassword())) {
            return Result.result(null, 500, "密码格式不对");
        }
        UserEntity userEntity = userService.findByMobileOrEmail(userName);
        if (userEntity == null) {
            return Result.result(null, 500, "用户不存在");
        } else if (!userEntity.getPassword().equals(passwordDto.getPassword())) {
            return Result.result(null, 500, "原密码有误");
        }

        // 删除jwt
        redisUtil.del(AllConstants.getJwtKey(UserType.CLIENT.getValue(), userEntity.getId()));
        redisUtil.del(AllConstants.getJwtKey(UserType.APP.getValue(), userEntity.getId()));
        //重置密码
        userService.reSetPassword(userName, passwordDto.getPassword());
        return Result.result(null, 200, "重置成功");
    }

    /**
     * 截取ip
     */
    private String getTokenIp(String token) {
        return token.substring(token.lastIndexOf("_") + 1);
    }

    /**
     * 客户端用户登出
     */
    @GetMapping(value = "/logout")
    public Result logout() {
        log.info("===用户退出===");
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipals() != null) {
            String token = (String) subject.getPrincipal();
            //清除redis用户登陆信息
            redisUtil.del(JWTUtil.getSessionId(token), JWTUtil.getTokenKey(token));
        }
        subject.logout();
        return Result.result(subject.getPrincipal(), 200, "退出成功");
    }

    @GetMapping(value = "/current")
    public Result<UserEntity> current(HttpServletRequest request) {
        String sessionId = JWTUtil.getSessionId(request.getHeader("token"));
        Object o = redisUtil.get(sessionId);
        return Result.result(JSON.parseObject(o.toString(), UserEntity.class), 200, "查询成功");
    }

    @GetMapping(value = "/test")
    public Result test() {
        log.info("===测试接口===");
        return Result.result(null, 200, "测试接口联通。。。。");
    }

    @GetMapping(value = "/hello")
    public Result hello(String hello) {
        log.info("===hello ===");
        return Result.result(null, 200, "hello hello hello ..." + hello);
    }
}
