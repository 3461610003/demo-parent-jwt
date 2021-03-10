package com.hao.service.impl;

import com.hao.bean.Permissions;
import com.hao.bean.Role;
import com.hao.bean.User;
import com.hao.service.LoginService;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Set<Permissions> permissionsSet = new HashSet<>();
    private static final Map<String, User> userMap = new HashMap<>();

    static {
        Permissions permissions1 = new Permissions("1", "query");
        Permissions permissions2 = new Permissions("2", "add");
        Permissions permissions3 = new Permissions("3", "edit");
        permissionsSet.add(permissions1);
        permissionsSet.add(permissions2);
        permissionsSet.add(permissions3);
        Role role = new Role("1", "admin", permissionsSet);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        User user = new User("1", "admin", "admin", roleSet);
        userMap.put(user.getUserName(), user);
        userMap.put("admin2", new User("2", "admin2", "admin2", roleSet));

        // 用户只有查询和修改权限
        Set<Permissions> permissionsSet1 = new HashSet<>();
        permissionsSet1.add(permissions1);
        permissionsSet1.add(permissions3);
        Role role1 = new Role("2", "user", permissionsSet1);
        Set<Role> roleSet1 = new HashSet<>();
        roleSet1.add(role1);
        User user1 = new User("3", "zhangsan", "123456", roleSet1);
        userMap.put(user1.getUserName(), user1);
    }


    /**
     * 模拟数据库查询
     *
     * @param userName 用户名
     * @return User
     */
    @Override
    public User getUserByName(String userName) {
        return userMap.get(userName);
    }

    @Override
    public void error() {
        throw new AuthorizationException("error ...");
    }

}
