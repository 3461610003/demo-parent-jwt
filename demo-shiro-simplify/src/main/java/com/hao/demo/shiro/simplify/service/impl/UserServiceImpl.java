package com.hao.demo.shiro.simplify.service.impl;

import com.hao.demo.shiro.simplify.model.UserEntity;
import com.hao.demo.shiro.simplify.model.UserLoginDto;
import com.hao.demo.shiro.simplify.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * UserServiceImpl
 * </p>
 *
 * @author zhenghao
 * @date 2020/10/14 14:33
 */
@Service
public class UserServiceImpl implements UserService {
    private static List<UserEntity> list = new ArrayList<>();
    static {
        list.add(new UserEntity(1000L, "15090508234", "3461610003@qq.com", "111222", "anchorite"));
        list.add(new UserEntity(1001L, "15555555555", "15555555555@139.com", "123456", "1001L"));
        list.add(new UserEntity(1002L, "16666666666", "16666666666@139.com", "112233", "1002L"));
        list.add(new UserEntity(1003L, "17777777777", "17777777777@139.com", "223344", "1003L"));
        list.add(new UserEntity(1004L, "18888888888", "18888888888@139.com", "666666", "1004L"));
    }

    @Override
    public UserEntity findByMobileOrEmail(String username) {
        for (UserEntity userEntity : list) {
            if (userEntity.getPhone().equals(username) || userEntity.getEmail().equals(username)) {
                return userEntity;
            }
        }
        return null;
    }

    @Override
    public void reSetPassword(String userName, String password) {
        for (UserEntity userEntity : list) {
            if (userEntity.getPhone().equals(userName) || userEntity.getEmail().equals(userName)) {
                userEntity.setPassword(password);
            }
        }
    }

    @Override
    public void register(UserLoginDto userLoginDto) {
        Long maxId = list.stream().map(UserEntity::getId).max(Long::compareTo).orElse(1000L) + 1;
        list.add(new UserEntity(maxId, userLoginDto.getUserName(), userLoginDto.getUserName() + "@139.com",
                userLoginDto.getPassword(), "新用户"));
    }
}
