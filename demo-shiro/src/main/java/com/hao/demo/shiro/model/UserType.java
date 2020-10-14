package com.hao.demo.shiro.model;

/**
 * 用户类型
 */
public enum UserType {

    ADMIN("adminUser"),
    CLIENT("clientUser"),
    APP("appUser"),
    BROKER("brokerUser");

    private String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
