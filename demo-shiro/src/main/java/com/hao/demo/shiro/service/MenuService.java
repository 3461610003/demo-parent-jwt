package com.hao.demo.shiro.service;

import java.util.Set;

/**
 * <p>
 * MenuService
 * </p>
 *
 * @author zhenghao
 * @date 2020/10/12 17:48
 */
public interface MenuService {

    Set<String> findMenuUrlByUserId(Long uid);
}
