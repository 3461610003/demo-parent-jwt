package com.hao.demo.shiro.simplify.model;

import lombok.Data;

/**
 * <p>
 * UserLoginDto
 * </p>
 *
 * @author zhenghao
 * @date 2020/10/14 13:39
 */
@Data
public class UserLoginDto {
    private String userName;
    private String password;
    private Integer client;
}
