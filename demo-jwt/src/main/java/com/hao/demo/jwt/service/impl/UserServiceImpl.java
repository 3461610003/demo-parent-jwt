package com.hao.demo.jwt.service.impl;

import com.hao.demo.jwt.model.User;
import com.hao.demo.jwt.service.UserService;
import org.springframework.stereotype.Service;

import java.util.*;

/*

 */
@Service
public class UserServiceImpl implements UserService {

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
