package com.hao.demo.shiro.simplify.config.shiro;

import com.hao.demo.shiro.simplify.model.AllConstants;
import com.hao.demo.shiro.simplify.model.JWTToken;
import com.hao.demo.shiro.simplify.model.UserEntity;
import com.hao.demo.shiro.simplify.model.UserType;
import com.hao.demo.shiro.simplify.service.UserService;
import com.hao.demo.shiro.simplify.utils.JWTUtil;
import com.hao.demo.shiro.simplify.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 客户端自定义shiro认证模块
 */
@Slf4j
public class ClientUserRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public String getName() {
        return UserType.CLIENT.getValue();
    }

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        log.info("===3pc=== supports");
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("===3pc=== 认证");
        //校验正确的当前用户
        if (!principalCollection.getRealmNames().contains(getName())) return null;
        log.error("=================getRealmNames={}", principalCollection.getRealmNames());
        log.error("=================primaryPrincipal={}",  principalCollection.getPrimaryPrincipal());
        log.error("=================getName()={}", getName());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(getName());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("===3pc=== 授权 校验正确的当前用户");
        String token = (String) authenticationToken.getCredentials();
        // 解密获得jwt内容
        String username = JWTUtil.getUsername(token);
        String sessionId = JWTUtil.getSessionId(token);
        String tokenKey = JWTUtil.getTokenKey(token);
        String salt = JWTUtil.getSalt(token);

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(sessionId) || StringUtils.isEmpty(salt)) {
            log.info("===3pc=== 认证失败，token 不合法");
            throw new AuthenticationException("token 不合法");
        }

        UserEntity user = userService.findByMobileOrEmail(username);
        if (user == null) {
            log.info("===3pc=== 认证失败，token 对应用户不存在");
            throw new UnknownAccountException("token 对应用户不存在");
        }
        if (null == redisUtil.get(tokenKey)) {
            log.info("===3pc=== 认证失败，token 已过期");
            throw new AuthenticationException("token 已过期");
        }
        if (!redisUtil.get(tokenKey).equals(salt)) {
            if (null != redisUtil.get(AllConstants.getRepeatKey(tokenKey))) {
                redisUtil.del(AllConstants.getRepeatKey(tokenKey), sessionId);
                log.info("===3pc=== 认证失败，已在异地登陆");
                throw new AuthenticationException("异地登陆");
            }
            log.info("===3pc=== 认证失败，token已失效");
            throw new AuthenticationException("token 认证失败");
        }
        //延长salt
        redisUtil.set(tokenKey, salt, 14400);
        //新增 或 延长session
        redisUtil.set(sessionId, user, 14400);
        log.info("===3pc=== 客户端-user 认证通过！");
        return new SimpleAuthenticationInfo(token, token, getName());
    }

}
