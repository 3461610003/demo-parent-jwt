package com.hao.demo.shiro.simplify.config;

import com.hao.demo.shiro.simplify.model.JWTToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 全局shiro拦截分发realm
 */
@Slf4j
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {
    private Map<String, Object> definedRealms;

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        // 判断getRealms()是否返回为空
        assertRealmsConfigured();
        // 强制转换回自定义的CustomizedToken
        JWTToken token = (JWTToken) authenticationToken;
        // 找到当前登录人的登录类型
        String loginType = token.getLoginType();
        if(null == loginType){
            log.error("业务逻辑异常={}", "token 无效非法!");
            throw new AuthenticationException("token 无效非法!");
        }
        // 所有Realm
        Collection<Realm> realms = getRealms();
        // 找到登录类型对应的指定Realm
        Collection<Realm> typeRealms = new ArrayList<>();
        for (Realm realm : realms) {
            if (realm.getName().contains(loginType)){
                typeRealms.add(realm);
            }
        }

        // 判断是单Realm还是多Realm
        if (typeRealms.size() == 1)
            return doSingleRealmAuthentication(typeRealms.iterator().next(), token);
        else
            return doMultiRealmAuthentication(typeRealms, token);
    }


    /**
     * 判断realm是否为空
     */
    @Override
    protected void assertRealmsConfigured() throws IllegalStateException {
        this.definedRealms = this.getDefinedRealms();
        if (CollectionUtils.isEmpty(this.definedRealms)) {
            throw new ShiroException("值传递错误!");
        }
    }

    public Map<String, Object> getDefinedRealms() {
        return this.definedRealms;
    }

    public void setDefinedRealms(Map<String, Object> definedRealms) {
        this.definedRealms = definedRealms;
    }
}

