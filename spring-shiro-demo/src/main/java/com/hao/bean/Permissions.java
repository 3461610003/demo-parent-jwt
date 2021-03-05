package com.hao.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 权限
 */
@Data
@AllArgsConstructor
public class Permissions {
    private String id;
    private String permissionsName;
}
