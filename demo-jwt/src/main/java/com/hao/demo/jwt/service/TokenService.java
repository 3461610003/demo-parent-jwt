package com.hao.demo.jwt.service;

import com.hao.demo.jwt.model.User;

/*

 */
public interface TokenService {

    String getToken(User user);
}
