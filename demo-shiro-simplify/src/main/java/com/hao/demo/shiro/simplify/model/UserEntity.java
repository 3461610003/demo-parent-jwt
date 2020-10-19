package com.hao.demo.shiro.simplify.model;

import com.hao.demo.shiro.simplify.utils.IdCardUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户Entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    private Long id;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否记住交易密码 1=记住 0=不记住
     */
    private Integer isRemember;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 账号状态 1=可用 0=禁用
     */
    private Integer status;

    /**
     * 登陆地ip
     */
    private String loginIp;

    public UserEntity(Long id, String phone, String email, String password, String nickname) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.idCard = IdCardUtil.getIdNo();
        this.status = 1;
    }
}