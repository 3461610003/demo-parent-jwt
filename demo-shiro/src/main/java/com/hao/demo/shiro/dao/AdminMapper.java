package com.hao.demo.shiro.dao;

import com.hao.demo.shiro.model.BaseEntity;
import com.hao.demo.shiro.model.DataUtils;
import com.hao.demo.shiro.util.PageUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * AdminMapper
 * </p>
 *
 * @author zhenghao
 * @date 2020/10/13 15:50
 */
@Component
public class AdminMapper implements BaseMapper {

    @Override
    public <T extends BaseEntity> int insert(T t) {
        return 1;
    }

    @Override
    public <T extends BaseEntity> int updateByPrimaryKey(T t) {
        return 1;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return 1;
    }

    @Override
    public <T extends BaseEntity> T selectByPrimaryKey(Long id) {
        return (T) DataUtils.selectAdminById(id);
    }

    @Override
    public <T extends BaseEntity> List<T> selectList(Map<String, Object> param) {
        return (List<T>) DataUtils.getAllAdmin();
    }

    @Override
    public Long count(Map param) {
        return (long) DataUtils.getAllAdmin().size();
    }

    @Override
    public <T extends BaseEntity> List<T> selectPage(PageUtils pageInfo) {
        return null;
    }

    @Override
    public <T extends BaseEntity> int insertSelective(T t) {
        return 1;
    }

    @Override
    public <T extends BaseEntity> int updateByPrimaryKeySelective(T t) {
        return 1;
    }
}
