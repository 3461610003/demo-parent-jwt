package com.hao.demo.jwt.controller;

import com.alibaba.fastjson.JSONObject;
import com.hao.demo.jwt.model.PassToken;
import com.hao.demo.jwt.model.User;
import com.hao.demo.jwt.model.UserLoginToken;
import com.hao.demo.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 测试api
 * @Author zhenghao
 * @Date 2019/12/18 16:43
 */
@RestController
@RequestMapping("api")
public class UserApi {
    @Autowired
    private UserService userService;

    //登录
    @PostMapping("/login")
    @PassToken
    public Object login(String userName, String password) {
        JSONObject jsonObject = new JSONObject();
        User userForBase = userService.findByUsername(userName, password);
        if (userForBase == null) {
            jsonObject.put("message", "登录失败,密码错误或用户不存在");
            return jsonObject;
        } else {
            // 生成token
            String token = userService.getToken(userForBase);
            jsonObject.put("token", token);
            jsonObject.put("user", userForBase);
            return jsonObject;
        }
    }

    @UserLoginToken
    @GetMapping("/test")
    public String test() {
        return "你已通过验证";
    }
}
