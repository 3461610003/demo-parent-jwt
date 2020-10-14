package com.hao.demo.shiro.service.impl;

import com.hao.demo.shiro.model.DataUtils;
import com.hao.demo.shiro.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * MenuServiceImpl
 * </p>
 *
 * @author zhenghao
 * @date 2020/10/13 14:15
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Override
    public Set<String> findMenuUrlByUserId(Long uid) {
        return DataUtils.selectMenuById(uid);
    }
}
