package com.hao.demo.jwt.service;

import com.hao.demo.jwt.model.User;

/*
主要流程:
1.从 http 请求头中取出 token，
2.判断是否映射到方法
3.检查是否有passtoken注释，有则跳过认证
4.检查有没有需要用户登录的注解，有则需要取出并验证
5.认证通过则可以访问，不通过会报相关错误信息
*/
public interface UserService {
    String getToken(User user);

    User findUserById(Long userId);

    User findByUsername(String userName, String password);
}
