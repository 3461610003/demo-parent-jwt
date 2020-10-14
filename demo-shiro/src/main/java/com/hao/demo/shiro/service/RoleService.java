package com.hao.demo.shiro.service;

import java.util.Set;

/**
 * <p>
 * RoleService
 * </p>
 *
 * @author zhenghao
 * @date 2020/10/12 17:48
 */
public interface RoleService {

    Set<String> findRoleByUserId(Long uid);
}
