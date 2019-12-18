package com.hao.demo.jwt.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hao.demo.jwt.model.User;
import com.hao.demo.jwt.service.TokenService;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @Author zhenghao
 * @Date 2019/12/18 16:54
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public String getToken(User user) {
        String token="";
        token= JWT.create().withAudience(user.getId().toString())   // 将 user id 保存到 token 里面
                .sign(Algorithm.HMAC256(user.getPassword()));       // 以 password 作为 token 的密钥
        return token;
    }
}
