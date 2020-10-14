package com.hao.demo.shiro.service.impl;

import com.hao.demo.shiro.model.DataUtils;
import com.hao.demo.shiro.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * RoleServiceImpl
 * </p>
 *
 * @author zhenghao
 * @date 2020/10/13 14:16
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Override
    public Set<String> findRoleByUserId(Long uid) {
        return DataUtils.selectRoleById(uid);
    }
}
