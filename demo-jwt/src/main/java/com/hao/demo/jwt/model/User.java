package com.hao.demo.jwt.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TODO
 * @Author zhenghao
 * @Date 2019/12/18 16:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    Long Id;
    String username;
    String password;

    public String getToken(User user) {
        String token="";
        token= JWT.create().withAudience(user.getId().toString())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}
