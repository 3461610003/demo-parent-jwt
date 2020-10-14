package com.hao.demo.shiro.model;

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
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
