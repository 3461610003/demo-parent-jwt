package com.hao.demo.shiro.simplify.service;

import com.hao.demo.shiro.simplify.model.UserEntity;
import com.hao.demo.shiro.simplify.model.UserLoginDto;

/**
 * <p>
 * UserService
 * </p>
 *
 * @author zhenghao
 * @date 2020/10/14 11:08
 */
public interface UserService {

    UserEntity findByMobileOrEmail(String username);

    void reSetPassword(String userName, String password);

    void register(UserLoginDto userLoginDto);
}
