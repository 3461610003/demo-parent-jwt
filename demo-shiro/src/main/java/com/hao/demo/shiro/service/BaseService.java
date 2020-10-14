package com.hao.demo.shiro.service;

import com.hao.demo.shiro.model.BaseEntity;
import com.hao.demo.shiro.util.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 项目基础Service
 */
public interface BaseService {

    /**
     * 新增
     */
    <T extends BaseEntity> boolean add(T t);


    /**
     * 根据主键删除
     */
    boolean remove(Long id);

    /**
     * 修改
     */
    <T extends BaseEntity> boolean modify(T t);


    /**
     * 根据主键查询
     */
    <T extends BaseEntity> T find(Long id);


    /**
     * 列表查询
     */
    <T extends BaseEntity> List<T> findList(Map<String, Object> param);


    /**
     * 动态新增
     */
    <T extends BaseEntity> boolean addSelective(T t);

    /**
     * 动态修改
     */
    <T extends BaseEntity> boolean modifySelective(T t);

    /**
     * 条件分页查询+类型转换
     */
    <T extends BaseEntity> void findPage(PageUtils<T> pageInfo, Class<T> clazz);

    /**
     * 条件分页查询
     */
    <T extends BaseEntity> void findPage(PageUtils<T> pageInfo);
}
