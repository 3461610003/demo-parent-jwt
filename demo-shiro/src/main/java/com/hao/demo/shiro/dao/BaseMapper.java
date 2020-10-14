package com.hao.demo.shiro.dao;

import com.hao.demo.shiro.model.BaseEntity;
import com.hao.demo.shiro.util.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 基础Mapper
 */
public interface BaseMapper {
    /**
     *
     * 新增
     */
    <T extends BaseEntity> int insert(T t);

    /**
     * 更新
     */
    <T extends BaseEntity> int updateByPrimaryKey(T t);

    /**
     * 根据主键删除
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 根据主键查询
     */
    <T extends BaseEntity> T selectByPrimaryKey(Long id);

    /**
     * 根据条件查询列表
     */
    <T extends BaseEntity> List<T> selectList(Map<String, Object> param);

    /**
     * 根据条件查询总条数
     */
    Long count(@Param("condition") Map param);

    /**
     * 根据条件分页查询
     */
    <T extends BaseEntity> List<T> selectPage(PageUtils pageInfo);

    /**
     * 动态新增
     */
    <T extends BaseEntity> int insertSelective(T t);

    /**
     * 动态修改
     */
    <T extends BaseEntity> int updateByPrimaryKeySelective(T t);
}
