package com.hao.demo.shiro.simplify.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型
 */
@Getter
@AllArgsConstructor
public enum UserType {
    // pc端
    CLIENT("clientUser"),
    // app端
    APP("appUser");

    private String value;
}
