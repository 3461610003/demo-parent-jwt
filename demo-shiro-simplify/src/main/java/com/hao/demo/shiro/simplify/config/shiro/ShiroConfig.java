package com.hao.demo.shiro.simplify.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

/**
 * shiro子配置
 */
@Configuration
@Slf4j
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        log.info("====1====ShiroFilterFactoryBean 初始化");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, Filter> map = new HashMap<>();
        map.put("auth", getJWTFilter());

        shiroFilterFactoryBean.setFilters(map);
        //拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/app-login", "anon");
        filterChainDefinitionMap.put("/app-sms-login", "anon");
        filterChainDefinitionMap.put("/register", "anon");
        filterChainDefinitionMap.put("/forgot-password", "anon");

        //过滤链定义，从上向下执行，一般将/**放在最下面 ==》注意！！！
        //authc：所有url都必须认证通过才能访问；anno：所有url都可以匿名访问
        filterChainDefinitionMap.put("/**", "auth");
        //如果不设置默认会自动寻找Web工程目录下的 login.jsp 页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        //首页，登陆成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //认证不通过跳转、未授权的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }


    /**
     * 自定义过滤器
     *
     * @return MyFilter
     */
    @Bean
    public JWTFilter getJWTFilter() {
        log.info("====1====初始化 JWTFilter");
        return new JWTFilter();
    }

    /**
     * 设置过滤优先级 低于shiro
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<JWTFilter> registration2() {
        log.info("====1==== 设置过滤优先级 低于shiro");
        FilterRegistrationBean<JWTFilter> registration = new FilterRegistrationBean<>(getJWTFilter());
        registration.setEnabled(false);
        return registration;
    }

    /**
     * 安全管理器
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        log.info("====1==== 初始化 DefaultWebSecurityManager");
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

//        //自定义realm分发
        securityManager.setAuthenticator(modularRealmAuthenticator());

        securityManager.setRealms(realms());
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        //关闭shiro自带的session
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);

        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        log.info("====1==== 开启shiro aop注解支持.");
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


    @Bean("realms")
    public Collection<Realm> realms() {
        log.info("====1==== 获取配置的多个realms");
        //配置多realm
        Collection<Realm> realms = new ArrayList<>();
        //pc用户
        realms.add(clientUserRealm());
        //app用户
        realms.add(appUserRealm());
        return realms;
    }

    @Bean("modularRealmAuthenticator")
    public ModularRealmAuthenticator modularRealmAuthenticator() {
        log.info("====1==== 初始化 ModularRealmAuthenticator");
        CustomModularRealmAuthenticator customModularRealmAuthenticator = new CustomModularRealmAuthenticator();
        Map<String, Object> definedRealms = new HashMap<>();
        definedRealms.put("clientUser", clientUserRealm());
        definedRealms.put("appUser", appUserRealm());
        customModularRealmAuthenticator.setDefinedRealms(definedRealms);
        customModularRealmAuthenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return customModularRealmAuthenticator;
    }

    @Bean
    public ClientUserRealm clientUserRealm() {
        log.info("====1==== 初始化 ClientUserRealm");
        return new ClientUserRealm();
    }

    @Bean
    public AppUserRealm appUserRealm() {
        log.info("====1==== 初始化 AppUserRealm");
        return new AppUserRealm();
    }

}
