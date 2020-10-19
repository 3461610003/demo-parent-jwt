package com.hao.demo.shiro.simplify.model;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * jwt token
 */
@Data
public class JWTToken implements AuthenticationToken {

    private static final long serialVersionUID = 6815700748012947312L;

    private String token;

    private String loginType;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {//身份
        return token;
    }

    @Override
    public Object getCredentials() {//凭据
        return token;
    }
}
