package com.hao.demo.shiro.service.impl;

import com.hao.demo.shiro.dao.BaseMapper;
import com.hao.demo.shiro.model.BaseEntity;
import com.hao.demo.shiro.service.BaseService;
import com.hao.demo.shiro.util.EntityToDtoUtil;
import com.hao.demo.shiro.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目基础ServiceImpl
 */
@Slf4j
public abstract class BaseServiceImpl implements BaseService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract BaseMapper getBaseMapper();

    @Override
    @Transactional
    public <T extends BaseEntity> boolean add(T t) {
        return getBaseMapper().insert(t) > 0;
    }

    @Override
    @Transactional
    public boolean remove(Long id) {
        return getBaseMapper().deleteByPrimaryKey(id) > 0;
    }

    @Override
    @Transactional
    public <T extends BaseEntity> boolean modify(T t) {
        return getBaseMapper().updateByPrimaryKey(t) > 0;
    }

    @Override
    public <T extends BaseEntity> T find(Long id) {
        return getBaseMapper().selectByPrimaryKey(id);
    }

    @Override
    public <T extends BaseEntity> List<T> findList(Map<String, Object> param) {
        return getBaseMapper().selectList(param);
    }

    @Override
    @Transactional
    public <T extends BaseEntity> boolean addSelective(T t) {
        return getBaseMapper().insertSelective(t) > 0;
    }

    @Override
    @Transactional
    public <T extends BaseEntity> boolean modifySelective(T t) {
        return getBaseMapper().updateByPrimaryKeySelective(t) > 0;
    }

    @Override
    public <T extends BaseEntity> void findPage(PageUtils<T> pageInfo, Class<T> clazz) {
        long total = getBaseMapper().count(pageInfo.getCondition());
        pageInfo.setTotal(total);
        if (total > 0) {
            pageInfo.setData(EntityToDtoUtil.copyList(getBaseMapper().selectPage(pageInfo), clazz));
        } else {
            pageInfo.setData(new ArrayList<>());
        }
    }

    @Override
    public <T extends BaseEntity> void findPage(PageUtils<T> pageInfo) {
        long total = getBaseMapper().count(pageInfo.getCondition());
        pageInfo.setTotal(total);
        if (total > 0) {
            pageInfo.setData(getBaseMapper().selectPage(pageInfo));
        } else {
            pageInfo.setData(new ArrayList<>());
        }
    }
}
