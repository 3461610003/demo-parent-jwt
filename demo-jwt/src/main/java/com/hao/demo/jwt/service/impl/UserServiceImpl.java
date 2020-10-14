package com.hao.demo.jwt.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hao.demo.jwt.model.User;
import com.hao.demo.jwt.service.UserService;
import org.springframework.stereotype.Service;

import java.util.*;

/*

 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public String getToken(User user) {
        Calendar now = Calendar.getInstance();
        Date nowTime = now.getTime();
        now.add(Calendar.SECOND, 60);   // 暂设1分钟过期
        return JWT.create().withAudience(user.getId().toString())   // 将 user id 保存到 token 里面
                .withIssuedAt(nowTime)
                .withExpiresAt(now.getTime())
                .sign(Algorithm.HMAC256(user.getPassword()));       // 以 password 作为 token 的密钥
    }

    private static List<User> db = Arrays.asList(
            new User(123L, "hello", "xx123456"),
            new User(456L, "test", "test"),
            new User(789L, "hello", "hello"),
            new User(9999999L, "admin", "admin"),
            new User(1L, "zhangsan", "ts123456"),
            new User(2L, "lisi", "lsabcde"),
            new User(3L, "wangwu", "wangwu")
    );

    @Override
    public User findUserById(Long userId) {
        for (User user : db) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findByUsername(String userName, String password) {
        for (User u : db) {
            if (u.getUsername().equals(userName) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}
