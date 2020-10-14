package com.hao.demo.shiro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 管理员Entity
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminEntity extends BaseEntity {

    private static final long serialVersionUID = 6846419173999060443L;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码盐
     */
    private String salt;

    /**
     * 操作密码
     */
    private String operationPassword;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 1=可用  0=禁用
     */
    private Integer status;

    /**
     * 登陆限制次数 默认0=不限次数
     */
    private Integer loginLimits;

    /**
     * 已登陆次数
     */
    private Integer loginTimes;

    /**
     * 登陆时间
     */
    private Date loginTime;

    public AdminEntity(Long id, String userName, String nickname, String password, String phone) {
        setId(id);
        this.userName = userName;
        this.nickname = nickname;
        this.password = password;
        this.phone = phone;
        this.loginLimits = this.loginTimes = 0;
        this.email = userName + "@qq.com";
    }
}