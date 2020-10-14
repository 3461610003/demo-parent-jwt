package com.hao.demo.shiro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * RoleMenuDto
 * </p>
 *
 * @author zhenghao
 * @date 2020/10/13 16:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoleMenuDto {
    private Long id;
    private String role;
    private String url;
}
