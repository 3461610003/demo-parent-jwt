package com.hao.demo.jwt.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hao.demo.jwt.model.User;
import com.hao.demo.jwt.service.TokenService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * @Description: token
 * @Author zhenghao
 * @Date 2019/12/18 16:54
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public String getToken(User user) {
        Calendar now = Calendar.getInstance();
        Date nowTime = now.getTime();
        now.add(Calendar.SECOND, 10);
        return JWT.create().withAudience(user.getId().toString())   // 将 user id 保存到 token 里面
                .withIssuedAt(nowTime)
                .withExpiresAt(now.getTime())
                .sign(Algorithm.HMAC256(user.getPassword()));       // 以 password 作为 token 的密钥
    }
}
