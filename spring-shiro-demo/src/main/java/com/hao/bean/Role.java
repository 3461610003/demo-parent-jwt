package com.hao.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * 角色
 */
@Data
@AllArgsConstructor
public class Role {

    private String id;
    private String roleName;
    /**
     * 角色对应权限集合
     */
    private Set<Permissions> permissions;

}
