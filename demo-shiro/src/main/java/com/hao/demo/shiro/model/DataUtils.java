package com.hao.demo.shiro.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 生成数据
 * </p>
 *
 * @author zhenghao
 * @date 2020/10/13 15:35
 */
public class DataUtils {
    private static final List<AdminEntity> adminList = Arrays.asList(
                new AdminEntity(1L, "admin", "admin", "admin", "123456"),
                new AdminEntity(2L, "user", "user22", "user33", "10086"),
                new AdminEntity(3L, "zhangsan", "zhangsan2", "zhangsan3", "10010"),
                new AdminEntity(4L, "lisi", "lisi", "lisi", "12306"),
                new AdminEntity(5L, "wangwu", "wangwu", "wangwu", "13333333333"),
                new AdminEntity(6L, "zhaoliu", "赵六", "zhaoliu", "13333333334"),
                new AdminEntity(7L, "xiaoqi", "小七", "xiaoqi", "13333333335"),
                new AdminEntity(8L, "bage", "八哥", "bage", "18888888888"),
                new AdminEntity(9L, "jiuniu", "九牛", "jiuniu", "16666666666"),
                new AdminEntity(10L, "shiquan", "十全十美", "shiquan","17777777777"),
                new AdminEntity(11L, "zhenghao", "anchorite", "123456", "15090508234")
        );

    private static final List<RoleMenuDto> roleMenuList = Arrays.asList(
            new RoleMenuDto(1L, "超级管理员", "GET=/test"),
//            new RoleMenuDto(1L, "超级管理员", "/**"),
//            new RoleMenuDto(2L, "管理员", "/**"),
//            new RoleMenuDto(3L, "管理员", "/**"),
//            new RoleMenuDto(4L, "管理员", "/**"),
//            new RoleMenuDto(5L, "管理员", "/**"),
//            new RoleMenuDto(6L, "客服", "/**"),
//            new RoleMenuDto(7L, "客服", "/**"),
//            new RoleMenuDto(8L, "客服", "/**"),
            new RoleMenuDto(9L, "客服", "GET=/test"),
            new RoleMenuDto(10L, "客服", "GET=/hello"),
            new RoleMenuDto(11L, "客服", "GET=/test")
    );

    public static List<AdminEntity> getAllAdmin() {
        return adminList;
    }
    public static List<RoleMenuDto> getAllRoleMenu() {
        return roleMenuList;
    }

    public static AdminEntity selectAdminById(Long id) {
        for (AdminEntity adminEntity : adminList) {
            if (adminEntity.getId().equals(id)) {
                return adminEntity;
            }
        }
        return null;
    }

    public static AdminEntity selectAdminByName(String userName) {
        for (AdminEntity adminEntity : adminList) {
            if (adminEntity.getUserName().equals(userName)) {
                return adminEntity;
            }
        }
        return null;
    }

    public static Set<String> selectRoleById(Long id) {
        return roleMenuList.stream().filter(e -> e.getId().equals(id)).map(RoleMenuDto::getRole).collect(Collectors.toSet());
    }
    public static Set<String> selectMenuById(Long id) {
        return roleMenuList.stream().filter(e -> e.getId().equals(id)).map(RoleMenuDto::getUrl).collect(Collectors.toSet());
    }


}
