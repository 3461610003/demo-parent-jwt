package com.hao.demo.shiro.config;

import com.hao.demo.shiro.model.AdminEntity;
import com.hao.demo.shiro.model.JWTToken;
import com.hao.demo.shiro.model.UserType;
import com.hao.demo.shiro.service.AdminService;
import com.hao.demo.shiro.service.MenuService;
import com.hao.demo.shiro.service.RoleService;
import com.hao.demo.shiro.util.JWTUtil;
import com.hao.demo.shiro.util.RedisUtil;
import com.hao.demo.shiro.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 管理平台自定义shiro认证模块
 */
@Slf4j
public class AdminUserRealm extends AuthorizingRealm {
    @Resource
    private AdminService userService;
    @Resource
    private MenuService menuService;
    @Resource
    private RoleService roleService;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public String getName() {
        return UserType.ADMIN.getValue();
    }

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("444444 管理端-user 登陆授权：");
        // 获取当前用户
        String token = (String) principals.getPrimaryPrincipal();
        Long uid = JWTUtil.getUid(token);
        Set<String> roles = roleService.findRoleByUserId(uid);
        Set<String> urlSet = menuService.findMenuUrlByUserId(uid);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(urlSet);
        info.addRoles(roles);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        log.info("333333 管理端-user 登陆认证：");
        String token = (String) authenticationToken.getCredentials();
        // 解密获得jwt内容
        String username = JWTUtil.getUsername(token);
        String sessionId = JWTUtil.getSessionId(token);
        String salt = JWTUtil.getSalt(token);
        assert salt != null;
        String tokenKey = JWTUtil.getTokenKey(token) + ":" + salt.substring(salt.lastIndexOf("_") + 1);

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(sessionId) || StringUtils.isEmpty(salt)) {
            throw new AuthenticationException("token 不合法");
        }

        AdminEntity user = userService.findByUserName(username);
        if (user == null) {
            throw new UnknownAccountException("token 对应用户不存在");
        }
        if (null == redisUtil.get(tokenKey)) {
            throw new AuthenticationException("token 已过期");
        }
//		if (!redisUtil.get(tokenKey).equals(salt)){
//			if(null != redisUtil.get(tokenKey+":repeat")){
//				redisUtil.del(tokenKey+":repeat", sessionId);
//				throw new AuthenticationException("异地登陆");
//			}
//			throw new AuthenticationException("token 认证失败");
//		}
        //延长salt
        redisUtil.set(tokenKey, salt, 14400);
        //新增 或 延长session
        UserUtils.setCurrentUser(sessionId, user, 14400);

        return new SimpleAuthenticationInfo(token, token, getName());
    }
}
