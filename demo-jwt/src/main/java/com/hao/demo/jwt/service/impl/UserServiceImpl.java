package com.hao.demo.jwt.service.impl;

import com.hao.demo.jwt.model.User;
import com.hao.demo.jwt.service.UserService;
import org.springframework.stereotype.Service;

/*

 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public User findUserById(Long userId) {
        if (123L == userId) {
            return new User(userId, "hello", "xx123456");
        }
        return null;
    }

    @Override
    public User findByUsername(User user) {
        if ("hello".equals(user.getUsername()) && "xx123456".equals(user.getPassword())) {
            return new User(123L, "hello", "xx123456");
        }
        return null;
    }
}
