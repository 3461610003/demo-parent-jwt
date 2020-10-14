package com.hao.demo.shiro.service.impl;

import com.hao.demo.shiro.dao.AdminMapper;
import com.hao.demo.shiro.dao.BaseMapper;
import com.hao.demo.shiro.model.AdminEntity;
import com.hao.demo.shiro.model.DataUtils;
import com.hao.demo.shiro.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * AdminServiceImpl
 * </p>
 *
 * @author zhenghao
 * @date 2020/10/13 14:01
 */
@Service
public class AdminServiceImpl extends BaseServiceImpl implements AdminService {
    @Resource
    private AdminMapper adminMapper;

    @Override
    protected BaseMapper getBaseMapper() {
        return adminMapper;
    }

    @Override
    public AdminEntity findByUserName(String userName) {
        return DataUtils.selectAdminByName(userName);
    }
}
