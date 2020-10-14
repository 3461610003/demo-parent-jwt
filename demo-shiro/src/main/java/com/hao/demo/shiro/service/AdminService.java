package com.hao.demo.shiro.service;

import com.hao.demo.shiro.model.AdminEntity;

/**
 * 管理员用户Service
 */
public interface AdminService extends BaseService {
    /**
     * 用户名查重
     */
    AdminEntity findByUserName(String userName);
}
